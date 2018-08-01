import java.awt.Color;
import java.util.ArrayList;

public class Kaito_Clone extends PartialHero{

	Kaito_Clone(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}

	public void assembleStats() {
		name = "Clone";
		title = "Bladed Shadow";
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
		qP="Heart of Darkness: At the end of each turn, if there are no allies adjacent to you, Kaito gains 1 stack of Gathering Darkness. Brethren of Twilight: This unit can be selected to swap tiles with Kaito.";
		q1="Form of Night (1): Gain 2 stacks of Gathering Darkness and ignore the next damage taken. If you have less than half health, gain 50 current health.";
		q2="Shadow Step (3): Mark a tile and channel for 1 turn, then teleport to the tile, rooting adjacent enemies, and gain 2 stacks of Gathering Darkness.";
	}
	
	public void die() {
		getBuff("Brethren of Twilight").caster.buffs.remove(getBuff("Brethren of Twilight").caster.getBuff("Brethren of Twilight"));
		super.die();
	}
	
	public void endOfTurn() {
		boolean ally = false;
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasAlly(this)) {
				ally = true;
			}
		}
		if(!ally) {
			addBuff(new BuffStack("Gathering Darkness",getBuff("Brethren of Twilight").caster,-1,this,false,1));
		}
		if(hasBuff("Gathering Darkness")) {
			int stacks = ((BuffStack)getBuff("Gathering Darkness")).stacks;
			addBuff(new BuffStack("Gathering Darkness",getBuff("Brethren of Twilight").caster,-1,this,false,stacks));
			removeSameBuff("Gathering Darkness");
		}
		super.endOfTurn();		
	}

	public void showAb1() {
		ability1(null);
	}

	public void ability1(Hex h) {
		addBuff(new BuffStack("Gathering Darkness",getBuff("Brethren of Twilight").caster,-1,this,false,2));
		addBuff(new Buff("Block",this,-1,this,false));
		if(currentHealth<maxHealth/2) {
			heal(50);
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.isEmpty()) {
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
		Kaito_ShadowStep s = new Kaito_ShadowStep(grid,str,h,this);
		grid.game.occupants.add(s);
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	@Override
	public void showAb3() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability3(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showUlt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ultimate(Hex h) {
		// TODO Auto-generated method stub
		
	}
}
