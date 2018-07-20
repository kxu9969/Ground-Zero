import java.awt.Color;
import java.util.ArrayList;

public class Marcos extends Hero{

	Marcos(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 400;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 50;
		basicRange = 1;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 2;
		ultcdMax = 8;	
		qU="A fast attacker whose attacks Curse opponents, he can quickly burst down enemies with attacks that keep coming, preventing enemies from responding. When he cannot get in range or his combos are disrupted, however, he is extremely fragile.";
		qP="Bleeding Out: Basic attacks Curse enemies for 1 turn, preventing them from healing. Additional stacks increase the Curse duration, up to 3 turns.";
		q1="Riposte (2): Lunges through an adjacent target to the tile behind, dealing 50 damage ignoring armor.";
		q2="Follow Through (2): Basic attacks an adjacent target twice. If you basic attacked last turn refill your stamina immediately.";
		q3="Lunge (3):  Dash to an enemy within 3 range and basic attack them twice. If you did not basic attack last turn, refill stamina immediately.";
		q4="Dancing Blades (8): Enchant up to three nearby targets within 5 range and remove their armor for 2 turns, then attack them all in order.";
	}
	
	public void basicAttack(Hex h, int damage, boolean armor, boolean shield, boolean anotherTurn) {
		if(!h.occupied.hasDebuff("Cursed")) {
			addDebuff(new Debuff("Cursed",h.occupied,1,this,false));
		}else {
			if(h.occupied.getDebuff("Cursed").duration<3) {
				h.occupied.getDebuff("Cursed").duration++;
			}
		}
		super.basicAttack(h, damage, armor, shield, anotherTurn);
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				ArrayList<Hex> hexes = position.findLine(grid, h);
				if(hexes.size()>1&&hexes.get(1).occupied == null) {
					hexes.get(1).color=Color.red;
				}
			}
		}
	}

	public void ability1(Hex h) {
		Hex difference = h.subtract(position);
		Hex half = new Hex(difference.q/2,difference.r/2,difference.s/2);
		Hex target = position.add(half);
		target = grid.getHex(target);
		target.occupied.takeAbility(50, this, false, true);
		setPosition(h);
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		basicAttack(h,basicDamage,true,true,true);
		basicAttack(h,basicDamage,true,true,true);
		if(basicAttackedLastTurn) {
			setStamina();
		}
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		if(queue3.size()==0) {
			for(Hex h:grid.hexes) {
				if((position.distance(h)<=3&&h.isEmpty()||h==position)&&h.adjacentEnemy(grid, this)) {
					h.color=Color.RED;
				}
			}
		}else if(queue3.size()==1) {
			Hex firstClick = (Hex) queue3.get(0);
			for(Hex h:grid.hexes)	{
				if(h.distance(firstClick)==1&&h.hasEnemy(this)) {
					h.color=Color.RED;
				}
			}
		}
	}
	
	public void clearAb3() {
		queue3.clear();
		grid.game.setButtons();
	}

	public void ability3(Hex h) {
		if(queue3.size()==0) {
			queue3.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showAb3();
		}
		else if(queue3.size()==1) {
			setPosition((Hex)queue3.get(0));
			basicAttack(h,basicDamage,true,true,true);
			if(!basicAttackedLastTurn) {
				setStamina();
			}
			abcdDelay[2]=true;
			grid.game.endOfTurn();
		}
	}

	public void showUlt() {
		if(queue4.size()<3) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)<=5&&h.hasEnemy(this)) {
					h.color=Color.RED;
				}
			}
			for(Object h:queue4) {
				((Hex)h).color=null;
			}
		}
		position.color=Color.green;
		
	}

	public void clearUlt() {
		queue4.clear();
		grid.game.setButtons();
	}
	
	public void ultimate(Hex h) {
		if(h==position) {
			for(Object h1:queue4) {
				addDebuff(new Debuff("Armor Removed",((Hex)h1).occupied,2,this,true));
				basicAttack((Hex)h1,basicDamage,true,true,true);
			}
			abcdDelay[3]=true;
			grid.game.endOfTurn();
		}else if(queue4.size()<3) {
			queue4.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showUlt();
		}
	}

}
