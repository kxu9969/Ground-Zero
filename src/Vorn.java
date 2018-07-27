import java.awt.Color;
import java.util.ArrayList;

public class Vorn extends Hero{

	Vorn(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 350;
		currentHealth = maxHealth;
		maxStamina = 45;
		currentStamina = 0;
		basicDamage = 80;
		defaultArmor = 20;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 1;
		ab1cdMax = 4;
		ab2cdMax = 6;
		ab3cdMax = 1;
		ultcdMax = 8;		
	}
	
	public void tickDebuffs() {
		ArrayList<Debuff> toBeRemoved = new ArrayList<Debuff>();
		for(Debuff b:debuffs) {
			if(!(b instanceof Mark)) {
				if(b.duration>1) {
					b.duration--;
				}if(b.duration==0||b.duration==1) {
					toBeRemoved.add(b);
				}
			}
		}
		for(Debuff b:toBeRemoved) {
			removeDebuff(b);
		}
	}

	public void showAb1() {
		for(Hex h:position.allAdjacents()) {
			grid.getHex(h).color=Color.red;
		}
	}

	public void ability1(Hex h) {
		h.addEffect(new TileEffect("Chronal Prison",this,8,this,false,h));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		Buff b = new Buff("Temporal Relativity",this,4,this,false);
		b.info.add(currentHealth);
		addBuff(b);
		setStamina();
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		ability3(null);
	}

	public void ability3(Hex h) {
		addBuff(new Buff("Accelerate",this,-1,this,false));
		abcdDelay[2]=true;
		grid.game.endOfTurn();		
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		for(Buff b:buffs) {
			b.duration=-1;
		}
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
