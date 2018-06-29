
public class Buff extends Effect{
	Buff(String effectName, Unit owner, int duration) {
		super(effectName, owner, duration);
	}
	
	Buff(String effectName, Unit owner, int duration, boolean enchant) {
		super(effectName, owner, duration, enchant);
	}

	public void onRemoval() {		
	}

}
