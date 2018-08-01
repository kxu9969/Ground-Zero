import java.awt.Color;

public class October extends Hero{

	October(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}

	public void assembleStats() {
		name = "October";
		title = "Baleful Chant";
		maxHealth = 400;
		currentHealth = maxHealth;
		maxStamina = 65;
		currentStamina = 0;
		basicDamage = 0;
		defaultArmor = 10;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 1;
		ab1cdMax = 5;
		ab2cdMax = 3;
		ab3cdMax = 1;
		ultcdMax = 8;		
		qU="An old, feral werewolf, he consumed the essence of a wandering magus on the eve of his namesake. Now he scours the land for more prey, hoping to increase his arcane prowess.";
		qP="Shackles and Fetters: October cannot basic attack.";
		q1="Madness and Agony (5): Deal 60 damage to an enemy within 5 range and stun them for 1 turn, dealing 60 damage again at the start of their stunned turn.";
		q2="Thunder and Storm (3): Mark a tile within 4 range for 6 turns. Occupants take 30 damage ignoring armor at the start and end of their turn and are Cursed for 2 turns.";
		q3="Scour and Burn (1): Deal 50 damage to an enemy within 3 range. If they are Marked by October, deal 80 damage instead, ignoring armor and shield, and reset the cooldown of this ability.";
		q4="Hunter and Prey (8): Mark an enemy within 3 range for 4 turns and reset the cooldowns of your other abilities. If the Marked enemy is adjacent to you, refill your stamina immediately. If the Marked enemy ends their turn adjacent to you, refill your stamina immediately.";
	}
	
	public boolean ableBasic() {
		return false;
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=5&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		h.occupied.takeAbility(60,this,true,true);
		addDebuff(new Debuff("Stunned",h.occupied,1,this,false));
		addDebuff(new Debuff("Madness and Agony",h.occupied,1,this,false));
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
		h.addEffect(new TileEffect("Thunder and Storm",this,6,this,false,h));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=3&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		if(h.occupied.hasMark(this)) {
			h.occupied.takeAbility(80,this,false,false);
			ab3cdMax=0;
		}else {
			h.occupied.takeAbility(50,this,true,true);
			ab3cdMax=1;
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=3&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}		
	}

	public void ultimate(Hex h) {
		ab1cd=0;
		ab2cd=0;
		ab3cd=0;
		addDebuff(new Mark("Marked",h.occupied,5,this,false));
		addDebuff(new Mark("Hunter and Prey",h.occupied,5,this,false));
		if(position.distance(h)==1) {
			setStamina();
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
