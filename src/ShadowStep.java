
public class ShadowStep extends Occupant{

	ShadowStep(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 0;
		currentHealth = maxHealth;
	}
	
	public void die() {};
	public void die(boolean b) {
		if(b) {
			super.die();
		}
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
