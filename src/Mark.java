
public class Mark extends Debuff{
	Unit recipient;
	Mark(String effectName, Unit owner, int duration, boolean enchant, Unit recipient) {
		super(effectName, owner, duration, enchant);
		this.recipient = recipient;
	}

}
