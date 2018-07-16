import java.awt.Color;

public class Veena extends Hero{

	Veena(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 450;
		currentHealth = maxHealth;
		maxStamina = 65;
		currentStamina = 0;
		basicDamage = 50;
		defaultArmor = 10;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 5;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 1;
		ultcdMax = 8;			
		qU="A curse-oriented specialist who chips away at her enemies by constantly applying debuffs, she is best at range while avoiding high damage enemies who can burst her down before she can respond.";
		qP="Virulent Toxins: Basic Attacks apply a stack of Virulent Toxins on the target, dealing 10 damage at the start of their turn ignoring armor and shield. Stacks last 3 turns, additional applications refresh the duration.";
		q1="Jungle Mamba (2): Basic attack a nearby enemy within 2 range and apply a stack of Curse for 1 turn, preventing their health from increasing until it disappears.";
		q2="Blinding Venom (3): Roots a target within 3 range for 1 turn and Blinds them for 3 turns, preventing them from attacking non-adjacent targets.";
		q3="Striped Viper (1): Basic attack an adjacent target and Root them for 1 turn.";
		q4="Mother of Snakes (8): Basic attack all enemies, healing for 20 per attack, and Curse all enemies for 2 turns.";
	}
	
	public void basicAttack(Hex h, int damage, boolean armor, boolean shield, boolean anotherTurn) {
		addDebuff(new Debuff("Virulent Toxins",h.occupied,3,this,false));
		super.basicAttack(h, damage, armor, shield, anotherTurn);
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=2&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		basicAttack(h,basicDamage,true,true,true);
		addDebuff(new Debuff("Cursed",h.occupied,1,this,false));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=3&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		addDebuff(new Debuff("Rooted",h.occupied,1,this,false));
		addDebuff(new Debuff("Blinded",h.occupied,3,this,false));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		basicAttack(h,basicDamage,true,true,true);	
		addDebuff(new Debuff("Rooted",h.occupied,1,this,false));
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		for(Unit u:enemyTeam) {
			basicAttack(u.position,basicDamage,true,true,true);
			heal(20);
			addDebuff(new Debuff("Cursed",u,2,this,false));
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
