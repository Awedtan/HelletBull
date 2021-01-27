import java.awt.*;
import java.awt.geom.*;

public class Maths {
	
	public static double angleTo(Rectangle origin, Rectangle target) {
		// Finds the angle from the origin to the target
		// FOR ALL ANGLES IN THIS GAME: down is 0, up is +-180, right is 90, left is -90
		
		return angleTo(centerX(origin), centerY(origin), centerX(target), centerY(target));
	}
	
	public static double angleTo(double originX, double originY, double targetX, double targetY) {
		// Finds the angle from the origin coordinates to the target coordinates
		// FOR ALL ANGLES IN THIS GAME: down is 0, up is +-180, right is 90, left is -90
		
		double angle;
		
		if (targetX > originX && targetY > originY) // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(targetX - originX) / Math.abs(targetY - originY)));
		
		else if (targetX < originX && targetY > originY) // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(targetX - originX) / Math.abs(targetY - originY)));
		
		else if (targetX < originX && targetY < originY) // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(targetX - originX) / Math.abs(targetY - originY)));
		
		else if (targetX > originX && targetY < originY) // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(targetX - originX) / Math.abs(targetY - originY)));
		
		else if (targetX == originX)
			if (targetY > originY)
				angle = 0;
			else
				angle = 180;
			
		else if (targetY == originY)
			if (targetX > originX)
				angle = 90;
			else
				angle = -90;
		else
			angle = 0;
		
		return angle;
	}
	
	public static double centerX(Rectangle rect) {
		// Returns the x coordinate of the center of the given rectangle
		
		return centerX(rect, 0);
	}
	
	public static double centerY(Rectangle rect) {
		// Returns the y coordinate of the center of the given rectangle
		
		return centerY(rect, 0);
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
		// Checks if the rectangle is within some arbitrary bounds
		// Returns an int depending on if and how the rectangle exceeds these bounds
		
		double x1 = rect.x;
		double y1 = rect.y;
		double x2 = rect.x + rect.width;
		double y2 = rect.y + rect.height;
		
		if (x2 > Game.PLAYSCREEN.width - buffer || x1 < buffer)
			if (y2 > Game.PLAYSCREEN.height - buffer || y1 < buffer)
				return 2; // Both horizontally and vertically
			else
				return 0; // Only horizontally
		else if (y2 > Game.PLAYSCREEN.height - buffer || y1 < buffer)
			return 1; // Only vertically
			
		return -1; // Doesn't
	}
	
	public static double distanceTo(Rectangle origin, Rectangle target) {
		// Returns the distance from the center of the origin rectangle to the center of the target rectangle
		
		return distanceTo(centerX(origin), centerY(origin), centerX(target), centerY(target));
	}
	
	public static double distanceTo(double originX, double originY, double targetX, double targetY) {
		// Returns the distance from one coordinate to another
		
		return Math.sqrt(Math.pow(originY - targetY, 2) + Math.pow(originX - targetX, 2));
	}
	
	public static Area ellipseHitbox(Shape shape) {
		// Takes in an shape
		// Assumes it is an ellipse
		// Returns an Area object of an octagon that's roughly represenative of the ellipse but 2/3 the size
		// This is needed because working directly with an ellipse is way too slow
		
		Rectangle2D.Double bound = (Rectangle2D.Double) shape.getBounds2D();
		
		bound.x += bound.width / 6.0; // Resizing the shape
		bound.y += bound.height / 6.0;
		bound.width *= (2.0 / 3.0);
		bound.height *= (2.0 / 3.0);
		
		Point topLeft = new Point((int) (bound.x), (int) (bound.y)); // Creating a rectangle that encompasses the entire shape
		Point botLeft = new Point((int) (bound.x), (int) (bound.y + bound.height));
		Point topRight = new Point((int) (bound.x + bound.width), (int) (bound.y));
		Point botRight = new Point((int) (bound.x + bound.width), (int) (bound.y + bound.height));
		Point origin = new Point((int) (bound.x + bound.width / 2), (int) (bound.y + bound.height / 2));
		
		Point tlMid = midpoint(topLeft, midpoint(topLeft, origin)); // Resizing the rectangle so its corners are 3/4 of the original distance to the center of the rectangle
		Point blMid = midpoint(botLeft, midpoint(botLeft, origin));
		Point trMid = midpoint(topRight, midpoint(topRight, origin));
		Point brMid = midpoint(botRight, midpoint(botRight, origin));
		
		Point left = new Point((int) (bound.x), (int) (bound.y + bound.height / 2)); // Creating a rhombus with points on the original rectangle's sides' midpoint
		Point right = new Point((int) (bound.x + bound.width), (int) (bound.y + bound.height / 2));
		Point up = new Point((int) (bound.x + bound.width / 2), (int) (bound.y));
		Point down = new Point((int) (bound.x + bound.width / 2), (int) (bound.y + bound.height));
		
		Path2D.Double path = new Path2D.Double();
		
		path.moveTo(left.x, left.y); // Connecting the points on the resized rectangle and rhombus to create an octagon
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
	
	public static double equivalentAngle(double i) {
		// Takes in an angle
		// Returns an equivalent angle with the smallest absolute value
		// 210 -> -150, -350 -> 10, 170 -> 170
		
		if (i > 180)
			return -180 + (i % 180);
		else if (i < -180)
			return 180 + (i % -180);
		else
			return i;
	}
	
	public static int fromHeight(int i) {
		// Turns pixel coordinates into grid coordinates
		
		return i / (Game.PLAYSCREEN.height / 10);
	}
	
	public static int fromWidth(int i) {
		// Turns pixel coordinates into grid coordinates
		
		return i / (Game.PLAYSCREEN.width / 10);
	}
	
	public static boolean intersects(Shape first, Shape second) {
		// Takes in two ellipses
		// Checks if they intersect
		
		Area firArea = ellipseHitbox(first);
		Area secArea = ellipseHitbox(second);
		
		Area temp = (Area) firArea.clone(); // This is the best way of checking for collisions I could think of
		firArea.subtract(secArea);
		
		return !firArea.equals(temp);
	}
	
	public static boolean intersects(Shape first, Shape second, double angle) {
		// Takes in two ellipses
		// Rotates the first one
		// Checks if they intersect
		
		Area firArea = new Area(rotate(ellipseHitbox(first), angle));
		Area secArea = ellipseHitbox(second);
		
		Area temp = (Area) firArea.clone();
		firArea.subtract(secArea);
		
		return !firArea.equals(temp);
	}
	
	public static Point midpoint(Point p1, Point p2) {
		// Takes in two points
		// Returns the midpoint between p1 and p2
		
		return new Point((int) (p1.x + p2.x) / 2, (int) (p1.y + p2.y) / 2);
	}
	
	public static Shape rotate(Shape s, double angle) {
		// Takes in a shape and an angle
		// Returns a rotated instance of the shape
		
		return AffineTransform.getRotateInstance(angle, centerX(s.getBounds()), centerY(s.getBounds())).createTransformedShape(s);
	}
	
	public static double toRadians(double angle) {
		// Takes in an angle
		// Returns the radian value of that angle
		
		return (-angle / 180.0 * Math.PI);
	}
	
	public static int toHeight(int i) {
		// Turns grid coordinates into pixel coordinates
		
		return Game.PLAYSCREEN.height / 10 * i;
	}
	
	public static int toWidth(int i) {
		// Turns grid coordinates into pixel coordinates
		
		return Game.PLAYSCREEN.width / 10 * i;
	}
}