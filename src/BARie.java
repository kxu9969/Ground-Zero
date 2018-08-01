import java.awt.Color;
import java.util.ArrayList;

public class BARie extends Hero{

	BARie(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}

	public void assembleStats() {
		name = "BAR.ie";
		title = "Heavy Ordnance Delivery System";
		maxHealth = 500;
		currentHealth = maxHealth;
		maxStamina = 70;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 60;
		currentArmor = defaultArmor;
		armorPiercing = 60;
		basicRange = 5;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 2;
		ultcdMax = 8;		
		qU="A long-ranged assassin, BAR.ie Marks his targets before continually striking at them from range. His attacks are best against Marked targets, and his ultimate lets him pick off wounded enemies easily.";
		qP="Targeting Matrix: Enemies hit by basic attacks are Marked for 3 turns.";
		q1="Spitfire Missile (2): Fires a missile to a marked target (no range), dealing 70 damage to adjacent tiles and consuming the Mark.";
		q2="Death Ray (3): Fires a laser blast at a target within 3 tiles, dealing 40 damage. If it is marked, range is infinite and cooldown is 0.";
		q3="Suppressive Fire (2): Channel for 1 turn, then basic attack all units in a line for 5 tiles at the start of your next turn, penetrating targets. Can be interrupted by silences.";
		q4="Surface-to-Surface Missiles (8): Gain +30 armor and Mark all enemies. Can no longer move but can attack any Marked target. Lasts 4 turns, refill stamina immediately.";
	}
	
	public void basicAttack(Hex h, int damage, boolean armor, boolean shield, boolean anotherTurn) {
		if(h.hasEnemy(this)) {
			addDebuff(new Mark("Marked",h.occupied,4,this,false));
		}
		super.basicAttack(h, damage, armor, shield, anotherTurn);
	}
	
	public boolean ableAb1() {
		if(marks.size()>0) {
			return true;
		}
		return false;
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)&&h.occupied.hasMark(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		for(Hex h1:grid.hexes) {
			if(h.distance(h1)==1&&h1.occupied!=null) {
				h1.occupied.takeAbility(70, this, true, true);
			}
		}
		h.occupied.takeAbility(70, this, true, true);
		h.occupied.removeMark(h.occupied.getMark(this));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)&&(position.distance(h)<=3||h.occupied.hasMark(this))) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		h.occupied.takeAbility(40, this, true, true);
		if(h.occupied.hasMark(this)) {
			ab2cdMax=0;
		}else {
			ab2cdMax = 3;
		}
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		ArrayList<Hex> hexes = position.extendLineAdj(grid, h);
		while(hexes.size()>5) {
			hexes.remove(5);
		}
		addBuff(new Channel("Channeling",this,1,this,false,"Suppressive Fire",hexes));
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		addBuff(new Buff("Surface-to-Surface Missiles",this,4,this,true));
		for(Unit u:enemyTeam) {
			addDebuff(new Mark("Marked",u,4,this,false));

		}
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
