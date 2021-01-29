import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Test {
	
	public static void main(String[] args) {
		
		ArrayDeque<Point2D.Double> path;
		Point p = new Point(toWidth(5), toHeight(6));
		String s = "5,0";
		
		path = Parser.parsePathing(s, p, 0.001, true, true);
		System.out.println(path.size());
	}
	
	public static int toHeight(int i) {
		
		return Game.PLAYSCREEN.height / 10 * i;
	}
	
	public static int toWidth(int i) {
		
		return Game.PLAYSCREEN.width / 10 * i;
	}
}