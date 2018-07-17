import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

public class Visual extends JPanel{
	Grid grid;
	final int Line_Thickness=3;
	BufferedImage image;
	JLabel canvas;
	Layout mainLayout = new Layout(Layout.flat,new Point(30,28),new Point(0,0));
	Layout borderLayout = new Layout(Layout.flat,new Point(27,25),new Point(0,0));

	Visual(Grid grid){
		this.grid = grid;
		image = new BufferedImage(Game.Visual_Width, Game.Visual_Height, BufferedImage.TYPE_INT_ARGB);
		canvas = new JLabel(new ImageIcon(image));
		this.add(canvas);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		image = new BufferedImage(((ImageIcon)canvas.getIcon()).getIconWidth(),(
				(ImageIcon)canvas.getIcon()).getIconHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();

		for (int x = 0; x < canvas.getWidth(); x++) {
			for (int y = 0; y < canvas.getHeight(); y++) {
				image.setRGB(x, y, Color.white.getRGB());
			}
		}
		g2.setColor(Color.BLACK);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		ArrayList<ArrayList<Point>> a = new ArrayList<ArrayList<Point>>() ;
		for(Hex h1: grid.hexes) {
			a.add(mainLayout.polygonCorners(h1));
		}
		for(ArrayList<Point> a1: a) {
			g2 = drawHex(g2,a1);
		}
		for(Hex h:grid.hexes) {
			if(h.hasEffect("Poisonseeds")) {
				g2 = fill(g2,mainLayout.hexToPixel(h),new Color(147,112,219).getRGB());
			}
			if(h.hasEffect("Nature's Bounty")) {
				g2.setColor(new Color(135,206,250));
				g2.fillOval((int)mainLayout.hexToPixel(h).x-14, (int)mainLayout.hexToPixel(h).y-14, 28, 28);
				g2.setColor(Color.black);
			}
			if(h.hasEffect("Burning")) {
				g2.setColor(Color.red);
				g2.fillOval((int)mainLayout.hexToPixel(h).x+6, (int)mainLayout.hexToPixel(h).y-17, 6, 6);
				g2.setColor(Color.black);
			}
			if(h.hasEffect("Thunder and Storm")) {
				g2.setColor(Color.cyan);
				g2.fillOval((int)mainLayout.hexToPixel(h).x-1, (int)mainLayout.hexToPixel(h).y-17, 6, 6);
				g2.setColor(Color.black);
			}
			if(h.hasEffect("Improvised Explosive")||h.hasEffect("D.M.R. Mine")) {
				g2.setColor(new Color(255,140,0));
				g2.fillOval((int)mainLayout.hexToPixel(h).x-8, (int)mainLayout.hexToPixel(h).y-17, 6, 6);
				g2.setColor(Color.black);
			}
			if(h.color!=null) {
				if(!(h.occupied instanceof ShadowStep)) {
					g2 = highlight(g2,h,mainLayout.hexToPixel(h),h.color);
				}
			}
			if(h.occupied!=null) {
				if(h.occupied.team==grid.game.team1)
					g2.setColor(Color.red);
				else
					g2.setColor(Color.blue);
				g2.drawString(h.occupied.name, (float)mainLayout.hexToPixel(h).x-25, 
						(float)mainLayout.hexToPixel(h).y);
				g2.setColor(Color.black);
			}
		}	
		canvas.setIcon(new ImageIcon(image));
	}
	
	public void clear() {
		for(Hex h:grid.hexes) {
			if(h.occupied==null) {
				h.color=null;
			}else if(h.occupied==grid.game.currentUnit){
				h.color=Color.blue;
			}else {
				h.color=Color.YELLOW;
			}
		}
	}
	
	public boolean checkBorder(Hex h,Point center,int rgb) {
		ArrayList<Point> corners= borderLayout.polygonCorners(h,center);
		for(Point p:corners) {
			int i = (int)p.x;
			int j = (int)p.y+1;
			if(image.getRGB(i,j)!=rgb)
				return false;
		}
		return true;
	}
	
	public Graphics2D highlight(Graphics2D g2, Hex h, Point center,Color color) {
		ArrayList<Point> a = borderLayout.polygonCorners(h,center);
		g2.setColor(color);
		g2 = drawHex(g2,a);
		g2.setColor(Color.BLACK);
		return g2;
	}

	public Graphics2D fill(Graphics2D g2, Point center,int rgb)	{//is broken
		int x = (int)center.x;
		int y = (int)center.y;
		int i = x;
		int j = y;
		while(image.getRGB(i, j)!=Color.BLACK.getRGB()) {
			j--;
		}
		j++;
		while(image.getRGB(i, j)!=Color.BLACK.getRGB()) {
			i--;
		}
		i++;
		while(image.getRGB(i, j)!=Color.BLACK.getRGB()) {
			while(image.getRGB(i, j)!=Color.BLACK.getRGB()) {
				image.setRGB(i, j, rgb);
				i++;
			}
			j++;
			i=x;
			while(image.getRGB(i, j)!=Color.BLACK.getRGB()) {
				i--;
			}
			i++;
		}
		g2 = image.createGraphics();
		g2.setColor(Color.black);		
		return g2;
	}

	public Graphics2D drawHex(Graphics2D g2, ArrayList<Point> a) {
		g2.setStroke(new BasicStroke(Line_Thickness));
		for(int i = 1;i<a.size();i++) {
			Shape s = new Line2D.Double(a.get(i-1).x, a.get(i-1).y,a.get(i).x, a.get(i).y);
			g2.draw(s);
		}
		Shape s = new Line2D.Double(a.get(a.size()-1).x, a.get(a.size()-1).y, a.get(0).x, a.get(0).y);
		g2.draw(s);
		return g2;
	}

}
