import java.awt.Color;
import java.util.ArrayList;

public class Alpha extends Hero{

	Alpha(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 350;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 30;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 50;
		basicRange = 5;
		ab1cdMax = 4;
		ab2cdMax = 5;
		ab3cdMax = 8;
		ultcdMax = 8;
		qU="An escaped military project, ALPHA.XD is an android with a humanity overwritten by her programming. Elusive and adaptive, she is constantly learning even in the midst of combat in a quest to maximize value.";
		qP="System Purge: Having a stun or root applied, including overwrites, grants 20 stamina.";
		q1="Disengage (4): Vault over an adjacent enemy onto an unoccupied tile, stunning then for 1 turn. If you lands adjacent to a different enemy, you can use Disengage again. You can move afterwards.";
		q2="Vent Exhaust (5): Deal 40 damage to all adjacent enemies and silence them for 1 turn. Cooldown is reduced by 1 for each target hit.";
		q3="Overclock Core (8): Increase basic attack damage by 40 for 5 turns. After, be silenced for 2 turns and gain 1 stack of Overclocked (enchant). Each stack reduces the duration of Overclock Core by a turn.";
		q4="Adaptive Programming (8): Grants a stack of Adaptive Programming(enchant). Each stack grants an additional basic attack whenever you basic attack.";
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				ArrayList<Hex> hexes = position.extendLineAdj(grid, h);
				if(hexes.size()>1&&hexes.get(1).occupied == null) {
					hexes.get(1).color=Color.red;
				}
			}
		}
	}

	public void ability1(Hex h) {
		Hex difference = h.subtract(position);
		Hex half = new Hex(difference.q/2,difference.r/2,difference.s/2);
		Hex target = position.add(half);
		target = grid.getHex(target);
		setPosition(h);
		grid.game.move.lock=false;
		grid.game.basic.lock=true;
		grid.game.ab1.lock=true;
		grid.game.ab2.lock=true;
		grid.game.ab3.lock=true;
		grid.game.ult.lock=true;
		grid.game.cancel.lock=false;
		grid.game.pass.lock=false;
		for(Hex h1:h.allAdjacents()) {
			h1=grid.getHex(h1);
			if(h1!=null&&h1.hasEnemy(this)&&!h1.equals(target)) {
				grid.game.ab1.lock=false;
			}
		}
		grid.game.clear();
		grid.game.setButtons();
		abcdDelay[0]=true;
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h1) {
		ab2cdMax = 5;
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.occupied.takeAbility(40, this, true, true);
				addDebuff(new Debuff("Silenced",h.occupied,1,this,false));
				ab2cdMax--;
			}
		}
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		ability3(null);
	}
	
	public boolean ableAb3() {
		if(hasDebuff("Overclocked")&&((DebuffStack)getDebuff("Overclocked")).stacks>=5) {
			return false;
		}
		return true;
	}

	public void ability3(Hex h) {
		int duration = 5;
		if(hasDebuff("Overclocked")) {
			duration-=((DebuffStack)getDebuff("Overclocked")).stacks;
		}
		addBuff(new Buff("Overclock Core",this,duration,this,false));
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		addBuff(new BuffStack("Adaptive Programming",this,-1,this,true,1));
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
