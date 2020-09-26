import java.awt.*;
import java.awt.geom.*;

public class projectile extends Ellipse2D.Double {
	
	String sprite;
	int inaccuracy; // How much deviation there is when initially shooting the proj
	double angle; // The current angle of the proj, 0 is down, 90 is right, -90 is left, 180 is up
	double turn; // How much the angle of the proj changes each frame
	boolean aimed; // When true, the angle of the proj will initially be torwards the player
	double velocity; // The current speed of the proj
	double acceleration; // How much the speed of the proj changes each frame
	int homing; // How close to the player before the proj stops homing, 0 for no homing
	int lifetime; // How many frames the proj will last, 0 for forever
	String subBullet;
	
	public projectile() {
		
		sprite = "";
		inaccuracy = 0;
		angle = 0;
		turn = 0;
		aimed = false;
		velocity = 0;
		acceleration = 0;
		homing = 0;
		lifetime = 0;
		width = 0;
		height = 0;
		subBullet = "";
	}
	
	public projectile(String spriteStr, int inaccInt, double angleDble, double turnDble, boolean aimedBool, double veloDble, double accelDble, int homingInt,
			int lifeInt, Dimension size, String secondary) {
		
		sprite = spriteStr;
		inaccuracy = inaccInt;
		angle = angleDble;
		turn = turnDble;
		aimed = aimedBool;
		velocity = veloDble;
		acceleration = accelDble;
		homing = homingInt;
		lifetime = lifeInt;
		width = size.width;
		height = size.height;
		subBullet = secondary;
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.yellow);
		
		for (int i = 0; i < game.activeBullets.size(); i++)
			g2.fill(game.activeBullets.get(i));
	}
	
	public static void update(int bullet) {
		
		projectileSingle blt = game.activeBullets.get(bullet);
		blt.velocity += blt.acceleration;
		blt.angle += blt.turn;
		blt.x += Math.sin(Math.toRadians(blt.angle)) * blt.velocity;
		blt.y += Math.cos(Math.toRadians(blt.angle)) * blt.velocity;
		blt.lifetime--;
	}
	
	public static void kill(int bullet) {
		
		projectile origin = game.activeBullets.get(bullet);
		
		if (game.bulletTypes.get(origin.subBullet) != null) {
			projectile blt = game.bulletTypes.get(origin.subBullet);
			
			if (!blt.aimed)
				game.activeBullets.add(new projectileSingle(new projectile(blt.sprite, blt.inaccuracy, origin.angle, blt.turn, blt.aimed, blt.velocity,
						blt.acceleration, blt.homing, blt.lifetime, new Dimension((int) blt.width, (int) blt.height), blt.subBullet), origin));
			else
				game.activeBullets.add(new projectileSingle(new projectile(blt.sprite, blt.inaccuracy, game.getAngle(origin, player.hitbox), blt.turn, false,
						blt.velocity, blt.acceleration, blt.homing, blt.lifetime, new Dimension((int) blt.width, (int) blt.height), blt.subBullet), origin));
		}
		
		game.activeBullets.remove(bullet);
	}
	
	public static boolean checkInBounds(int bullet) {
		
		int buffer = 10;
		
		projectileSingle blt = game.activeBullets.get(bullet);
		
		double x1 = blt.x;
		double y1 = blt.y;
		double x2 = blt.x + blt.width;
		double y2 = blt.y + blt.height;
		
		if (x1 > game.SCREENWIDTH - buffer || x2 < buffer || y1 > game.SCREENHEIGHT - buffer || y2 < buffer)
			return false;
		
		return true;
	}
}