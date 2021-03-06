


import java.awt.Color;

public class Akar extends Hero{

	Akar(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}

	public void assembleStats() {
		name = "Ak'ar";
		title = "Lizardman Shaman";
		maxHealth = 550;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 30;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 5;
		ab1cdMax = 3;
		ab2cdMax = 4;
		ab3cdMax = 3;
		ultcdMax = 8;
		qU = "A powerful support character, Ak�ar is able to increase the vitality of his allies, protecting them with summoned creatures, healing them, and denying enemies favorable positioning.";
		qP = "Fireblood: At the end of your turn, heal for 40 current health.";
		q1 = "Thornscales (3): Mark an ally, healing them for 30 and making them return damage taken for 2 turns.";
		q2 = "Swamp Spirit (4): Summon a Bog Beast on an unoccupied adjacent tile. Lasts for 3 of its turns.";
		q3 = "Mark up to three enemies to take additional 20 damage when attacked ignoring armor and shield for 2 turns.";
		q4 = "Infect a tile, causing it to deal 20 damage to occupants ignoring armor and shield at the start of their turn. The infection spreads to all adjacent tile every turn and lasts 2 turns.";
	}

	public void endOfTurn() {
		heal(40);
		super.endOfTurn();
	}

	public void showAb1() {
		for(Hex h: grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.RED;
			}
		}
	}

	public void ability1(Hex h) {
		h.occupied.heal(40);
		addBuff(new Buff("Thornscales",h.occupied,2,this,false));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h: grid.hexes) {
			if(position.distance(h)==1&&h.occupied==null) {
				h.color=Color.RED;
			}
		}
	}

	public void ability2(Hex h) {
		String str;
		if(team==grid.game.team1) {
			str="Team 1";
		}
		else {
			str="Team 2";
		}
		Akar_BogBeast b = new Akar_BogBeast(grid,str,h,this);
		grid.game.addUnit(b);
		addDebuff(new Debuff("Timed Life",b,3,this,true));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		if(queue3.size()<3) {
			for(Hex h:grid.hexes) {
				if(h.hasEnemy(this)) {
					h.color=Color.RED;
				}
			}
			for(Object h:queue3) {
				((Hex)h).color=null;
			}
		}
		position.color=Color.green;
	}

	public void clearAb3() {
		queue3.clear();
		grid.game.setButtons();		
	}

	public void ability3(Hex h) {
		if(h==position) {
			for(Object h1:queue3) {
				addDebuff(new Debuff("Stormcall",((Hex)h1).occupied,2,this,false));
			}
			abcdDelay[2]=true;
			grid.game.endOfTurn();
		}else if(queue3.size()<3) {
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

	}

	public void showUlt() {
		for(Hex h: grid.hexes) {
			h.color=Color.RED;
		}
	}

	public void ultimate(Hex h) {
		h.addEffect(new TileEffect("Poisonseeds",this,2,this,false,h));
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
