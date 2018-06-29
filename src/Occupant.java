
public abstract class Occupant extends Unit{//non-turn taking space holders
	Occupant(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}
	
	public void die() {
		grid.game.occupants.remove(this);
		super.die();
	}
}
