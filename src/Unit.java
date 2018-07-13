import java.awt.Color;
import java.util.ArrayList;

public abstract class Unit {//broadest branch, all space takers
	int maxHealth,currentHealth,maxStamina,currentStamina,basicDamage,defaultArmor,currentArmor,armorPiercing,basicRange,ab1cdMax,ab2cdMax,ab3cdMax,ultcdMax;
	int currentShield = 0;
	int moveRange = 3;
	int ab1cd,ab2cd,ab3cd = 0;
	int ultcd = 8;
	boolean[] abcdDelay = {false,false,false,false};
	boolean dead,basicAttackedThisTurn,basicAttackedLastTurn = false;
	boolean hasAb1,hasAb2,hasAb3,hasUlt=true;
	Hex position;
	Grid grid;
	String name,qM,qB,q1,q2,q3,q4,qP,qU;{
		qM = "Target an unoccupied tile within a range of 3 to move onto";
		qB = "Target an enemy within range to execute a basic attack";
		q1 = "Ability 1 text";
		q2 = "Ability 2 text";
		q3 = "Ability 3 text";
		q4 = "Ult text";
		qP = "Passive description text";
		qU = "Unit description text";}
	ArrayList<Unit> team;
	ArrayList<Unit> enemyTeam;
	ArrayList<Unit> inAura = new ArrayList<Unit>();
	ArrayList<Buff> buffs = new ArrayList<Buff>();
	ArrayList<Debuff> debuffs = new ArrayList<Debuff>();
	ArrayList<Mark> marks = new ArrayList<Mark>();
	ArrayList<Buff> addedBuffs = new ArrayList<Buff>();//use when giving self an effect on own turn
	ArrayList<Debuff> addedDebuffs = new ArrayList<Debuff>();
	ArrayList<Object> queue1 = new ArrayList<Object>();//stores relevant info for multistep functions, is cleared
	ArrayList<Object> queue2 = new ArrayList<Object>();
	ArrayList<Object> queue3 = new ArrayList<Object>();
	ArrayList<Object> queue4 = new ArrayList<Object>();
	int setStamina = 0;

	Unit(Grid grid,String name,String team,Hex h){
		this.grid = grid;
		this.name = name;
		if(team.equals("Team 1")) {
			this.team = grid.game.team1;
			enemyTeam = grid.game.team2;
		}else {
			this.team = grid.game.team2;
			enemyTeam = grid.game.team1;
		}
		position = grid.getHex(h).setHero(this);
		assembleStats();
	}

	public void setStamina(int i) {
		setStamina += i;
	}

	public void setStamina() {
		setStamina = maxStamina;
	}

	public void setPosition(Hex h) {		
		for(Hex h1:grid.hexes) {
			if(h1.equals(h)) {
				h=h1;
			}
		}
		position.clearHex();
		position = h;
		position.setHero(this);
		updateAura();
		//set debuffs
	}

	public abstract void assembleStats();

	public void showMove() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=moveRange&&h.occupied==null) {
				h.color=Color.RED;
			}
		}
	}

	public void move(Hex h) {
		setPosition(h);
		if(hasBuff("Whirling Scythes")&&!getBuff("Whirling Scythes").toggle) {
			grid.game.move.lock=true;
			grid.game.basic.lock=false;
			grid.game.ab1.lock=true;
			grid.game.ab2.lock=true;
			grid.game.ab3.lock=true;
			grid.game.ult.lock=true;
			grid.game.cancel.lock=false;
			grid.game.pass.lock=false;
			grid.game.clear();
			grid.game.setButtons();
			getBuff("Whirling Scythes").toggle=true;
		}else {
			grid.game.endOfTurn();
		}
	}

	public int heal(int health) {
		if(!hasDebuff("Cursed")) {
			currentHealth+=health;
			if(currentHealth>maxHealth) {
				int difference = currentHealth-maxHealth;
				currentHealth = maxHealth;
				return difference;
			}
		}
		return 0;
	}
	
	public void gainShield(int shield) {
		currentShield+=shield;
	}

	public void showBasic() {
		boolean singularity = false;
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=basicRange&&position.distance(h)>0&&h.occupied!=null
					&&!h.occupied.team.equals(team)) {//also needs to check for team
				h.color=Color.RED;
				if(h.occupied instanceof Singularity) {
					singularity = true;
				}
			}
		}
		if(hasBuff("Surface-to-Surface Missiles")) {
			for(Hex h:grid.hexes) {
				if(h.hasEnemy(this)&&h.occupied.hasMark((Hero) this)) {
					h.color=Color.red;
				}
			}
		}
		if(singularity) {
			for(Hex h:grid.hexes) {
				if(h.color==Color.red&&!(h.occupied instanceof Singularity)) {
					h.color= null;
				}
			}
		}
	}
	
	public boolean noArmor() {
		if(hasBuff("Configuration: FMJ Plasma")||hasBuff("Empowered Animus")) {//buffs for armor pierce
			return true;
		}
		return false;
	}
	
	public boolean hasLeech() {
		if(hasBuff("Configuration: Leeching Potato")||hasBuff("Succubus' Rage")
				||this instanceof Malor) {
			return true;
		}
		return false;
	}
	
	public boolean anotherTurn() {
		if(hasBuff("Configuration: Hyper-Potato")&&!getBuff(
				"Configuration: Hyper-Potato").toggle) {
			return true;
		}else if(hasBuff("Third Ring, Second Sign")&&!getBuff(
				"Third Ring, Second Sign").toggle) {
			return true;
		}else if(hasBuff("Whirling Scythes")&&!getBuff("Whirling Scythes").toggle) {
			return true;
		}
		return false;
	}
	
	public void clearBasic() {};

	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean anotherTurn) {
		if(hasBuff("Spiritual Unity")) {
			damage+=10;
		}
		if(hasBuff("Rip")) {
			damage+=10;
		}
		if(hasBuff("Energetic Blows")) {
			damage+=20;
		}
		if(hasBuff("Centered Stance")) {
			damage+=20;
		}
		if(hasBuff("Hunting Rites")) {
			damage+=20;
		}
		if(hasBuff("Lethality")) {
			damage+=((BuffStack)getBuff("Lethality")).stacks*10;
			removeSameBuff("Lethality");
		}
		if(hasBuff("Empowered Animus")) {
			for(Hex h1:grid.hexes) {
				if(h1.hasEnemy(this)) {
					h1.occupied.takeBasic(damage, this, armor, shield);
				}
			}
		}
		damage = h.occupied.takeBasic(damage, this, armor, shield);
		basicAttackedThisTurn = true;
		if(hasLeech()) {//buffs for lifesteal
			heal(damage);
		}
		if(hasBuff("Succubus' Rage")) {
			addDebuff(new Debuff("Stunned",h.occupied,1,this,false));
		}
		if(!anotherTurn) {
			grid.game.endOfTurn();
		}else {
			if(hasBuff("Configuration: Hyper-Potato")&&!getBuff(
					"Configuration: Hyper-Potato").toggle) {
				grid.game.move.lock=true;
				grid.game.basic.lock=false;
				grid.game.ab1.lock=true;
				grid.game.ab2.lock=true;
				grid.game.ab3.lock=true;
				grid.game.ult.lock=true;
				grid.game.cancel.lock=false;
				grid.game.pass.lock=false;
				grid.game.clear();
				grid.game.setButtons();
				getBuff("Configuration: Hyper-Potato").toggle=true;
			}else if(hasBuff("Third Ring, Second Sign")&&!getBuff(
					"Third Ring, Second Sign").toggle) {
				grid.game.move.lock=true;
				grid.game.basic.lock=false;
				grid.game.ab1.lock=true;
				grid.game.ab2.lock=true;
				grid.game.ab3.lock=true;
				grid.game.ult.lock=true;
				grid.game.cancel.lock=false;
				grid.game.pass.lock=false;
				grid.game.clear();
				grid.game.setButtons();
				getBuff("Third Ring, Second Sign").toggle=true;
			}
			else if(hasBuff("Whirling Scythes")&&!getBuff("Whirling Scythes").toggle) {
				grid.game.move.lock=false;
				grid.game.basic.lock=true;
				grid.game.ab1.lock=true;
				grid.game.ab2.lock=true;
				grid.game.ab3.lock=true;
				grid.game.ult.lock=true;
				grid.game.cancel.lock=false;
				grid.game.pass.lock=false;
				grid.game.clear();
				grid.game.setButtons();
				getBuff("Whirling Scythes").toggle=true;
			}

		}
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield) {
		boolean anotherTurn = false;
		if(anotherTurn()) {
			anotherTurn = true;
		}
		basicAttack(h,damage,armor,shield,anotherTurn);
	}
	
	public void basicAttack(Hex h,int damage) {
		boolean armor = true;
		boolean anotherTurn = false;
		if(noArmor()) {
			armor = false;
		}
		if(anotherTurn()) {
			anotherTurn = true;
		}
		basicAttack(h,damage,armor,true,anotherTurn);
	}
	
	public void basicAttack(Hex h) {
		boolean armor = true;
		boolean anotherTurn = false;
		if(noArmor()) {
			armor = false;
		}
		if(anotherTurn()) {
			anotherTurn = true;
		}
		basicAttack(h,this.basicDamage,armor,true,anotherTurn);
	}
	
	public int takeBasic(int damage, Unit attacker,boolean armor,boolean shield) {
		return takeDamage(damage,attacker,armor,shield);
	}
	public int takeAbility(int damage, Unit attacker,boolean armor, boolean shield) {
		return takeDamage(damage,attacker,armor,shield);
	}

	private int takeDamage(int damage, Unit h, boolean armor, boolean shield) {//process armor
		if(hasBuff("Second Ring, Fourth Sign")&&
				(!getBuff("Second Ring, Fourth Sign").caster.hasBuff("Second Ring, Fourth Sign")||
				(getBuff("Second Ring, Fourth Sign").caster.hasBuff("Second Ring, Fourth Sign")&&
				!getBuff("Second Ring, Fourth Sign").caster.getBuff("Second Ring, Fourth Sign").toggle))) {
			getBuff("Second Ring, Fourth Sign").toggle= true;
			getBuff("Second Ring, Fourth Sign").caster.takeDamage(damage,h,armor,shield);
			getBuff("Second Ring, Fourth Sign").toggle= false;
		}
		
		if(hasDebuff("Stormcall")) {
			damage+=20;
		}
		if(armor) {
			if(h.armorPiercing<=this.currentArmor) {
				damage-=this.currentArmor-h.armorPiercing;
				if(damage<0)
					damage=0;
			}
		}
		if(shield) {
			damage= this.takeDamage(damage);
		}else {
			damage= this.takeTrueDamage(damage);
		}
		System.out.println(this.name+" just took "+damage+" damage from "+h.name);
		if(hasBuff("Thornscales")&&h!=this) {
			h.takeAbility(damage, this, true, true);
		}
		return damage;
	}

	private int takeDamage(int damage) {//process shield
		if(hasBuff("Block")) {
			removeSameBuff("Block");
			return 0;
		}else {
			if(currentShield>=damage) {
				currentShield-=damage;
				return 0;
			}else {
				damage-=currentShield;
				currentShield = 0;
			}
			currentHealth-=damage;
			if(currentHealth<=0) {
				die();
			}
			return damage;
		}
	}

	private int takeTrueDamage(int damage) {
		if(hasBuff("Block")) {
			removeSameBuff("Block");
			return 0;
		}currentHealth-=damage;
		if(currentHealth<=0) {
			die();
		}
		return damage;
	}
	
	public void die() {
		for(Unit u:inAura) {
			removeAura(u);
		}
		if(hasBuff("Twilight Bomb")) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.hasEnemy(this)) {
					h.occupied.takeAbility(70, this, false, false);
				}
			}
		}
		if(!(this instanceof Hero)) {
			if(grid.game.units.contains(this)) {
				grid.game.units.remove(this);
			}
			if(grid.game.team1.contains(this)) {
				grid.game.team1.remove(this);
			}
			if(grid.game.team2.contains(this)) {
				grid.game.team2.remove(this);
			}
		}
		dead = true;
		position.clearHex();
		grid.game.checkGameOver();
		if(this==grid.game.currentUnit&&!grid.game.ending)
			grid.game.endOfTurn();
	}
	
	public void rewriteBuff(Buff b,ArrayList<Buff> buffs) {
		boolean addBuff = true;
		ArrayList<Buff> toBeRemoved = new ArrayList<Buff>();
		for(Buff b1:buffs) {
			if(!(b1 instanceof Channel)&&b1.effectName.equals(b.effectName)) {
				if(b instanceof BuffStack) {
					b1.onRemoval();
					((BuffStack)b1).stacks+=((BuffStack) b).stacks;
					if(((BuffStack)b1).stacks>=((BuffStack)b1).stackCap) {
						((BuffStack)b1).stacks=((BuffStack)b1).stackCap;
					}
					if(buffs==b1.owner.buffs) {
						b1.onAddition();
					}
					addBuff = false;
				}
				else if (b1.duration>b.duration) {
					addBuff = false;
				}
				else {
					toBeRemoved.add(b1);
				}
			}
		}
		for(Buff b1:toBeRemoved) {
			buffs.remove(b1);
		}
		if(addBuff) {
			buffs.add(b);
			if(buffs==b.owner.buffs) {
				b.onAddition();
			}
		}
	}
	
	public void rewriteDebuff(Debuff d,ArrayList<Debuff> debuffs) {
		boolean addDebuff = true;
		ArrayList<Debuff> toBeRemoved = new ArrayList<Debuff>();
		for(Debuff d1: debuffs) {
			if(!(d instanceof Mark )&& d1.effectName.equals(d.effectName)) {
				if(d instanceof DebuffStack) {
					d1.onRemoval();
					((DebuffStack)d1).stacks+=((DebuffStack)d).stacks;
					if(((DebuffStack)d1).stacks>=((DebuffStack)d1).stackCap) {
						((DebuffStack)d1).stacks=((DebuffStack)d1).stackCap;
					}
					if(debuffs==d1.owner.debuffs) {
						d1.onAddition();
					}
					addDebuff = false;
				}
				else if(d1.duration>d.duration) {
					addDebuff= false;
				}
				else {
					toBeRemoved.add(d1);
				}
			}
			if(d instanceof Mark) {
				if(d1.owner==d.owner&&d1.effectName.equals(d.effectName)) {
					if(d1.duration>d.duration) {
						addDebuff = false;
					}else {
						toBeRemoved.add(d1);
					}
				}
			}
		}
		for(Debuff d1:toBeRemoved) {
			debuffs.remove(d1);
			if(d1 instanceof Mark) {
				d1.caster.marks.remove(d1);
			}
		}
		if(addDebuff) {
			debuffs.add(d);
			if(debuffs==d.owner.debuffs) {
				d.onAddition();
			}
			if(d instanceof Mark) {
				d.caster.marks.add((Mark) d);
			}
		}
		if(d.effectName.equals("Silenced")) {
			for(int i = d.owner.buffs.size()-1;i>=0;i--) {
				if(d.owner.buffs.get(i) instanceof Channel) {
					removeBuff(d.owner.buffs.get(i));
				}
			}
		}
	}

	public void addBuff(Buff b) {
		if(b.owner==b.caster) {//when adding a buff to yourself, waits until end of turn to add
			rewriteBuff(b,addedBuffs);
		}else {//when adding to others, adds it immediately
			rewriteBuff(b,b.owner.buffs);
		}
	}

	//DON'T CALL THIS! ONLY ENDOFTURN CALLS THIS
	public void addSelfBuffs() {
		for(Buff b1:addedBuffs){
			rewriteBuff(b1,buffs);
		}
		addedBuffs.clear();
	}

	public void addDebuff(Debuff b) {
		if(b.owner==b.caster) {//when adding a buff to yourself, waits until end of turn to add
			rewriteDebuff(b,addedDebuffs);
		}else {//when adding to others, adds it immediately
			rewriteDebuff(b,b.owner.debuffs);
		}
	}

	public void addSelfDebuffs() {
		for(Debuff b1:addedDebuffs){
			rewriteDebuff(b1,debuffs);
		}
		addedDebuffs.clear();
	}

	public void removeSameBuff(String str) {
		ArrayList<Buff> toBeRemoved = new ArrayList<Buff>();
		for(Buff b:buffs) {
			if(b.effectName.equals(str)) {
				toBeRemoved.add(b);
			}
		}
		for(Buff b:toBeRemoved) {
			removeBuff(b);
		}
	}
	
	public void removeBuff(Buff b) {
		if(!b.enchant||b.duration==0) {
			b.owner.buffs.remove(b);
			b.onRemoval();
		}
	}

	public void removeSameDebuff(String str) {
		ArrayList<Debuff> toBeRemoved = new ArrayList<Debuff>();
		for(Debuff b:debuffs) {
			if(b.effectName.equals(str)) {
				toBeRemoved.add(b);
			}
		}
		for(Debuff b:toBeRemoved) {
			removeDebuff(b);
		}
	}
	
	public void removeDebuff(Debuff d) {
		if(!d.enchant||d.duration==0) {
			d.owner.debuffs.remove(d);
			d.onRemoval();
		}
		if(d instanceof Mark) {
			d.caster.marks.remove(d);
		}
	}
	
	public void removeMark(Mark d) {
		if(!d.enchant||d.duration==0) {
			d.onRemoval();
			debuffs.remove(d);
			d.owner.marks.remove(d);
		}
	}

	public boolean hasBuff(String str) {
		for(Buff b:buffs) {
			if(b.effectName.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasMark(Hero h) {
		for(Debuff d: debuffs) {
			if(d instanceof Mark) {
				if(d.effectName.equals("Marked")&&d.caster==h) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasDebuff(String str) {
		for(Debuff d: debuffs) {
			if(d.effectName.equals(str)) {
				return true;
			}
		}
		return false;
	}

	
	
	public Buff getBuff(String str) {
		for(Buff b:buffs) {
			if(b.effectName.equals(str)) {
				return b;
			}
		}
		return null;
	}
	
	public ArrayList<Buff> getBuffs(String str) {
		ArrayList<Buff> a = new ArrayList<Buff>();
		for(Buff d: buffs) {
			if(d.effectName.equals(str)) {
				a.add(d);
			}
		}
		return a;
	}
	

	public Debuff getDebuff(String str) {
		for(Debuff d: debuffs) {
			if(d.effectName.equals(str)) {
				return d;
			}
		}
		return null;
	}
	
	public ArrayList<Debuff> getDebuffs(String str) {
		ArrayList<Debuff> a = new ArrayList<Debuff>();
		for(Debuff d: debuffs) {
			if(d.effectName.equals(str)) {
				a.add(d);
			}
		}
		return a;
	}
	
	public Mark getMark(Hero h) {
		for(Debuff d: debuffs) {
			if(d instanceof Mark) {
				if(d.effectName.equals("Marked")&&d.caster==h) {
					return (Mark) d;
				}
			}
		}
		return null;
	}

	public void tickBuffs() {
		ArrayList<Buff> toBeRemoved = new ArrayList<Buff>();
		for(Buff b:buffs) {
			if(!(b instanceof Channel)) {
				if(b.duration>0) {
					b.duration--;
				}if(b.duration==0) {
					toBeRemoved.add(b);
				}
			}
		}
		for(Buff b:toBeRemoved) {
			removeBuff(b);
		}
	}

	public void tickDebuffs() {
		ArrayList<Debuff> toBeRemoved = new ArrayList<Debuff>();
		for(Debuff b:debuffs) {
			if(!(b instanceof Mark)) {
				if(b.duration>0) {
					b.duration--;
				}if(b.duration==0) {
					toBeRemoved.add(b);
				}
			}
		}
		for(Debuff b:toBeRemoved) {
			removeDebuff(b);
		}
	}
	
	public void tickMarks() {
		ArrayList<Mark> toBeRemoved = new ArrayList<Mark>();
		for(Mark m:marks) {
			if(m.duration>0) {
				m.duration--;
			}if(m.duration==0) {
				toBeRemoved.add(m);
			}
		}
		for(Mark m:toBeRemoved) {
			marks.remove(m);
			m.caster.removeDebuff(m);
		}
	}
	
	public void tickChannels() {
		ArrayList<Channel> toBeRemoved = new ArrayList<Channel>();
		boolean has = false;
		for(Buff c:buffs) {
			if(c instanceof Channel) {
				if(c.duration>0) {
					c.duration--;
				}if(c.duration==0) {
					toBeRemoved.add((Channel) c);
				}
				has = true;
			}
		}
		for(Channel c:toBeRemoved) {
			removeBuff(c);
		}
		if(has) {
			grid.game.endOfTurn();
		}
	}

	public void tickAbilities() {
		if(ab1cd>0&&!abcdDelay[0]) {
			ab1cd--;
		}if(ab2cd>0&&!abcdDelay[1]) {
			ab2cd--;
		}if(ab3cd>0&&!abcdDelay[2]) {
			ab3cd--;
		}if(ultcd>0&&!abcdDelay[3]) {
			ultcd--;
		}if(abcdDelay[0]) {
			ab1cd=ab1cdMax;
		}if(abcdDelay[1]) {
			ab2cd=ab2cdMax;
		}if(abcdDelay[2]) {
			ab3cd=ab3cdMax;
		}if(abcdDelay[3]) {
			ultcd=ultcdMax;
		}
		abcdDelay[0]=false;
		abcdDelay[1]=false;
		abcdDelay[2]=false;
		abcdDelay[3]=false;
	}

	public void startOfTurn() {
		if(position.hasEffect("Poisonseeds")) {
			takeAbility(20,position.getEffect("Poisonseeds").owner,false,false);
		}
		if(position.hasEffect("Nature's Bounty")) {
			heal(30);
		}
		if(position.hasEffect("Burning")) {
			takeAbility(10,position.getEffect("Burning").owner,false,false);
		}
		if(position.hasEffect("Thunder and Storm")) {
			takeAbility(30,position.getEffect("Thunder and Storm").owner,false,true);
			rewriteDebuff(new Debuff("Cursed",this,2,position.getEffect("Thunder and Storm").owner,false),debuffs);
		}
		if(hasBuff("Divine Radiance")) {
			heal(20);
		}
		if(hasDebuff("Molten Blast")) {
			takeAbility(20,getDebuff("Molten Blast").caster,false,false);
		}
		if(hasDebuff("Madness and Agony")) {
			takeAbility(60,getDebuff("Madness and Agony").caster,true,true);
		}
				
	};
	public void endOfTurn() {
		if(position.hasEffect("Thunder and Storm")) {
			takeAbility(30,position.getEffect("Thunder and Storm").owner,false,true);
			addDebuff(new Debuff("Cursed",this,3,position.getEffect("Thunder and Storm").owner,false));
		}
		if(position.hasEffect("Burning")) {
			takeAbility(10,position.getEffect("Burning").owner,false,false);
		}
		if(hasDebuff("Hunter and Prey")) {
			if(position.distance(getDebuff("Hunter and Prey").caster.position)==1) {
				getDebuff("Hunter and Prey").caster.currentStamina = getDebuff("Hunter and Prey").caster.maxStamina;
			}
		}
		if(!dead) {
			currentStamina = setStamina;
			setStamina = 0;
			if(hasBuff("Configuration: Hyper-Potato")) {
				getBuff("Configuration: Hyper-Potato").toggle = false;
			}
			if(hasBuff("Third Ring, Second Sign")) {
				getBuff("Third Ring, Second Sign").toggle = false;
			}
			if(hasBuff("Whirling Scythes")) {
				getBuff("Whirling Scythes").toggle = false;
			}
			if(basicAttackedThisTurn) {
				basicAttackedLastTurn=true;
			}else {
				basicAttackedLastTurn=false;
			}
			basicAttackedThisTurn = false;
			for(Unit u:grid.game.units) {
				u.updateAura();
			}
		}
	}
	
	public void updateAura() {
		if(inAura()!=null) {
			ArrayList<Unit> toBeRemoved = new ArrayList<Unit>();
			for(Unit u:inAura()) {
				if(!inAura.contains(u)) {
					addAura(u);
					inAura.add(u);
				}
			}
			for(Unit u:inAura) {
				if(!inAura().contains(u)) {
					removeAura(u);
					toBeRemoved.add(u);
				}
			}
			for(Unit u:toBeRemoved) {
				inAura.remove(u);
			}
		}
	}
	
	public ArrayList<Unit> inAura(){return null;}
	public void addAura(Unit u) {}
	public void removeAura(Unit u) {}
	public void runAura() {}
	
	public boolean hasAb1() {return hasAb1;}
	public abstract void showAb1();
	public void clearAb1() {};
	public abstract void ability1(Hex h);
	public boolean hasAb2(){return hasAb2;}
	public abstract void showAb2();
	public void clearAb2() {};
	public abstract void ability2(Hex h);
	public boolean hasAb3(){return hasAb3;}
	public abstract void showAb3();
	public void clearAb3() {};
	public abstract void ability3(Hex h);
	public boolean hasUlt(){return hasUlt;}
	public abstract void showUlt();
	public void clearUlt() {};
	public abstract void ultimate(Hex h);
	public boolean ableBasic() {return true;}
	public boolean ableAb1() {return true;}
	public boolean ableAb2() {return true;}
	public boolean ableAb3() {return true;}
	public boolean ableUlt() {return true;}

}
