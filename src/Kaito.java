import java.awt.Color;
import java.util.ArrayList;

public class Kaito extends Hero{

	Kaito(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 400;
		currentHealth = maxHealth;
		maxStamina = 70;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 30;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 1;
		ab1cdMax = 1;
		ab2cdMax = 3;
		ab3cdMax = 2;
		ultcdMax = 8;
		qU="A simultaneous tank and assassin, Kaito is able to armor himself up and protect his allies before suddenly bursting out a large amount of damage to a single target. This versatility allows him to adapt strategically to teamfights and to eliminate lone targets.";
		qP="Heart of Darkness: At the end of each turn, if there are no allies adjacent to you, gain 1 stack of Gathering Darkness, giving you +5 Armor. Holds up to 9 stacks.";
		q1="Form of Night (1): Gain 2 stacks of Gathering Darkness and ignore the next damage taken. If you have less than half health, gain 50 current health.";
		q2="Shadow Step (3): Mark a tile and channel for 1 turn, then teleport to the tile, rooting adjacent enemies, and gain 2 stacks of Gathering Darkness.";
		q3="Lethality (2): Consume all stacks of Gathering Darkness to increase the damage of your next basic attack by 10 per. Refill stamina immediately.";
		q4="Brethren of Twilight (8): Summon a Clone on a nearby vacant tile with half your max health and your abilities. Gathering Darkness stacks from the clone will transfer onto you. When a Clone is alive, the cooldown of this ability is 4 and you can reactivate to switch tiles with the Clone. When the Clone dies, the cooldown of this ability becomes 8 and the ability will summon another Clone.";
	}
	
	public void endOfTurn() {
		boolean ally = false;
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasAlly(this)) {
				ally = true;
			}
		}
		if(!ally) {
			addBuff(new BuffStack("Gathering Darkness",this,-1,this,false,1));
		}
		super.endOfTurn();
	}

	public void showAb1() {
		ability1(null);
	}

	public void ability1(Hex h) {
		addBuff(new BuffStack("Gathering Darkness",this,-1,this,false,2));
		addBuff(new Buff("Block",this,-1,this,false));
		if(currentHealth<maxHealth/2) {
			heal(50);
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.occupied==null) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		ArrayList<Hex> affected = new ArrayList<Hex>() {{add(h);}};
		addBuff(new Channel("Channeling",this,1,this,false,"Shadow Step",affected));
		String str;
		if(team==grid.game.team1) {
			str="Team 1";
		}
		else {
			str="Team 2";
		}
		ShadowStep s = new ShadowStep(grid,"Shadow Step",str,h,this);
		grid.game.occupants.add(s);
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {	
		ability3(null);
	}

	public void ability3(Hex h) {	
		if(hasBuff("Gathering Darkness")) {
			addBuff(new BuffStack("Lethality",this,-1,this,false,((BuffStack)getBuff("Gathering Darkness")).stacks,99));
			removeSameBuff("Gathering Darkness");
		}
		setStamina();
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		if(!hasBuff("Brethren of Twilight")) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.occupied==null) {
					h.color=Color.red;
				}
			}
		}else {
			ultimate(null);
		}
	}

	public void ultimate(Hex h) {
		if(!hasBuff("Brethren of Twilight")) {
			String str;
			if(team==grid.game.team1) {
				str="Team 1";
			}
			else {
				str="Team 2";
			}
			Clone c = new Clone(grid,"Clone",str,h);
			c.maxHealth=maxHealth/2;
			if(c.currentHealth>c.maxHealth) {
				c.currentHealth=c.maxHealth;
			}
			c.hasAb3=false;
			c.hasUlt=false;
			addBuff(new Buff("Brethren of Twilight",this,-1,c,true));
			addBuff(new Buff("Brethren of Twilight",c,-1,this,true));
			grid.game.addUnit(c);
			ultcdMax=4;
			grid.game.endOfTurn();
		}else {
			Hex other = getBuff("Brethren of Twilight").caster.position;
			Hex temp = new Hex(other.q,other.r,other.s);
			Hex temp2 = new Hex(position.q,position.r,position.s);
			setPosition(temp);
			getBuff("Brethren of Twilight").caster.setPosition(temp2);
			setPosition(temp);
			abcdDelay[3]=true;
			grid.game.endOfTurn();
		}
	}

}
