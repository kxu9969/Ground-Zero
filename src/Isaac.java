import java.awt.Color;
import java.util.ArrayList;

public class Isaac extends Hero{

	Isaac(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 150;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 20;
		defaultArmor = 0;
		currentArmor = defaultArmor;
		armorPiercing = 100;
		basicRange = 1;
		ab1cdMax = 1;
		ab2cdMax = 4;
		ab3cdMax = 5;
		ultcdMax = 8;	
		qU="A hard-to-kill tank despite his stats, Isaac excels at making his team hard to kill. His passive makes him a priority target for the enemy team, which will try to kill him early on to overwrite Original Sin before they can get value. His ability to grant damage immunity and nullify enemy engages makes him excellent on defense, and his ultimate can save an ally on the brink.";
		qP="Divine Scapegoat: At the start of the game, all other allies gain Original Sin(enchant). Upon death, they are instead set to half health and lose the buff. On Isaac’s death, all other allies gain Original Sin(this overwrites the first).";
		q1="Blessed Purge (1): Removes all debuffs on an ally.";
		q2="Holy Guard (4): Mark an ally. Until their next turn, any damage they take is reduced to zero.";
		q3="Angelic Command (5): Target a tile. Place it and all adjacent tiles into stasis for 6 turns.";
		q4="Great Equalizer (8): Set the current health of all other heroes at 300 and set their stamina back down to 0.";
	}
	
	public void die() {
		for(Unit u:team) {
			if(u !=this) {
				addBuff(new Buff("Original Sin",u,-1,this,true));
			}
		}
		super.die();
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		ArrayList<Debuff> toBeRemoved = new ArrayList<Debuff>();
		for(Debuff b:h.occupied.debuffs) {
			toBeRemoved.add(b);
		}
		for(Debuff b:toBeRemoved) {
			h.occupied.removeDebuff(b);
		}		
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		addBuff(new Buff("Holy Guard",h.occupied,1,this,false));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			h.color=Color.red;
		}
	}

	public void ability3(Hex h) {
		ArrayList<Hex> affected = new ArrayList<Hex>();
		for(Hex h1:h.allAdjacents()) {
			affected.add(grid.getHex(h1));
		}
		affected.add(h);
		for(Hex h1:affected) {
			h1.addEffect(new TileEffect("Stasis",this,6,this,false,h1));
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {	
		ultimate(null);
	}

	public void ultimate(Hex h) {
		for(Unit u: grid.game.units) {
			if(u != this) {
				u.currentHealth = 300;
				u.currentStamina = 0;
			}
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

	public ArrayList<Unit> inAura(){
		ArrayList<Unit> affected = team;
		affected.remove(this);
		return affected;
	}
	public void addAura(Unit u) {
		addBuff(new Buff("Original Sin",u,-1,this,true));
	}
	public void removeAura(Unit u) {
	}
}
