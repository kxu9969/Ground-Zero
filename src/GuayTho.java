import java.awt.Color;

public class GuayTho extends Hero{

	GuayTho(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 700;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 30;
		defaultArmor = 30;
		currentArmor = defaultArmor;
		armorPiercing = 60;
		basicRange = 1;
		ab1cdMax = 1;
		ab2cdMax = 2;
		ab3cdMax = 0;
		ultcdMax = 8;	
		qU="A once-powerful king who lost a bet to a minor god, he now roams the sky as an attendant, fishing rogue stars out of the river Night before they fall to earth.";
		qP="Celestial Fur: Has an additional 40 armor against abilities.";
		q1="Fishing Hook (1): Pull the target enemy within 5 range onto an unoccupied adjacent tile.";
		q2="Spear and Net (2): Deal 20 damage to all adjacent enemies and root them for 1 turn, gaining a Star stack for each enemy.";
		q3="Consume Star (0): Consume a Star, restoring up to 80 current health and setting your current stamina at 30.";
		q4="Star Trap (8): Cast Spear and Net, silencing enemies hit for 1 turn and lowering the cooldown of this ability by 1 per enemy hit.";		
	}
	
	public int takeAbility(int damage, Unit attacker,boolean armor, boolean shield) {
		currentArmor+=40;
		int i = super.takeAbility(damage, attacker, armor, shield);
		currentArmor-=40;
		return i;
	}

	public void showAb1() {
		if(queue1.size()==0) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)<=5&&h.hasEnemy(this)) {
					h.color=Color.red;
				}
			}
		}else if(queue1.size()==1) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.isEmpty()) {
					h.color=Color.red;
				}
			}
		}
	}
	
	public void clearAb1() {
		queue1.clear();
		grid.game.setButtons();
	}

	public void ability1(Hex h) {
		if(queue1.size()==0) {
			queue1.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showAb1();
		}
		else if(queue1.size()==1) {
			((Hex)queue1.get(0)).occupied.setPosition(h);
			abcdDelay[0]=true;
			grid.game.endOfTurn();
		}		
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.hasEnemy(this)) {
				h1.occupied.takeAbility(20, this, true, true);
				addDebuff(new Debuff("Rooted",h1.occupied,1,this,false));
				addBuff(new BuffStack("Star",this,-1,this,false,1,99));
			}
		}
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public boolean ableAb3() {
		if(hasBuff("Star")) {
			return true;
		}
		return false;
	}
	
	public void showAb3() {
		ability3(null);
	}

	public void ability3(Hex h) {
		BuffStack star = ((BuffStack)getBuff("Star"));
		star.stacks--;
		if(star.stacks<=0) {
			removeBuff(star);
		}
		heal(80);
		setStamina(30);
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		ultcdMax = 8;
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.hasEnemy(this)) {
				h1.occupied.takeAbility(20, this, true, true);
				addDebuff(new Debuff("Rooted",h1.occupied,1,this,false));
				addDebuff(new Debuff("Silenced",h1.occupied,1,this,false));
				addBuff(new BuffStack("Star",this,-1,this,false,1,99));
				if(ultcdMax>0) {
					ultcdMax--;
				}
			}
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();		
	}

}
