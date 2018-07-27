
public abstract class Hero extends PartialHero{
	boolean tetherDie=false;
	Hero(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}
	
	public void die() {
		if(hasBuff("Temporal Relativity")) {
			currentHealth = (int) getBuff("Temporal Relativity").info.get(0);
			removeSameBuff("Temporal Relativity");
		}else if(hasBuff("Original Sin")) {
			currentHealth = maxHealth/2;
			buffs.remove(getBuff("Original Sin"));
		}else {
			boolean tether = false;//is there a tether effect
			boolean expire = false;//is your tether already procc'd
			for(Unit u:team) {
				if(u instanceof Amon) {
					tether= true;
				}
			}
			if(tether) {
				if(tetherDie) {
					expire = true;
				}
			}
			if(tether&&expire) {
				tetherDie=false;
				super.die();
			}else if(tether&&!expire) {
				if(grid.game.ending) {
					addBuff(new Buff("Necrotic Tether",this,1,this,true));
					addDebuff(new Debuff("Cursed",this,1,this,true));
				}else {
					rewriteBuff(new Buff("Necrotic Tether",this,1,this,true),buffs);
					rewriteDebuff(new Debuff("Cursed",this,1,this,true),debuffs);
					this.currentHealth=0;
				}
			}else if(!tether) {
				super.die();
			}
		}
	}

}
