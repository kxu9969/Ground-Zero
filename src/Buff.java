
public class Buff extends Effect{
	Buff(String effectName, Unit owner, int duration) {
		super(effectName, owner, duration);
	}
	
	Buff(String effectName, Unit owner, int duration, boolean enchant) {
		super(effectName, owner, duration, enchant);
	}

	public void onRemoval() {	
		if(effectName.equals("Singularity")) {
			if(owner.queue4.size()>0) {
				try {
					((Singularity)owner.queue4.get(0)).die();
				}catch(Exception e) {}
			}
		}
	}

}
