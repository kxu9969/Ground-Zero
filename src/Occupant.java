
public abstract class Occupant extends Unit{//non-turn taking space holders
	Unit owner;
	Occupant(Grid grid, String name, String team, Hex h,Unit owner) {
		super(grid, name, team, h);
		hasAb1=false;
		hasAb2=false;
		hasAb3=false;
		hasUlt=false;
		this.owner=owner;
	}
	
	public void die() {
		grid.game.occupants.remove(this);
		super.die();
	}
}
