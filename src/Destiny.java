import java.awt.Color;

public class Destiny extends Hero{

	Destiny(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 30;
		defaultArmor = 60;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 2;
		ab1cdMax = 2;
		ab2cdMax = 1;
		ab3cdMax = 2;
		ultcdMax = 8;
		qU="Destiny is a support character who uses her stacks of Tranquility to scale her abilities rapidly, increasing the number of affected targets or their duration.";
		qP="Inner Peace: Gains a stack of Tranquility at the start of each turn, up to 9.";
		q1="Spiritual Unity (2): Heal another ally for 40 and increase their basic damage by 10 for 3 turns. Costs 1 Tranquility for each additional ally.";
		q2="Internal Strife (2): Stun a target for given number of turns, costing 2 Tranquility per turn.";
		q3="Harmonious Stride (1): Teleport allies to adjacent tiles, costing 2 Tranquility per ally.";
		q4="Enlightenment (8): Gain max stacks of Tranquility. Your abilities next turn don’t consume Tranquility stacks on cast. Refill stamina immediately.";
	}

	public void startOfTurn() {
		rewriteBuff(new BuffStack("Tranquility",this,-1,this,false,1),buffs);
		super.startOfTurn();
	}

	public void showAb1() {
		if(queue1.size()==0) {
			if(hasBuff("Tranquility"))
				queue1.add(((BuffStack)getBuff("Tranquility")).stacks);
			else
				queue1.add(0);
		}
		if(queue1.size()==1||hasBuff("Tranquility")) {
			for(Hex h:grid.hexes) {
				if(h.hasAlly(this)) {
					h.color = Color.red;
				}
			}
			for(int i =1;i<queue1.size();i++) {
				Object o = queue1.get(i);
				((Hex)o).color=null;
			}
		}
		position.color=Color.GREEN;

	}

	public void clearAb1() {
		try {
			if(((int)queue1.get(0))!=0) {
				if(hasBuff("Tranquility")) {
					((BuffStack)getBuff("Tranquility")).stacks=(int) queue1.get(0);
				}else {
					rewriteBuff(new BuffStack("Tranquility",this,-1,this,false,(int) queue1.get(0)),buffs);
				}
			}
		}catch(Exception e) {}
		queue1.clear();
		grid.game.setButtons();
	}

	public void ability1(Hex h) {
		if(h==position) {
			for(int i =1;i<queue1.size();i++) {
				Object o = queue1.get(i);
				((Hex)o).occupied.heal(40);
				addBuff(new Buff("Spiritual Unity",((Hex)o).occupied,3,this,false));
			}
			queue1.clear();
			abcdDelay[0]=true;
			grid.game.endOfTurn();
		}else if(queue1.size()>1){
			queue1.add(h);
			if(hasBuff("Enlightenment")) {}
			else if(hasBuff("Tranquility")) {
				((BuffStack)getBuff("Tranquility")).stacks--;
				if(((BuffStack)getBuff("Tranquility")).stacks<=0) {
					removeSameBuff("Tranquility");
				}
			}
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showAb1();
		}else if(queue1.size()==1) {
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
	}

	public boolean ableAb2() {
		if((hasBuff("Tranquility")&&((BuffStack)getBuff("Tranquility")).stacks>=2)) {
			return true;
		}
		else {
			return false;
		}
	}

	public void showAb2() {
		if(queue2.size()==0) {
			queue2.add(((BuffStack)getBuff("Tranquility")).stacks);
		}if(queue2.size()==1) {
			for(Hex h:grid.hexes) {
				if(h.hasEnemy(this)) {
					h.color=Color.red;
				}
			}
		}else if(queue2.size()>=2){
			((Hex)queue2.get(1)).color = Color.red;
		}
		position.color=Color.green;
	}

	public void clearAb2() {
		try {
			if(hasBuff("Tranquility")) {
				((BuffStack)getBuff("Tranquility")).stacks=(int) queue2.get(0);
			}else {
				rewriteBuff(new BuffStack("Tranquility",this,-1,this,false,(int) queue2.get(0)),buffs);
			}
		}catch(Exception e) {}
		queue2.clear();
		grid.game.setButtons();
	}

	public void ability2(Hex h) {
		if(h==position) {
			addDebuff(new Debuff("Stunned",((Hex)queue2.get(1)).occupied,queue2.size()-1,this,false));
			queue2.clear();
			abcdDelay[1]=true;
			grid.game.endOfTurn();
		}else if(queue2.size()>=2) {
			queue2.add(1);
			if(hasBuff("Enlightenment")) {}
			else if(hasBuff("Tranquility")) {
				((BuffStack)getBuff("Tranquility")).stacks-=2;
				if(((BuffStack)getBuff("Tranquility")).stacks<=0) {
					removeSameBuff("Tranquility");
				}
			}
			grid.game.setButtons();
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			position.color=Color.green;
		}else if(queue2.size()==1){
			queue2.add(h);
			if(hasBuff("Enlightenment")) {}
			else {
				((BuffStack)getBuff("Tranquility")).stacks-=2;
				if(((BuffStack)getBuff("Tranquility")).stacks<=0) {
					removeSameBuff("Tranquility");
				}
			}
			grid.game.setButtons();
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			position.color=Color.green;
		}
	}

	public boolean ableAb3() {
		if((hasBuff("Tranquility")&&((BuffStack)getBuff("Tranquility")).stacks>=2)) {
			return true;
		}
		else {
			return false;
		}
	}

	public void showAb3() {
		if(queue3.size()==0) {
			queue3.add(((BuffStack)getBuff("Tranquility")).stacks);
		}if(queue3.size()%2!=0) {//remove those already selected
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.isEmpty()) {
					h.color=Color.red;
				}
			}
		}else if(queue3.size()>1&&queue3.size()%2==0){//remove those already selected
			for(Hex h:grid.hexes) {
				if(h.hasAlly(this)) {
					h.color=Color.red;
				}
			}
		}
		for(int i = 1;i<queue3.size();i++) {
			((Hex)queue3.get(i)).color=null;
		}
		position.color=Color.green;
	}

	public void clearAb3() {
		try {
			if(hasBuff("Tranquility")) {
				((BuffStack)getBuff("Tranquility")).stacks=(int) queue3.get(0);
			}else {
				rewriteBuff(new BuffStack("Tranquility",this,-1,this,false,(int) queue3.get(0)),buffs);
			}
		}catch(Exception e) {}
		queue3.clear();
		grid.game.setButtons();
	}

	public void ability3(Hex h) {
		if(h==position) {
			try {
				for(int i =1;i<queue3.size();i+=2) {
					((Hex)queue3.get(i+1)).occupied.setPosition(((Hex)queue3.get(i)));
				}
			}catch(Exception e) {}
			queue3.clear();
			abcdDelay[2]=true;
			grid.game.endOfTurn();
		}else if(queue3.size()%2!=0) {//adj tile
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
		}else if(queue3.size()>1&&queue3.size()%2==0) {//select ally
			queue3.add(h);
			if(hasBuff("Enlightenment")) {}
			else {
				((BuffStack)getBuff("Tranquility")).stacks-=2;
				if(((BuffStack)getBuff("Tranquility")).stacks<=0) {
					removeSameBuff("Tranquility");
				}
			}
			grid.game.setButtons();
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			position.color=Color.green;
		}
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		addBuff(new BuffStack("Tranquility",this,-1,this,false,9));
		addBuff(new Buff("Enlightenment",this,1,this,true));
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
