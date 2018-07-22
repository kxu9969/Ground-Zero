import java.awt.Color;
import java.util.ArrayList;

public class Lindera extends Hero{
	Lindera(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 550;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 5;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 0;
		ultcdMax = 8;	
		qU="Lindera is a specialist who excels at disrupting enemies on the battlefield through forced repositioning by her basic attack and her abilities. Her ultimate can deal damage but is best at distracting her enemies and is best used when the most enemies are trapped by its spell.";
		qP="Null Shot: Basic attacks teleport targets to a designated adjacent tile.";
		q1="Vacant Soul (2): Silence an enemy within 2 tiles for 1 turn and teleport them to a tile within 3 tiles of their current position.";
		q2="Twilight Bomb (3):  Buff an ally with less than 50% health for 3 turns. If they die with the buff, deal 70 damage to adjacent enemies ignoring armor and shield.";
		q3="Mark of the Abyss (0): Mark a target for 2 turns (debuff). If you cast this ability on a Marked target, set their health at 50%.";
		q4="Void Spike (8): Marks a tile with a singularity that enemies must basic attack if possible. After 300 damage or 2 of Lindera’s turns, it detonates to deal all damage taken to adjacent enemies.";
	}
	
	public void showBasic() {
		if(queueB.size()==0) {
			super.showBasic();
		}else {
			for(Hex h:grid.hexes) {
				if(((Hex)queueB.get(0)).distance(h)==1&&h.isEmpty()) {
					h.color=Color.red;
				}else if(((Hex)queueB.get(0)).equals(h)) {
					h.color=Color.red;
				}
			}
		}
	}
	
	public void clearBasic() {
		queueB.clear();
		grid.game.setButtons();
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean end) {
		if(queueB.size()==0) {
			queueB.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showBasic();
		}else {
			((Hex)queueB.get(0)).occupied.setPosition(h);
			queueB.clear();
			super.basicAttack(h,damage,armor,shield,end);
		}
	}

	public void showAb1() {
		if(queue1.size()==0) {
			for(Hex h: grid.hexes) {
				if(position.distance(h)<=2&&h.hasEnemy(this)) {
					h.color=Color.red;
				}
			}
		}else if(queue1.size()==1) {
			for(Hex h:grid.hexes) {
				if(((Hex)queue1.get(0)).distance(h)<=3&&h.occupied==null) {
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
		}else if(queue1.size()==1) {
			((Hex)queue1.get(0)).occupied.setPosition(h);
			addDebuff(new Debuff("Silenced",h.occupied,1,this,false));
			abcdDelay[0]=true;
			grid.game.endOfTurn();
		}
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)&&h.occupied.currentHealth<(h.occupied.maxHealth/2)) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		addBuff(new Buff("Twilight Bomb",h.occupied,3,this,false));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(h.occupied!=null) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		if(h.occupied.hasMark(this)) {
			h.occupied.currentHealth=h.occupied.maxHealth/2;
		}else {
			addDebuff(new Mark("Marked",h.occupied,3,this,false));
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Hex h: grid.hexes) {
			if(position.distance(h)<=2&&h.occupied==null) {
				h.color=Color.red;
			}
		}
	}

	public void ultimate(Hex h) {
		String str;
		if(team==grid.game.team1) {
			str="Team 1";
		}
		else {
			str="Team 2";
		}
		Singularity b = new Singularity(grid,"Singularity",str,h,this);
		grid.game.occupants.add(b);
		addBuff(new Buff("Singularity",this,2,this,true));
		queue4.add(b);
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
