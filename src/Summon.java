
public abstract class Summon extends Unit{//may not have certain abilities
	
	Summon(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
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
