import java.awt.Color;
import java.util.ArrayList;

public class Magmus extends Hero{
	boolean preventLoop = false;
	
	Magmus(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 650;
		currentHealth = maxHealth;
		maxStamina = 65;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 30;
		currentArmor = defaultArmor;
		armorPiercing = 60;
		basicRange = 1;
		ab1cdMax = 4;
		ab2cdMax = 2;
		ab3cdMax = 3;
		ultcdMax = 8;		
		qU="A powerful bruiser that excels head-to-head against groups of enemies, Magmus can quickly blast away weakened enemies with wave after wave of area blows, or soften them up for an allied sniper to pick off those who run. His high health also means that he can survive on the front lines despite a lack of self-heal. His abilities can damage allies as well as enemies.";
		qP="Magma Core: When this unit is attacked by an enemy, if the attacker is within two tiles, deal 40 damage to the attacker ignoring shields and armor.";
		q1="Molten Blast (4): Deal 40 damage in a straight line to all occupants for 3 tiles. Those hit take additional 20 damage at the start of their turn ignoring armor and shield for 3 turns.";
		q2="Volcanic Detonation (2): Deal 20 damage to adjacent tiles and stun occupants for 1 turn.";
		q3="Lava Fall (3): Teleport to an unoccupied tile up to 5 tiles away and deal 30 damage to all adjacent tiles.";
		q4="Magmatic Shockwave (8): Deal 70 damage to a target tile within 3 range and all tiles adjacent to it. If this only hits one person, deal 100 damage instead and ignore shields and armor.";
	}

	public int takeBasic(int damage, Unit attacker, boolean armor, boolean shield) {//process armor
		damage = super.takeBasic(damage,attacker,armor,shield);
		if(attacker.team!=team&&attacker.position.distance(position)<=2&&!preventLoop) {
			preventLoop = true;
			attacker.takeAbility(40, this, false, false);
			preventLoop = false;
		}
		return damage;
	}
	
	public int takeAbility(int damage, Unit attacker,boolean armor,boolean shield) {
		damage = super.takeAbility(damage, attacker, armor, shield);
		if(attacker.team!=team&&attacker.position.distance(position)<=2&&!preventLoop) {
			preventLoop = true;
			attacker.takeAbility(40, this, false, false);
			preventLoop = false;
		}
		return damage;
	}
	
	public void showAb1() {	
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		ArrayList<Hex> hexes = position.findLine(grid, h);
		while(hexes.size()>3) {
			hexes.remove(3);
		}
		for(Hex h1:hexes) {
			h1.occupied.takeAbility(40, this, true, true);
			addDebuff(new Debuff("Molten Blast",h1.occupied,3,this,false));
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.occupied!=null) {
				h1.occupied.takeAbility(20, this, true, true);
				addDebuff(new Debuff("Stunned",h1.occupied,1,this,false));
			}
		}
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=5&&h.isEmpty()) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		setPosition(h);
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.occupied!=null) {
				h1.occupied.takeAbility(30, this, true, true);
			}
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=3) {
				h.color=Color.red;
			}
		}
	}

	public void ultimate(Hex h) {
		boolean only = true;
		for(Hex h1:grid.hexes) {
			if(h.distance(h1)==1&&h1.occupied!=null) {
				only= false;
			}
		}
		if(only) {
			if(h.occupied!=null) {
				h.occupied.takeAbility(100, this, false, false);
			}
		}else {
			for(Hex h1:grid.hexes) {
				if(h.distance(h1)==1&&h1.occupied!=null) {
					h1.occupied.takeAbility(70, this, true, true);
				}
			}
			h.occupied.takeAbility(70, this, true, true);
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
