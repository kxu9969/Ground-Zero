

public class JARie extends Hero{
	
	JARie(Grid grid,String name,String team,Hex h){
		super(grid,name,team,h);
	}
	
	public void assembleStats() {
		maxHealth = 400;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 30;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 5;
		ab1cdMax = 3;
		ab2cdMax = 3;
		ab3cdMax = 3;
		ultcdMax = 8;
		qU="A potato-powered robot of destruction, JAR.ie reconfigures himself for optimal combat roles and is able to overwhelm even the strongest of foes with a flurry of attacks.";
		qP="Fear the Potato: Basic attacks against targets with more health than you deal an additional 40 damage. You cannot move the turn after you use an ability.";
		q1="Configuration: Hyper-Potato (3): You can now attack twice a turn. Remove previous configuration. Root yourself for one turn.";
		q2="Configuration: FMJ Plasma (3): Your basic attacks now ignore armor. Remove previous configuration. Root yourself for one turn.";
		q3="Configuration: Leeching Potato (3): Your basic attacks now have lifesteal. Remove previous configuration. Root yourself for one turn.";
		q4="Über Potato (8): Activate all configurations for 3 turns. Root yourself for one turn.";
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean end) {
		damage = this.basicDamage;
		if(h.occupied.currentHealth>currentHealth)
			damage+=40;
		super.basicAttack(h,damage,armor,shield,end);
	}

	public void showAb1() {
		grid.game.clear();
		ability1(null);
	}

	public void ability1(Hex h) {
		removeSameBuff("Configuration: Hyper-Potato");
		removeSameBuff("Configuration: FMJ Plasma");
		removeSameBuff("Configuration: Leeching Potato");
		addBuff(new Buff("Configuration: Hyper-Potato",this,-1,this,true));
		addDebuff(new Debuff("Rooted",this,1,this,false));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {		
		grid.game.clear();
		ability2(null);
	}

	public void ability2(Hex h) {
		removeSameBuff("Configuration: Hyper-Potato");
		removeSameBuff("Configuration: FMJ Plasma");
		removeSameBuff("Configuration: Leeching Potato");
		addBuff(new Buff("Configuration: FMJ Plasma",this,-1,this,true));
		addDebuff(new Debuff("Rooted",this,1,this,false));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		grid.game.clear();
		ability3(null);
	}

	public void ability3(Hex h) {
		removeSameBuff("Configuration: Hyper-Potato");
		removeSameBuff("Configuration: FMJ Plasma");
		removeSameBuff("Configuration: Leeching Potato");
		addBuff(new Buff("Configuration: Leeching Potato",this,-1,this,true));
		addDebuff(new Debuff("Rooted",this,1,this,false));
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		grid.game.clear();
		ultimate(null);
	}

	public void ultimate(Hex h) {
		removeSameBuff("Configuration: Hyper-Potato");
		removeSameBuff("Configuration: FMJ Plasma");
		removeSameBuff("Configuration: Leeching Potato");
		
		
		addBuff(new Buff("Configuration: Hyper-Potato",this,3,this,true));
		addBuff(new Buff("Configuration: FMJ Plasma",this,3,this,true));
		addBuff(new Buff("Configuration: Leeching Potato",this,3,this,true));
		addDebuff(new Debuff("Rooted",this,1,this,false));

		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}
}
