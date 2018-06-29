
public class Debuff extends Effect{
	Debuff(String effectName, Unit owner, int duration) {
		super(effectName, owner, duration);
	}
	
	Debuff(String effectName, Unit owner, int duration, boolean enchant) {
		super(effectName, owner, duration, enchant);
	}

	public void onRemoval() {
		if(effectName.equals("Timed Life")) {
			owner.die();
		}
	}

}
