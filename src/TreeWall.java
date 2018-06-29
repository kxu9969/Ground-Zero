
public class TreeWall extends Occupant{

	TreeWall(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	@Override
	public void assembleStats() {
		maxHealth = 50;
		currentHealth = maxHealth;
		armor = 0;
	}

	@Override
	public boolean hasAb1() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showAb1() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearAb1() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability1(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasAb2() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showAb2() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearAb2() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability2(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasAb3() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showAb3() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearAb3() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability3(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasUlt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showUlt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearUlt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ultimate(Hex h) {
		// TODO Auto-generated method stub
		
	}

}
