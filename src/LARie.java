import java.awt.Color;
import java.util.ArrayList;

public class LARie extends Hero{

	LARie(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 20;
		armor = 60;
		armorPiercing = 20;
		basicRange = 1;
		ab1cdMax = 1;
		ab2cdMax = 3;
		ab3cdMax = 2;
		ultcdMax = 8;		
	}
	
	public void basicAttack(Hex h) {
		int damage=basicDamage;
		if(hasBuff("Rev'n Up")) {
			damage+=((BuffStack)getBuff("Rev'n Up")).stacks*15;
			removeSameBuff("Rev'n Up");
		}
		super.basicAttack(h,damage);
	}
	
	public void endOfTurn() {
		if(!basicAttackedThisTurn) {
			this.addBuff(this,new BuffStack("Rev'n Up",this,-1,false,1));
		}
		super.endOfTurn();
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		h.occupied.takeAbility(20, this, false, true);
		h.push(grid, position);
		abcdDelay[0] = true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		heal(40);
		this.addBuff(this, new Buff("Block",this,-1,false));
		abcdDelay[1] = true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		ability3(null);
	}

	public void ability3(Hex h) {
		ArrayList<Hex> targets = new ArrayList<Hex>();
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.hasEnemy(this)) {
				targets.add(h1);
			}
		}
		int damage = 30+targets.size()*10;
		for(Hex h1:targets) {
			h1.occupied.takeAbility(damage, this, true, true);
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {	
		ultimate(null);
	}

	public void ultimate(Hex h) {
		this.addBuff(this, new BuffStack("Rev'n Up", this,-1,false,9));
		gainShield(200);
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
