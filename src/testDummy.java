import java.awt.*;
import java.awt.geom.*;

public class TestDummy {
	
	static int size = 20;
	
	static Ellipse2D.Double dummy = new Ellipse2D.Double(Game.SCREENWIDTH / 2, Game.SCREENHEIGHT / 5, size, size);
	
	public static void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		if (Game.frameCount == 0)
			return;
			
		// if (game.frameCount % 200 == 0) {
		// projectile.create("LCurveBlt", game.activeEnemies.get(0), 16);
		// }
		
		// if (game.frameCount % 150 == 0) {
		// projectile.create("BigBlt", game.activeEnemies.get(0), 5, 15.0);
		// }
		
		// if (game.frameCount % 5 == 0) {
		// projectile.create("StraightBlt", game.activeEnemies.get(0));
		// }
		
		g2.setColor(Color.RED);
		// g2.fill(dummy);
	}
}