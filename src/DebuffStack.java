
public class DebuffStack extends Debuff{
	int stacks;
	int stackCap = 9;
	DebuffStack(String effectName, Unit owner, int duration, Unit caster, boolean enchant,int stack) {
		super(effectName, owner, duration, caster, enchant);
		stacks = stack;
	}
	
	DebuffStack(String effectName, Unit owner, int duration, Unit caster, boolean enchant,int stack,int stackCap) {
		super(effectName, owner, duration, caster, enchant);
		stacks = stack;
		this.stackCap = stackCap;
	}
}
