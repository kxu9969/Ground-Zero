import java.awt.Color;
import java.util.ArrayList;

public class Asger extends Hero{

	Asger(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 50;
		defaultArmor = 30;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 5;
		ab1cdMax = 4;
		ab2cdMax = 2;
		ab3cdMax = 3;
		ultcdMax = 8;
		qU="A potential damage dealer that scales in power with his missing health, Asger is able to build up damage by hurting himself. His abilities allow him to rapidly shoot up in strength, but he is vulnerable when he cannot deal the damage he needs and can be picked off from safety.";
		qP="Fifth Ring, First Sign: When under half health, attacks deal additional 40 damage and stamina is reduced by 30.";
		q1="Third Ring, Fourth Sign (4): Deal 20 damage to yourself, attack all adjacent enemies and silence them for 1 turn.";
		q2="Third Ring, Second Sign (2): Deal 10 damage to yourself, can attack twice for 2 turns. Regain stamina immediately.";
		q3="Second Ring, Fourth Sign (3): Link to an ally, increasing your and their damage by 30 and sharing damage taken (armor calculated independently). Lasts 2 turns.";
		q4="First Ring, First Sign (8): Deal damage to an adjacent target equal to your missing health, ignoring armor and shield.";
	}
	
	public ArrayList<Unit> inAura(){
		ArrayList<Unit> self = new ArrayList<Unit>();
		if(currentHealth<maxHealth/2) {
			self.add(this);
		}
		return self;
	}
	public void addAura(Unit u) {
		u.maxStamina-=30;
		u.basicDamage+=40;
	}
	public void removeAura(Unit u) {
		u.maxStamina+=30;
		u.basicDamage-=40;
	}

	public void showAb1() {
		ability1(null);
	}

	public void ability1(Hex h) {
		takeAbility(20,this,false,false);
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.hasEnemy(this)) {
				basicAttack(h1);
				addDebuff(new Debuff("Silenced",h1.occupied,1,this,false));
			}
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		takeAbility(10,this,false,false);
		addBuff(new Buff("Third Ring, Second Sign",this,2,this,false));
		setStamina();
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h: grid.hexes) {
			if(h.hasAlly(this)&&!h.equals(position)) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		addBuff(new Buff("Second Ring, Fourth Sign",this,3,h.occupied,false));
		addBuff(new Buff("Second Ring, Fourth Sign",h.occupied,2,this,false));
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ultimate(Hex h) {
		int damage = maxHealth-currentHealth;
		h.occupied.takeAbility(damage, this, false, false);
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
