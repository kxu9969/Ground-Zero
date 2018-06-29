import java.awt.Color;

public class Serenity extends Hero{

	Serenity(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 20;
		armor = 60;
		armorPiercing = 40;
		basicRange = 1;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 1;
		ultcdMax = 8;			
	}
	
	public void basicAttack(Hex h) {
		int damage = basicDamage;
		if(currentShield>200) {
			damage+=70;
		}else if(currentShield>100) {
			damage+=50;
		}else if(currentShield>50) {
			damage+=30;
		}
		super.basicAttack(h,damage);
	}

	public void showAb1() {
		ability1(null);
	}

	public void ability1(Hex h) {
		gainShield(100);
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)==1&&h1.hasEnemy(this)){
				gainShield(20);
			}
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)){
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		int damage= 40+currentShield;
		h.occupied.takeAbility(damage, this, true, true);
		addDebuff(h.occupied,new Debuff("Stunned",this,1,false));
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
		int shield = h.occupied.currentShield;
		h.occupied.currentShield=0;
		gainShield(shield);
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		for(Unit u:team) {
			u.gainShield(200);
		}
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

	@Override
	public void ultimate(Hex h) {
		// TODO Auto-generated method stub
		
	}

}
