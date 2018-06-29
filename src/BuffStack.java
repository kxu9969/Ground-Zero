
public class BuffStack extends Buff{
	int stacks;
	int stackCap = 9;
	BuffStack(String effectName, Unit owner, int duration, boolean enchant,int stack) {
		super(effectName, owner, duration, enchant);
		stacks = stack;
	}
	
	BuffStack(String effectName, Unit owner, int duration, boolean enchant,int stack,int stackCap) {
		super(effectName, owner, duration, enchant);
		stacks = stack;
		this.stackCap = stackCap;
	}
	
	
}
