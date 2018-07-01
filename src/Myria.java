import java.awt.Color;

public class Myria extends Hero{
	
	Myria(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {	
		maxHealth = 550;
		currentHealth = maxHealth;
		maxStamina = 55;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 60;
		basicRange = 1;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 3;
		ultcdMax = 8;
		qU="A powerful initiator, Myria is able to start fights on her terms, stunning enemies when she flys in from a distance and pulling back those who try to escape the bloodbath.";
		qP="Fires of Hell: All enemies take 10 damage at the start of your turn, ignoring armor and shields.";
		q1="Fireburst (2): Dash to the target within 3 tiles, attack them, and stun them for 1 turn.";
		q2="Flame Whip (4): Pull an enemy within 5 tiles to an adjacent tile and root them for 1 turn. Basic attack them.";
		q3="Wings of Sin (5): Fly to a target tile within 6 tiles, stunning adjacent enemies for 1 turn.";
		q4="Succubus’ Rage (8): Basic attacks stun targets for 1 turn and have lifesteal. Lasts 5 turns.";
	}
	
	public void startOfTurn() {
		for(Unit u:enemyTeam) {
			u.takeAbility(10, this, false, false);
		}
	}

	public void showAb1() {	
		if(queue1.size()==0) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)<=3&&h.occupied==null&&h.adjacentEnemy(grid, this)) {
					h.color=Color.RED;
				}
			}
		}else if(queue1.size()==1) {
			Hex firstClick = (Hex) queue1.get(0);
			for(Hex h:grid.hexes)	{
				if(h.distance(firstClick)==1&&h.hasEnemy(this)) {
					h.color=Color.RED;
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
			setPosition((Hex)queue1.get(0));
			basicAttack(h);
			this.addDebuff(h.occupied,new Debuff("Stunned",h.occupied,1,false));
			abcdDelay[0]=true;
			grid.game.endOfTurn();
		}
	}

	public void showAb2() {	
		if(queue2.size()==0) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)<=3&&h.hasEnemy(this)){
					h.color=Color.RED;
				}
			}
		}else if(queue2.size()==1) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.occupied==null) {
					h.color=Color.RED;
				}
			}
		}
	}

	public void clearAb2() {
		queue2.clear();
		grid.game.setButtons();
	}

	public void ability2(Hex h) {
		if(queue2.size()==0) {
			queue2.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showAb2();
		}else if(queue2.size()==1) {
			((Hex)queue2.get(0)).occupied.setPosition(h);
			h.occupied.takeBasic(basicDamage, this, true, true);
			this.addDebuff(h.occupied, new Debuff("Rooted",h.occupied,1,false));
			abcdDelay[1]=true;
			grid.game.endOfTurn();
		}
	}

	public void showAb3() {		
		for(Hex h: grid.hexes) {
			if(position.distance(h)<=6&&h.occupied==null) {
				h.color=Color.RED;
			}
		}
	}
	public void ability3(Hex h) {	
		setPosition(h);
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.hasEnemy(this)) {
				this.addDebuff(h1.occupied,new Debuff("Stunned",h1.occupied,1,false));
			}
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {		
		ultimate(null);
	}

	public void ultimate(Hex h) {	
		this.addBuff(this, new Buff("Succubus' Rage",this,5,true));
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
