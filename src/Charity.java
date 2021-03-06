import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Charity extends Hero{

	Charity(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}

	public void assembleStats() {
		name = "Charity";
		title = "Shield of Spirit";
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 60;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 1;
		ab1cdMax = 2;
		ab2cdMax = 1;
		ab3cdMax = 2;
		ultcdMax = 8;
		qU="A powerful healer, Charity is a support character who can is best at healing allies. Her skill even lets her revive a fallen ally to rejoin the battle, and her ample healing makes her team extremely resilient.";
		qP="Blessing of Strength: Adjacent allies heal 20 health at the start of your turn.";
		q1="Vigor (2): Heal an ally within 3 tiles for 100 health and remove all debuffs.";
		q2="Weight of Sin (1): Root an enemy for 1 turn.";
		q3="Divine Radiance (2): Heal up to 3 other allies that are adjacent to each other for 20 health at the start of each turn for 3 turns.";
		q4="Spiritual Tether (8): Revive a dead ally with 10% of their maximum health.";
	}

	public void startOfTurn() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)==1&&h.hasAlly(this)) {
				h.occupied.heal(20);
			}
		}
		super.startOfTurn();
	}
	
	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=3&&h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability1(Hex h) {
		h.occupied.heal(100);
		for(int i = h.occupied.debuffs.size()-1;i>=0;i--) {
			h.occupied.removeDebuff(h.occupied.debuffs.get(i));
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(h.hasEnemy(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		addDebuff(new Debuff("Rooted",h.occupied,1,this,false));
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		if(queue3.isEmpty()) {
			for(Hex h:grid.hexes) {
				if(h.hasAlly(this)) {
					h.color=Color.red;
				}
			}
		}else {
			if(queue3.size()<3) {
				for(Object h:queue3) {
					for(Hex h1:grid.hexes) {
						if(((Hex)h).distance(h1)==1&&h1.hasAlly(this)) {
							h1.color=Color.red;
						}
					}
				}
				for(Object h:queue3) {
					((Hex)h).color=null;
				}
			}
		}
		position.color=Color.green;
		
	}
	
	public void clearAb3() {
		queue3.clear();
		grid.game.setButtons();	
	}

	public void ability3(Hex h) {
		if(h==position) {
			for(Object h1:queue3) {
				addBuff(new Buff("Divine Radiance",((Hex)h1).occupied,3,this,false));
			}
			abcdDelay[2]=true;
			grid.game.endOfTurn();
		}else if(queue3.size()<3) {
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
	}

	public void showUlt() {	
		if(queue4.size()==0) {
			JFrame temp = new JFrame("Spiritual Tether");
			JPanel panel = new JPanel();
			for(Unit u: team) {
				if(u.dead) {
					unitButton u1 = new unitButton(u.name,u);
					u1.addActionListener(new unitButtonListener());
					panel.add(u1);
				}
			}
			temp.add(panel);
			temp.pack();
			temp.setVisible(true);
		}else if(queue4.size()==1) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.occupied==null) {
					h.color=Color.red;
				}
			}
		}
		
	}

	public void ultimate(Hex h) {
		Hero h1 = (Hero) queue4.get(0);
		h1.dead = false;
		h1.currentHealth = (int) (h1.maxHealth*0.1);
		h1.setPosition(h);
		h1.currentStamina=0;
		h1.currentShield=0;
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

	class unitButton extends JButton{
		Unit u;
		unitButton(String name,Unit u) {
			super(name);
			this.u=u;
			
		}
		
	}
	
	class unitButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			queue4.add(((unitButton)e.getSource()).u);
			Component component = (Component) e.getSource();
		    JFrame frame = (JFrame) SwingUtilities.getRoot(component);
		    frame.dispose();
			showUlt();
		}
		
	}
}
