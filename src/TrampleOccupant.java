
public abstract class TrampleOccupant extends Occupant{

	TrampleOccupant(Grid grid, String name, String team, Hex h, Unit owner) {
		super(grid, name, team, h, owner);
		position.occupied=null;
		position.tramples.add(this);
	}
	
	public void onTrample() {
		die();
	}
	
	public void die() {
		position.tramples.remove(this);
		super.die();
	}

}
