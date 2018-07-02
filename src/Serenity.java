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
		defaultArmor = 60;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 1;
		ab1cdMax = 2;
		ab2cdMax = 3;
		ab3cdMax = 1;
		ultcdMax = 8;	
		qU="A shield oriented hero whose damage scales with her shield level, Serenity thrives on the front lines where she can quickly fill up her shields and then blast down enemy heroes.";
		qP="Secondary Impact: Basic attacks scale in damage based on your shield. If your shield is less than 51, there is no bonus. From 51 to 100, deal additional 30 damage. From 101 to 200, deal additional 50 damage. From 201 and above, deal additional 70 damage.";
		q1="Divine Faith (2): Gain a shield for 100 and 20 for each adjacent enemy.";
		q2="Holy Retribution (3): Deal 40 damage to an adjacent enemy, dealing additional damage equal to your shield and stun them for 1 turn.";
		q3="Repentance (1): Strip an adjacent enemy of its shield and gain it.";
		q4="Light of Faith (8): Give all allies including yourself 200 shield, regain stamina immediately.";
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean end) {
		damage = basicDamage;
		if(currentShield>200) {
			damage+=70;
		}else if(currentShield>100) {
			damage+=50;
		}else if(currentShield>50) {
			damage+=30;
		}
		super.basicAttack(h,damage,armor,shield,end);
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
		addDebuff(new Debuff("Stunned",h.occupied,1,this,false));
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
