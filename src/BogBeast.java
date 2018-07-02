
public class BogBeast extends Summon{

	BogBeast(Grid grid, String name, String team, Hex h) {
		super(grid, name, team, h);
	}

	public void assembleStats() {
		maxHealth = 300;
		currentHealth = maxHealth;
		maxStamina = 45;
		currentStamina = 0;
		basicDamage = 40;
		defaultArmor = 50;
		currentArmor = defaultArmor;
		armorPiercing = 70;
		basicRange = 1;	
		qU="A monstrosity summoned by Ak’ar, it can root enemies with basic attacks and is hard to kill.";
		qP="Tendrils: Basic attacks root the target for 1 turn. Timed Life:  This unit automatically dies after 3 of its turns.\r\n";
	}
	
	public void basicAttack(Hex h,int damage,boolean armor,boolean shield,boolean end) {
		addDebuff(new Debuff("Rooted",h.occupied,1,this,false));
		super.basicAttack(h,damage,armor,shield,end);
	}

	@Override
	public void showAb1() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability1(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAb2() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability2(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAb3() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ability3(Hex h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showUlt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ultimate(Hex h) {
		// TODO Auto-generated method stub
		
	}
	


}
