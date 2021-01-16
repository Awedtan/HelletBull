import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Test {
	
	public static void main(String[] args) {
		
		ArrayDeque<Point2D.Double> path;
		Point p = new Point(Maths.toWidth(0), Maths.toHeight(-1));
		
		path = Parser.parsePathing("6,0,4", p, 0.003, true, true);
		System.out.println(path.size());
	}
}