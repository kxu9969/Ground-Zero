import java.awt.Color;
import java.util.ArrayList;

public class BARie extends Hero{

	BARie(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
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
	}
	
	public void basicAttack(Hex h, int damage, boolean armor, boolean shield, boolean anotherTurn) {
		if(h.hasEnemy(this)) {
			addDebuff(new Mark("Marked",h.occupied,4,this,false));
		}
		super.basicAttack(h, damage, armor, shield, anotherTurn);
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
		ArrayList<Hex> hexes = position.findLine(grid, h);
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
