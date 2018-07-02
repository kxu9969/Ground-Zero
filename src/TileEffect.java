
public class TileEffect extends Effect{
	Hex location;
	TileEffect(String effectName, Unit owner, int duration, Unit caster, Hex h) {
		super(effectName, owner, duration,caster);
		location = h;
	}
	TileEffect(String effectName, Unit owner, int duration, Unit caster,boolean enchant, Hex h) {
		super(effectName, owner, duration, caster, enchant);
		location = h;
	}
	
	public void onAddition() {}
	
	public void onRemoval() {
		if(effectName.equals("Poisonseeds")) {
			for(Hex h:owner.grid.hexes) {
				if(location.distance(h)==1&&!h.hasEffect("Poisonseeds")) {
					h.effects.add(new TileEffect("Poisonseeds",owner,2,caster,false,h));
				}
			}
		}
	}

}
