
public abstract class Effect {
	boolean enchant;
	int duration;
	String effectName;
	Unit owner;
	boolean toggle = false;
	
	Effect(String effectName, Unit owner, int duration){
        this.effectName = effectName;
        this.owner = owner;
        this.duration = duration;
    }
	
	Effect(String effectName, Unit owner, int duration,boolean enchant){
        this.effectName = effectName;
        this.owner = owner;
        this.duration = duration;
        this.enchant = enchant;
    }
	
	public abstract void onAddition();
	
	public abstract void onRemoval() ;
	
	public String toString() {
		String str = "";
		str+=effectName;
		if(enchant) {
			str+="(E)";
		}
		if(duration>0) {
		str+="(D:"+duration+")";
		}
		if(this instanceof BuffStack) {
			str+="(S:"+((BuffStack)this).stacks+")";
		}
		if(this instanceof DebuffStack) {
			str+="(S:"+((DebuffStack)this).stacks+")";
		}
		return str;
	}
}
