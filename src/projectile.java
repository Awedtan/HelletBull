import java.awt.*;
import java.awt.geom.*;

public class projectile extends Ellipse2D.Double {
	
	String sprite;
	int inaccuracy; // How much deviation there is when initially shooting the proj
	double angle; // The current angle of the proj, 0 is down, 90 is right, -90 is left, 180 is up
	double turn; // How much the angle of the proj changes each frame
	boolean aimed; // When true, the angle of the proj will initially be towards the player
	double velocity; // The current speed of the proj
	double acceleration; // How much the speed of the proj changes each frame
	double maxVelocity; // The max velocity
	double minVelocity; // The min velocity
	int homingInt; // How close to the player before the proj stops homing, 0 for no homing
	int lifetime; // How many frames the proj will last, -1 for forever
	String subBullet;
	
	public projectile() {
		
		sprite = "";
		inaccuracy = 0;
		angle = 0;
		turn = 0;
		aimed = false;
		velocity = 0;
		acceleration = 0;
		maxVelocity = 0;
		minVelocity = 0;
		homingInt = 0;
		lifetime = 0;
		width = 0;
		height = 0;
		subBullet = "";
	}
	
	public projectile(String sprite, int inaccuracy, double angle, double turn, boolean aimed, double velocity, double acceleration, double maxVelocity,
			double minVelocity, int homingInt, int lifetime, Dimension size, String subBullet) { // Stored projectiles
		
		this.sprite = sprite;
		this.inaccuracy = inaccuracy;
		this.angle = angle;
		this.turn = turn;
		this.aimed = aimed;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;
		this.minVelocity = minVelocity;
		this.homingInt = homingInt;
		this.lifetime = lifetime;
		width = size.width;
		height = size.height;
		this.subBullet = subBullet;
	}
	
	public projectile(projectile proj, Ellipse2D.Double ellipse) { // Active projectiles
		
		sprite = proj.sprite;
		inaccuracy = proj.inaccuracy;
		turn = proj.turn;
		aimed = proj.aimed;
		velocity = proj.velocity;
		acceleration = proj.acceleration;
		maxVelocity = proj.maxVelocity;
		minVelocity = proj.minVelocity;
		homingInt = proj.homingInt;
		lifetime = proj.lifetime;
		width = proj.width;
		height = proj.height;
		x = game.centerX(ellipse) - width / 2;
		y = game.centerY(ellipse) - height / 2;
		subBullet = proj.subBullet;
		
		if (aimed)
			angle = game.angleTo(x, y, game.centerX(player.hitbox) - width / 2, game.centerY(player.hitbox) - height / 2);
		
		angle += proj.angle;
		
		if ((int) (Math.random() * 2) == 0) {
			angle += (Math.random() * inaccuracy);
		} else {
			angle -= (Math.random() * inaccuracy);
		}
	}
	
	public static boolean checkInBounds(int bullet) {
		
		int buffer = 10;
		
		projectile blt = game.activeBullets.get(bullet);
		
		double x1 = blt.x;
		double y1 = blt.y;
		double x2 = blt.x + blt.width;
		double y2 = blt.y + blt.height;
		
		if (x1 > game.SCREENWIDTH - buffer || x2 < buffer || y1 > game.SCREENHEIGHT - buffer || y2 < buffer)
			return false;
		return true;
	}
	
	public static void create(String proj, Ellipse2D.Double origin) { // Single bullet
		
		game.activeBullets.add(new projectile(game.bulletTypes.get(proj), origin));
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount) { // Circle bullet
		
		for (int i = 0; i < amount; i++) {
			projectile blt = game.bulletTypes.get(proj);
			
			game.activeBullets.add(new projectile(
					new projectile(blt.sprite, blt.inaccuracy, blt.angle + i * (360.0 / amount), blt.turn, blt.aimed, blt.velocity, blt.acceleration,
							blt.maxVelocity, blt.minVelocity, blt.homingInt, blt.lifetime, new Dimension((int) blt.width, (int) blt.height), blt.subBullet),
					origin));
		}
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount, double angle) { // Multi bullet
		
		for (int i = 0; i < amount; i++) {
			projectile blt = game.bulletTypes.get(proj);
			
			game.activeBullets.add(new projectile(new projectile(blt.sprite, blt.inaccuracy, blt.angle + i * (angle / amount) - angle / 2.5, blt.turn,
					blt.aimed, blt.velocity, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homingInt, blt.lifetime,
					new Dimension((int) blt.width, (int) blt.height), blt.subBullet), origin));
		}
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		
		for (int i = 0; i < game.activeBullets.size(); i++)
			g2.fill(game.activeBullets.get(i));
	}
	
	public static void kill(int bullet) {
		
		projectile origin = game.activeBullets.get(bullet);
		
		if (game.bulletTypes.get(origin.subBullet) != null)
			spawn(bullet);
		
		game.activeBullets.remove(bullet);
	}
	
	public static void move(int bullet) {
		
		projectile blt = game.activeBullets.get(bullet);
		
		if (blt.homingInt > 0) {
			
			blt.velocity += blt.acceleration;
			
			if (blt.velocity > blt.maxVelocity)
				blt.velocity = blt.maxVelocity;
			if (blt.velocity < blt.minVelocity)
				blt.velocity = blt.minVelocity;
			
			blt.angle = game.angleTo(blt, player.hitbox);
			
			blt.x += Math.sin(Math.toRadians(blt.angle)) * blt.velocity;
			blt.y += Math.cos(Math.toRadians(blt.angle)) * blt.velocity;
			
			if (game.distanceTo(blt, player.hitbox) <= blt.homingInt)
				blt.homingInt = 0;
		} else {
			
			blt.velocity += blt.acceleration;
			
			if (blt.velocity > blt.maxVelocity)
				blt.velocity = blt.maxVelocity;
			if (blt.velocity < blt.minVelocity)
				blt.velocity = blt.minVelocity;
			
			blt.angle += blt.turn;
			
			blt.x += Math.sin(Math.toRadians(blt.angle)) * blt.velocity;
			blt.y += Math.cos(Math.toRadians(blt.angle)) * blt.velocity;
		}
	}
	
	public static void spawn(int bullet) {
		
		projectile origin = game.activeBullets.get(bullet);
		projectile blt = game.bulletTypes.get(origin.subBullet);
		
		if (!blt.aimed)
			game.activeBullets.add(new projectile(new projectile(blt.sprite, blt.inaccuracy, origin.angle, blt.turn, blt.aimed, blt.velocity, blt.acceleration,
					blt.maxVelocity, blt.minVelocity, blt.homingInt, blt.lifetime, new Dimension((int) blt.width, (int) blt.height), blt.subBullet), origin));
		else
			game.activeBullets.add(new projectile(
					new projectile(blt.sprite, blt.inaccuracy, game.angleTo(origin, player.hitbox), blt.turn, false, blt.velocity, blt.acceleration,
							blt.maxVelocity, blt.minVelocity, blt.homingInt, blt.lifetime, new Dimension((int) blt.width, (int) blt.height), blt.subBullet),
					origin));
	}
	
	public static void update(int bullet) {
		
		move(bullet);
		projectile blt = game.activeBullets.get(bullet);
		blt.lifetime--;
	}
}