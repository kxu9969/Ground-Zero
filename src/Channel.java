import java.util.ArrayList;

public class Channel extends Buff{
	String methodName;
	ArrayList<Hex> affected;
	Channel(String effectName, Unit owner, int duration, Unit caster,String methodName,ArrayList<Hex> affected) {
		super(effectName, owner, duration, caster);
		this.methodName=methodName;
		this.affected = affected;
	}
	Channel(String effectName, Unit owner, int duration, Unit caster, boolean enchant,String methodName,ArrayList<Hex> affected) {
		super(effectName, owner, duration, caster, enchant);
		this.methodName=methodName;
		this.affected = affected;

	}
	public void onAddition() {}
	public void onRemoval() {
		if(duration<=0) {
			if(methodName.equals("Shadow Step")) {
				Hex h = affected.get(0);
				((Kaito_ShadowStep)h.occupied).die(true);
				owner.setPosition(h);
				for(Hex h1:owner.grid.hexes) {
					if(owner.position.distance(h1)==1&&h1.hasEnemy(owner)) {
						owner.addDebuff(new Debuff("Rooted",h1.occupied,1,owner,false));
					}
				}
				owner.rewriteBuff(new BuffStack("Gathering Darkness",owner,-1,owner,false,2),owner.buffs);
			}else if(methodName.equals("Herald of the End")) {
				Hex h = affected.get(0);
				((Saa_Herald)h.occupied).die(true);
				owner.setPosition(h);
				for(Hex h1:owner.grid.hexes) {
					if(owner.position.distance(h1)<=2&&h1.hasEnemy(owner)) {
						owner.addDebuff(new Debuff("Herald of the End",h1.occupied,1,owner,false));
					}
				}
			}
			else if(methodName.equals("Suppressive Fire")) {
				for(Hex h:affected) {
					if(h.occupied!=null) {
						owner.basicAttack(h);
					}
				}
			}
			else if(methodName.equals("Ancient Tongue: Firestorm")) {
				for(Hex h:affected) {
					if(h.occupied!=null) {
						h.occupied.takeAbility(80, owner, true, true);
					}
					for(Hex h1:owner.grid.hexes) {
						if(h1.distance(h)==1&&h1.occupied!=null) {
							h1.occupied.takeAbility(80, owner, true, true);
						}
					}
					h.addEffect(new TileEffect("Burning",owner,6,owner,false,h));
				}
			}
			else if(methodName.equals("Ancient Tongue: Blazing Pillars")) {
				for(Hex h:affected) {
					if(h.occupied!=null) {
						h.occupied.takeAbility(80, owner, false, true);
					}
					for(Hex h1:owner.grid.hexes) {
						if(h1.distance(h)==1&&h1.occupied!=null) {
							h1.occupied.takeAbility(80, owner, false, true);
						}
					}
					h.addEffect(new TileEffect("Burning",owner,6,owner,false,h));
				}
			}
			else if(methodName.equals("Ancient Tongue: Inferno")) {
				for(Hex h:affected) {
					for(Hex h1:owner.grid.hexes) {
						if(h1.distance(h)==1&&h1.hasEnemy(h.occupied)) {
							h1.occupied.takeAbility(60, owner, true, true);
							h.occupied.heal(40);
						}
					}
				}
			}
			else if(methodName.equals("Forbidden Tongue: Death")) {
				for(Unit u:owner.enemyTeam) {
					u.takeAbility(600, owner, false, false);
				}
			}
			else if(methodName.equals("Right Leg, Phoenix")) {
				Hex h = affected.get(0);
				Hex target = affected.get(1);
				((Senryu_Phoenix)h.occupied).die(true);
				owner.setPosition(h);
				if(target.occupied!=null) {
					target.occupied.takeAbility(150, owner, true, true);
				}
				for(Hex h1:target.allAdjacents()) {
					h1=owner.grid.getHex(h1);
					if(h1!=null&&h1.occupied!=null&&h1.occupied!=owner) {
						if(h1.hasEnemy(owner)) {
							h1.occupied.takeAbility(50, owner, true, true);
						}
						h1.push(owner.grid, target);
					}
				}
				owner.addSenryuStack = true;
				owner.abcdDelay[3]=true;

			}
		}else {
			if(methodName.equals("Shadow Step")) {
				Hex h = affected.get(0);
				((Kaito_ShadowStep)h.occupied).die(true);	
			}
			else if(methodName.equals("Herald of the End")) {
				Hex h = affected.get(0);
				((Saa_Herald)h.occupied).die(true);	
			}
			else if(methodName.equals("Right Leg, Phoenix")) {
				Hex h = affected.get(0);
				((Senryu_Phoenix)h.occupied).die(true);	
				owner.setPosition(h);
			}
		}
	}
}
