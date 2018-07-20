
public class BuffStack extends Buff{
	int stacks;
	int stackCap = 9;
	BuffStack(String effectName, Unit owner, int duration, Unit caster, boolean enchant,int stack) {
		super(effectName, owner, duration, caster, enchant);
		stacks = stack;
	}
	
	BuffStack(String effectName, Unit owner, int duration, Unit caster, boolean enchant,int stack,int stackCap) {
		super(effectName, owner, duration, caster, enchant);
		stacks = stack;
		this.stackCap = stackCap;
	}
	
	public void onAddition() {
		if(effectName.equals("Gathering Darkness")) {
			owner.currentArmor+=stacks*5;
		}
		if(effectName.equals("Earthen Resilience")) {
			owner.currentArmor+=stacks*10;
		}
	}
	
	public void onRemoval() {
		if(effectName.equals("Gathering Darkness")) {
			owner.currentArmor-=stacks*5;
		}
		if(effectName.equals("Earthen Resilience")) {
			owner.currentArmor-=stacks*10;
		}
	}
	
}
