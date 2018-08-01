
public class Saa_Herald extends Occupant{

	Saa_Herald(Grid grid, String team, Hex h, Unit owner) {
		super(grid, team, h, owner);
		// TODO Auto-generated constructor stub
	}

	public void assembleStats() {
		name = "Herald of the End";
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
