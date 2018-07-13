import java.awt.Color;

public class October extends Hero{

	October(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
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
		h.effects.add(new TileEffect("Thunder and Storm",this,6,this,false,h));
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
		addDebuff(new Mark("Marked",h.occupied,4,this,false));
		addDebuff(new Mark("Hunter and Prey",h.occupied,4,this,false));
		if(position.distance(h)==1) {
			setStamina();
		}
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
