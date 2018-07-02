import java.awt.Color;
import java.util.ArrayList;

public class Olaf extends Hero{

	Olaf(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 550;
		currentHealth = maxHealth;
		maxStamina = 65;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 20;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 1;
		ab1cdMax = 5;
		ab2cdMax = 3;
		ab3cdMax = 3;
		ultcdMax = 8;	
		qU="A fat man who loves food, Olaf is an exceptional healer who is best with damage dealers nearby, giving them health and shields, refilling stamina, and even rooting enemies with his ultimate.";
		qP="Hardiness: At the end of his turn, heal adjacent allies for 10 health.";
		q1="Royal Feast (5): Heal the target for 200 health. Extra health is gained as shield.";
		q2="Invigorating Meal (3): Fill the target’s stamina.";
		q3="Bountiful Banquet (3): Heal the target and adjacent allies for 40.";
		q4="Imperial Ham (8): Fires a ham from your cornucopia in a line for 5 tiles, healing allies hit for 100 with excess gained as shield and rooting enemies hit for 2 turns.";
	}
	
	public void endOfTurn() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasAlly(this)) {
				h.occupied.heal(10);
			}
		}
		super.endOfTurn();
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		h.occupied.gainShield(h.occupied.heal(200));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	@Override
	public void ability2(Hex h) {
		h.occupied.setStamina();
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}		
	}

	public void ability3(Hex h) {
		h.occupied.heal(40);
		for(Hex h1:grid.hexes) {
			if(h.distance(h1)==1&&h1.hasAlly(this)) {
				h1.occupied.heal(40);
			}
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Hex h: grid.hexes) {
			if(position.distance(h)==1) {
				h.color=Color.RED;
			}
		}		
	}

	@Override
	public void ultimate(Hex h) {
		ArrayList<Hex> hexes = position.findLine(grid, h);
		while(hexes.size()>5) {
			hexes.remove(5);
		}
		for(Hex h1:hexes) {
			if(h1.hasEnemy(this)) {
				addDebuff(new Debuff("Rooted",h1.occupied,2,this,false));
			}else if(h1.hasAlly(this)) {
				h1.occupied.gainShield(h1.occupied.heal(100));
			}
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
		
	}

}
