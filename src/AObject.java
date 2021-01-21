import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public abstract class AObject extends DObject {
	
	final int delay = 6;
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
		
		if (animate)
			g.drawImage(image, 0, 0, null);
		else
			g.drawImage(image, -width, 0, null);
		
		g2.rotate(radianAngle, Maths.centerX(getBounds()), Maths.centerY(getBounds()));
		g2.drawImage(bImage, at, null);
		g2.rotate(-radianAngle, Maths.centerX(getBounds()), Maths.centerY(getBounds()));
		
		if (counter == 0) {
			animate = !animate;
			counter = delay;
		}
		counter--;
	}
}