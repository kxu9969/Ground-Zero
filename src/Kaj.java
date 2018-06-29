import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Kaj extends Hero{

	Kaj(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 500;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 30;
		armor = 40;
		armorPiercing = 40;
		basicRange = 5;
		ab1cdMax = 3;
		ab2cdMax = 2;
		ab3cdMax = 4;
		ultcdMax = 8;		
	}

	public void showAb1() {
		for(Hex h:grid.hexes) {
			if(!h.hasEffect("Nature's Bounty"))
				h.color=Color.red;
		}
	}

	public void ability1(Hex h) {
		h.effects.add(new TileEffect("Nature's Bounty",this,-1,false,h));
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(!h.hasAlly(this)) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		if(h.hasEnemy(this)) {
			addDebuff(h.occupied, new Debuff("Rooted",this,2,false));
		}
		else if(h.occupied==null) {
			String str;
			if(team==grid.game.team1) {
				str="Team 1";
			}
			else {
				str="Team 2";
			}
			TreeWall b = new TreeWall(grid,"Wall of Trees",str,h);
			addDebuff(b,new Debuff("Timed Life",b,3,true));
			grid.game.occupants.add(b);
		}
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		for(Hex h:grid.hexes) {
			if(h.occupied==null) {
				h.color=Color.red;
			}
		}
	}

	public void ability3(Hex h) {
		String str;
		if(team==grid.game.team1) {
			str="Team 1";
		}
		else {
			str="Team 2";
		}
		SpiritGuide b = new SpiritGuide(grid,"Spirit Guide",str,h);
		grid.game.addUnit(b);
		abcdDelay[2]=true;
		grid.game.endOfTurn();

	}

	public void showUlt() {
		if(queue4.size()==0) {
			for(Hex h:grid.hexes) {
				if(h.occupied!=null&&h.occupied instanceof Hero) {
					h.color=Color.red;
				}
			}
		}else if(queue4.size()==1) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.occupied==null) {
					h.color=Color.RED;
				}
			}
		}
	}

	public void clearUlt() {
		queue4.clear();
		grid.game.setButtons();
	}

	public void ultimate(Hex h) {
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
		}else if(queue4.size()==1) {
			try {
				String name = "Clone: "+((Hex)queue4.get(0)).occupied.name;
				String str;
				if(team==grid.game.team1) {
					str="Team 1";
				}
				else {
					str="Team 2";
				}
				Constructor[] constructors = ((Hex)queue4.get(0)).occupied.
						getClass().getDeclaredConstructors();
				Constructor constructor = null;
				for(Constructor ctor:constructors) {
					Class<?>[] pTypes = ctor.getParameterTypes();
					if(pTypes.length==4) {
						constructor = ctor;
					}
				}
				Unit c = (Unit) constructor.newInstance(grid,name,str,h);
				c.maxHealth = ((Hex)queue4.get(0)).occupied.maxHealth;
				c.currentHealth = ((Hex)queue4.get(0)).occupied.currentHealth;
				c.maxHealth/=2;
				c.currentHealth/=2;
				c.team = team;
				c.enemyTeam= enemyTeam;
				c.currentStamina=0;
				c.hasUlt=false;
				grid.game.addUnit(c);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			abcdDelay[3]=true;
			grid.game.endOfTurn();
		}
	}
	public ArrayList<Unit> inAura(){
		ArrayList<Unit> affected = team;
		affected.remove(this);
		return affected;
	}
	public void addAura(Unit u) {
		u.maxHealth*=1.1;
	}
	public void removeAura(Unit u) {
		u.maxHealth/=1.1;
		if(u.currentHealth>u.maxHealth) {
			u.currentHealth=u.maxHealth;
		}
	}

}
