import java.awt.*;
import java.awt.geom.*;

public class Maths {
	
	public static double angleTo(Ellipse2D.Double origin, Ellipse2D.Double target) {
		// Finds the angle from the origin to the target
		// FOR ALL ANGLES IN THIS GAME: down is 0, up is +-180, right is 90, left is -90
		
		double angle;
		
		if (target.x > origin.x && target.y > origin.y) { // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x < origin.x && target.y > origin.y) { // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x < origin.x && target.y < origin.y) { // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x > origin.x && target.y < origin.y) { // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x == origin.x)
			if (target.y > origin.y)
				angle = 0;
			else
				angle = 180;
		else if (target.y == origin.y)
			if (target.x > origin.x)
				angle = 90;
			else
				angle = -90;
		else
			angle = 0;
		
		return angle;
	}
	
	public static double angleTo(double originX, double originY, double tarCenterX, double tarCenterY) {
		// Finds the angle from the origin coordinates to the target coordinates
		// FOR ALL ANGLES IN THIS GAME: down is 0, up is +-180, right is 90, left is -90
		
		double angle;
		
		if (tarCenterX > originX && tarCenterY > originY) { // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
			
		} else if (tarCenterX < originX && tarCenterY > originY) { // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
			
		} else if (tarCenterX < originX && tarCenterY < originY) { // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
			
		} else if (tarCenterX > originX && tarCenterY < originY) { // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
			
		} else if (tarCenterX == originX)
			if (tarCenterY > originY)
				angle = 0;
			else
				angle = 180;
		else if (tarCenterY == originY)
			if (tarCenterX > originX)
				angle = 90;
			else
				angle = -90;
		else
			angle = 0;
		
		return angle;
	}
	
	public static double centerX(Ellipse2D.Double ellipse) {
		// Returns the x coordinate of the center of the given ellipse
		
		return ellipse.x + ellipse.width / 2;
	}
	
	public static double centerY(Ellipse2D.Double ellipse) {
		// Returns the y coordinate of the center of the given ellipse
		
		return ellipse.y + ellipse.height / 2;
	}
	
	public static double centerX(Rectangle2D.Double ellipse) {
		// Returns the x coordinate of the center of the given rectangle
		
		return ellipse.x + ellipse.width / 2;
	}
	
	public static double centerY(Rectangle2D.Double ellipse) {
		// Returns the y coordinate of the center of the given rectangle
		
		return ellipse.y + ellipse.height / 2;
	}
	
	public static boolean checkInBounds(Ellipse2D.Double ellipse) {
		// Checks if the bullet is within some arbitrary rectangle
		// Returns false if the bullet exceeds these bounds
		
		int buffer = 10;
		
		double x1 = ellipse.x;
		double y1 = ellipse.y;
		double x2 = ellipse.x + ellipse.width;
		double y2 = ellipse.y + ellipse.height;
		
		if (x1 > Game.SCREENWIDTH - buffer || x2 < buffer || y1 > Game.SCREENHEIGHT - buffer || y2 < buffer)
			return false;
		return true;
	}
	
	public static double distanceTo(Ellipse2D.Double origin, Ellipse2D.Double target) {
		// Returns the distance from the center of the origin ellipse to the center of the target ellipse
		
		return Math.sqrt(Math.pow(centerY(origin) - centerY(target), 2) + Math.pow(centerX(origin) - centerX(target), 2));
	}
	
	public static double distanceTo(Rectangle2D.Double origin, Ellipse2D.Double target) {
		// Returns the distance from the center of the origin rectangle to the center of the target rectangle
		
		return Math.sqrt(Math.pow(centerY(origin) - centerY(target), 2) + Math.pow(centerX(origin) - centerX(target), 2));
	}
	
	public static Area ellipseArea(Ellipse2D.Double ellipse) {
		// Takes in an ellipse
		// Returns an area object of an octagon that's roughly represenative of the ellipse
		// This is needed because turning an ellipse directly into an area is way too slow
		
		Rectangle2D.Double bound = (Rectangle2D.Double) ellipse.getBounds2D();
		
		Point topLeft = new Point((int) (bound.x), (int) (bound.y));
		Point botLeft = new Point((int) (bound.x), (int) (bound.y + bound.height));
		Point topRight = new Point((int) (bound.x + bound.width), (int) (bound.y));
		Point botRight = new Point((int) (bound.x + bound.width), (int) (bound.y + bound.height));
		Point origin = new Point((int) (bound.x + bound.width / 2), (int) (bound.y + bound.height / 2));
		
		Point tlMid = midpoint(topLeft, midpoint(topLeft, origin));
		Point blMid = midpoint(botLeft, midpoint(botLeft, origin));
		Point trMid = midpoint(topRight, midpoint(topRight, origin));
		Point brMid = midpoint(botRight, midpoint(botRight, origin));
		
		Point left = new Point((int) (bound.x), (int) (bound.y + bound.height / 2));
		Point right = new Point((int) (bound.x + bound.width), (int) (bound.y + bound.height / 2));
		Point up = new Point((int) (bound.x + bound.width / 2), (int) (bound.y));
		Point down = new Point((int) (bound.x + bound.width / 2), (int) (bound.y + bound.height));
		
		Path2D.Double path = new Path2D.Double();
		
		path.moveTo(left.x, left.y);
		path.lineTo(tlMid.x, tlMid.y);
		path.lineTo(up.x, up.y);
		path.lineTo(trMid.x, trMid.y);
		path.lineTo(right.x, right.y);
		path.lineTo(brMid.x, brMid.y);
		path.lineTo(down.x, down.y);
		path.lineTo(blMid.x, blMid.y);
		path.closePath();
		
		return new Area(path);
	}
	
	public static boolean intersects(Ellipse2D.Double first, Ellipse2D.Double second) {
		// Takes in two ellipses
		// Checks if they roughly intersect
		
		Area thisArea = ellipseArea(first);
		Area ellArea = ellipseArea(second);
		
		Area temp = (Area) thisArea.clone();
		thisArea.subtract(ellArea);
		
		return !thisArea.equals(temp);
	}
	
	public static int log(int x) {
		
		return (int) (Math.log(x) / Math.log(2) + 1e-10);
	}
	
	public static Point midpoint(Point p1, Point p2) {
		// Takes in two points
		// Returns the midpoint of the line between p1 and p2
		
		return new Point((int) (p1.x + p2.x) / 2, (int) (p1.y + p2.y) / 2);
	}
	
	public static int toHeight(int i) {
		
		if (i > 10 || i < 0)
			throw new RuntimeException();
		
		return Game.SCREENHEIGHT / 10 * i;
	}
	
	public static int toWidth(int i) {
		
		if (i > 10 || i < 0)
			throw new RuntimeException();
		
		return Game.SCREENWIDTH / 10 * i;
	}
}
