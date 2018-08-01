
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
		else if(effectName.equals("Earthen Resilience")) {
			owner.currentArmor+=stacks*10;
		}
		else if(effectName.equals("Heavenly Wisdom")) {
			owner.armorPiercing+=stacks*5;
			owner.currentArmor+=stacks*5;
			owner.maxStamina-=stacks*5;
		}
		else if(effectName.equals("Earthen Strength")) {
			owner.currentHealth+=stacks*30;
			owner.maxHealth+=stacks*30;
			owner.basicDamage+=stacks*5;
			owner.maxStamina+=stacks*5;
		}
		else if(effectName.equals("Mountain's Resilience")) {
			owner.currentArmor+=stacks*5;
			owner.maxStamina+=stacks*5;
		}
	}
	
	public void onRemoval() {
		if(effectName.equals("Gathering Darkness")) {
			owner.currentArmor-=stacks*5;
		}
		else if(effectName.equals("Earthen Resilience")) {
			owner.currentArmor-=stacks*10;
		}
		else if(effectName.equals("Heavenly Wisdom")) {
			owner.armorPiercing-=stacks*5;
			owner.currentArmor-=stacks*5;
			owner.maxStamina+=stacks*5;
		}
		else if(effectName.equals("Earthen Strength")) {
			owner.currentHealth-=stacks*30;
			owner.maxHealth-=stacks*30;
			owner.basicDamage-=stacks*5;
			owner.maxStamina-=stacks*5;
		}
		else if(effectName.equals("Mountain's Resilience")) {
			owner.currentArmor-=stacks*5;
			owner.maxStamina-=stacks*5;
		}
	}
	
}
