import java.awt.Color;
import java.util.ArrayList;

public class Malor extends Hero{
	boolean preventLoop = false;
	
	Malor(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 350;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 70;
		defaultArmor = 10;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 1;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 3;
		ultcdMax = 8;
		qU="A powerful damage dealer, Malor is able to quickly counterattack and steal life from his enemies. His attacks are able to deal heavy area damage but his low health means he requires careful positioning.";
		qP="Vampiric Chains: Counterattacks if possible, attacks have lifesteal.";
		q1="Whiplash (2): Attacks an adjacent enemy twice, ignoring armor.";
		q2="Impale (3): Deal 40 damage to all adjacent tiles, healing 20 for each target hit.";
		q3="Whirling Scythes (3): Can move and basic attack for 2 turns. Refill stamina immediately.";
		q4="The Culling (8): Deal damage in a line, ignoring armor and shields, healing 40 for each target hit.";
	}
	
	public int takeBasic(int damage, Unit attacker, boolean armor, boolean shield) {//process armor
		damage = super.takeBasic(damage,attacker,armor,shield);
		if(attacker.position.distance(position)==1&&!preventLoop) {
			preventLoop = true;
			basicAttack(attacker.position,basicDamage,true,true,true);
			preventLoop = false;
		}
		return damage;
	}
	
	public int takeAbility(int damage, Unit attacker,boolean armor,boolean shield) {
		damage = super.takeAbility(damage, attacker, armor, shield);
		if(attacker.position.distance(position)==1) {
			basicAttack(attacker.position,basicDamage,true,true,true);
		}
		return damage;
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.color=Color.RED;
			}
		}
	}

	public void ability1(Hex h) {
		basicAttack(h,basicDamage,false,true,true);
		basicAttack(h,basicDamage,false,true,true);
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.hasEnemy(this)) {
				h1.occupied.takeAbility(40, this, true, true);
				heal(20);
			}
		}
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		 ability3(null);
	}

	public void ability3(Hex h) {
		this.addBuff(new Buff("Whirling Scythes",this,2,this,false));
		setStamina();
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Hex h: grid.hexes) {
			if(position.distance(h)==1) {
				h.color=Color.RED;
			}
		}
	}

	public void ultimate(Hex h) {
		ArrayList<Hex> hexes = position.findLine(grid, h);
		for(Hex h1:hexes) {
			if(h1.hasEnemy(this)) {
				int damage = h1.occupied.takeAbility(50, this, false, false);
				heal(damage);
			}
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
