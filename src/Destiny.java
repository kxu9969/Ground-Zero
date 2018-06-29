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
		armor = 60;
		armorPiercing = 40;
		basicRange = 2;
		ab1cdMax = 2;
		ab2cdMax = 1;
		ab3cdMax = 2;
		ultcdMax = 8;		
	}

	public void startOfTurn() {
		System.out.println("hi");
		rewriteBuff(new BuffStack("Tranquility",this,-1,false,1),buffs);
		super.startOfTurn();
	}

	public void showAb1() {
		if(queue1.size()==0||hasBuff("Tranquility")) {
			for(Hex h:grid.hexes) {
				if(h.hasAlly(this)) {
					h.color = Color.red;
				}
			}
			for(Object o: queue1) {
				((Hex)o).color=null;
			}
		}
		position.color=Color.GREEN;

	}
	
	public void clearAb1() {
		queue1.clear();
		grid.game.setButtons();
	}

	public void ability1(Hex h) {
		if(h==position) {
			System.out.println(queue1.size());
			abcdDelay[0]=true;
			grid.game.endOfTurn();
		}else if(queue1.size()>0){
			queue1.add(h);
			if(hasBuff("Tranquility")) {
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
		}else if(queue1.size()==0) {
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

	@Override
	public void showAb2() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ability2(Hex h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showAb3() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ability3(Hex h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showUlt() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ultimate(Hex h) {
		// TODO Auto-generated method stub

	}

}
