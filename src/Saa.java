import java.awt.Color;
import java.util.ArrayList;

public class Saa extends Hero{

	Saa(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 800;
		currentHealth = maxHealth;
		maxStamina = 70;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 30;
		currentArmor = defaultArmor;
		armorPiercing = 20;
		basicRange = 1;
		ab1cdMax = 4;
		ab2cdMax = 3;
		ab3cdMax = 0;
		ultcdMax = 8;			
	}

	public void showAb1() {
		ability1(null);
	}

	public void ability1(Hex h) {
		for(Hex h1:grid.hexes) {
			if(position.distance(h1)<=3) {
				h1.addEffect(new TileEffect("Frozen",this,4,this,false,h1));
				if(!h1.isEmpty()) {
					addDebuff(new Debuff("Rooted",h1.occupied,1,this,false));
					if(h1.hasEnemy(this)) {
						h1.occupied.takeAbility(20, this, true, true);
					}
				}
			}
		}
		abcdDelay[0]=true;
		grid.game.endOfTurn();
	}

	public void showAb2() {
		for(Unit u:enemyTeam) {
			for(Hex h:grid.hexes) {
				if(h.isEmpty()&&u.position.distance(h)<=2) {
					h.color=Color.red;
				}
			}
		}
	}

	public void ability2(Hex h) {
		ArrayList<Hex> affected = new ArrayList<Hex>() {{add(h);}};
		addBuff(new Channel("Channeling",this,1,this,false,"Herald of the End",affected));
		String str;
		if(team==grid.game.team1) {
			str="Team 1";
		}
		else {
			str="Team 2";
		}
		Saa_Herald s = new Saa_Herald(grid,"Herald of the End",str,h,this);
		grid.game.occupants.add(s);
		abcdDelay[1]=true;
		grid.game.endOfTurn();
	}

	public void showAb3() {
		ability3(null);
	}

	public void ability3(Hex h) {
		addBuff(new Buff("Undying",this,2,this,false));
		rewriteDebuff(new DebuffStack("Undeath",this,-1,this,false,1,99),debuffs);
		ab3cd = 0 + ((DebuffStack)getDebuff("Undeath")).stacks/2;
		setStamina();
		abcdDelay[2]=true;
		grid.game.endOfTurn();
	}

	public void showUlt() {
		if(queue4.size()<2) {
			for(Hex h:grid.hexes) {
				if(position.distance(h)==1&&h.hasEnemy(this)) {
					h.color=Color.RED;
				}
			}
			for(Object h:queue4) {
				((Hex)h).color=null;
			}
		}
		position.color=Color.green;
	}

	public void clearUlt() {
		queue4.clear();
		grid.game.setButtons();
	}
	
	public void ultimate(Hex h) {
		if(h==position) {
			for(Object h1:queue4) {
				addDebuff(new Debuff("Time's End",((Hex)h1).occupied,1,this,true));
			}
			position.addEffect(new TileEffect("Stasis",this,-1,this,false,position));
			abcdDelay[3]=true;
			grid.game.endOfTurn();
		}else if(queue4.size()<2) {
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
		}		
	}
	
	public ArrayList<Unit> inAura(){
		ArrayList<Unit> affected = new ArrayList<Unit>();
		for(Hex h:grid.hexes) {
			if(h.distance(position)<=2&&!h.isEmpty()) {
				affected.add(h.occupied);
			}
		}
		affected.remove(this);
		return affected;
	}
	public void addAura(Unit u) {
		u.maxStamina+=30;
	}
	public void removeAura(Unit u) {
		u.maxStamina-=30;
		if(u.currentStamina>u.maxStamina) {
			u.currentStamina=u.maxStamina;
		}
	}

}
