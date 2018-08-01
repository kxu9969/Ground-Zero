import java.awt.Color;

public class Wrock extends Hero{

	Wrock(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}

	public void assembleStats() {
		name = "Wrock";
		title = "Rumble in the Mountain";
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 65;
		currentStamina = 0;
		basicDamage = 20;
		defaultArmor = 50;
		currentArmor = defaultArmor;
		armorPiercing = 60;
		basicRange = 1;
		ab1cdMax = 4;
		ab2cdMax = 5;
		ab3cdMax = 4;
		ultcdMax = 8;
		qU="An earth elemental tasked by the gods with herding young mountains, Wrock fights poachers and earthquakes that would seek to disturb his flock.";
		qP="Earthen Resilience: Whenever Wrock takes damage, gain a stack of Earthen Resilience, granting him 10 armor. Lose these stacks at the start of his next turn.";
		q1="From Below (4): Spear an enemy within 3 range with a column of stone, dealing 40 damage and rooting them until Wrock casts this again or is silenced, stunned, or placed in stasis.";
		q2="From Above (5): Throw an adjacent ally onto an empty nearby tile within 4 range, dealing 50 damage to adjacent tiles. Grant the ally Shield of Earth for 1 turn, making Wrock take their damage instead.";
		q3="From Within (4): Gain 50 shield at the start of your turns and reduce your max stamina by 25 for two turns.";
		q4="From All Sides (8): Trap an enemy in a prison of earth, creating up to 6 permanent spears on empty adjacent tiles that block movement. Attempting to move onto a spear tile deals 60 damage ignoring armor and shield and stuns for 2 turns, consuming the spear. The spears have 150 health and can be destroyed by damage.";
	}
	
	public void startOfTurn() {
		super.startOfTurn();
		if(hasBuff("Earthen Resilience")) {
			removeSameBuff("Earthen Resilience");
		}
	}
	
	public int takeBasic(int damage,Unit attacker,boolean armor,boolean shield) {
		int i = super.takeBasic(damage, attacker, armor, shield);
		rewriteBuff(new BuffStack("Earthen Resilience",this,-1,this,false,1),buffs);
		return i;
	}
	
	public int takeAbility(int damage,Unit attacker,boolean armor, boolean shield) {
		int i = super.takeAbility(damage, attacker, armor, shield);
		rewriteBuff(new BuffStack("Earthen Resilience",this,-1,this,false,1),buffs);
		return i;
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=3&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		if(hasBuff("From Below")) {
			removeSameBuff("From Below");
		}
		addBuff(new Buff("From Below",this,-1,h.occupied,false));
		h.occupied.takeAbility(40, this, true, true);
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		if(queue2.size()==0) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.hasAlly(this)) {
					h.color=Color.red;
				}
			}
		}else if(queue2.size()==1) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)<=4&&h.isEmpty()) {
					h.color=Color.red;
				}
			}
		}
	}
	
	public void clearAb2() {
		queue2.clear();
		grid.game.setButtons();
	}

	public void ability2(Hex h) {
		if(queue2.size()==0) {
			queue2.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showAb2();
		}
		else if(queue2.size()==1) {
			((Hex)queue2.get(0)).occupied.setPosition(h);
			for(Hex h1:grid.hexes) {
				if(h.distance(h1)==1&&h1.occupied!=null) {
					h1.occupied.takeAbility(50,this,true,true);
				}
			}
			addBuff(new Buff("Shield of Earth",h.occupied,1,this,false));
			abcdDelay[1]=true;
			grid.game.endOfTurn();
		}
	}

	public void showAb3() {
		ability3(null);
	}

	public void ability3(Hex h) {
		addBuff(new Buff("From Within",this,2,this,false));
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ultimate(Hex h) {
		for(Hex h1:grid.hexes) {
			if(h1.distance(h)==1&&h1.occupied==null) {
				String str;
				if(team==grid.game.team1) {
					str="Team 1";
				}
				else {
					str="Team 2";
				}
				Wrock_Spear b = new Wrock_Spear(grid,str,h1,this);
				grid.game.occupants.add(b);			}
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
