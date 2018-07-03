
public abstract class PartialHero extends Unit{
	
	PartialHero(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
		hasAb1=true;
		hasAb2=true;
		hasAb3=true;
		hasUlt=true;
	}
}
