import java.awt.Color;

public class Kaluk extends Hero{

	Kaluk(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 400;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 60;
		defaultArmor = 20;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 1;
		ab1cdMax = 1;
		ab2cdMax = 2;
		ab3cdMax = 2;
		ultcdMax = 8;	
		qU="A fast moving damage dealer, Kaluk is an excellent surgical striker that can quickly travel past obstacles to take advantage of weak spots in the enemy.";
		qP="Blood Spear:  Basic attacks Mark targets, allowing you to attack them from range. Marks last for 3 turns.";
		q1="Charging Strike (1): Dash to an enemy within 3 range, attack them twice, and stun them for 1 turn.";
		q2="Centered Stance (2): Increases damage by 20 and armor piercing by 10 for 3 turns. Refill stamina immediately.";
		q3="Pounce (2): Leap to a nearby tile within 5 range, refills stamina immediately.";
		q4="Hunting Rites (8): Fill stamina for all allies and give all allies additional 20 damage for 2 turns (enchant).";
	}
	
	public void showBasic() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=5&&h.hasEnemy(this)&&h.occupied.hasMark(this)) {
				h.color=Color.red;
			}
		}
		super.showBasic();
	}
	
	public void basicAttack(Hex h,int damage,boolean armor, boolean shield, boolean end) {
		addDebuff(new Mark("Marked",h.occupied,4,this,false));
		super.basicAttack(h, damage, armor, shield, end);
	}

	public void showAb1() {	
		if(queue1.size()==0) {
			for(Hex h:grid.hexes) {
				if((position.distance(h)<=3&&h.occupied==null||h==position)&&h.adjacentEnemy(grid, this)) {
					h.color=Color.RED;
				}
			}
		}else if(queue1.size()==1) {
			Hex firstClick = (Hex) queue1.get(0);
			for(Hex h:grid.hexes)	{
				if(h.distance(firstClick)==1&&h.hasEnemy(this)) {
					h.color=Color.RED;
				}
			}
		}
	}

	public void clearAb1() {		
		queue1.clear();
		grid.game.setButtons();
	}

	public void ability1(Hex h) {
		if(queue1.size()==0) {
			queue1.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showAb1();
		}
		else if(queue1.size()==1) {
			setPosition((Hex)queue1.get(0));
			basicAttack(h,basicDamage,true,true,true);
			basicAttack(h,basicDamage,true,true,true);
			addDebuff(new Debuff("Stunned",h.occupied,1,this,false));
			abcdDelay[0]=true;
			grid.game.endOfTurn();
		}		
	}

	public void showAb2() {
		ability2(null);
	}

	public void ability2(Hex h) {
		addBuff(new Buff("Centered Stance",this,3,this,false));
		setStamina();
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=5&&h.occupied==null) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		setPosition(h);
		setStamina();
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		for(Unit u:team) {
			u.currentStamina=u.maxStamina;
			addBuff(new Buff("Hunting Rites",u,2,this,true));
		}
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
