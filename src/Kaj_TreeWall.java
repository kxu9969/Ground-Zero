
public class Kaj_TreeWall extends Occupant{

	Kaj_TreeWall(Grid grid, String name, String team, Hex h,Unit owner) {
		super(grid, name, team, h,owner);
	}

	@Override
	public void assembleStats() {
		maxHealth = 50;
		currentHealth = maxHealth;
		currentArmor = 0;
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
