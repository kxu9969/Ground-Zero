import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Startup {
	JFrame screen;
	JPanel panel;
	String[] names = {"None","JAR.ie","Cragg","Blur","Olaf","Guay-Tho","Beholder"};
	JComboBox<String> s1;
	JComboBox<String> s2;
	JComboBox<String> s3;
	JComboBox<String> s4;
	JComboBox<String> s5;
	JComboBox<String> s6;
	JComboBox<String> s7;
	JComboBox<String> s8;
	JComboBox<String> s9;
	JComboBox<String> s10;
	ArrayList<JComboBox<String>> t1;
	ArrayList<JComboBox<String>> t2;
	JButton confirm;
	Startup(){
		screen = new JFrame("Hero Select");
		screen.setPreferredSize(new Dimension(300,400));
		panel = new JPanel();
		s1 = new JComboBox<String>(names); 
		s2 = new JComboBox<String>(names);
		s3 = new JComboBox<String>(names);
		s4 = new JComboBox<String>(names);
		s5 = new JComboBox<String>(names);
		s6 = new JComboBox<String>(names);
		s7 = new JComboBox<String>(names);
		s8 = new JComboBox<String>(names);
		s9 = new JComboBox<String>(names);
		s10 = new JComboBox<String>(names);
		t1 = new ArrayList<JComboBox<String>>(){{add(s1);add(s2);add(s3);add(s4);add(s5);}};
		t2 = new ArrayList<JComboBox<String>>(){{add(s6);add(s7);add(s8);add(s9);add(s10);}};
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
	
	public void setListNone(JComboBox<String>s,ArrayList<JComboBox<String>> t) {
		s.removeAllItems();
		for(String s1:names) {
			boolean no = false;
			for(JComboBox<String> c:t) {
				if(c!=s) {
					if(c.getSelectedItem().equals(s1)&&!c.getSelectedItem().equals("None"))
						no = true;
				}
			}
			if(!no)
				s.addItem(s1);
		}
	}
	
	public void setList(JComboBox<String>s,ArrayList<JComboBox<String>> t,String select) {
		s.removeAllItems();
		s.addItem(select);
		for(String s1:names) {
			boolean no = false;
			for(JComboBox<String> c:t) {
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
			ArrayList<JComboBox<String>> team;
			JComboBox<String> s = (JComboBox<String>) e.getSource();
			boolean found = false;
			for(JComboBox<String> c:t1) {
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
				for(JComboBox<String> c:team) {
					if(c!=s) {
						String str = (String)c.getSelectedItem();
						c.setSelectedItem(str);
					}
				}
			}else {
				setList(s,team,selected);
				for(JComboBox<String> c:team) {
					if(c!=s) {
						String str = (String)c.getSelectedItem();
						c.setSelectedItem(str);
					}
				}
				for(JComboBox<String> c:team) {
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
			ArrayList<String> heroes = new ArrayList<String>();
			ArrayList<String> team1 = new ArrayList<String>();
			ArrayList<String> team2 = new ArrayList<String>();
			for(JComboBox<String> c:t1) {
				if(!((String)c.getSelectedItem()).equals("None"))
					team1.add((String) c.getSelectedItem());
			}
			for(JComboBox<String> c:t2) {
				if(!((String)c.getSelectedItem()).equals("None"))
					team2.add((String) c.getSelectedItem());
			}
			screen.setVisible(false);
			new Game(team1, team2);
		}
		
	}
	
	public static void main(String[] args) {
		Startup s = new Startup();
	}
}
