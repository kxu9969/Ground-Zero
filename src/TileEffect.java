
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
	
	public void onAddition() {
		if(effectName.equals("Stasis")) {
			owner.grid.stasisHex(location);
			if(location.occupied!=null) {
				owner.addDebuff(new Debuff("Stasis",location.occupied,-1,owner,true));
			}
		}
	}
	
	public void onRemoval() {
		if(effectName.equals("Poisonseeds")) {
			for(Hex h:owner.grid.hexes) {
				if(location.distance(h)==1&&!h.hasEffect("Poisonseeds")) {
					h.effects.add(new TileEffect("Poisonseeds",owner,2,caster,false,h));
				}
			}
		}else if(effectName.equals("Stasis")) {
			owner.grid.restoreHex(location);
			if(location.occupied!=null) {
				location.occupied.debuffs.remove(location.occupied.getDebuff("Stasis"));
			}
		}
		else if(effectName.equals("Improvised Explosive")) {
			for(Hex h:owner.grid.hexes) {
				if(location.distance(h)==1&&h.hasEnemy(owner)) {
					h.occupied.takeAbility(70, owner, true, true);
					owner.addDebuff(new Debuff("Cursed",h.occupied,1,owner,false));
				}
			}
		}
		else if(effectName.equals("Echo")) {
			for(int i = info.size()-1;i>=0;i--) {
				owner.basicAttack(((Hex)info.get(i)), owner.basicDamage, true, true, true);
			}
		}
	}

}
