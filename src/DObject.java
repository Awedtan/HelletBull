import java.awt.*;
import java.awt.geom.*;

public abstract class DObject extends Ellipse2D.Double {
	
	String sprite;
	Image image;
	double radianAngle = 0;
	double sinAngle = 0;
	double cosAngle = 1;
	
	abstract void kill();
	
	abstract void move();
	
	abstract void update();
	
	boolean collides(Shape s) {
		
		return Maths.intersects(this, s, radianAngle);
	}
	
	void draw(Graphics2D g2) {
		
		if (image == null)
			throw new RuntimeException("Image not found.");
		
		AffineTransform at = AffineTransform.getTranslateInstance(Maths.centerX(getBounds(), image.getWidth(null)), Maths.centerY(getBounds(), image.getHeight(null)));
		
		g2.rotate(radianAngle, Maths.centerX(getBounds()), Maths.centerY(getBounds()));
		g2.drawImage(image, at, null);
		g2.rotate(-radianAngle, Maths.centerX(getBounds()), Maths.centerY(getBounds()));
	}
}