import java.awt.*;
import java.awt.geom.*;

public class Maths {
	
	public static double angleTo(Rectangle origin, Rectangle target) {
		// Finds the angle from the origin to the target
		// FOR ALL ANGLES IN THIS GAME: down is 0, up is +-180, right is 90, left is -90
		
		double angle;
		
		if (target.x > origin.x && target.y > origin.y) // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
		
		else if (target.x < origin.x && target.y > origin.y) // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
		
		else if (target.x < origin.x && target.y < origin.y) // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
		
		else if (target.x > origin.x && target.y < origin.y) // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
		
		else if (target.x == origin.x)
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
		
		if (tarCenterX > originX && tarCenterY > originY) // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
		
		else if (tarCenterX < originX && tarCenterY > originY) // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
		
		else if (tarCenterX < originX && tarCenterY < originY) // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
		
		else if (tarCenterX > originX && tarCenterY < originY) // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
		
		else if (tarCenterX == originX)
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
	
	public static double centerX(Rectangle rect) {
		// Returns the x coordinate of the center of the given rectangle
		
		return rect.x + rect.width / 2;
	}
	
	public static double centerY(Rectangle rect) {
		// Returns the y coordinate of the center of the given rectangle
		
		return rect.y + rect.height / 2;
	}
	
	public static double centerX(Rectangle rect, double offset) {
		// Returns the x coordinate of the center of the given rectangle with an offset
		
		return rect.x + rect.width / 2 - offset / 2;
	}
	
	public static double centerY(Rectangle rect, double offset) {
		// Returns the y coordinate of the center of the given rectangle with an offset
		
		return rect.y + rect.height / 2 - offset / 2;
	}
	
	public static int checkInBounds(Rectangle rect, double buffer) {
		// Checks if the rectangle is within some arbitrary rectangle
		// Returns false if the rectangle exceeds these bounds
		
		double x1 = rect.x;
		double y1 = rect.y;
		double x2 = rect.x + rect.width;
		double y2 = rect.y + rect.height;
		
		if (x2 > Game.PLAYSCREEN.width - buffer || x1 < buffer)
			if (y2 > Game.PLAYSCREEN.height - buffer || y1 < buffer)
				return 2;
			else
				return 0;
		else if (y2 > Game.PLAYSCREEN.height - buffer || y1 < buffer)
			return 1;
		
		return -1;
	}
	
	public static double distanceTo(Rectangle origin, Rectangle target) {
		// Returns the distance from the center of the origin rectangle to the center of the target rectangle
		
		return Math.sqrt(Math.pow(centerY(origin.getBounds()) - centerY(target.getBounds()), 2) + Math.pow(centerX(origin.getBounds()) - centerX(target.getBounds()), 2));
	}
	
	public static double distanceTo(double originX, double originY, double targetX, double targetY) {
		// Returns the distance from the center of the origin rectangle to the center of the target rectangle
		
		return Math.sqrt(Math.pow(originY - targetY, 2) + Math.pow(originX - targetX, 2));
	}
	
	public static Area octagonShape(Shape shape) {
		// Takes in an ellipse shape
		// Returns an area object of an octagon that's roughly represenative of the ellipse
		// This is needed because turning an ellipse directly into an area is way too slow
		
		Rectangle2D.Double bound = (Rectangle2D.Double) shape.getBounds2D();
		
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
	
	public static boolean intersects(Shape first, Shape second) {
		// Takes in two ellipses
		// Checks if they intersect
		
		Area firArea = octagonShape(first);
		Area secArea = octagonShape(second);
		
		Area temp = (Area) firArea.clone();
		firArea.subtract(secArea);
		
		return !firArea.equals(temp);
	}
	
	public static boolean intersects(Shape first, Shape second, double angle) {
		// Takes in two ellipses
		// Checks if they intersect
		
		Area firArea = new Area(rotate(octagonShape(first), angle));
		Area secArea = octagonShape(second);
		
		Area temp = (Area) firArea.clone();
		firArea.subtract(secArea);
		
		return !firArea.equals(temp);
	}
	
	public static int log(int x) {
		
		return (int) (Math.log(x) / Math.log(2) + 1e-10);
	}
	
	public static Point midpoint(Point p1, Point p2) {
		// Takes in two points
		// Returns the midpoint between p1 and p2
		
		return new Point((int) (p1.x + p2.x) / 2, (int) (p1.y + p2.y) / 2);
	}
	
	public static Shape rotate(Shape s, double angle) {
		
		return AffineTransform.getRotateInstance(angle, Maths.centerX(s.getBounds()), Maths.centerY(s.getBounds())).createTransformedShape(s);
	}
	
	public static int toHeight(int i) {
		
		if (Math.abs(i) > Game.GRIDLINES)
			throw new RuntimeException();
		
		if (i >= 0)
			return Game.PLAYSCREEN.height / 10 * i;
		else
			return Game.PLAYSCREEN.height / 10 * i;
	}
	
	public static double toRadians(double angle) {
		// System.out.println(Math.abs(angle)/180);
		return -angle/180.0 * Math.PI;
	}
	
	public static int toWidth(int i) {
		
		if (Math.abs(i) > Game.GRIDLINES)
			throw new RuntimeException();
		
		if (i >= 0)
			return Game.PLAYSCREEN.width / 10 * i;
		else
			return Game.PLAYSCREEN.width / 10 * i;
	}
}
