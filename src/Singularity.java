import java.util.ArrayList;

public class Singularity extends Occupant{

	Singularity(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 300;
		currentHealth = maxHealth;
		armor = 0;		
	}

	public void die() {
		int damage = maxHealth-currentHealth;
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.occupied.takeAbility(damage, this, false, false);
			}
		}
		super.die();
	}
	public ArrayList<Unit> inAura(){
		ArrayList<Unit> enemies = new ArrayList<Unit>();
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)) {
				enemies.add(h.occupied);
			}
		}
		return enemies;
	}
	public void addAura(Unit u) {}
	public void removeAura(Unit u) {}
	public void runAura() {
		if(inAura.contains(grid.game.currentUnit)&&grid.game.currentUnit.position.distance(
				position)<=grid.game.currentUnit.basicRange) {
			grid.game.move.lock=true;
			grid.game.ab1.lock=true;
			grid.game.ab2.lock=true;
			grid.game.ab3.lock=true;
			grid.game.ult.lock=true;
			grid.game.pass.lock=true;
			grid.game.move.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
		}
	}

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
