
public class MissileBomb extends Occupant{

	MissileBomb(Grid grid, String name, String team, Hex h,Unit owner) {
		super(grid, name, team, h,owner);
	}

	public void assembleStats() {
		maxHealth = 100;
		currentHealth = maxHealth;
		currentArmor = 0;		
	}
	
	public int takeBasic(int damage, Unit attacker,boolean armor,boolean shield) {	
		int i = super.takeBasic(damage, attacker, armor, shield);
		if(attacker==owner) {
			die();
		}
		return i;
	}
	
	public void die() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.occupied.takeAbility(50, owner, true, true);
			}
		}
		super.die();
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
