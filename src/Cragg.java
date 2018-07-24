import java.util.ArrayList;

public class Cragg extends Hero{

	Cragg(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 700;
		currentHealth = maxHealth;
		maxStamina = 80;
		currentStamina = 0;
		basicDamage = 60;
		defaultArmor = 50;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 1;
		ab1cdMax = 2;
		ab2cdMax = 1;
		ab3cdMax = 2;
		ultcdMax = 8;		
		qU="A man made golem, Cragg gained sentience and free will after his master’s death. Able to assimilate the earth, he is extremely resilient and can alter his stats. In addition, he naturally attracts energy, disrupting enemies’ attacks.";
		qP="Arcane Affinity: Takes ability damage on behalf of adjacent allies. Gains a stack of Feral Magic upon taking ability damage, up to 9.";
		q1="Natural Assimilation (2): Gain +40 max health and +80 current health.";
		q2="Granite Fists (1): Consume all stacks of Feral Magic and gain +10 attack for each on your next attack. Refill stamina immediately.";
		q3="Diorite Plating (2): Consume all stacks of Feral Magic and gain 20 shield for each stack.";
		q4="Alchemical Purge (8): Remove all debuffs.Heal yourself to max health. Max stamina is decreased by 10, down to a minimum of 40.";
	}
	
	public int takeAbility(int damage, Unit attacker,boolean armor, boolean shield) {
		rewriteBuff(new BuffStack("Feral Magic",this,-1,this,false,1,9),buffs);
		return super.takeAbility(damage, attacker, armor, shield);
	}
	
	public void showAb1() {
		ability1(null);
	}

	public void ability1(Hex h) {
		maxHealth+=40;
		heal(80);
		abcdDelay[0]=true;
		grid.game.endOfTurn();		
	}

	public void showAb2() {
		ability2(null);
	}
	
	public boolean ableAb2() {
		if(hasBuff("Feral Magic")) {
			return true;
		}
		return false;
	}

	public void ability2(Hex h) {
		addBuff(new BuffStack("Granite Fists",this,-1,this,false,((BuffStack)getBuff("Feral Magic")).stacks,99));
		removeSameBuff("Feral Magic");
		setStamina();
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		ability3(null);
	}
	
	public boolean ableAb3() {
		if(hasBuff("Feral Magic")) {
			return true;
		}
		return false;
	}

	public void ability3(Hex h) {
		gainShield(((BuffStack)getBuff("Feral Magic")).stacks*20);
		removeSameBuff("Feral Magic");
		for(Hex h1:position.allAdjacents()) {
			h1=grid.getHex(h1);
			if(h1.hasAlly(this)) {
				addBuff(new Buff("Diorite Plating",h1.occupied,2,this,false));
			}
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		ArrayList<Debuff> toBeRemoved = new ArrayList<Debuff>();
		for(Debuff b:debuffs) {
			toBeRemoved.add(b);
		}
		for(Debuff b:toBeRemoved) {
			removeDebuff(b);
		}
		heal(maxHealth-currentHealth);
		if(maxStamina>40) {
			maxStamina-=10;
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}
}
