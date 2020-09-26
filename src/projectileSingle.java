import java.awt.geom.*;

public class projectileSingle extends projectile {
	
	public projectileSingle(projectile proj, Ellipse2D.Double ellipse) {
		
		sprite = proj.sprite;
		inaccuracy = proj.inaccuracy;
		// angle
		turn = proj.turn;
		aimed = proj.aimed;
		velocity = proj.velocity;
		acceleration = proj.acceleration;
		homing = proj.homing;
		lifetime = proj.lifetime;
		width = proj.width;
		height = proj.height;
		x = game.getX(ellipse) - width / 2;
		y = game.getY(ellipse) - height / 2;
		subBullet = proj.subBullet;
		
		if (aimed) 
			angle = game.getAngle(x, y, game.getX(player.hitbox) - width / 2, game.getY(player.hitbox) - height / 2);
		
		angle += proj.angle;
		
		if ((int) (Math.random() * 2) == 0) {
			angle += (Math.random() * inaccuracy);
		} else {
			angle -= (Math.random() * inaccuracy);
		}
	}
	
	public static void create(String bulletType, Ellipse2D.Double origin) {
		
		game.activeBullets.add(new projectileSingle(game.bulletTypes.get(bulletType), origin));
	}
}