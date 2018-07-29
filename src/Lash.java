import java.awt.Color;
import java.util.ArrayList;

public class Lash extends Hero{

	Lash(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 350;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 10;
		basicRange = 5;
		ab1cdMax = 4;
		ab2cdMax = 5;
		ab3cdMax = 5;
		ultcdMax = 8;			
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		h.occupied.takeAbility(40, this, true, true);
		addDebuff(new Debuff("Rooted",h.occupied,2,this,false));
		addDebuff(new Debuff("Thorns' Embrace",h.occupied,2,this,false));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		if(queue2.size()==0) {
			for(Hex h:grid.hexes) {
				if(h.hasEnemy(this)) {
					h.color=Color.red;
				}
			}
		}else if(queue2.size()==1) {
			for(Hex h:grid.hexes) {
				if(((Hex)queue2.get(0)).distance(h)<=3&&h.hasAlly(this)){
					h.color=Color.red;
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
			Buff b = new Buff("Vampiric Vine",h.occupied,-1,((Hex)queue2.get(0)).occupied,false);
			Debuff d = new Debuff("Vampiric Vine",((Hex)queue2.get(0)).occupied,-1,h.occupied,false);
			b.info.add(d);
			d.info.add(b);
			addBuff(b);
			addDebuff(d);
			abcdDelay[1]=true;
			grid.game.endOfTurn();
		}
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			h.color=Color.red;
		}
	}

	public void ability3(Hex h) {
		ArrayList<Hex> hexes = h.allAdjacents();
		hexes.add(h);
		for(Hex h1:hexes) {
			h1=grid.getHex(h1);
			if(h1.occupied==null) {
				String str;
				if(team==grid.game.team1) {
					str="Team 1";
				}
				else {
					str="Team 2";
				}
				Lash_Flower b = new Lash_Flower(grid,"Flower",str,h1,this);
				grid.game.occupants.add(b);	
			}
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		for(Hex h1:grid.hexes) {
			if(h1.distance(position)<=3&&h1.hasEnemy(this)) {
				addDebuff(new Debuff("Rooted",h1.occupied,2,this,false));
				Buff b = new Buff("Vampiric Vine",this,-1,h1.occupied,false);
				Debuff d = new Debuff("Vampiric Vine",h1.occupied,-1,this,false);
				b.info.add(d);
				d.info.add(b);
				addBuff(b);
				addDebuff(d);
			}
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
