
public class Amon_Lich extends Summon{

	Amon_Lich(Grid grid, String team, Hex h, Unit owner) {
		super(grid, team, h, owner);
	}

	public void assembleStats() {
		name = "Lich";
		title = "Fallen Magus";
		maxHealth = 100;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 0;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 5;		
		qU="These once-great archmages now rise to serve once again, firing bolts of unholy magic into enemy ranks.";
		qP="Unhallowed Magic: Attacks deal damage to enemies in adjacent tiles.";
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
