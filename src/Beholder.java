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

	Beholder(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}

	public void assembleStats() {
		name = "Beholder";
		title = "Avatar of Chaos";
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
		qU="T̲̪̰̣̙̜̕ͅh̪̪e̦̬̮͇̜ m̟̼̳óṛ̀t̩͉̩͔̗͚̖̀a̺̳̼̙l͈̞͕ ̪͔̯͕̱C͉̺͈̥̮̲̱h̷̼̙a̢̝̖̭̗̫͍m̦̥̦͇̘͟p̲̪͉͙i̼̩̥̻o̺̣̥̩̰̜̪͠ṇ̳̲̖͇͖͓ ͍̗̼͈̜͘o̼̗̤̣f͎ ҉̤̪̹͓t͝h̝͠e̸͈̱ ͕̰̖ͅG̤͍̼̖̙͢o̼͘d̮̲̰̮͢ͅ ̖̻͚̭̪̤͉̀ọ̣̭̻̻f̳̟̜͓͠ͅ ͕̼̗͈̱̕C̠͇̙̯h̲̤͔̖̮͉͕a̸͉̤͓ͅo̱͇s͍͓̺͈,҉̥̮ B͙͚͎̟̣͍̕e͎̥̞̮h͔o̧͔͎̻̟̪̩ḽ̡̦d̗̹̦͚͈̫̙e͙̗̭̠̞r̷͉̺͚̦̲̜ ̟̩̫̼̣͝e̗̝m͢b̧̦̜͙ͅo̘d̼͎͚̥ͅi͚͡e̴͙̲̭͙s̖̗ ̛̠̙m̰̠̞͈̹a̖͘d̴͎̞̲͕̹̙n̗͕̝͡e̳̜̮ṣs̤̗͇̯̱͇ o̴̲͔̣̥̞̲f͈̥̰̜͇̮͚͡ ̵t͕͝h̞e̝̦ ̢h̝͚̺̦̗i͉͈̼̪̦̱g͎̥͜h̶̭̺e̷s̞̹t̛ ͍̖o̥͉̝͡r͉̭͙̺͟d͇̦̟̩͕̳̲ȩr̟ ̤͔͡b̵̟̹e͚̻̹̫̼̭y̼̠͕͚ͅo͓̣̘n̛̘͖̲̼d̻̱ ̵͈̠̗̘ͅc̡̗o͍mp̵͖̬̻ͅr̟e͈͓̠͚̞̻͟h̘̯͙͓̜̝ͅe͏͙͈̻̳n̰̫̦̞̹̭̜s̕i͍̼̟o̞͕̥̥͔̤n̪̳̙̯̮̺͠";
		qP="Event Horizon: At the start of each turn, change your abilities into those of other heroes at random.";
		q1="Total Chaos (0): At the start of each turn, this becomes a random hero’s ability.";
		q2="Total Chaos (0): At the start of each turn, this becomes a random hero’s ability.";
		q3="Total Chaos (0): At the start of each turn, this becomes a random hero’s ability.";
		q4="Reroll (8): Gain a random amount (1-20) of max health, damage, and armor. Restore yourself to max health and refill your stamina immediately.";
	}

	public Constructor generate(Class c) {
		Constructor[] constructors = c.getDeclaredConstructors();
		Constructor constructor = null;
		for(Constructor ctor:constructors) {
			Class<?>[] pTypes = ctor.getParameterTypes();
			if(pTypes.length==3) {
				constructor = ctor;
			}
		}
		return constructor;
	}

	public void startOfTurn() {
		super.startOfTurn();
		Hex h = null;
		int i = 0;
		do {
		i = (int) (Math.random()*grid.game.classes.length);
		c1=grid.game.classes[i];
		}while(grid.game.classes[i].equals(Beholder.class));
		do {
		i = (int) (Math.random()*grid.game.classes.length);
		c2=grid.game.classes[i];
		}while(grid.game.classes[i].equals(Beholder.class));
		do {
		i = (int) (Math.random()*grid.game.classes.length);
		c3=grid.game.classes[i];
		}while(grid.game.classes[i].equals(Beholder.class));
		
		String str;
		if(team==grid.game.team1) {
			str="Team 1";
		}
		else {
			str="Team 2";
		}
		try {
			Constructor constructor = generate(c1);
			u1 = (Unit) constructor.newInstance(grid,str,h);
			constructor = generate(c2);
			u2 = (Unit) constructor.newInstance(grid,str,h);
			constructor = generate(c3);
			u3 = (Unit) constructor.newInstance(grid,str,h);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		q1=u1.q1;
		q2=u2.q2;
		q3=u3.q3;
	}
	
	public void clearAb1() {
		u1.clearAb1();
	}
	
	public boolean ableAb1() {
		return u1.ableAb1();
	}

	public void showAb1() {
		u1.assembleStats(this);
		grid.game.pauseEndTurn = true;
		grid.game.triedToEnd = false;
		u1.showAb1();
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			this.assembleStats(u1);
			ab1cdMax=u1.ab1cdMax;
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
		setPosition(position);
		if(abcdDelay[0]) {
			grid.game.clear();
			grid.game.setButtons();
		}
		grid.game.pauseEndTurn = false;
		ab1cdMax=u1.ab1cdMax;
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			u1.position.occupied=this;
			grid.game.endOfTurn();
		}
	}
	
	public void clearAb2() {
		u2.clearAb2();
	}
	
	public boolean ableAb2() {
		return u2.ableAb2();
	}

	public void showAb2() {
		u2.assembleStats(this);
		grid.game.pauseEndTurn = true;
		grid.game.triedToEnd = false;
		u2.showAb2();
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			this.assembleStats(u2);
			ab2cdMax=u2.ab2cdMax;
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
		setPosition(position);
		if(abcdDelay[1]) {
			grid.game.clear();
			grid.game.setButtons();
		}
		grid.game.pauseEndTurn = false;
		ab2cdMax=u2.ab2cdMax;
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			u2.position.occupied=this;
			grid.game.endOfTurn();
		}
	}
	
	public void clearAb3() {
		u3.clearAb3();
	}
	
	public boolean ableAb3() {
		return u3.ableAb3();
	}

	public void showAb3() {
		u3.assembleStats(this);
		grid.game.pauseEndTurn = true;
		grid.game.triedToEnd = false;
		u3.showAb3();
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			this.assembleStats(u3);
			ab3cdMax=u3.ab3cdMax;
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
		setPosition(position);
		if(abcdDelay[3]) {
			grid.game.clear();
			grid.game.setButtons();
		}
		grid.game.pauseEndTurn = false;
		ab3cdMax=u3.ab3cdMax;
		if(grid.game.triedToEnd) {
			grid.game.triedToEnd = false;
			u3.position.occupied=this;
			grid.game.endOfTurn();
		}
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
