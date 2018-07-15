import java.awt.Color;

public class Helena extends Hero{

	Helena(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 400;
		currentHealth = maxHealth;
		maxStamina = 65;
		currentStamina = 0;
		basicDamage = 30;
		defaultArmor = 30;
		currentArmor = defaultArmor;
		armorPiercing = 50;
		basicRange = 5;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 1;
		ultcdMax = 8;			
	}
	
	public void showBasic() {
		int stacks;
		if(hasBuff("One With The Earth")) {
			stacks = ((BuffStack)getBuff("One With The Earth")).stacks;
		}else {
			stacks = 0;
		}
		basicRange = 5+stacks;
		super.showBasic();
	}
	
	public void startOfTurn() {
		rewriteBuff(new BuffStack("One With The Earth",this,-1,this,false,1,9),buffs);
		super.startOfTurn();
	}
	
	public void move(Hex h) {
		if(hasBuff("One With The Earth")) {
			removeSameBuff("One With The Earth");
		}
		super.move(h);
	}

	public void showAb1() {
		ability1(null);
	}

	public void ability1(Hex h) {
		int stacks;
		if(hasBuff("One With The Earth")) {
			stacks = ((BuffStack)getBuff("One With The Earth")).stacks;
		}else {
			stacks = 0;
		}
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)<=1+stacks&&h1.hasEnemy(this)) {
				h1.occupied.takeAbility(10+(stacks*10/2), this, true, true);
			}
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		int stacks;
		if(hasBuff("One With The Earth")) {
			stacks = ((BuffStack)getBuff("One With The Earth")).stacks;
		}else {
			stacks = 0;
		}
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)<=1+stacks&&h1.hasEnemy(this)) {
				addDebuff(new Debuff("Rooted",h1.occupied,1,this,false));
			}
		}
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(h.occupied==null||h.occupied instanceof Tree) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		if(h.occupied==null) {
			String str;
			if(team==grid.game.team1) {
				str="Team 1";
			}
			else {
				str="Team 2";
			}
			Tree b = new Tree(grid,"Tree",str,h);
			grid.game.occupants.add(b);
		}else {
			h.clearHex();
			setPosition(h);
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}
	
	public boolean ableUlt() {
		if(hasBuff("One With The Earth")&&((BuffStack)getBuff("One With The Earth")).stacks>0) {
			return true;
		}
		return false;
	}

	public void ultimate(Hex h) {
		int stacks;
		stacks = ((BuffStack)getBuff("One With The Earth")).stacks;
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)<=1+stacks&&h1.hasAlly(this)) {
				addBuff(new BuffStack("Rampage",h1.occupied,-1,this,false,stacks,9));
			}
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();		
	}

}
