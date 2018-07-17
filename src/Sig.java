import java.awt.Color;

public class Sig extends Hero{

	Sig(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 65;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 50;
		basicRange = 5;
		ab1cdMax = 3;
		ab2cdMax = 4;
		ab3cdMax = 1;
		ultcdMax = 8;	
		qU="Sig is a support type ranged character that uses his inventions to delay, harass, and abuse his enemies.";
		qP="Detonation Rounds:  Basic attacks deal an additional 10 damage to the target and adjacent enemies at the start of their next turn, ignoring shield and armor.";
		q1="Improvised Explosive (3): Marks a tile within 2 range with a bomb that detonates after 3 turns, dealing 70 damage to and Cursing the tile and all all adjacent targets for 1 turn. If the tile is occupied, the bomb instead immediately detonates but only hits the one target.";
		q2="D.M.R. Mine (4): Marks a target tile within 4 range with a mine that detonates when an enemy walks on it at the start of their turn, dealing 30 damage and stunning them for 1 turn. ";
		q3="Missile Bomb (1): Creates a pile of missiles on an unoccupied tile within 3 range with 100 health that detonates after 3 turns or death to deal 50 damage all enemies in its range. Can be immediately detonated if the caster basic attacks it.";
		q4="Ratchet Suit (8): For 3 turns the target ally’s attacks deal damage to adjacent tiles and they can attack twice per turn (enchant).";
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean anotherTurn) {
		addDebuff(new Debuff("Detonation Rounds",h.occupied,1,this,false));
		super.basicAttack(h, damage, armor, shield, anotherTurn);
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=2) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		if(h.occupied!=null) {
			h.occupied.takeAbility(70, this, true, true);
			addDebuff(new Debuff("Cursed",h.occupied,1,this,false));
		}else {
			h.effects.add(new TileEffect("Improvised Explosive",this,3,this,false,h));
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=4) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		h.effects.add(new TileEffect("D.M.R. Mine",this,-1,this,false,h));	
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=3&&h.occupied==null) {
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
		MissileBomb b = new MissileBomb(grid,"Missile Bomb",str,h,this);
		addDebuff(new Debuff("Timed Life",b,3,this,true));
		grid.game.occupants.add(b);
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ultimate(Hex h) {
		addBuff(new Buff("Ratchet Suit",h.occupied,3,this,true));
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
