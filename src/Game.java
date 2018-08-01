import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Game implements MouseListener, MouseMotionListener{
	JFrame frame;
	JPanel mainPanel;
	JPanel buttons;
	Visual visual;
	JFrame description;
	JFrame information;
	boolButton move,basic,ab1,ab2,ab3,ult,cancel,pass;
	JButton qM,qB,q1,q2,q3,q4,qC,qP;
	ArrayList<boolButton> buttonList;
	ArrayList<Unit> units = new ArrayList<Unit>();
	ArrayList<Unit> team1 = new ArrayList<Unit>();
	ArrayList<Unit> team2 = new ArrayList<Unit>();
	ArrayList<Unit> toBeRemoved = new ArrayList<Unit>();
	ArrayList<Occupant> occupants = new ArrayList<Occupant>();
	Grid grid = new Grid(this);;
	Unit currentUnit= new Senryu(grid,"Senryu","Team 1",new Hex(5,2, -7));
	Hero tempHero = new GuayTho(grid,"Guay-Tho","Team 2",new Hex(4,2,-6));
	Hero one = new JARie(grid,"Jarie","Team 2",new Hex(6,2,-8));
	Hero two = new Amon(grid,"Amon","Team 2",new Hex(7,2,-9));
	Hero three = new Wrock(grid,"Wrock","Team 2",new Hex(8,2,-10));

	final static int Visual_Width = 1200;
	final static int Visual_Height = 970;
	int nextUnitCounter = 0;
	boolean ending = false;
	boolean pauseEndTurn,triedToEnd = false;

	Game(ArrayList<String> team1,ArrayList<String>team2, ArrayList<Hex> hex1, ArrayList<Hex> hex2){		

		for(int i = 0;i<team1.size();i++) {
			this.team1.add(makeHero(team1.get(i), "Team 1",hex1.get(i)));
		}
		for(int i = 0;i<team2.size();i++) {
			this.team2.add(makeHero(team2.get(i),"Team 2",hex2.get(i)));
		}
		for(Unit h:this.team1) {
			this.units.add(h);
		}
		for(Unit h:this.team2) {
			this.units.add(h);
		}
//		currentUnit = units.get(0);
		this.team1.add(currentUnit);
		units.add(currentUnit);
		this.team2.add(tempHero);
		units.add(tempHero);
		this.team2.add(one);
		this.team2.add(two);
		this.team2.add(three);
		units.add(one);
		units.add(two);
		units.add(three);

		for(Unit h:units) {
			grid.getHex(h.position).occupied=h;
		}

		loadGame();
		nextTurn();
	}

	public void loadGame() {
		frame = new JFrame("Ground Zero");
		visual = new Visual(grid);
		buttons = new JPanel();
		mainPanel = new JPanel();
		move = new boolButton("Move");
		basic = new boolButton("Basic Attack");
		ab1 = new boolButton("Ability 1 (0)");
		ab2 = new boolButton("Ability 2 (0)");
		ab3 = new boolButton("Ability 3 (0)");
		ult = new boolButton("Ultimate (8)");
		cancel = new boolButton("Cancel");
		pass = new boolButton("Pass Turn");
		qM = new JButton("?");
		qB = new JButton("?");
		q1 = new JButton("?");
		q2 = new JButton("?");
		q3 = new JButton("?");
		q4 = new JButton("?");
		qC = new JButton("?");
		qP = new JButton("?");
		buttonList = new ArrayList<boolButton>() {{add(move);add(basic);add(ab1);add(ab2);add(ab3);add(ult);add(cancel);add(pass);}};
		for(boolButton b:buttonList) {
			b.addActionListener(new buttonListener());
		}
		qM.addActionListener(new buttonListener());
		qB.addActionListener(new buttonListener());
		q1.addActionListener(new buttonListener());
		q2.addActionListener(new buttonListener());
		q3.addActionListener(new buttonListener());
		q4.addActionListener(new buttonListener());
		qC.addActionListener(new buttonListener());
		qP.addActionListener(new buttonListener());
		visual.addMouseListener(this);
		visual.addMouseMotionListener(this);

		visual.setPreferredSize(new Dimension(Visual_Width,Visual_Height));
		buttons.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.ipady = 10; //increases button height
		c.insets = new Insets(10,0,0,5); //buffer space: above,left,below,right
		c.gridy=0;
		buttons.add(move,c);
		c.gridy=1;
		buttons.add(basic,c);
		c.gridy=2;
		buttons.add(ab1,c);
		c.gridy=3;
		buttons.add(ab2,c);
		c.gridy=4;
		buttons.add(ab3,c);
		c.gridy=5;
		buttons.add(ult,c);
		c.gridy=6;
		buttons.add(cancel,c);
		c.gridy=7;
		buttons.add(pass,c);
		c.gridx=1;
		c.gridy=0;
		buttons.add(qM,c);
		c.gridy=1;
		buttons.add(qB,c);
		c.gridy=2;
		buttons.add(q1,c);
		c.gridy=3;
		buttons.add(q2,c);
		c.gridy=4;
		buttons.add(q3,c);
		c.gridy=5;
		buttons.add(q4,c);
		c.gridy=6;
		buttons.add(qC,c);
		c.gridy=7;
		buttons.add(qP,c);
		mainPanel.add(visual);
		mainPanel.add(buttons);
		frame.add(mainPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		description = new JFrame("Description");
		description.setLocation(1200, 200);
		information = new JFrame("Information");
		information.setLocation(1200, 400);
	}

	public void startOfTurn() {
		clear();
		for(Unit u:units) {
			u.updateAura();
		}
		for(Occupant o:occupants) {
			o.updateAura();
			o.runAura();
		}
		currentUnit.startOfTurn();
		currentUnit.tickChannels();
		if(currentUnit.hasDebuff("Stunned")) {
			System.out.println(currentUnit.name+" Stunned");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			endOfTurn();
		}
		setButtons();
		checkGameOver();
	}

	public void endOfTurn() {
		outerloop:
		if(!pauseEndTurn) {
			ending = true;
			clear();
			
			if(currentUnit instanceof Senryu) {	
				boolean abilty1 = false;
				boolean ability2 = false;
				boolean ability3 = false;
				boolean ultimate = false;
				if(currentUnit.addSenryuStack) {
					 abilty1 = currentUnit.abcdDelay[0];
					 ability2 = currentUnit.abcdDelay[1];
					 ability3 = currentUnit.abcdDelay[2];
					 ultimate = currentUnit.abcdDelay[3];
					 
				}

				if(currentUnit.hasBuff("Heavenly Wisdom")&&((BuffStack)currentUnit.getBuff("Heavenly Wisdom")).stacks==4) {
					if(abilty1||ability2||ability3||ultimate) {
						((Senryu)currentUnit).swapForms();
						grid.game.move.lock=true;
						grid.game.basic.lock=true;
						grid.game.ab1.lock=true;
						grid.game.ab2.lock=true;
						grid.game.ab3.lock=true;
						grid.game.ult.lock=true;
						grid.game.cancel.lock=false;
						grid.game.pass.lock=true;
						grid.game.clear();
						grid.game.setButtons();
						if(abilty1&&currentUnit.ultcd==0) {
							ult.lock=false;
							ult.toggle=true;
							currentUnit.showUlt();
						}else if(ability2&&currentUnit.ab3cd==0) {
							ab3.lock=false;
							ab3.toggle=true;
							currentUnit.showAb3();
						}else if(ability3&&currentUnit.ab2cd==0) {
							ab2.lock=false;
							ab2.toggle=true;
							currentUnit.showAb2();
						}else if(ultimate&&currentUnit.ab1cd==0){
							currentUnit.abcdDelay[3]=false;
							ab1.lock=false;
							ab1.toggle=true;
							currentUnit.showAb1();
						}
						if(grid.validTarget()) {
							break outerloop;
						}
					}
				}else if(currentUnit.hasBuff("Earthen Strength")&&((BuffStack)currentUnit.getBuff("Earthen Strength")).stacks==4) {
					if(abilty1||ability2||ability3||ultimate) {
						((Senryu)currentUnit).swapForms();
						grid.game.move.lock=true;
						grid.game.basic.lock=true;
						grid.game.ab1.lock=true;
						grid.game.ab2.lock=true;
						grid.game.ab3.lock=true;
						grid.game.ult.lock=true;
						grid.game.cancel.lock=false;
						grid.game.pass.lock=true;
						grid.game.clear();
						grid.game.setButtons();
						if(abilty1&&currentUnit.ultcd==0) {
							ult.lock=false;
							ult.toggle=true;
							currentUnit.showUlt();
						}else if(ability2&&currentUnit.ab3cd==0) {
							ab3.lock=false;
							ab3.toggle=true;
							currentUnit.showAb3();
						}else if(ability3&&currentUnit.ab2cd==0) {
							ab2.lock=false;
							ab2.toggle=true;
							currentUnit.showAb2();
						}else if(ultimate&&currentUnit.ab1cd==0){
							ab1.lock=false;
							ab1.toggle=true;
							currentUnit.showAb1();
						}
						if(grid.validTarget()) {
							break outerloop;
						}
					}
				}
				if(currentUnit.addSenryuStack) {
					if(((Senryu)currentUnit).form.equals("Heaven")) {
						currentUnit.rewriteBuff(new BuffStack("Heavenly Wisdom",currentUnit,-1,currentUnit,false,1,4),currentUnit.buffs);
					}else {
						currentUnit.rewriteBuff(new BuffStack("Earthen Strength",currentUnit,-1,currentUnit,false,1,4),currentUnit.buffs);
					}
					currentUnit.addSenryuStack=false;
				}
			}
			currentUnit.addSenryuStack = false;
			
			currentUnit.endOfTurn();
			currentUnit.tickAbilities();
			currentUnit.tickBuffs();
			currentUnit.tickDebuffs();
			currentUnit.tickMarks();
			currentUnit.addSelfBuffs();
			currentUnit.addSelfDebuffs();
			for(Hex h:grid.hexes) {
				h.tickEffects();
			}
			for(int i = grid.stasis.size()-1;i>=0;i--) {
				Hex h = grid.stasis.get(i);
				h.tickEffects();
			}
			for(int i = 0;i<occupants.size();i++) {
				Occupant o = occupants.get(i);
				o.endOfTurn();
				o.tickAbilities();
				o.tickBuffs();
				o.tickDebuffs();
				o.addSelfBuffs();
				o.addSelfDebuffs();
			}
			for(boolButton b:buttonList) {
				b.lock = false;
			}
			ending = false;
			nextTurn();
		}else {
			triedToEnd = true;
		}
	}

	public void nextTurn() {
		if(currentUnit.currentStamina>=currentUnit.maxStamina) {//grants another turn if just refilled
			startOfTurn();
		}else {
			outerloop:
				while(true) {
					for(;nextUnitCounter<units.size();nextUnitCounter++) {//keeps track of current place in list
						if(!units.get(nextUnitCounter).dead&&!units.get(nextUnitCounter).hasDebuff("Stasis")) {
							if(units.get(nextUnitCounter).currentStamina>=units.get(
									nextUnitCounter).maxStamina&&!units.get(nextUnitCounter).dead) {
								currentUnit = units.get(nextUnitCounter);
								nextUnitCounter++;
								break outerloop;
							}else {
								units.get(nextUnitCounter).currentStamina+=5;
							}
						}
					}
					
					for(Unit u:toBeRemoved) {
						units.remove(u);
						u.team.remove(u);
					}
					toBeRemoved.clear();
					nextUnitCounter = 0;

				}
		startOfTurn();
		}
	}
	
	public void addUnit(Unit u) {
		u.team.add(u);
		units.add(u);
	}
	
	public void removeUnit(Unit u) {
		toBeRemoved.add(u);
	}

	public void setButtons() {
		if(currentUnit.hasDebuff("Rooted")||currentUnit.hasBuff("Surface-to-Surface Missiles")) {
			move.setEnabled(false);
		}else {
			move.setEnabled(true);
			qM.setEnabled(true);
		}
		if(!currentUnit.ableBasic()) {
			basic.setEnabled(false);
		}else {
			basic.setEnabled(true);
			qB.setEnabled(true);
		}
		if(currentUnit.hasAb1()) {
			ab1.setText("Ability 1 ("+currentUnit.ab1cd+")");
			if(currentUnit.ab1cd!=0||!currentUnit.ableAb1()||currentUnit.hasDebuff("Silenced")) {
				ab1.setEnabled(false);
			}else {
				ab1.setEnabled(true);
				q1.setEnabled(true);
			}
		}
		else {
			ab1.setText("");
			ab1.setEnabled(false);
			q1.setEnabled(false);
		}
		if(currentUnit.hasAb2()) {
			ab2.setText("Ability 2 ("+currentUnit.ab2cd+")");
			if(currentUnit.ab2cd!=0||!currentUnit.ableAb2()||currentUnit.hasDebuff("Silenced")) {
				ab2.setEnabled(false);
			}else {
				ab2.setEnabled(true);
				q2.setEnabled(true);
			}
		}
		else {
			ab2.setText("");
			ab2.setEnabled(false);
			q2.setEnabled(false);
		}
		if(currentUnit.hasAb3()) {
			ab3.setText("Ability 3 ("+currentUnit.ab3cd+")");
			if(currentUnit.ab3cd!=0||!currentUnit.ableAb3()||currentUnit.hasDebuff("Silenced")) {
				ab3.setEnabled(false);
			}else {
				ab3.setEnabled(true);
				q3.setEnabled(true);
			}
		}
		else {
			ab3.setText("");
			ab3.setEnabled(false);
			q3.setEnabled(false);
		}
		if(currentUnit.hasUlt()) {
			ult.setText("Ultimate ("+currentUnit.ultcd+")");
			if(currentUnit.ultcd!=0||!currentUnit.ableUlt()||currentUnit.hasDebuff("Silenced")) {
				ult.setEnabled(false);
			}else {
				ult.setEnabled(true);
				q4.setEnabled(true);
			}
		}
		else {
			ult.setText("");
			ult.setEnabled(false);
			q4.setEnabled(false);
		}
		for(boolButton b:buttonList) {
			if(b.lock) {
				b.setEnabled(false);
			}
		}
	}

	public void checkGameOver() {
		boolean team1Dead = true,team2Dead = true;
		for(Unit u : team1) {
			if(u.hasDebuff("Stasis")&&u.getDebuff("Stasis").duration==-1) {}
			else if(!u.dead) {
				team1Dead = false;
			}
		}
		for(Unit u : team2) {
			if(u.hasDebuff("Stasis")&&u.getDebuff("Stasis").duration==-1) {}
			else if(!u.dead) {
				team2Dead = false;
			}
		}
		if(team1Dead||team2Dead) {
			if(!team1Dead) {
				gameOver("Team 1 Wins!");
			}else if(!team2Dead) {
				gameOver("Team 2 Wins!");
			}else {
				gameOver("Tie!");
			}
		}
	}

	public void gameOver(String str) {
		frame.setVisible(false);
		JFrame endFrame = new JFrame("End Screen");
		endFrame.add(new JPanel().add(new JLabel(str)));
		endFrame.pack();
		endFrame.setVisible(true);
	}

	class boolButton extends JButton{
		boolean toggle = false;//use to determine what action is currently occuring
		boolean lock = false;//use to prevent 

		boolButton(String str){
			super(str);
		}
	}

	class buttonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==move) {
				for(boolButton b:buttonList) {
					if(b!=e.getSource()&&b!=cancel) {
						b.setEnabled(false);
					}
				}
				((boolButton)e.getSource()).toggle=true;
				currentUnit.showMove();
			}else if(e.getSource()==basic) {
				for(boolButton b:buttonList) {
					if(b!=e.getSource()&&b!=cancel) {
						b.setEnabled(false);
					}
				}				
				((boolButton)e.getSource()).toggle=true;
				currentUnit.showBasic();
			}else if(e.getSource()==ab1) {
				for(boolButton b:buttonList) {
					if(b!=e.getSource()&&b!=cancel) {
						b.setEnabled(false);
					}
				}
				((boolButton)e.getSource()).toggle=true;
				currentUnit.showAb1();
			}else if(e.getSource()==ab2) {
				for(boolButton b:buttonList) {
					if(b!=e.getSource()&&b!=cancel) {
						b.setEnabled(false);
					}
				}
				((boolButton)e.getSource()).toggle=true;
				currentUnit.showAb2();
			}else if(e.getSource()==ab3) {
				for(boolButton b:buttonList) {
					if(b!=e.getSource()&&b!=cancel) {
						b.setEnabled(false);
					}
				}
				((boolButton)e.getSource()).toggle=true;
				currentUnit.showAb3();
			}else if(e.getSource()==ult) {
				for(boolButton b:buttonList) {
					if(b!=e.getSource()&&b!=cancel) {
						b.setEnabled(false);
					}
				}
				((boolButton)e.getSource()).toggle=true;
				currentUnit.showUlt();
			}else if(e.getSource()==cancel) {
				clear();
				setButtons();
			}else if(e.getSource()==pass) {
				endOfTurn();
			}else if(e.getSource()==qM) {
				description.getContentPane().removeAll();
				description.add(new JPanel() {{add(new JLabel(currentUnit.qM));}});
				description.pack();
				description.setVisible(true);
			}else if(e.getSource()==qB) {
				description.getContentPane().removeAll();
				description.add(new JPanel() {{add(new JLabel(currentUnit.qB));}});
				description.pack();
				description.setVisible(true);
			}else if(e.getSource()==q1) {
				description.getContentPane().removeAll();
				description.add(new JPanel() {{add(new JLabel(currentUnit.q1));}});
				description.pack();
				description.setVisible(true);
			}else if(e.getSource()==q2) {
				description.getContentPane().removeAll();
				description.add(new JPanel() {{add(new JLabel(currentUnit.q2));}});
				description.pack();
				description.setVisible(true);
			}else if(e.getSource()==q3) {
				description.getContentPane().removeAll();
				description.add(new JPanel() {{add(new JLabel(currentUnit.q3));}});
				description.pack();
				description.setVisible(true);
			}else if(e.getSource()==q4) {
				description.getContentPane().removeAll();
				description.add(new JPanel() {{add(new JLabel(currentUnit.q4));}});
				description.pack();
				description.setVisible(true);
			}else if(e.getSource()==qC) {
				description.getContentPane().removeAll();
				description.add(new JPanel() {{add(new JLabel(currentUnit.qP));}});
				description.pack();
				description.setVisible(true);
			}else if(e.getSource()==qP) {
				description.getContentPane().removeAll();
				description.add(new JPanel() {{add(new JLabel(currentUnit.qU));}});
				description.pack();
				description.setVisible(true);
			}
		}
	}

	public Unit makeHero(String str,String team, Hex hex) {
		Unit h = null;
		if(str.equals("Ak'ar")) {
			h = new Akar(grid,str,team,hex);
		}else if(str.equals("Asger")) {
			h = new Asger(grid,str,team,hex);
		}else if(str.equals("Charity")) {
			h = new Charity(grid,str,team,hex);
		}else if(str.equals("Destiny")) {
			h = new Destiny(grid,str,team,hex);
		}else if(str.equals("JAR.ie")) {
			h = new JARie(grid,str,team,hex);
		}else if(str.equals("Kaito")) {
			h = new Kaito(grid,str,team,hex);
		}else if(str.equals("Kaj")) {
			h = new Kaj(grid,str,team,hex);
		}else if(str.equals("LAR.ie")) {
			h = new LARie(grid,str,team,hex);
		}else if(str.equals("Lindera")) {
			h = new Lindera(grid,str,team,hex);
		}else if(str.equals("Magmus")) {
			h = new Magmus(grid,str,team,hex);
		}else if(str.equals("Malor")) {
			h = new Malor(grid,str,team,hex);
		}else if(str.equals("Mortimer")) {
			h = new Mortimer(grid,str,team,hex);
		}else if(str.equals("Myria")) {
			h = new Myria(grid,str,team,hex);
		}else if(str.equals("Olaf")) {
			h = new Olaf(grid,str,team,hex);
		}else if(str.equals("Serenity")) {
			h = new Serenity(grid,str,team,hex);
		}
			
		return h;
	}

	public boolean onMap(Hex h) {
		for(Hex h1:grid.hexes) {
			if(h.equals(h1)) {
				return true;
			}
		}
		return false;
	}

	public void mouseClicked(MouseEvent e) {
		try {
			if(visual.image.getRGB(e.getX(), e.getY())!=Color.BLACK.getRGB()) {
				Hex h = visual.grid.getHex(visual.mainLayout.pixelToHex(
						new Point(e.getX(),e.getY())).hexRound());
				if(h==null) {
					 h = visual.grid.getDeletedHex(visual.mainLayout.pixelToHex(
								new Point(e.getX(),e.getY())).hexRound());
				}if(h==null) {
					 h = visual.grid.getStasisHex(visual.mainLayout.pixelToHex(
								new Point(e.getX(),e.getY())).hexRound());
				}
				if(e.getButton()==MouseEvent.BUTTON1) {
					if(h!=null&&(visual.checkBorder(h, visual.mainLayout.hexToPixel(h), Color.red.getRGB())
							||visual.checkBorder(h, visual.mainLayout.hexToPixel(h), Color.green.getRGB()))) {
						if(move.toggle) {
							currentUnit.move(h);
						}else if(basic.toggle) {
							currentUnit.basicAttack(h);
						}else if(ab1.toggle) {
							currentUnit.ability1(h);
						}else if(ab2.toggle) {
							currentUnit.ability2(h);
						}else if(ab3.toggle) {
							currentUnit.ability3(h);
						}else if(ult.toggle) {
							currentUnit.ultimate(h);
						}
					}
				}else if(e.getButton()==MouseEvent.BUTTON3) {
					JPanel temp = new JPanel();
					temp.setLayout(new BoxLayout(temp,BoxLayout.PAGE_AXIS));
					temp.add(new JLabel("Info:"));
					temp.add(new JLabel(h.q+" "+h.r+" "+h.s));
					if(h.occupied!=null) {
						if(h.occupied.currentShield>0) {
							temp.add(new JLabel("Health: ("+h.occupied.currentShield+")"+h.occupied.currentHealth+"/"+
									h.occupied.maxHealth));
						}else {
							temp.add(new JLabel("Health: "+h.occupied.currentHealth+"/"+
									h.occupied.maxHealth));
						}
						temp.add(new JLabel("Armor: "+h.occupied.currentArmor+"/"
								+h.occupied.defaultArmor));
						temp.add(new JLabel("Armor Piercing: "+h.occupied.armorPiercing));
						temp.add(new JLabel("Stamina: "+h.occupied.currentStamina+"/"
								+h.occupied.maxStamina));
						String str = "Buffs: ";
						temp.add(new JLabel(str));
						for(Buff b:h.occupied.buffs) {
							temp.add(new JLabel(""+b));
						}
						str = "Debuffs: ";
						temp.add(new JLabel(str));
						for(Debuff d:h.occupied.debuffs) {
							temp.add(new JLabel(""+d));
						}
					}
					if(h.effects.size()>0) {
						temp.add(new JLabel("Tile Effects:"));
						for(Effect e1:h.effects) {
							temp.add(new JLabel(e1.effectName+"("+e1.duration+")"));
						}
					}
					information.add(temp);
					information.pack();
					information.setVisible(true);	
				}
			}
		}
		catch(NullPointerException e1) {

		}
	}

	public void mouseDragged(MouseEvent e) {
		mouseClicked(e);
	}

	public void clear() {
		for(boolButton b:buttonList) {
			b.toggle = false;
			b.setEnabled(true);
		}
		currentUnit.clearBasic();
		currentUnit.clearAb1();
		currentUnit.clearAb2();
		currentUnit.clearAb3();
		currentUnit.clearUlt();
		visual.clear();
	}

	public void mousePressed(MouseEvent e) {		
	}

	public void mouseReleased(MouseEvent e) {		
	}

	public void mouseEntered(MouseEvent e) {		
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}


}
