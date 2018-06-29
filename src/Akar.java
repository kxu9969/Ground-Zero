


import java.awt.Color;

public class Akar extends Hero{

	Akar(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 550;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 30;
		armor = 40;
		armorPiercing = 40;
		basicRange = 3;
		ab1cdMax = 3;
		ab2cdMax = 4;
		ab3cdMax = 3;
		ultcdMax = 8;			
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
		this.addBuff(h.occupied, new Buff("Thornscales",this,2,false));
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
		BogBeast b = new BogBeast(grid,"Bog Beast",str,h);
		grid.game.addUnit(b);
		addDebuff(b,new Debuff("Timed Life",b,3,true));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)) {
				h.color=Color.RED;
			}
		}
		position.color=Color.green;
		for(Object h:queue3) {
			((Hex)h).color=null;
		}
	}

	public void clearAb3() {
		queue3.clear();
		grid.game.setButtons();		
	}

	public void ability3(Hex h) {
		if(h==position) {
			for(Object h1:queue3) {
				addDebuff(((Hex)h1).occupied,new Debuff("Stormcall",this,2,false));
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
		h.effects.add(new TileEffect("Poisonseeds",this,2,false,h));
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
