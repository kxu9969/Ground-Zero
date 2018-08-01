import java.awt.Color;
import java.util.ArrayList;

public class Senryu extends Hero{
	String form = "Heaven";
	int formAb1cd = 0;
	int formAb2cd = 0;
	int formAb3cd = 0;
	int formUltcd = 8;
	int formAb1cdMax = 3;
	int formAb2cdMax = 1;
	int formAb3cdMax = 2;
	int formUltcdMax = 8;


	Senryu(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 550;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 65;
		defaultArmor = 20;
		currentArmor = defaultArmor;
		armorPiercing = 10;
		basicRange = 1;
		ab1cdMax = 3;
		ab2cdMax = 1;
		ab3cdMax = 2;
		ultcdMax = 8;
		qU="A master of all martial arts and the embodiment of the Great River Dragon, Senryu is able to both support his team though the tactical repositioning of both enemies and allies and deal great amounts of damage.";
		qP="Body of Heaven: Using an ability or basic attacking gives a stack of Heavenly Wisdom up to 4, increasing armor piercing by 5, increasing armor by 5, and decreasing max stamina by 5 per stack. Once at 4 stacks, the next ability used causes Senryu to change forms after and then triggers the ability linked to the same limb.";
		q1="Left Arm, Boar (3): Target an adjacent tile, charging in that direction for 3 tiles. Any characters struck take 60 damage and are shoved to the end of the path. Allies take no damage.";
		q2="Right Arm, Tiger (1): Target an enemy within 3 range, dashing to them and basic attacking, dealing 5 more damage for every stack of Heavenly Wisdom.";
		q3="Left Leg, Crane (2): Select up to two adjacent characters and whip them 2 tiles counterclockwise, dealing 30 damage and lowering their current stamina by 10. If the destination tiles are occupied, occupants are pushed out of the way and take 30 damage. Allies take no damage.";
		q4="Right Leg, Phoenix (8): Target an unoccupied tile within 5 range and select an adjacent tile. Channel for 1 turn and gain 20 current stamina, jumping into the air and becoming untargetable, then land on the targeted tile. Enemies on the selected tile take 150 damage. Any characters, excluding you, adjacent to the selected tile are knocked back 1 tile, with enemies also taking 50 damage. This cannot be cast while rooted.";
	}

	public void basicAttack(Hex h,int damage,boolean armor, boolean shield, boolean anotherTurn) {
		if(h.occupied.hasMark(this)) {
			h.occupied.removeMark(h.occupied.getMark(this));
			super.basicAttack(h, damage, armor, shield, true);
		}
		super.basicAttack(h, damage, armor, shield, anotherTurn);
	}

	public void tickAbilities() {
		if(formAb1cd>0) {
			formAb1cd--;
		}if(formAb2cd>0) {
			formAb2cd--;
		}if(formAb3cd>0) {
			formAb3cd--;
		}if(formUltcd>0) {
			formUltcd--;
		}
		super.tickAbilities();
	}

	public void swapForms() {
		if(form.equals("Heaven")) {
			removeSameBuff("Heavenly Wisdom");
			form="Earth";
			maxHealth = 400;
			if(currentHealth>maxHealth) {
				currentHealth=maxHealth;	
			}
			maxStamina = 35;
			currentStamina = 0;
			basicDamage = 40;
			defaultArmor = 45;
			currentArmor = defaultArmor;
			armorPiercing = 30;
			qP="Body of Earth: Using an ability or basic attacking gives a stack of Earthen Strength up to 4, granting 30 current and maximum health, increasing damage by 5, and increasing max stamina by 5 per stack. Once at 4 stacks, the next ability used causes Senryu to change forms after and then triggers the ability linked to the same limb.";
			q1="Right Leg, Waterfall (3): Kick an adjacent enemy, dealing 20 damage and pushing them back one tile. Move onto the tile previously occupied by the kicked enemy if possible. Can be cast up to 3 times on the same turn.";
			q2="Left Leg, Mountain (1): Choose an adjacent tile and the tiles adjacent to it and you. Their occupants are pushed back and take 30 damage. Allies take no damage. Gain a stack of Mountain’s Resilience until switching forms, granting 5 armor and 5 max stamina. Gain Mountain’s Will for one turn, becoming immune to displacement.";
			q3="Right Arm, Lightning (2): Basic attack an adjacent enemy 2 times in quick succession and apply a Mark for 3 turns. Basic attacking a marked target consumes the Mark and basic attacks a second time.";
			q4="Left Arm, Forest (8): Deal 100 damage to adjacent enemies and stun them for one turn, silencing them for one turn after. Root adjacent allies for 1 turn and give them Forest’s Heart for 2 turns, healing them for 50 at the start of their turn.";
		}else {
			removeSameBuff("Earthen Strength");
			removeSameBuff("Mountain's Resilience");
			form = "Heaven";
			maxHealth = 550;
			currentHealth+=150;
			maxStamina = 60;
			currentStamina = 0;
			basicDamage = 65;
			defaultArmor = 20;
			currentArmor = defaultArmor;
			armorPiercing = 10;
			qP="Body of Heaven: Using an ability or basic attacking gives a stack of Heavenly Wisdom up to 4, increasing armor piercing by 5, increasing armor by 5, and decreasing max stamina by 5 per stack. Once at 4 stacks, the next ability used causes Senryu to change forms after and then triggers the ability linked to the same limb.";
			q1="Left Arm, Boar (3): Target an adjacent tile, charging in that direction for 3 tiles. Any characters struck take 60 damage and are shoved to the end of the path. Allies take no damage.";
			q2="Right Arm, Tiger (1): Target an enemy within 3 range, dashing to them and basic attacking, dealing 5 more damage for every stack of Heavenly Wisdom.";
			q3="Left Leg, Crane (2): Select up to two adjacent characters and whip them 2 tiles counterclockwise, dealing 30 damage and lowering their current stamina by 10. If the destination tiles are occupied, occupants are pushed out of the way and take 30 damage. Allies take no damage.";
			q4="Right Leg, Phoenix (8): Target an unoccupied tile within 5 range and select an adjacent tile. Channel for 1 turn and gain 20 current stamina, jumping into the air and becoming untargetable, then land on the targeted tile. Enemies on the selected tile take 150 damage. Any characters, excluding you, adjacent to the selected tile are knocked back 1 tile, with enemies also taking 50 damage. This cannot be cast while rooted.";
		}
		if(abcdDelay[0]) {
			ab1cd=ab1cdMax+1;
		}if(abcdDelay[1]) {
			ab2cd=ab2cdMax+1;
		}if(abcdDelay[2]) {
			ab3cd=ab3cdMax+1;
		}if(abcdDelay[3]) {
			ultcd=ultcdMax+1;
		}		
		abcdDelay[0]=false;
		abcdDelay[1]=false;
		abcdDelay[2]=false;
		abcdDelay[3]=false;

		ab1cd += formAb1cd;
		formAb1cd = ab1cd-formAb1cd;
		ab1cd = ab1cd-formAb1cd;
		ab2cd += formAb2cd;
		formAb2cd = ab2cd-formAb2cd;
		ab2cd = ab2cd-formAb2cd;
		ab3cd += formAb3cd;
		formAb3cd = ab3cd-formAb3cd;
		ab3cd = ab3cd-formAb3cd;
		ultcd += formUltcd;
		formUltcd = ultcd-formUltcd;
		ultcd = ultcd-formUltcd;

		ab1cdMax += formAb1cdMax;
		formAb1cdMax = ab1cdMax-formAb1cdMax;
		ab1cdMax = ab1cdMax-formAb1cdMax;
		ab2cdMax += formAb2cdMax;
		formAb2cdMax = ab2cdMax-formAb2cdMax;
		ab2cdMax = ab2cdMax-formAb2cdMax;
		ab3cdMax += formAb3cdMax;
		formAb3cdMax = ab3cdMax-formAb3cdMax;
		ab3cdMax = ab3cdMax-formAb3cdMax;
		ultcdMax += formUltcdMax;
		formUltcdMax = ultcdMax-formUltcdMax;
		ultcdMax = ultcdMax-formUltcdMax;
		addSenryuStack = false;
	}

	public void showAb1() {
		if(form.equals("Heaven")) {
			System.out.println("Boar");	
			for(Hex h:position.allAdjacents()) {
				h=grid.getHex(h);
				if(h!=null) {
					h.color=Color.red;
				}
			}
		}else {
			if(queue1.isEmpty()||((int)queue1.get(0))<=2) {
				System.out.println("Waterfall");
				for(Hex h:position.allAdjacents()) {
					h=grid.getHex(h);
					if(h.hasEnemy(this)) {
						h.color=Color.red;
					}
				}
			}
			position.color=Color.green;
		}
	}

	public void clearAb1() {
		queue1.clear();
		grid.game.setButtons();
	}

	public void ability1(Hex h) {
		if(form.equals("Heaven")) {
			Unit u = h.occupied;
			Hex h1 = grid.getHex(h);
			Hex difference = h1.subtract(position);
			ArrayList<Hex> affected = new ArrayList<Hex>();
			for(int i = 0;i<3;i++) {
				if(grid.getHex(position.add(difference)).isEmpty()) {
					setPosition(grid.getHex(position.add(difference)));
					u = grid.getHex(position.add(difference)).occupied;
				}else {
					h1 = grid.getHex(u.position);
					affected = h1.pushList(grid, position);
					if(affected!=null) {
						setPosition(h1);
					}
				}
			}
			for(int i = 0;i<affected.size()-1;i++) {
				Hex h2 = affected.get(i);
				if(h2.hasEnemy(this)) {
					h2.occupied.takeAbility(60, this, true, true);
				}
			}
			abcdDelay[0]=true;
			addSenryuStack = true;
			grid.game.endOfTurn();

		}else {
			if(h==position) {
				abcdDelay[0]=true;
				addSenryuStack = true;
				grid.game.endOfTurn();

			}else if(queue1.size()==0) {
				if(h.push(grid, position)!=null) {
					setPosition(h);
				}
				queue1.add(1);
				grid.game.move.setEnabled(false);
				grid.game.basic.setEnabled(false);
				grid.game.ab1.setEnabled(false);
				grid.game.ab2.setEnabled(false);
				grid.game.ab3.setEnabled(false);
				grid.game.ult.setEnabled(false);
				grid.game.pass.setEnabled(false);
				grid.game.visual.clear();
				showAb1();
			}else if(((int)queue1.get(0)<=2)) {
				if(h.push(grid, position)!=null) {
					setPosition(h);
				}
				int i = (int) queue1.get(0);
				i++;
				queue1.clear();
				queue1.add(i);
				grid.game.move.setEnabled(false);
				grid.game.basic.setEnabled(false);
				grid.game.ab1.setEnabled(false);
				grid.game.ab2.setEnabled(false);
				grid.game.ab3.setEnabled(false);
				grid.game.ult.setEnabled(false);
				grid.game.cancel.setEnabled(false);
				grid.game.pass.setEnabled(false);
				grid.game.visual.clear();
				showAb1();
			}
		}

	}

	public void showAb2() {
		if(form.equals("Heaven")) {
			if(queue2.size()==0) {
				System.out.println("Tiger");
				for(Hex h:grid.hexes) {
					if(position.distance(h)<=3&&h.isEmpty()&&h.adjacentEnemy(grid, this)) {
						h.color=Color.RED;
					}
				}
				position.color=Color.red;
			}else if(queue2.size()==1) {
				Hex firstClick = (Hex) queue2.get(0);
				for(Hex h:grid.hexes)	{
					if(h.distance(firstClick)==1&&h.hasEnemy(this)) {
						h.color=Color.RED;
					}
				}
			}
		}else {
			System.out.println("Mountain");
			for(Hex h:position.allAdjacents()) {
				h=grid.getHex(h);
				if(h!=null) {
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
		if(form.equals("Heaven")) {
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
				setPosition((Hex)queue2.get(0));
				int bonus = 0;
				if(hasBuff("Heavenly Wisdom")) {
					bonus += 5*((BuffStack)getBuff("Heavenly Wisdom")).stacks;
				}
				basicAttack(h,basicDamage+bonus,true,true,true);
				abcdDelay[1]=true;
				addSenryuStack = true;
				grid.game.endOfTurn();

			}
		}else {
			ArrayList<Hex> adj = position.allAdjacents();
			ArrayList<Hex> second = h.allAdjacents();
			queue2.add(h);
			for(Hex h1:second) {
				for(Hex h2:adj) {
					if(h1.equals(h2)) {
						queue2.add(grid.getHex(h1));
					}
				}
			}
			for(Object o:queue2) {
				Hex h1 = ((Hex)o);
				if(h1.hasEnemy(this)) {
					h1.occupied.takeAbility(30, this, true, true);
				}
				if(!h1.isEmpty()) {
					h1.push(grid,position);
				}
			}
			addBuff(new BuffStack("Mountain's Resilience",this,-1,this,false,1));
			addBuff(new Buff("Mountain's Will",this,1,this,false));
			abcdDelay[1]=true;
			addSenryuStack = true;
			grid.game.endOfTurn();

		}

	}

	public void showAb3() {
		if(form.equals("Heaven")) {
			if(queue3.size()==0) {
				System.out.println("Crane");
				for(Hex h:position.allAdjacents()) {
					h=grid.getHex(h);
					if(h!=null&&h.hasEnemy(this)) {
						h.color=Color.red;
					}
				}
			}else if(queue3.size()==1) {
				for(Hex h:position.allAdjacents()) {
					h=grid.getHex(h);
					if(h!=null&&h.hasEnemy(this)) {
						h.color=Color.red;
					}
				}
				((Hex)queue3.get(0)).color=null;
			}
			position.color=Color.green;
		}else {
			System.out.println("Lightning");
			for(Hex h:position.allAdjacents()) {
				h=grid.getHex(h);
				if(h.hasEnemy(this)) {
					h.color=Color.red;
				}
			}
		}
	}

	public void clearAb3() {
		queue3.clear();
		grid.game.setButtons();
	}

	public void ability3(Hex h) {
		if(form.equals("Heaven")) {
			if(h==position) {
				for(int counter = 0;counter<queue3.size();counter++) {//loops for selected tiles
					h = (Hex) queue3.get(counter);
					h = grid.getHex(h);
					if(h.hasEnemy(this)) {
						h.occupied.takeAbility(30, this, true, true);
					}
					if(!h.isEmpty()) {
						h.occupied.currentStamina-=10;
						if(h.occupied.currentStamina<0) {
							h.occupied.currentStamina=0;
						}
					}
					Unit u = h.occupied;
					Hex difference = h.subtract(position);
					Hex behindHex = difference;
					for(int j = 0;j<Hex.adjacents.size();j++) {//finds the hex behind pushing direction
						if(difference.equals(Hex.adjacents.get(j))) {
							if(j==4) {
								behindHex = h.subtract(Hex.adjacents.get(0));
							}
							else if(j==5) {
								behindHex = h.subtract(Hex.adjacents.get(1));
							}else {
								behindHex = h.subtract(Hex.adjacents.get(j+2));
							}
						}
					}
					behindHex = grid.getHex(behindHex);
					Hex extendHex = h.subtract(behindHex).add(h);//checks if destination is blocked
					extendHex = grid.getHex(extendHex);
					if(extendHex.occupied!=null) {
						if(extendHex.hasEnemy(this)) {
							extendHex.occupied.takeAbility(30,this,true,true);
						}
						if(queue3.contains(extendHex)) {
							queue3.remove(extendHex);
						}
						extendHex.push(grid, new Hex(h.q,h.r,h.s));//if so, pushes with false tile
					}
					h.push(grid, behindHex);//pushes target h from behind
					//repeats for second push
					h = u.position;
					difference = h.subtract(position);
					behindHex = difference;
					for(int j = 0;j<Hex.adjacents.size();j++) {
						if(difference.equals(Hex.adjacents.get(j))) {
							if(j==4) {
								behindHex = h.subtract(Hex.adjacents.get(0));
							}
							else if(j==5) {
								behindHex = h.subtract(Hex.adjacents.get(1));
							}else {
								behindHex = h.subtract(Hex.adjacents.get(j+2));
							}
						}
					}
					behindHex = grid.getHex(behindHex);
					extendHex = h.subtract(behindHex).add(h);
					extendHex = grid.getHex(extendHex);
					if(extendHex.occupied!=null) {
						if(extendHex.hasEnemy(this)) {
							extendHex.occupied.takeAbility(30,this,true,true);
						}
						if(queue3.contains(extendHex)) {
							queue3.remove(extendHex);
						}
						extendHex.push(grid, new Hex(h.q,h.r,h.s));
					}
					h.push(grid, behindHex);

				}
				abcdDelay[2]=true;
				addSenryuStack = true;
				grid.game.endOfTurn();
			}else if(queue3.size()<2) {
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

		}else {
			basicAttack(h,basicDamage,true,true,true);
			basicAttack(h,basicDamage,true,true,true);
			addDebuff(new Mark("Marked",h.occupied,4,this,false));

			abcdDelay[2]=true;
			addSenryuStack = true;
			grid.game.endOfTurn();

		}	
	}

	public void showUlt() {
		if(form.equals("Heaven")) {
			if(queue4.size()==0) {
				System.out.println("Phoenix");
				for(Hex h:grid.hexes) {
					if(position.distance(h)<=5&&h.isEmpty()) {
						h.color=Color.RED;
					}
				}
			}else if(queue4.size()==1) {
				Hex firstClick = (Hex) queue4.get(0);
				for(Hex h:grid.hexes)	{
					if(h.distance(firstClick)==1) {
						h.color=Color.RED;
					}
				}
			}
		}else {
			System.out.println("Forest");
			ultimate(null);
		}
	}

	public void clearUlt() {
		queue4.clear();
		grid.game.setButtons();
	}

	public boolean ableUlt() {
		if(form.equals("Heaven")&&hasDebuff("Rooted")) {
			return false;
		}
		return true;
	}

	public void ultimate(Hex h) {
		if(form.equals("Heaven")) {
			if(queue4.size()==0) {
				queue4.add(h);
				grid.game.move.setEnabled(false);
				grid.game.basic.setEnabled(false);
				grid.game.ab1.setEnabled(false);
				grid.game.ab2.setEnabled(false);
				grid.game.ab3.setEnabled(false);
				grid.game.ult.setEnabled(false);
				grid.game.pass.setEnabled(false);
				grid.game.visual.clear();
				showUlt();
			}
			else if(queue4.size()==1) {
				ArrayList<Hex> affected = new ArrayList<Hex>() {{add((Hex) queue4.get(0));add(h);}};
				setPosition(new Hex(0,0,0));
				addBuff(new Channel("Channeling",this,1,this,false,"Right Leg, Phoenix",affected));
				String str;
				if(team==grid.game.team1) {
					str="Team 1";
				}
				else {
					str="Team 2";
				}
				Senryu_Phoenix s = new Senryu_Phoenix(grid,"Phoenix",str,(Hex) queue4.get(0),this);
				grid.game.occupants.add(s);
				setStamina(20);
				abcdDelay[3]=true;
				grid.game.endOfTurn();

			}
		}else {
			for(Hex h1:position.allAdjacents()) {
				h1=grid.getHex(h1);
				if(h1.hasEnemy(this)) {
					h1.occupied.takeAbility(100, this, true, true);
					addDebuff(new Debuff("Stunned",h1.occupied,1,this,false));
					addDebuff(new Debuff("Forest's Wrath",h1.occupied,1,this,false));
				}else if(h1.hasAlly(this)) {
					addDebuff(new Debuff("Rooted",h1.occupied,1,this,false));
					addBuff(new Buff("Forest's Heart",h1.occupied,2,this,false));
				}
			}
			abcdDelay[3]=true;
			addSenryuStack = true;
			grid.game.endOfTurn();

		}

	}

}
