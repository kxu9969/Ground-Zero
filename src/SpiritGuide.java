import java.util.ArrayList;

public class SpiritGuide extends Summon{

	SpiritGuide(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 150;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 0;
		armor = 0;
		armorPiercing = 0;
		basicRange = 1;	
	}
	
	public ArrayList<Unit> inAura(){
		ArrayList<Unit> allies = new ArrayList<Unit>();
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasAlly(this)) {
				allies.add(h.occupied);
			}
		}
		return allies;
	}
	public void addAura(Unit u) {
		u.basicDamage+=10;
	}
	public void removeAura(Unit u) {
		u.basicDamage-=10;
	}

	public void endOfTurn() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasAlly(this)) {
				h.occupied.heal(40);
			}
		}
		super.endOfTurn();
	}
	
	public boolean hasAb1() {
		return false;
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
