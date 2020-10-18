import java.awt.*;
import java.awt.geom.*;

public class Projectile extends Ellipse2D.Double {
	
	String sprite;
	int inaccuracy; // How much deviation there can be when initially shooting the proj
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, 180 is up
	double turn; // How much the angle of the proj changes each frame
	boolean aimed; // When true, the angle will initially be directed at the player. Inaccuracy and angle calculations will come afterwards
	double velocity; // The current speed of the proj
	double acceleration; // How much the speed of the proj changes each frame
	double maxVelocity; // The maximum speed
	double minVelocity; // The minimum speed
	int homingInt; // If greater than 0, the proj will follow the player until this many pixels away
	int lifetimeInt; // How many frames the proj will last, -1 for forever
	String subbullet; // The proj to be created when the current proj dies
	
	public Projectile() {
		
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
		lifetimeInt = 0;
		width = 0;
		height = 0;
		subbullet = "";
	}
	
	public Projectile(String sprite, int inaccuracy, double angle, double turn, boolean aimed, double velocity, double acceleration, double maxVelocity,
			double minVelocity, int homingInt, int lifetime, Dimension size, String subBullet) { // Stored projectile types
		
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
		this.lifetimeInt = lifetime;
		width = size.width;
		height = size.height;
		this.subbullet = subBullet;
	}
	
	public Projectile(Projectile proj, Ellipse2D.Double ellipse) { // Active projectiles
		
		sprite = proj.sprite;
		inaccuracy = proj.inaccuracy;
		turn = proj.turn;
		aimed = proj.aimed;
		velocity = proj.velocity;
		acceleration = proj.acceleration;
		maxVelocity = proj.maxVelocity;
		minVelocity = proj.minVelocity;
		homingInt = proj.homingInt;
		lifetimeInt = proj.lifetimeInt;
		width = proj.width;
		height = proj.height;
		x = Game.centerX(ellipse) - width / 2;
		y = Game.centerY(ellipse) - height / 2;
		subbullet = proj.subbullet;
		
		if (aimed)
			angle = Game.angleTo(x, y, Game.centerX(Player.hitbox) - width / 2, Game.centerY(Player.hitbox) - height / 2);
		
		angle += proj.angle;
		
		if ((int) (Math.random() * 2) == 0) {
			angle += (Math.random() * inaccuracy);
		} else {
			angle -= (Math.random() * inaccuracy);
		}
	}
	
	public static boolean checkInBounds(int bullet) {
		// Checks if the bullet is within an arbitrary rectangle based on screen size
		// Returns false if the bullet exceeds these bounds
		
		int buffer = 10;
		
		Projectile blt = Game.activeBullets.get(bullet);
		
		double x1 = blt.x;
		double y1 = blt.y;
		double x2 = blt.x + blt.width;
		double y2 = blt.y + blt.height;
		
		if (x1 > Game.SCREENWIDTH - buffer || x2 < buffer || y1 > Game.SCREENHEIGHT - buffer || y2 < buffer)
			return false;
		return true;
	}
	
	public static void create(String proj, Ellipse2D.Double origin) {
		// Creates a single bullet
		
		Game.activeBullets.add(new Projectile(Game.bulletMap.get(proj), origin));
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount) {
		// Creates a circle of bullets
		
		for (int i = 0; i < amount; i++) {
			Projectile blt = Game.bulletMap.get(proj);
			
			Game.activeBullets.add(new Projectile(
					new Projectile(blt.sprite, blt.inaccuracy, blt.angle + i * (360.0 / amount), blt.turn, blt.aimed, blt.velocity, blt.acceleration,
							blt.maxVelocity, blt.minVelocity, blt.homingInt, blt.lifetimeInt, new Dimension((int) blt.width, (int) blt.height), blt.subbullet),
					origin));
		}
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount, double angle) {
		// Creates a arc of bullets
		
		for (int i = 0; i < amount; i++) {
			Projectile blt = Game.bulletMap.get(proj);
			
			Game.activeBullets.add(new Projectile(new Projectile(blt.sprite, blt.inaccuracy, blt.angle + i * (angle / amount) - angle / 2.5, blt.turn,
					blt.aimed, blt.velocity, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homingInt, blt.lifetimeInt,
					new Dimension((int) blt.width, (int) blt.height), blt.subbullet), origin));
		}
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		
		for (int i = 0; i < Game.activeBullets.size(); i++)
			g2.fill(Game.activeBullets.get(i));
	}
	
	public static void kill(int bullet) {
		// Deletes the bullet
		// If the bullet has a subbullet, creates the subbullet
		
		Projectile origin = Game.activeBullets.get(bullet);
		
		if (Game.bulletMap.get(origin.subbullet) != null)
			spawn(bullet);
		
		Game.activeBullets.remove(bullet);
	}
	
	public static void move(int bullet) {
		// Updates a bullet's position
		
		Projectile blt = Game.activeBullets.get(bullet);
		
		if (blt.homingInt > 0) {
			
			blt.velocity += blt.acceleration;
			
			if (blt.velocity > blt.maxVelocity)
				blt.velocity = blt.maxVelocity;
			if (blt.velocity < blt.minVelocity)
				blt.velocity = blt.minVelocity;
			
			blt.angle = Game.angleTo(blt, Player.hitbox);
			
			blt.x += Math.sin(Math.toRadians(blt.angle)) * blt.velocity;
			blt.y += Math.cos(Math.toRadians(blt.angle)) * blt.velocity;
			
			if (Game.distanceTo(blt, Player.hitbox) <= blt.homingInt)
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
		// Creates new bullet(s) from a subbullet
		
		Projectile origin = Game.activeBullets.get(bullet);
		Projectile blt = Game.bulletMap.get(origin.subbullet);
		
		if (!blt.aimed)
			Game.activeBullets.add(new Projectile(new Projectile(blt.sprite, blt.inaccuracy, origin.angle, blt.turn, blt.aimed, blt.velocity, blt.acceleration,
					blt.maxVelocity, blt.minVelocity, blt.homingInt, blt.lifetimeInt, new Dimension((int) blt.width, (int) blt.height), blt.subbullet),
					origin));
		else
			Game.activeBullets.add(new Projectile(
					new Projectile(blt.sprite, blt.inaccuracy, Game.angleTo(origin, Player.hitbox), blt.turn, false, blt.velocity, blt.acceleration,
							blt.maxVelocity, blt.minVelocity, blt.homingInt, blt.lifetimeInt, new Dimension((int) blt.width, (int) blt.height), blt.subbullet),
					origin));
	}
	
	public static void update(int bullet) {
		// Updates a bullet
		
		move(bullet);
		Game.activeBullets.get(bullet).lifetimeInt--;
	}
}