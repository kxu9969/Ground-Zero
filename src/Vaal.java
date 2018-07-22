import java.awt.Color;
import java.util.ArrayList;

public class Vaal extends Hero{
	
	Vaal(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 450;
		currentHealth = maxHealth;
		maxStamina = 45;
		currentStamina = 0;
		basicDamage = 60;
		defaultArmor = 30;
		currentArmor = defaultArmor;
		armorPiercing = 40;
		basicRange = 1;
		ab1cdMax = 3;
		ab2cdMax = 3;
		ab3cdMax = 4;
		ultcdMax = 8;		
	}
	
	public void showBasic() {
		if(queueB.size()==0) {
			queueB.add(position);
			for(Hex h:grid.floodFill(position,1)){
				if(h.adjacentEnemy(grid, this)) {
					h.color=Color.RED;
				}
			}		
			position.color=Color.red;
		}
		else {
			super.showBasic((Hex)queueB.get(1));
		}
	}
	
	public void clearBasic() {
		queueB.clear();
		grid.game.setButtons();
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean end) {
		if(queueB.size()==1) {
			queueB.add(h);
			grid.game.move.setEnabled(false);
			grid.game.basic.setEnabled(false);
			grid.game.ab1.setEnabled(false);
			grid.game.ab2.setEnabled(false);
			grid.game.ab3.setEnabled(false);
			grid.game.ult.setEnabled(false);
			grid.game.pass.setEnabled(false);
			grid.game.visual.clear();
			showBasic();
		}else if(queueB.size()==2) {
			setPosition((Hex)queueB.get(1));
			queueB.clear();
			super.basicAttack(h,damage,armor,shield,end);
		}else {
			super.basicAttack(h,damage,armor,shield,end);
		}
	}
	
	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(h.isEmpty()) {
				h.color=Color.RED;
			}
		}
		for(Hex h:grid.deleted) {
			h.color=Color.red;
		}
	}

	public void ability1(Hex h) {
		if(grid.getHex(h)==null) {
			grid.restoreHex(h);
		}else {
			grid.deleteHex(h);
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		if(queue2.isEmpty()) {
			for(Hex h:grid.hexes) {
				if(h.hasEnemy(this)) {
					h.color=Color.red;
				}
			}
		}else {
			if(queue2.size()<5) {
				for(Object h:queue2) {
					for(Hex h1:grid.hexes) {
						if(((Hex)h).distance(h1)==1&&h1.hasEnemy(this)) {
							h1.color=Color.red;
						}
					}
				}
				for(Object h:queue2) {
					((Hex)h).color=null;
				}
			}
		}
		position.color=Color.green;
	}
	
	public void clearAb2() {
		queue2.clear();
		grid.game.setButtons();
	}

	public void ability2(Hex h) {
		if(h==position) {
			for(Object h1:queue2) {
				basicAttack((Hex)h1,basicDamage,true,true,true);
			}
			abcdDelay[1]=true;
			grid.game.endOfTurn();
		}else if(queue2.size()<5) {
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
		ability3(null);
	}

	public void ability3(Hex h) {
		addBuff(new Buff("Relative Perception",this,3,this,false));
		position.effects.add(new TileEffect("Relative Perception",this,-1,this,false,position));
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		if(queue4.size()%2==0) {
			for(Hex h:grid.hexes) {
				if(h.isEmpty()) {
					h.color=Color.red;
				}
			}
		}else if(queue4.size()%2!=0){
			for(Hex h:grid.hexes) {
				if(h.hasAlly(this)) {
					h.color=Color.red;
				}
			}
		}
		if(queue4.size()%2==0) {
			for(int i =0;i<queue4.size();i+=2) {
				((Hex)queue4.get(i+1)).color=Color.red;
				((Hex)queue4.get(i)).color=null;
			}
		}else if(queue4.size()%2!=0) {
			for(int i =0;i+1<queue4.size();i+=2) {
				((Hex)queue4.get(i+1)).color=null;
			}
		}
	}

	public void ultimate(Hex h) {
		queue4.add(h);	
		if(queue4.size()/2==team.size()) {
			try {
				for(int i =0;i+1<queue4.size();i+=2) {
					((Hex)queue4.get(i+1)).occupied.setPosition(((Hex)queue4.get(i)));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			queue4.clear();
			abcdDelay[3]=true;
			grid.game.endOfTurn();
		}else {
			grid.game.setButtons();
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
	}

}
