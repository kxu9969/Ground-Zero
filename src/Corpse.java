
public class Corpse extends Summon{

	Corpse(Grid grid, String name, String team, Hex h,Unit owner) {
		super(grid, name, team, h,owner);
		hasAb1=true;
	}

	public void assembleStats() {
		maxHealth = 250;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 0;
		defaultArmor = 20;
		currentArmor = defaultArmor;
		armorPiercing = 70;
		basicRange = 1;
		ab1cdMax=0;
		qU="Bloated bodies filled with pestilence and disease, they shuffle into enemy lines and explode.";
		qP="Diseased Carcass: When this unit dies, deal 70 damage to adjacent enemies. Timed Life: This unit automatically dies after 4 of its turns.";
		q1="Imperial Command (0); Kill this unit.";
	}
	
	public boolean ableBasic() {
		return false;
	}
	
	public void die() {
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.hasEnemy(this)) {
				h1.occupied.takeAbility(70, this, true, true);
			}
		}
		super.die();
	}

	public void showAb1() {
		ability1(null);		
	}

	public void ability1(Hex h) {
		die();
		grid.game.endOfTurn();
	}

	@Override
	public void showAb2() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability2(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAb3() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability3(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showUlt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ultimate(Hex h) {
		// TODO Auto-generated method stub
		
	}

}
