import java.awt.*;
import java.awt.geom.*;

public class testDummy {
	
	static int size = 20;
	
	static Ellipse2D.Double dummy = new Ellipse2D.Double(game.SCREENWIDTH / 2, game.SCREENHEIGHT / 5, size, size);
	
	public static void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		if (game.frameCount % 200 == 0) {
			projectileCircle.create("LCurveBlt", 16, dummy);
		}
		
		if (game.frameCount % 150 == 0) {
			projectileMulti.create(new String[] { "spread1", "spread2", "spread3" }, dummy);
		}
		
		if (game.frameCount % 5 == 0) {
			projectileSingle.create("StraightBlt", dummy);
		}
		
		g2.setColor(Color.red);
		g2.fill(dummy);
	}
}