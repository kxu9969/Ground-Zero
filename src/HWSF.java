import java.awt.Color;
import java.util.ArrayList;

public class HWSF extends Hero{

	HWSF(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 55;
		currentStamina = 0;
		basicDamage = 70;
		defaultArmor = 50;
		currentArmor = defaultArmor;
		armorPiercing = 20;
		basicRange = 5;
		ab1cdMax = 1;
		ab2cdMax = 4;
		ab3cdMax = 2;
		ultcdMax = 8;		
		qU="A powerful dragon who excels at slow but powerful channeled abilities, He Who Speaks Fire can deal devastating damage when used with a team that can root enemies, protect him from silences, and follow up on the openings he creates.";
		qP="Ancient Tongue: Vaporize: Basic attacks and abilities Mark tiles with Burning for 6 turns, dealing 10 damage to occupants at the start and end of their turn, ignoring shield and armor.";
		q1="Ancient Tongue: Firestorm (1): Mark a tile and channel for a turn, then deal 80 damage to the tile and adjacent tiles. Can be interrupted by silences. The tile gains Burning.";
		q2="Ancient Tongue: Blazing Pillars (4): Mark up to 5 nearby tiles within 4 range and channel for 2 turns. The tiles then deal 80 damage to occupants and adjacent tiles ignoring armor. Can be interrupted by silences. The tiles gain Burning.";
		q3="Ancient Tongue: Inferno (2): Mark an ally and channel for one turn. If cast on self, gain 20 armor for 1 turn. Then deal 60 damage to their adjacent enemies and heal them 40 for each enemy hit for 3 turns. Can be interrupted by silences.";
		q4="Forbidden Tongue: Death (8): Channel for 5 turns, then deal 600 damage to all enemies ignoring armor and shields.";
	}
	
	public void basicAttack(Hex h, int damage, boolean armor, boolean shield, boolean anotherTurn) {
		h.addEffect(new TileEffect("Burning",this,6,this,false,h));
		super.basicAttack(h, damage, armor, shield, anotherTurn);
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			h.color=Color.red;
		}
	}

	public void ability1(Hex h) {
		ArrayList<Hex> hexes = new ArrayList<Hex>() {{add(h);}};
		addBuff(new Channel("Channeling",this,1,this,false,"Ancient Tongue: Firestorm",hexes));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		if(queue2.size()<5) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)<=4) {
					h.color=Color.RED;
				}
			}
			for(Object h:queue2) {
				((Hex)h).color=null;
			}
		}
		position.color=Color.green;		
	}
	
	public void clearAb2() {
		queue2.clear();
		grid.game.setButtons();
	}

	public void ability2(Hex h) {
		if(h==position) {
			ArrayList<Hex> affected = new ArrayList<Hex>();
			for(Object h1:queue2) {
				affected.add((Hex) h1);
			}
			addBuff(new Channel("Channeling",this,2,this,false,"Ancient Tongue: Blazing Pillars",affected));
			abcdDelay[1]=true;
			grid.game.endOfTurn();
		}else if(queue2.size()<5) {
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
		}
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		ArrayList<Hex> hexes = new ArrayList<Hex>() {{add(h);}};
		addBuff(new Channel("Channeling",this,1,this,false,"Ancient Tongue: Inferno",hexes));
		if(h==position) {
			addBuff(new Buff("Ancient Tongue: Inferno",this,1,this,false));
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		addBuff(new Channel("Channeling",this,5,this,false,"Forbidden Tongue: Death",null));
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
