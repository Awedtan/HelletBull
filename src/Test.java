import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Test {
	
	public static void main(String[] args) {
		
		ArrayDeque<Point2D.Double> path;
		Point p = new Point(toWidth(8), toHeight(2));
		String s = "6,4,4";
		
		path = Parser.parsePathing(s, p, 0.003, true, true);
		System.out.println(path.size());
	}
	
	public static int toHeight(int i) {
		
		if (Math.abs(i) > Game.GRIDLINES)
			throw new RuntimeException();
		
		return Game.PLAYSCREEN.height / 10 * i;
	}
	
	public static int toWidth(int i) {
		
		if (Math.abs(i) > Game.GRIDLINES)
			throw new RuntimeException();
		
		return Game.PLAYSCREEN.width / 10 * i;
	}
}