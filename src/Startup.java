import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Startup {
	JFrame screen;
	JPanel panel;
	String[] names = {"None","Ak'ar","Asger","Charity","Destiny","JAR.ie","Kaito","Kaj","LAR.ie","Lindera","Magmus","Malor","Mortimer","Myria","Olaf","Serenity"};
	JHeroBox<String> s1;
	JHeroBox<String> s2;
	JHeroBox<String> s3;
	JHeroBox<String> s4;
	JHeroBox<String> s5;
	JHeroBox<String> s6;
	JHeroBox<String> s7;
	JHeroBox<String> s8;
	JHeroBox<String> s9;
	JHeroBox<String> s10;
	ArrayList<JHeroBox<String>> t1;
	ArrayList<JHeroBox<String>> t2;
	JButton confirm;
	Startup(){
		screen = new JFrame("Hero Select");
		screen.setPreferredSize(new Dimension(300,400));
		panel = new JPanel();
		s1 = new JHeroBox<String>(names); 
		s2 = new JHeroBox<String>(names);
		s3 = new JHeroBox<String>(names);
		s4 = new JHeroBox<String>(names);
		s5 = new JHeroBox<String>(names);
		s6 = new JHeroBox<String>(names);
		s7 = new JHeroBox<String>(names);
		s8 = new JHeroBox<String>(names);
		s9 = new JHeroBox<String>(names);
		s10 = new JHeroBox<String>(names);
		s1.h=new Hex(4,2,-6);
		s2.h=new Hex(4,5,-9);
		s3.h=new Hex(4,8,-12);
		s4.h=new Hex(4,11,-15);
		s5.h=new Hex(4,14,-18);
		s6.h=new Hex(22,-7,-15);
		s7.h=new Hex(22,-4,-18);
		s8.h=new Hex(22,-1,-21);
		s9.h=new Hex(22,2,-24);
		s10.h=new Hex(22,5,-27);
		t1 = new ArrayList<JHeroBox<String>>(){{add(s1);add(s2);add(s3);add(s4);add(s5);}};
		t2 = new ArrayList<JHeroBox<String>>(){{add(s6);add(s7);add(s8);add(s9);add(s10);}};
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 20));
		panel.add(new JLabel("Team 1"));
		panel.add(s1);
		panel.add(s2);
		panel.add(s3);
		panel.add(s4);
		panel.add(s5);
		s1.addActionListener(new selectListener());
		s2.addActionListener(new selectListener());
		s3.addActionListener(new selectListener());
		s4.addActionListener(new selectListener());
		s5.addActionListener(new selectListener());
		panel.add(new JLabel("Team 2"));
		panel.add(s6);
		panel.add(s7);
		panel.add(s8);
		panel.add(s9);
		panel.add(s10);
		s6.addActionListener(new selectListener());
		s7.addActionListener(new selectListener());
		s8.addActionListener(new selectListener());
		s9.addActionListener(new selectListener());
		s10.addActionListener(new selectListener());
		confirm = new JButton("Confirm");
		confirm.addActionListener(new buttonListener());
		panel.add(confirm);
		screen.add(panel);
		screen.pack();
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setVisible(true);
	}
	
	public void setListNone(JHeroBox<String>s,ArrayList<JHeroBox<String>> t) {
		s.removeAllItems();
		for(String s1:names) {
			boolean no = false;
			for(JHeroBox<String> c:t) {
				if(c!=s) {
					if(c.getSelectedItem().equals(s1)&&!c.getSelectedItem().equals("None"))
						no = true;
				}
			}
			if(!no)
				s.addItem(s1);
		}
	}
	
	public void setList(JHeroBox<String>s,ArrayList<JHeroBox<String>> t,String select) {
		s.removeAllItems();
		s.addItem(select);
		for(String s1:names) {
			boolean no = false;
			for(JHeroBox<String> c:t) {
				if(c!=s) {
					if(c.getSelectedItem().equals(s1)&&!c.getSelectedItem().equals("None"))
						no = true;
				}
			}
			if(!no&&!s1.equals(select))
				s.addItem(s1);
		}
	}
	
	class selectListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ArrayList<JHeroBox<String>> team;
			JHeroBox<String> s = (JHeroBox<String>) e.getSource();
			boolean found = false;
			for(JHeroBox<String> c:t1) {
				if(s==c) {
					found = true;
				}
			}
			if(found)
				team = t1;
			else {
				team = t2;
			}
			String selected = (String) s.getSelectedItem();
			if(selected.equals("None")) {
				setListNone(s,team);
				for(JHeroBox<String> c:team) {
					if(c!=s) {
						String str = (String)c.getSelectedItem();
						c.setSelectedItem(str);
					}
				}
			}else {
				setList(s,team,selected);
				for(JHeroBox<String> c:team) {
					if(c!=s) {
						String str = (String)c.getSelectedItem();
						c.setSelectedItem(str);
					}
				}
				for(JHeroBox<String> c:team) {
					if(c!=s) {
						int index = 0;
						boolean remove = false;
						for(int i = 0;i<c.getItemCount();i++) {
							if(((String)c.getItemAt(i)).equals(selected)) {
								index = i;
								remove = true;
							}
						}
						if (remove) {
							c.removeItemAt(index);;
						}
					}
				}
			}
		}
		
	}
	
	class buttonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			ArrayList<String> team1 = new ArrayList<String>();
			ArrayList<String> team2 = new ArrayList<String>();
			ArrayList<Hex> hex1 = new ArrayList<Hex>();
			ArrayList<Hex> hex2 = new ArrayList<Hex>();
			for(JHeroBox<String> c:t1) {
				if(!((String)c.getSelectedItem()).equals("None")) {
					team1.add((String) c.getSelectedItem());
					hex1.add(c.h);
				}
			}
			for(JHeroBox<String> c:t2) {
				if(!((String)c.getSelectedItem()).equals("None")) {
					team2.add((String) c.getSelectedItem());
					hex2.add(c.h);
				}
			}
			screen.setVisible(false);
			new Game(team1, team2,hex1,hex2);
		}
		
	}
	
	class JHeroBox<String> extends JComboBox<String>{
		Hex h;
		JHeroBox(String[] names){
			super(names);
		}
	}
	
	public static void main(String[] args) {
		Startup s = new Startup();
	}
}
