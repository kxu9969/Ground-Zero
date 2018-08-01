import java.awt.Color;
import java.util.ArrayList;

public class Grid {
	ArrayList<Hex> hexes = new ArrayList<Hex>();
	ArrayList<Hex> deleted = new ArrayList<Hex>();
	ArrayList<Hex> stasis = new ArrayList<Hex>();
	Game game;

	Grid(Game game){//25 columns of 20-21
		this.game = game;
		int offset = 0, counter = 0;
		for (int q = 1; q <= 25; q++) {
			if(counter == 1) {
				offset--;
				counter = 0;
			}
			else {
				counter++;
			}
			for (int r = 1+offset; r-offset <= 18; r++) {
				hexes.add(new Hex(q, r, -q-r));
			}
			if(q%2==0) {
				hexes.add(new Hex(q,19+offset,-q-19-offset));
			}
		}
	}

	public Hex getHex(Hex h) {
		for(Hex h1:hexes) {
			if(h.equals(h1)) {
				return h1;
			}
		}
		return null;
	}

	public Hex getHex(int q, int r, int s) {
		for(Hex h:hexes) {
			if(h.q==q&&h.r==r&&h.s==s) {
				return h;
			}
		}
		return null;
	}
	
	public Hex getDeletedHex(Hex h) {
		for(Hex h1:deleted) {
			if(h.equals(h1)) {
				return h1;
			}
		}
		return null;
	}
	
	public Hex getStasisHex(Hex h) {
		for(Hex h1:stasis) {
			if(h.equals(h1)) {
				return h1;
			}
		}
		return null;
	}
	
	public void deleteHex(Hex h) {
		for(Hex h1:hexes) {
			if(h.equals(h1)) {
				hexes.remove(h);
				deleted.add(h);
				h.color=null;
				return;
			}
		}
	}
	
	public void stasisHex(Hex h) {
		for(Hex h1:hexes) {
			if(h.equals(h1)) {
				hexes.remove(h);
				stasis.add(h);
				h.color=Color.gray;
				return;
			}
		}
	}
	
	public void restoreHex(Hex h) {
		for(Hex h1:deleted) {
			if(h.equals(h1)) {
				hexes.add(h);
				deleted.remove(h);
				h.color=null;
				return;
			}
		}
		for(Hex h1:stasis) {
			if(h.equals(h1)) {
				hexes.add(h);
				stasis.remove(h);
				h.color=null;
				return;
			}
		}
	}

	public ArrayList<Hex> floodFill(Hex start, int fillRange) {
		ArrayList<Hex> list = new ArrayList<Hex>();
		ArrayList<MovementHex> List = new ArrayList<MovementHex>();
		int steps = 0;

		for(Hex h:start.allAdjacents()) {
			if(game.onMap(h)) {
				h=getHex(h);
				continueFill(h,fillRange,steps+1,List);
			}
		}
		for(MovementHex h:List) {
			list.add(h.h);
		}
		return list;
	}
	
	public boolean validTarget() {
		for(Hex h:hexes) {
			if(h.color==Color.red) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Hex> fillMove(Hex start, int fillRange,Unit u) {
		ArrayList<Hex> list = new ArrayList<Hex>();
		ArrayList<MovementHex> List = new ArrayList<MovementHex>();
		int steps = 0;

		for(Hex h:start.allAdjacents()) {
			if(game.onMap(h)) {
				h=getHex(h);
				continueFill(h,fillRange,steps+h.getMoveCost(u),List,u);
			}
		}
		for(MovementHex h:List) {
			list.add(h.h);
		}
		return list;
	}

	private void continueFill(Hex start, int fillRange, int steps, ArrayList<MovementHex> list) {
		if(steps>fillRange||!start.isEmpty()) {
			return;
		}else if(contains(list,start)&&steps<fetch(list,start).steps) {
			rewrite(list,start,steps);
		}else {
			list.add(new MovementHex(start,steps));
		}
		if(!(start.occupied!=null&&start.occupied instanceof TrampleOccupant)){
			for(Hex h:start.allAdjacents()) {
				if(game.onMap(h)) {
					h=getHex(h);
					continueFill(h,fillRange,steps+h.movementCost,list);
				}
			}
		}
	}
	
	private void continueFill(Hex start, int fillRange, int steps, ArrayList<MovementHex> list,Unit u) {
		if(steps>fillRange||!start.isEmpty()) {
			return;
		}else if(contains(list,start)&&steps<fetch(list,start).steps) {
			rewrite(list,start,steps);
		}else {
			list.add(new MovementHex(start,steps));
		}
		if(!(start.occupied!=null&&start.occupied instanceof TrampleOccupant)){
			for(Hex h:start.allAdjacents()) {
				if(game.onMap(h)) {
					h=getHex(h);
					continueFill(h,fillRange,steps+h.getMoveCost(u),list);
				}
			}
		}
	}

	class MovementHex{
		Hex h;
		int steps;
		MovementHex(Hex h, int steps){
			this.h=h;
			this.steps = steps;
		}
	}

	public MovementHex fetch(ArrayList<MovementHex> list,Hex h) {
		for(MovementHex h1:list) {
			if(h1.h.equals(h)) {
				return h1;
			}
		}
		return null;
	}

	public boolean contains(ArrayList<MovementHex> list,Hex h) {
		for(MovementHex h1:list) {
			if(h1.h.equals(h)) {
				return true;
			}
		}
		return false;
	}

	public void rewrite(ArrayList<MovementHex> list,Hex h,int steps) {
		for(MovementHex h1:list) {
			if(h1.h.equals(h)) {
				h1.steps=steps;
			}
		}
	}
}
