

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
		armor = 30;
		armorPiercing = 40;
		basicRange = 5;
		ab1cdMax = 3;
		ab2cdMax = 3;
		ab3cdMax = 3;
		ultcdMax = 8;
	}
	
	public void basicAttack(Hex h) {
		int damage = this.basicDamage;
		if(h.occupied.currentHealth>currentHealth)
			damage+=40;
		super.basicAttack(h,damage);
	}

	public void showAb1() {
		grid.game.clear();
		ability1(null);
	}

	public void ability1(Hex h) {
		removeSameBuff("Configuration: Hyper-Potato");
		removeSameBuff("Configuration: FMJ Plasma");
		removeSameBuff("Configuration: Leeching Potato");
		addBuff(this,new Buff("Configuration: Hyper-Potato",this,-1,true));
		addDebuff(this,new Debuff("Rooted",this,1,false));
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
		addBuff(this,new Buff("Configuration: FMJ Plasma",this,-1,true));
		addDebuff(this,new Debuff("Rooted",this,1,false));
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
		addBuff(this,new Buff("Configuration: Leeching Potato",this,-1,true));
		addDebuff(this,new Debuff("Rooted",this,1,false));
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
		
		
		addBuff(this,new Buff("Configuration: Hyper-Potato",this,3,true));
		addBuff(this,new Buff("Configuration: FMJ Plasma",this,3,true));
		addBuff(this,new Buff("Configuration: Leeching Potato",this,3,true));
		addDebuff(this,new Debuff("Rooted",this,1,false));

		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}
}
