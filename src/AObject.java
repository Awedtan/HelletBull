import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public abstract class AObject extends DObject {
	// Class with animated sprites (enemies)
	// Only 2 frames of animation
	// Images have two halves, only one is drawn at any given moment
	
	final int delay = 6; // Switches sprite every 6 frames
	int counter = delay;
	boolean animate = true;
	
	@Override
	void draw(Graphics2D g2) {
		
		if (image == null)
			throw new RuntimeException("Image not found.");
		
		int width = image.getWidth(null) / 2, height = image.getHeight(null);
		AffineTransform at = AffineTransform.getTranslateInstance(Maths.centerX(getBounds(), width), Maths.centerY(getBounds(), height));
		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bImage.getGraphics();
		
		if (animate) // Draws half of the image onto the bufferedimage
			g.drawImage(image, 0, 0, null);
		else
			g.drawImage(image, -width, 0, null);
		
		g2.rotate(radianAngle, Maths.centerX(getBounds()), Maths.centerY(getBounds())); // Nothing animated actually gets rotated in the game, but the support for it is here :)
		g2.drawImage(bImage, at, null);
		g2.rotate(-radianAngle, Maths.centerX(getBounds()), Maths.centerY(getBounds()));
		
		if (counter == 0) {
			animate = !animate;
			counter = delay;
		}
		counter--;
	}
}