import java.util.ArrayList;

public class Grid {
	ArrayList<Hex> hexes = new ArrayList<Hex>();
	Game game;
	
	Grid(Game game){
		this.game = game;
		int offset = 0, counter = 0;
		for (int q = 1; q <= 15; q++) {
			if(counter == 1) {
				offset--;
				counter = 0;
			}
			else {
				counter++;
			}
		    for (int r = 1+offset; r-offset <= 8; r++) {
		        hexes.add(new Hex(q, r, -q-r));
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
}
