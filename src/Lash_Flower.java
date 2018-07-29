
public class Lash_Flower extends TrampleOccupant{

	Lash_Flower(Grid grid, String name, String team, Hex h, Unit owner) {
		super(grid, name, team, h, owner);
	}

	public void assembleStats() {
		maxHealth = 50;
		currentHealth = maxHealth;
		currentArmor = 0;		
	}
	
	public void onTrample(Unit u) {
		if(u==grid.game.currentUnit) {
			addDebuff(new Debuff("Silenced",u,2,owner,false));
		}else {
			addDebuff(new Debuff("Silenced",u,1,owner,false));
		}
		super.onTrample(u);
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
