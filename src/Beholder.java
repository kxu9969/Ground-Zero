import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Beholder extends Hero{//ALSO NEEDS TO SUPER THE HASABILITY METHODS
	Class c1;
	Class c2;
	Class c3;
	Unit u1;
	Unit u2;
	Unit u3;

	Beholder(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 600;
		currentHealth = maxHealth;
		maxStamina = 60;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 40;
		currentArmor = defaultArmor;
		armorPiercing = 20;
		basicRange = 1;
		ab1cdMax = 0;
		ab2cdMax = 0;
		ab3cdMax = 0;
		ultcdMax = 8;		
	}
	
	public Constructor generate(Class c) {
		Constructor[] constructors = c.getDeclaredConstructors();
		Constructor constructor = null;
		for(Constructor ctor:constructors) {
			Class<?>[] pTypes = ctor.getParameterTypes();
			if(pTypes.length==4) {
				constructor = ctor;
			}
		}
		return constructor;
	}

	public void startOfTurn() {
		Hex h = null;
		c1=Destiny.class;
		c2=BARie.class;
		c3=Amon.class;
		String str;
		if(team==grid.game.team1) {
			str="Team 1";
		}
		else {
			str="Team 2";
		}
		try {
			Constructor constructor = generate(c1);
			u1 = (Unit) constructor.newInstance(grid,name,str,h);
			constructor = generate(c2);
			u2 = (Unit) constructor.newInstance(grid,name,str,h);
			constructor = generate(c3);
			u3 = (Unit) constructor.newInstance(grid,name,str,h);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		q1=u1.q1;
		q2=u2.q2;
		q3=u3.q3;
		super.startOfTurn();
	}

	public void showAb1() {
		u1.assembleStats(this);
		grid.game.pauseEndTurn = true;
		grid.game.triedToEnd = false;
		u1.showAb1();
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			this.assembleStats(u1);
			grid.game.pauseEndTurn = false;
			grid.game.endOfTurn();
		}
	}

	public void ability1(Hex h) {
			u1.assembleStats(this);
			position.occupied=u1;
			grid.game.pauseEndTurn = true;
			u1.ability1(h);
			this.assembleStats(u1);
			u1.position.occupied=this;
			grid.game.pauseEndTurn = false;
			grid.game.endOfTurn();
	}

	public void showAb2() {
		u2.assembleStats(this);
		grid.game.pauseEndTurn = true;
		grid.game.triedToEnd = false;
		u2.showAb2();
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			this.assembleStats(u2);
			grid.game.pauseEndTurn = false;
			grid.game.endOfTurn();
		}
	}

	public void ability2(Hex h) {
		u2.assembleStats(this);
		position.occupied=u2;
		grid.game.pauseEndTurn = true;
		u2.ability2(h);
		this.assembleStats(u2);
		u2.position.occupied=this;
		grid.game.pauseEndTurn = false;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		u3.assembleStats(this);
		grid.game.pauseEndTurn = true;
		grid.game.triedToEnd = false;
		u3.showAb3();
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			this.assembleStats(u3);
			grid.game.pauseEndTurn = false;
			grid.game.endOfTurn();
		}
	}

	public void ability3(Hex h) {
		u3.assembleStats(this);
		position.occupied=u3;
		grid.game.pauseEndTurn = true;
		u3.ability3(h);
		this.assembleStats(u3);
		u3.position.occupied=this;
		grid.game.pauseEndTurn = false;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		ultimate(null);
	}

	public void ultimate(Hex h) {
		int i = (int) (Math.random()*20+1);
		maxHealth+=i;
		i = (int) (Math.random()*20+1);
		basicDamage+=i;
		i = (int) (Math.random()*20+1);
		defaultArmor+=i;
		currentArmor+=i;
		setStamina();
		abcdDelay[3]=true;
		grid.game.endOfTurn();
	}

}
