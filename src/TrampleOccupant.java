
public abstract class TrampleOccupant extends Occupant{

	TrampleOccupant(Grid grid, String name, String team, Hex h, Unit owner) {
		super(grid, name, team, h, owner);
	}
	
	public void onTrample(Unit u) {
		die();
	}
	
	public void die() {
		super.die();
	}

}
