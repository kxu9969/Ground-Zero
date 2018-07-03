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
				((ShadowStep)h.occupied).die(true);
				owner.setPosition(h);
				for(Hex h1:owner.grid.hexes) {
					if(owner.position.distance(h1)==1&&h1.hasEnemy(owner)) {
						owner.addDebuff(new Debuff("Rooted",h1.occupied,1,owner,false));
					}
				}
				owner.rewriteBuff(new BuffStack("Gathering Darkness",owner,-1,owner,false,2),owner.buffs);
				owner.grid.game.endOfTurn();
			}
		}else {
			if(methodName.equals("Shadow Step")) {
				Hex h = affected.get(0);
				((ShadowStep)h.occupied).die(true);			}
		}
	}
}
