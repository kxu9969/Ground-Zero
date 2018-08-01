import java.awt.Color;
import java.util.ArrayList;

public class Vera extends Hero{

	Vera(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}

	public void assembleStats() {
		name = "Vera";
		title = "Blade of Energy";
		maxHealth = 500;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 50;
		defaultArmor = 50;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 1;
		ab1cdMax = 3;
		ab2cdMax = 2;
		ab3cdMax = 3;
		ultcdMax = 8;
		qU="Unlike the other Blades, Vera excels at setting up enemies for allies to finish off by stopping them, removing buffs, or by flat up increasing damage taken. When needed for the finishing blow, however, she can do it just as good as any other Blade.";
		qP="Sapping Strikes: Attacks steal 20 stamina from the target.";
		q1="Dynamic Repression (3): Root a target and remove its armor for 2 turns.";
		q2="Energetic Blows (2): Target an ally, giving them additional 20 basic attack damage for 2 turns.";
		q3="Arcane Shift (3): Dash to a nearby target within 3 range, remove all buffs, and attack them.";
		q4="Empowered Animus (8): Target an ally, making their attacks deal damage to adjacent enemies and ignore armor (buff). No timer.";
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean end) {
		super.basicAttack(h, damage, armor, shield, end);
		int i = 20;
		h.occupied.currentStamina-=20;
		if(h.occupied.currentStamina<0) {
			i+=h.occupied.currentStamina;
			h.occupied.currentStamina=0;
		}
		setStamina(i);
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		addDebuff(new Debuff("Rooted",h.occupied,2,this,false));
		addDebuff(new Debuff("Armor Removed",h.occupied,2,this,false));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		addBuff(new Buff("Energetic Blows",h.occupied,2,this,false));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		if(queue3.size()==0) {
			for(Hex h:grid.hexes) {
				if((position.distance(h)<=3&&h.isEmpty()||h==position)&&h.adjacentEnemy(grid, this)) {
					h.color=Color.RED;
				}
			}
		}else if(queue3.size()==1) {
			Hex firstClick = (Hex) queue3.get(0);
			for(Hex h:grid.hexes)	{
				if(h.distance(firstClick)==1&&h.hasEnemy(this)) {
					h.color=Color.RED;
				}
			}
		}
	}
	
	public void clearAb3() {
		queue3.clear();
		grid.game.setButtons();
	}

	public void ability3(Hex h) {
		if(queue3.size()==0) {
			queue3.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showAb3();
		}
		else if(queue3.size()==1) {
			setPosition((Hex)queue3.get(0));
			ArrayList<Buff> toBeRemoved = new ArrayList<Buff>();
			for(Buff b:h.occupied.buffs) {
				toBeRemoved.add(b);
			}
			for(Buff b:toBeRemoved) {
				h.occupied.removeBuff(b);
			}
			basicAttack(h,basicDamage,true,true,true);
			abcdDelay[2]=true;
			grid.game.endOfTurn();
		}			
	}

	public void showUlt() {
		for(Hex h:grid.hexes) {
			if(h.hasAlly(this)) {
				h.color=Color.red;
			}
		}		
	}

	public void ultimate(Hex h) {
		addBuff(new Buff("Empowered Animus",h.occupied,-1,this,false));
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
