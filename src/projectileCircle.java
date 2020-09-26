import java.awt.*;
import java.awt.geom.*;

public class projectileCircle {
	
	public static void create(String proj, int amount, Ellipse2D.Double origin) {
		
		for (int i = 0; i < amount; i++) {
			projectile blt = game.bulletTypes.get(proj);
			
			game.activeBullets.add(new projectileSingle(new projectile(blt.sprite, blt.inaccuracy, blt.angle + i * (360.0 / amount), blt.turn, blt.aimed,
					blt.velocity, blt.acceleration, blt.homing, blt.lifetime, new Dimension((int) blt.width, (int) blt.height), blt.subBullet), origin));
		}
	}
}