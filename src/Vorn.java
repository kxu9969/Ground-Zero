import java.awt.Color;
import java.util.ArrayList;

public class Vorn extends Hero{

	Vorn(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 350;
		currentHealth = maxHealth;
		maxStamina = 45;
		currentStamina = 0;
		basicDamage = 80;
		defaultArmor = 20;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 1;
		ab1cdMax = 4;
		ab2cdMax = 6;
		ab3cdMax = 0;
		ultcdMax = 8;
		qU="A fast-hitting melee character, Vorn specializes in gunning down individuals and flitting around the map while protecting himself from direct damage, being rather flimsy himself.";
		qP="Transience: Debuffs expire one turn sooner, with a minimum of one turn.";
		q1="Chronal Prison (4): Place an adjacent tile in stasis for 8 turns. After it expires, increase the cooldown of any occupant’s basic abilities by 1 turn.";
		q2="Temporal Relativity (6): Mark your current health. After 4 turns or upon death, set your health to that value. Refill stamina immediately.";
		q3="Accelerate (0): The next time you move, it doesn’t consume stamina.";
		q4="Anachronic Existence (8): Make all current buffs have infinite durations. Refill stamina immediately.";
	}
	
	public void tickDebuffs() {
		ArrayList<Debuff> toBeRemoved = new ArrayList<Debuff>();
		for(Debuff b:debuffs) {
			if(!(b instanceof Mark)) {
				if(b.duration>1) {
					b.duration--;
				}if(b.duration==0||b.duration==1) {
					toBeRemoved.add(b);
				}
			}
		}
		for(Debuff b:toBeRemoved) {
			removeDebuff(b);
		}
	}

	public void showAb1() {
		for(Hex h:position.allAdjacents()) {
			grid.getHex(h).color=Color.red;
		}
	}

	public void ability1(Hex h) {
		h.addEffect(new TileEffect("Chronal Prison",this,8,this,false,h));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		Buff b = new Buff("Temporal Relativity",this,4,this,false);
		b.info.add(currentHealth);
		addBuff(b);
		setStamina();
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		ability3(null);
	}

	public void ability3(Hex h) {
		addBuff(new Buff("Accelerate",this,-1,this,false));
		abcdDelay[2]=true;
		grid.game.endOfTurn();		
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		for(Buff b:buffs) {
			b.duration=-1;
		}
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
