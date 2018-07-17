import java.awt.Color;
import java.util.ArrayList;

public class Amon extends Hero{

	Amon(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {//test passive proc on not his turn, start of his turn, end of his turn
		maxHealth = 400;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 50;
		defaultArmor = 10;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 5;
		ab1cdMax = 2;
		ab2cdMax = 2;
		ab3cdMax = 3;
		ultcdMax = 8;	
		qU="A specialist character who excels at creating and manipulating the battlefield through his undead legions, Amon can trap enemies with Skeletons, attack fortified locations with Corpses, strike from afar with Liches, and even temporarily tether deceased allies to the fight.";
		qP="Necrotic Tether:  When an ally hero dies, they get one more turn. However, they are Cursed(enchant) at 0 health.";
		q1="Raise Legions (3): Summon up to 6 Skeletons to surround a tile. Lasts 3 of their turns.";
		q2="Shambling Corpses (2): Summon a Corpse on an adjacent tile that explodes when it dies. Can be manually detonated, lasts 4 turns.";
		q3="Undead Guard (3): Summon a Lich on an adjacent tile. Liches deal area of effect ranged damage.";
		q4="Prince’s Command (8): Detonate all summons, causing each to deal 40 damage to adjacent tiles. Gain 20 shield for each detonated.";
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			h.color=Color.red;
		}
	}

	public void ability1(Hex h) {
		ArrayList<Hex> affected = new ArrayList<Hex>();
		for(Hex h1:grid.hexes) {
			if(h.distance(h1)==1&&h1.occupied==null) {
				affected.add(h1);
			}
		}
		for(Hex h1:affected) {
			String str;
			if(team==grid.game.team1) {
				str="Team 1";
			}
			else {
				str="Team 2";
			}
			Skeleton b = new Skeleton(grid,"Skeleton",str,h1,this);
			grid.game.addUnit(b);
			addDebuff(new Debuff("Timed Life",b,3,this,true));
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.occupied==null) {
				h.color=Color.red;
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
		Corpse b = new Corpse(grid,"Corpse",str,h,this);
		grid.game.addUnit(b);
		addDebuff(new Debuff("Timed Life",b,4,this,true));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.occupied==null) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		String str;
		if(team==grid.game.team1) {
			str="Team 1";
		}
		else {
			str="Team 2";
		}
		Lich b = new Lich(grid,"Lich",str,h,this);
		grid.game.addUnit(b);
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		ArrayList<Summon> summons = new ArrayList<Summon>();
		for(Unit u:team) {
			if(u instanceof Summon&& ((Summon)u).owner==this) {
				summons.add((Summon) u);
			}
		}
		for(Summon u:summons ) {
			for(Hex h1:grid.hexes) {
				if(u.position.distance(h1)==1&&h1.hasEnemy(u)) {
					h1.occupied.takeAbility(40, u, true, true);
				}
			}
			u.die();
			gainShield(20);
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
