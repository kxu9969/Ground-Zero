
public abstract class PartialHero extends Unit{
	
	PartialHero(Grid grid, String team, Hex h) {
		super(grid, team, h);
		hasAb1=true;
		hasAb2=true;
		hasAb3=true;
		hasUlt=true;
	}
}
