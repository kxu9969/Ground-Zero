
public class Debuff extends Effect{
	Debuff(String effectName, Unit owner, int duration) {
		super(effectName, owner, duration);
	}
	
	Debuff(String effectName, Unit owner, int duration, boolean enchant) {
		super(effectName, owner, duration, enchant);
	}

	public void onAddition() {
		if(effectName.equals("Consuming Flame")) {
			owner.currentArmor=0;
		}
	}
	
	public void onRemoval() {
		if(effectName.equals("Timed Life")) {
			owner.die();
		}
		if(effectName.equals("Consuming Flame")) {
			if(true) {//has other armor setting effects
				owner.currentArmor=owner.defaultArmor;
			}
		}
	}

}
