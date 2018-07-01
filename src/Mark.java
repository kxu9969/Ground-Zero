
public class Mark extends Debuff{
	Unit caster;
	Mark(String effectName, Unit owner, int duration, boolean enchant, Unit caster) {
		super(effectName, owner, duration, enchant);
		this.caster = caster;
	}

}
