import java.awt.Color;

public class Mortimer extends Hero{

	Mortimer(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 550;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 60;
		defaultArmor = 50;
		currentArmor = defaultArmor;
		armorPiercing = 0;
		basicRange = 1;
		ab1cdMax = 3;
		ab2cdMax = 2;
		ab3cdMax = 4;
		ultcdMax = 8;			
		qU="A potent damage dealer, Mortimer excels at ignoring enemy defenses or even using them against them. His kit lets him rush in to attack and even finish off wounded enemies from range if necessary. Against enemies with simply high health or high regen, however, he lacks the base damage needed to finish them off.";
		qP="Burning Claws: Attacks and abilities ignore armor.";
		q1="Blazing Stampede (3): Dash to an enemy within 3 tiles and attack them twice, ignoring armor.";
		q2="Hell Rend (2): Attack up to two enemies adjacent to you, ignoring armor and shields.";
		q3="Hellfire (4): Hurl a fireball to a target within 5 tiles, dealing 40 damage ignoring armor and additional damage equal to their armor and shield.";
		q4="Consuming Flame (8): Deal damage ignoring armor to all enemies equal to their armor, strip all enemies of armor for 2 turns (enchant), and refill stamina immediately. If you have less than 25% health, the damage dealt is increased by 70.";
	}

	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean end) {
		super.basicAttack(h, basicDamage, false, shield,end);
	}
	
	public void showAb1() {	
		if(queue1.size()==0) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)<=3&&h.occupied==null&&h.adjacentEnemy(grid, this)) {
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
			basicAttack(h,basicDamage,false,true);
			basicAttack(h,basicDamage,false,true);
			abcdDelay[0]=true;
			grid.game.endOfTurn();
		}		
	}
	
	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasEnemy(this)) {
				h.color=Color.RED;
			}
		}
		position.color=Color.green;
		for(Object h:queue2) {
			((Hex)h).color=null;
		}		
	}
	
	public void clearAb2() {
		queue2.clear();
		grid.game.setButtons();
	}

	public void ability2(Hex h) {
		if(h==position) {
			for(Object o:queue2) {
				basicAttack(((Hex)o),basicDamage,false,false);
			}
			abcdDelay[1]=true;
			grid.game.endOfTurn();
		}else if(queue2.size()<2) {
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
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=5&&h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		int damage=40+h.occupied.currentArmor+h.occupied.currentShield;
		h.occupied.takeAbility(damage, this, false, true);
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	@Override
	public void ultimate(Hex h1) {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)) {
				int damage = h.occupied.currentArmor;
				if(currentHealth<maxHealth*0.25) {
					damage+=70;
				}
				h.occupied.takeAbility(damage, this, false, true);
				addDebuff(h.occupied,new Debuff("Consuming Flame",h.occupied,2,true));
			}
		}
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
