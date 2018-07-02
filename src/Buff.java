
public class Buff extends Effect{
	Buff(String effectName, Unit owner, int duration, Unit caster) {
		super(effectName, owner, duration,caster);
	}
	
	Buff(String effectName, Unit owner, int duration, Unit caster, boolean enchant) {
		super(effectName, owner, duration, caster, enchant);
	}

	public void onAddition() {
		if(effectName.equals("Second Ring, Fourth Sign")) {
			owner.basicDamage+=30;
		}
	}
	
	public void onRemoval() {	
		if(effectName.equals("Second Ring, Fourth Sign")) {
			owner.basicDamage-=30;
		}
		else if(effectName.equals("Singularity")) {
			if(owner.queue4.size()>0) {
				try {
					((Singularity)owner.queue4.get(0)).die();
				}catch(Exception e) {}
			}
		}
	}

}
