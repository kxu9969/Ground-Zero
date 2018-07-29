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
		qU="A support that is able to immobilize nearly the entire team, Lash is a fragile yet potent hero whose kit allows her to root, slow, and silence enemy heroes. Her vines allow her to punish immobile enemies while her passive deters heavy crowd control.";
		qP="Symbiosis: Whenever an ally is rooted, including overwrites, they are also healed for 60.";
		q1="Thorns’ Embrace (4): Deal 40 damage to an enemy and root them for 2 turns, silencing them for 1 turn after.";
		q2="Vampiric Vines (5): Tether together an enemy and an ally that are within 3 tiles of each other. At the start of either target’s turn, deal 20 damage ignoring armor to the enemy and heal the ally for 20. The tether can be broken by moving apart. Vines stack.";
		q3="Moonglaze Flowers (5):  Target a tile and adjacent tiles. Any unoccupied tiles become covered in flowers with 50 health. Walking on one destroys the flowers and silences the occupant for 1 turn.";
		q4="Fury of the Queen (8): Root all enemies within 3 range for 2 turns and place a Vampiric Vine tether on them all.";
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
