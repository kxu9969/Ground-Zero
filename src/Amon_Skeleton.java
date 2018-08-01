
public class Amon_Skeleton extends Summon{

	Amon_Skeleton(Grid grid, String team, Hex h, Unit owner) {
		super(grid, team, h, owner);
	}

	public void assembleStats() {
		name = "Skeleton";
		title = "Fallen Legionnaires";
		maxHealth = 60;
		currentHealth = maxHealth;
		maxStamina = 70;
		currentStamina = 0;
		basicDamage = 10;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 1;	
		qU="Armed with sword and shield, they emerge to surround enemies and protect their master.";
		qP="Timed Life: This unit automatically dies after 3 of its turns.";
		
	}

	@Override
	public void showAb1() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability1(Hex h) {
		// TODO Auto-generated method stub
		
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
