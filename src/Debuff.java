
public class Debuff extends Effect{
	Debuff(String effectName, Unit owner, int duration, Unit caster) {
		super(effectName, owner, duration,caster);
	}
	
	Debuff(String effectName, Unit owner, int duration,Unit caster, boolean enchant) {
		super(effectName, owner, duration,caster, enchant);
	}

	public void onAddition() {
		if(effectName.equals("Armor Removed")) {
			owner.currentArmor=0;
		}
	}
	
	public void onRemoval() {
		if(effectName.equals("Timed Life")) {
			owner.die();
		}
		else if(effectName.equals("Armor Removed")) {
			if(!owner.hasDebuff("Armor Removed")) {//has other armor setting effects
				owner.currentArmor=owner.defaultArmor;
			}
		}
		else if(effectName.equals("Thorns' Embrace")) {
			owner.addDebuff(new Debuff("Silenced",owner,1,caster,false));
		}
		else if(effectName.equals("Vampiric Vine")) {
			caster.buffs.remove((Buff)info.get(0));
		}
		else if(effectName.equals("Time's End")) {
			owner.position.addEffect(new TileEffect("Stasis",caster,-1,caster,false,owner.position));
		}
	}

}
