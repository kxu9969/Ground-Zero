import java.awt.Color;
import java.util.ArrayList;

class Hex
{
	Color color = null;
	Unit occupied = null;
	int movementCost = 1;
	ArrayList<TileEffect> effects = new ArrayList<TileEffect>();
	
	public void addEffect(TileEffect t) {
		t.onAddition();
		effects.add(t);
	}
	
	public void tickEffects() {
		for(int i = 0;i<effects.size();i++) {
			Effect b = effects.get(i);
			if(b.duration>0) {
				b.duration--;
			}if(b.duration==0) {
				removeEffect(b);
			}
		}
	}
	
	public Effect getEffect(String str) {
		for(Effect e: effects) {
			if(e.effectName.equals(str)) {
				return e;
			}
		}
		return null;
	}
	
	public void removeEffect(String str) {
		ArrayList<Effect> toBeRemoved = new ArrayList<Effect>();
		for(Effect b:effects) {
			if(b.effectName.equals(str)) {
				toBeRemoved.add(b);
			}
		}
		for(Effect b:toBeRemoved) {
			removeEffect(b);
		}
	}
	
	public void removeEffect(Effect d) {
		if(!d.enchant||d.duration==0) {
			d.onRemoval();
			effects.remove(d);
		}
	}
	
	public boolean hasEffect(String str) {
		for(Effect b:effects) {
			if(b.effectName.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public Hex push(Grid grid, Hex position) {//pushes from position onto this, return null if can't
		Hex difference = this.subtract(position);
		Hex nextHex = this.add(difference);
		nextHex = grid.getHex(nextHex);
		if(nextHex!=null&&nextHex.isEmpty()) {
			this.occupied.setPosition(nextHex);
			return this;//return empty hex
		}else if(nextHex==null) {
			return null;
		}else {
			Hex finish = nextHex.push(grid, this);	
			if(finish!=null) {
				this.occupied.setPosition(finish);
				return this;
			}
			return null;
		}
	}
	
	public ArrayList<Hex> pushList(Grid grid, Hex position) {//pushes from position onto this, return null if can't
		Hex difference = this.subtract(position);
		Hex nextHex = this.add(difference);
		nextHex = grid.getHex(nextHex);
		if(nextHex!=null&&nextHex.isEmpty()) {
			this.occupied.setPosition(nextHex);
			ArrayList<Hex> a = new ArrayList<Hex>();
			a.add(nextHex);
			a.add(this);
			return a ;//return empty hex
		}else if(nextHex==null) {
			return null;
		}else {
			ArrayList<Hex> finish = nextHex.pushList(grid, this);	
			if(finish!=null) {
				this.occupied.setPosition(finish.get(finish.size()-1));
				finish.add(this);
				return finish;
			}
			return null;
		}
	}
	
//	public boolean push(Grid grid, Hex position) {
//		Hex difference = this.subtract(position);
//		Hex nextHex = this.add(difference);
//		nextHex = grid.getHex(nextHex);
//		if(grid.game.onMap(nextHex)&&nextHex.isEmpty()) {
//			this.occupied.setPosition(nextHex);
//			return true;
//		}
//		return false;	
//	}
	
	public Hex setHero(Unit h) {
		color = Color.YELLOW;
		occupied = h;
		return this;
	}
	
	public Hex clearHex() {
		color = null;
		occupied = null;
		return this;
	}
	
    public Hex(int q, int r, int s)
    {
        this.q = q;
        this.r = r;
        this.s = s;
        if (q + r + s != 0) throw new IllegalArgumentException("q + r + s must be 0");
    }
    public final int q;
    public final int r;
    public final int s;

    public Hex add(Hex b)
    {
        return new Hex(q + b.q, r + b.r, s + b.s);
    }


    public Hex subtract(Hex b)
    {
        return new Hex(q - b.q, r - b.r, s - b.s);
    }


    public Hex scale(int k)
    {
        return new Hex(q * k, r * k, s * k);
    }


    public Hex rotateLeft()
    {
        return new Hex(-s, -q, -r);
    }


    public Hex rotateRight()
    {
        return new Hex(-r, -s, -q);
    }

    static public ArrayList<Hex> directions = new ArrayList<Hex>(){{add(new Hex(1, 0, -1)); add(new Hex(1, -1, 0)); add(new Hex(0, -1, 1)); add(new Hex(-1, 0, 1)); add(new Hex(-1, 1, 0)); add(new Hex(0, 1, -1));}};

    static public Hex direction(int direction)
    {
        return Hex.directions.get(direction);
    }


    public Hex neighbor(int direction)
    {
        return add(Hex.direction(direction));
    }

    static public ArrayList<Hex> diagonals = new ArrayList<Hex>(){{add(new Hex(2, -1, -1)); add(new Hex(1, -2, 1)); add(new Hex(-1, -1, 2)); add(new Hex(-2, 1, 1)); add(new Hex(-1, 2, -1)); add(new Hex(1, 1, -2));}};
    
    static public ArrayList<Hex> adjacents = new ArrayList<Hex>(){{add(new Hex(0, -1, 1)); add(new Hex(-1, 0, 1)); add(new Hex(-1, 1, 0)); add(new Hex(0, 1, -1)); add(new Hex(1, 0, -1)); add(new Hex(1, -1, 0));}};

    
    public Hex diagonalNeighbor(int direction)
    {
        return add(Hex.diagonals.get(direction));
    }
    
    public ArrayList<Hex> allAdjacents(){
    	ArrayList<Hex> adjacent = new ArrayList<Hex>();
    	for(Hex h:adjacents) {
    		adjacent.add(add(h));
    	}
    	return adjacent;
    }


    public int length()
    {
        return (int)((Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2);
    }


    public int distance(Hex b)
    {
        return subtract(b).length();
    }
    
    public boolean equals(Hex b) {
    	if(q == b.q&& r == b.r && s == b.s) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isEmpty() {
    	if(this.occupied==null||this.occupied instanceof TrampleOccupant) {
    		return true;
    	}
    	return false;
    }
    
    public boolean hasAlly(Unit h) {
    	if(this.occupied!=null&&this.occupied.team==h.team) {
    		return true;
    	}
    	return false;
    }
    
    public boolean hasEnemy(Unit h) {
    	if(this.occupied!=null&&this.occupied.team!=h.team) {
    		return true;
    	}
    	return false;
    }
    
    public boolean adjacentEnemy(Grid grid, Hero h1) {
    	for(Hex h: grid.hexes) {
    		if(h.distance(this)==1&&h.hasEnemy(h1)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean adjacentAlly(Grid grid, Hero h1) {
    	for(Hex h: grid.hexes) {
    		if(h.distance(this)==1&&h.hasAlly(h1)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public ArrayList<Hex> extendLineAdj(Grid grid,Hex h){
    	Hex difference = h.subtract(this);
    	Hex nextHex = this.add(difference);
    	ArrayList<Hex> hexes = new ArrayList<Hex>();
    	while(grid.game.onMap(nextHex)) {
    		hexes.add(grid.getHex(nextHex));
    		nextHex=nextHex.add(difference);
    	}
    	return hexes;
    }
    
    public String toString() {
    	return q+" "+r+" "+s;
    }

	public int getMoveCost(Unit u) {
		int i = movementCost;
		if(hasEffect("Frozen")&&getEffect("Frozen").owner!=u){
			i++;
		}
		return i;
	}
}
