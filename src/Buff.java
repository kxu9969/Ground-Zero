
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
		if(effectName.equals("Centered Stance")) {
			owner.armorPiercing+=10;
		}
		if(effectName.equals("Surface-to-Surface Missiles")) {
			owner.currentArmor+=30;
		}
		if(effectName.equals("Ancient Tongue: Inferno")) {
			owner.currentArmor+=20;
		}
	}
	
	public void onRemoval() {	
		if(effectName.equals("Second Ring, Fourth Sign")) {
			owner.basicDamage-=30;
		}
		if(effectName.equals("Centered Stance")) {
			owner.armorPiercing-=10;
		}
		if(effectName.equals("Surface-to-Surface Missiles")) {
			owner.currentArmor-=30;
		}
		if(effectName.equals("Ancient Tongue: Inferno")) {
			owner.currentArmor-=20;
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
