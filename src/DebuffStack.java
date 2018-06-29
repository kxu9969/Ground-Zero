
public class DebuffStack extends Debuff{
	int stacks;
	int stackCap = 9;
	DebuffStack(String effectName, Unit owner, int duration, boolean enchant,int stack) {
		super(effectName, owner, duration, enchant);
		stacks = stack;
	}
	
	DebuffStack(String effectName, Unit owner, int duration, boolean enchant,int stack,int stackCap) {
		super(effectName, owner, duration, enchant);
		stacks = stack;
		this.stackCap = stackCap;
	}
}
