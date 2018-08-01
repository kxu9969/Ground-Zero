
public abstract class Summon extends Unit{//may not have certain abilities
	Unit owner;
	Summon(Grid grid, String team, Hex h,Unit owner) {
		super(grid, team, h);
		this.owner = owner;
		hasAb1=false;
		hasAb2=false;
		hasAb3=false;
		hasUlt=false;
	}
	
	public void die() {
		grid.game.removeUnit(this);
		super.die();
	}

}
