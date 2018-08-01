import java.awt.Color;
import java.util.ArrayList;

public class December extends Hero{

	December(Grid grid, String team, Hex h) {
		super(grid, team, h);
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean anotherTurn) {
		if(!h.equals(position)) {
			ArrayList<Hex> hexes = FractionalHex.hexExtrapolate(position,h,grid);
			while(hexes.size()>3) {
				hexes.remove(3);
			}
			for(Hex h1:hexes) {
				h1=grid.getHex(h1);
				if(h1.hasEnemy(this)) {
					partialBasic(h1, damage, armor, shield,true);
					break;
				}
			}
		}
		super.basicAttack(h, damage, armor, shield, anotherTurn);
	}
	
	public void assembleStats() {
		name = "December";
		title = "Arcane Archer";
		maxHealth = 450;
		currentHealth = maxHealth;
		maxStamina = 50;
		currentStamina = 0;
		basicDamage = 50;
		defaultArmor = 10;
		currentArmor = defaultArmor;
		armorPiercing = 30;
		basicRange = 5;
		ab1cdMax = 3;
		ab2cdMax = 3;
		ab3cdMax = 2;
		ultcdMax = 8;		
		qU="A werewolf archer who specializes in all things magical, December can output heavy damage to multiple enemies after a few turns of preparation yet is also able to protect himself through repositioning and disables.";
		qP="Arcane Penetration: Basic attacks pierce through for 3 tiles, damaging enemies behind and applying on-hit effects.";
		q1="Entangling Shot (3): Your next basic attack roots for 1 turn and deals 10 per turn additional damage ignoring armor for 2 turns. Refresh your basic attack.";
		q2="Warp Shot (3): Fire an arrow at an unoccupied target tile within 5 range, dealing basic attack damage to enemies the arrow passes through, then teleport to the arrow. Refresh your basic attack.";
		q3="Bow Mastery (2): Fire twice, basic attacking up to 2 different targets.";
		q4="Flurry (8): Warp time for 4 turns(enchant), causing your basic attacks to be echoed 3 turns later from the same position and in the same direction as initially fired. Refresh your basic attack.";
	}

	public void showAb1() {
		ability1(null);
	}

	public void ability1(Hex h) {
		rewriteBuff(new Buff("Entangling Shot",this,-1,this,false),buffs);
		grid.game.move.lock=true;
		grid.game.basic.lock=false;
		grid.game.ab1.lock=true;
		grid.game.ab2.lock=true;
		grid.game.ab3.lock=true;
		grid.game.ult.lock=true;
		grid.game.cancel.lock=false;
		grid.game.pass.lock=false;
		grid.game.clear();
		grid.game.setButtons();
		abcdDelay[0]=true;
	}

	public void showAb2() {
		for(Hex h:grid.hexes) {
			if(position.distance(h)<=5&&h.occupied==null) {
				h.color=Color.red;
			}
		}
	}

	public void ability2(Hex h) {
		ArrayList<Hex> hexes = FractionalHex.hexInterpolate(position,h);
		for(Hex h1:hexes) {
			h1=grid.getHex(h1);
			if(h1.hasEnemy(this)) {
				h1.occupied.takeAbility(basicDamage, this, true, true);
			}
		}
		setPosition(h);
		grid.game.move.lock=true;
		grid.game.basic.lock=false;
		grid.game.ab1.lock=true;
		grid.game.ab2.lock=true;
		grid.game.ab3.lock=true;
		grid.game.ult.lock=true;
		grid.game.cancel.lock=false;
		grid.game.pass.lock=false;
		grid.game.clear();
		grid.game.setButtons();
		abcdDelay[1]=true;
	}

	public void showAb3() {
		if(queue3.size()==0) {
			showBasic();
		}else if(queue3.size()==1) {
			showBasic();
			((Hex)queue3.get(0)).color=null;
		}
		position.color=Color.green;
	}

	public void ability3(Hex h) {
		if(h==position) {
			basicAttack((Hex)queue3.get(0),basicDamage,true,true,true);
			basicAttack((Hex)queue3.get(1),basicDamage,true,true,true);
			abcdDelay[2]=true;
			grid.game.endOfTurn();
		}
		else if(queue3.size()<2) {
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
		ultimate(null);
	}

	public void ultimate(Hex h) {
		rewriteBuff(new Buff("Flurry",this,4,this,true),buffs);
		grid.game.move.lock=true;
		grid.game.basic.lock=false;
		grid.game.ab1.lock=true;
		grid.game.ab2.lock=true;
		grid.game.ab3.lock=true;
		grid.game.ult.lock=true;
		grid.game.cancel.lock=false;
		grid.game.pass.lock=false;
		grid.game.clear();
		grid.game.setButtons();
		abcdDelay[3]=true;
	}

}
