import java.awt.*;
import java.awt.geom.*;

public class EnemyProjectile extends Ellipse2D.Double {
	
	String sprite;
	int inaccuracy; // How much deviation there can be when initially shooting the proj
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, 180 is up
	double turn; // How much the angle of the proj changes each frame
	boolean aimed; // When true, the angle will initially be directed at the player. Inaccuracy and angle calculations will come afterwards
	double velocity; // The current speed of the proj
	double acceleration; // How much the speed of the proj changes each frame
	double maxVelocity; // The maximum speed
	double minVelocity; // The minimum speed
	int homing; // If greater than 0, the proj will follow the player until this many pixels away
	int lifetime; // How many frames the proj will last, -1 for forever
	String subbullet; // The proj to be created when the current proj dies
	
	public EnemyProjectile() {
		
		sprite = "";
		inaccuracy = 0;
		angle = 0;
		turn = 0;
		aimed = false;
		velocity = 0;
		acceleration = 0;
		maxVelocity = 0;
		minVelocity = 0;
		homing = 0;
		lifetime = 0;
		width = 0;
		height = 0;
		subbullet = "";
	}
	
	public EnemyProjectile(String sprite, int inaccuracy, double angle, double turn, boolean aimed, double velocity, double acceleration,
			double maxVelocity, double minVelocity, int homing, int lifetime, Dimension size, String subBullet) { // Stored projectile types
		
		this.sprite = sprite;
		this.inaccuracy = inaccuracy;
		this.angle = angle;
		this.turn = turn;
		this.aimed = aimed;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;
		this.minVelocity = minVelocity;
		this.homing = homing;
		this.lifetime = lifetime;
		width = size.width;
		height = size.height;
		this.subbullet = subBullet;
	}
	
	public EnemyProjectile(String sprite, String inaccuracy, String angle, String turn, String aimed, String velocity, String acceleration,
			String maxVelocity, String minVelocity, String homing, String lifetime, Dimension size, String subBullet) { // Stored projectile types
		
		this.sprite = sprite;
		this.inaccuracy = Integer.parseInt(inaccuracy);
		this.angle = java.lang.Double.parseDouble(angle);
		this.turn = java.lang.Double.parseDouble(turn);
		this.aimed = Boolean.parseBoolean(aimed);
		this.velocity = java.lang.Double.parseDouble(velocity);
		this.acceleration = java.lang.Double.parseDouble(acceleration);
		this.maxVelocity = java.lang.Double.parseDouble(maxVelocity);
		this.minVelocity = java.lang.Double.parseDouble(minVelocity);
		this.homing = Integer.parseInt(homing);
		this.lifetime = Integer.parseInt(lifetime);
		width = size.width;
		height = size.height;
		this.subbullet = subBullet;
	}
	
	public EnemyProjectile(EnemyProjectile proj, Ellipse2D.Double ellipse) { // Active projectiles
		
		sprite = proj.sprite;
		inaccuracy = proj.inaccuracy;
		turn = proj.turn;
		aimed = proj.aimed;
		velocity = proj.velocity;
		acceleration = proj.acceleration;
		maxVelocity = proj.maxVelocity;
		minVelocity = proj.minVelocity;
		homing = proj.homing;
		lifetime = proj.lifetime;
		width = proj.width;
		height = proj.height;
		x = Maths.centerX(ellipse) - width / 2;
		y = Maths.centerY(ellipse) - height / 2;
		subbullet = proj.subbullet;
		
		if (aimed)
			angle = Maths.angleTo(x, y, Maths.centerX(Player.hitbox) - width / 2, Maths.centerY(Player.hitbox) - height / 2);
		
		angle += proj.angle;
		
		if ((int) (Math.random() * 2) == 0) {
			angle += (Math.random() * inaccuracy);
		} else {
			angle -= (Math.random() * inaccuracy);
		}
	}
	
	public boolean collides(Ellipse2D.Double ellipse) {
		// Checks for bullet collision
		// Returns true if collided
		
		return Maths.intersects(this, ellipse);
	}
	
	public static void create(String proj, Ellipse2D.Double origin) {
		// Creates a single bullet
		
		Game.activeBullets.add(new EnemyProjectile(Game.bulletMap.get(proj), origin));
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount) {
		// Creates a circle of bullets
		
		for (int i = 0; i < amount; i++) {
			EnemyProjectile blt = Game.bulletMap.get(proj);
			
			Game.activeBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, blt.angle + i * (360.0 / amount), blt.turn, blt.aimed,
					blt.velocity, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime,
					new Dimension((int) blt.width, (int) blt.height), blt.subbullet), origin));
		}
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount, double angle) {
		// Creates a arc of bullets
		
		for (int i = 0; i < amount; i++) {
			EnemyProjectile blt = Game.bulletMap.get(proj);
			
			Game.activeBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, blt.angle + i * (angle / amount) - angle / 2.5, blt.turn,
					blt.aimed, blt.velocity, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime,
					new Dimension((int) blt.width, (int) blt.height), blt.subbullet), origin));
		}
	}
	
	public static void drawAll(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		
		for (EnemyProjectile p : Game.activeBullets)
			g2.fill(p);
	}
	
	public void kill() {
		// Marks the bullet for deletion
		
		Game.deadBullets.add(this);
	}
	
	public void move() {
		// Updates the bullet's position
		
		if (this.homing > 0) {
			
			this.velocity += this.acceleration;
			
			if (this.velocity > this.maxVelocity)
				this.velocity = this.maxVelocity;
			if (this.velocity < this.minVelocity)
				this.velocity = this.minVelocity;
			
			this.angle = Maths.angleTo(this, Player.hitbox);
			
			this.x += Math.sin(Math.toRadians(this.angle)) * this.velocity;
			this.y += Math.cos(Math.toRadians(this.angle)) * this.velocity;
			
			if (Maths.distanceTo(this, Player.hitbox) <= this.homing)
				this.homing = 0;
		} else {
			
			this.velocity += this.acceleration;
			
			if (this.velocity > this.maxVelocity)
				this.velocity = this.maxVelocity;
			if (this.velocity < this.minVelocity)
				this.velocity = this.minVelocity;
			
			this.angle += this.turn;
			
			this.x += Math.sin(Math.toRadians(this.angle)) * this.velocity;
			this.y += Math.cos(Math.toRadians(this.angle)) * this.velocity;
		}
	}
	
	public void spawn() {
		// Creates new bullet(s) from a subbullet
		
		EnemyProjectile blt = Game.bulletMap.get(this.subbullet);
		
		if (!blt.aimed)
			Game.activeBullets.add(new EnemyProjectile(
					new EnemyProjectile(blt.sprite, blt.inaccuracy, this.angle, blt.turn, blt.aimed, blt.velocity, blt.acceleration, blt.maxVelocity,
							blt.minVelocity, blt.homing, blt.lifetime, new Dimension((int) blt.width, (int) blt.height), blt.subbullet),
					this));
		else
			Game.activeBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, Maths.angleTo(this, Player.hitbox), blt.turn, false,
					blt.velocity, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime,
					new Dimension((int) blt.width, (int) blt.height), blt.subbullet), this));
	}
	
	public void update() {
		// Updates the bullet
		
		this.move();
		this.lifetime--;
		
		if (this.lifetime == 0 || !Maths.checkInBounds(this))
			this.kill();
		
		if (Maths.distanceTo(this, Player.hitbox) < 20)
			if (this.collides(Player.hitbox)) {
				this.kill();
				Player.hit();
			}
	}
}
