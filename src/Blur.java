import java.awt.Color;

public class Blur extends Hero{

	Blur(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 400;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 50;
		defaultArmor = 10;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 1;
		ab1cdMax = 2;
		ab2cdMax = 2;
		ab3cdMax = 3;
		ultcdMax = 8;	
		qU="A direwolf, Blur is a powerful striker that carefully sets up and prepares his targets before dashing in and getting the kill before their allies can even respond.";
		qP="Stalk: Targets basic attacked by Blur have their current stamina reduced by 20.";
		q1="Pounce (2): Jumps to a nearby target within 3 tiles, and basic attacks them. If the target has been Marked by Blur, regain stamina immediately.";
		q2="Hunt (2): Mark an enemy for 2 turns. If the target is already Marked, reset the Mark and attack them if possible.";
		q3="Rip (3): Basic attack an adjacent enemy twice. Increase your damage by 10 for 2 turns. If the target has been marked by Blur, cooldown is 1 and regain stamina immediately.";
		q4="Howl (8): Mark all enemies for 2 turns and set their stamina back down to 0. Regain stamina immediately.";
	}

	public void basicAttack(Hex h,int damage,boolean armor, boolean shield,boolean anotherTurn) {
		h.occupied.currentStamina-=20;
		if(h.occupied.currentStamina<0) {
			h.occupied.currentStamina=0;
		}
		super.basicAttack(h, damage, armor, shield, anotherTurn);
	}
	
	public void showAb1() {	
		if(queue1.size()==0) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)<=3&&h.occupied==null&&h.adjacentEnemy(grid, this)) {
					h.color=Color.RED;
				}
			}
			position.color=Color.red;
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
			if(h.occupied.hasMark(this)) {
				setStamina();
			}
			abcdDelay[0]=true;
			grid.game.endOfTurn();
		}
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		if(h.occupied.hasMark(this)) {
			h.occupied.getMark(this).duration=3;
			basicAttack(h,basicDamage,true,true,true);
		}else {
			addDebuff(new Mark("Marked",h.occupied,3,this,false));
		}
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
		basicAttack(h,basicDamage,true,true,true);
		basicAttack(h,basicDamage,true,true,true);
		addBuff(new Buff("Rip",this,2,this,false));
		if(h.occupied.hasMark(this)) {
			ab3cdMax = 1;
			setStamina();
		}else {
			ab3cdMax = 3;
		}
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		for(Unit u:enemyTeam) {
			u.currentStamina=0;
			if(u.hasMark(this)) {
				u.getMark(this).duration=3;
			}else {
			addDebuff(new Mark("Marked",u,3,this,false));
			}
		}
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
