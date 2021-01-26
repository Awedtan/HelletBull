import java.awt.*;
import java.awt.geom.*;

public class EnemyProjectile extends DObject {
	
	static final int BORDERBUFFER = -1000;
	static final double GRAVITYACCEL = 0.02;
	static final Point2D.Double GRAVITY = new Point2D.Double(0, -GRAVITYACCEL);
	
	int inaccuracy; // How much deviation there can be when initially shooting the proj
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, +-180 is up
	double turn; // How much the angle of the proj changes each frame
	boolean aimed; // When true, the angle will initially be directed at the player. Inaccuracy and angle calculations will come afterwards
	double velocity; // The current speed of the proj
	double acceleration; // How much the speed of the proj changes each frame
	double maxVelocity; // The maximum speed
	double minVelocity; // The minimum speed
	int homing; // If greater than 0, the proj will follow the player until this many pixels away
	boolean gravity;
	boolean border;
	int lifetime; // How many frames the proj will last, -1 for forever
	String subBullet; // The proj to be created when the current proj dies
	
	int spawnFrame;
	boolean grazed = false;
	Path2D.Double rotated = null;
	
	static String grazeClip = "graze";
	
	public EnemyProjectile(String sprite, int inaccuracy, double angle, double turn, boolean aimed, double velocity, double acceleration, double maxVelocity, double minVelocity, int homing,
			int lifetime, boolean gravity, boolean border, String subBullet) { // Stored projectile types
		
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
		this.gravity = gravity;
		this.border = border;
		this.subBullet = subBullet;
	}
	
	public EnemyProjectile(EnemyProjectile proj, Ellipse2D.Double origin) { // Active projectiles
		
		image = Game.getImage(proj.sprite);
		inaccuracy = proj.inaccuracy;
		turn = proj.turn;
		aimed = proj.aimed;
		velocity = proj.velocity;
		acceleration = proj.acceleration;
		maxVelocity = proj.maxVelocity;
		minVelocity = proj.minVelocity;
		homing = proj.homing;
		lifetime = proj.lifetime;
		gravity = proj.gravity;
		border = proj.border;
		width = image.getWidth(null);
		height = image.getHeight(null);
		x = Maths.centerX(origin.getBounds()) - width / 2;
		y = Maths.centerY(origin.getBounds()) - height / 2;
		subBullet = proj.subBullet;
		spawnFrame = Game.frameCount;
		
		if (aimed)
			angle = Maths.angleTo(x, y, Maths.centerX(Player.hitboxModel.getBounds()) - width / 2, Maths.centerY(Player.hitboxModel.getBounds()) - height / 2);
		angle += proj.angle;
		
		if ((int) (Math.random() * 2) == 0)
			angle += (Math.random() * inaccuracy);
		else
			angle -= (Math.random() * inaccuracy);
		
		radianAngle = Maths.toRadians(angle);
		sinAngle = Math.sin(radianAngle);
		cosAngle = Math.cos(radianAngle);
		
		if (angle != 0)
			rotated = (Path2D.Double) Maths.rotate(this, radianAngle);
		
	}
	
	public static void create(String proj, Ellipse2D.Double origin) {
		// Creates a single bullet
		
		Game.activeEnemyBullets.add(new EnemyProjectile(Game.bulletMap.get(proj), origin));
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount) {
		// Creates a circle of bullets
		
		create(proj, origin, amount, 360 - (360 / amount));
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount, int angle) {
		// Creates a arc of bullets
		
		for (int i = 0; i < amount; i++) {
			EnemyProjectile blt = Game.bulletMap.get(proj);
			
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, blt.angle + i * (angle / (amount - 1)) - (angle / 2.0), blt.turn, blt.aimed, blt.velocity,
					blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime, blt.gravity, blt.border, blt.subBullet), origin));
		}
	}
	
	public Shape getShape() {
		
		if (rotated != null)
			return rotated;
		return this;
	}
	
	public void graze() {
		
		Game.playClip(grazeClip);
		Player.addScore();
		grazed = true;
	}
	
	@Override
	public void kill() {
		// Marks the bullet for deletion
		
		Game.deadEnemyBullets.add(this);
	}
	
	@Override
	public void move() {
		// Updates the bullet's position
		
		if (homing > 0) { // Homing
			
			velocity += acceleration;
			
			if (velocity > maxVelocity)
				velocity = maxVelocity;
			if (velocity < minVelocity)
				velocity = minVelocity;
			
			double oldAngle = angle;
			angle = Maths.angleTo(getBounds(), Player.hitboxModel.getBounds());
			
			if (oldAngle != angle) {
				radianAngle = Maths.toRadians(angle);
				sinAngle = Math.sin(radianAngle);
				cosAngle = Math.cos(radianAngle);
			}
			
			x += -sinAngle * velocity;
			y += cosAngle * velocity;
			
			rotated = (Path2D.Double) Maths.rotate(this, radianAngle);
			
			if (Maths.distanceTo(getBounds(), Player.hitboxModel.getBounds()) < homing)
				homing = 0;
			
		} else if (gravity) { // Gravity
			
			angle = Maths.toAngle(angle);
			angle -= Maths.toAngle(angle) / 180;
			
			if (angle > 0)
				velocity += 180 / angle * 0.002;
			else if (angle < 0)
				velocity -= 180 / angle * 0.002;
			
			if (velocity > maxVelocity)
				velocity = maxVelocity;
			if (velocity < minVelocity)
				velocity = minVelocity;
			
			radianAngle = Maths.toRadians(angle);
			sinAngle = Math.sin(radianAngle);
			cosAngle = Math.cos(radianAngle);
			
			x += -sinAngle * velocity;
			y += cosAngle * velocity;
			
		} else { // Not homing
			
			velocity += acceleration;
			
			if (velocity > maxVelocity)
				velocity = maxVelocity;
			if (velocity < minVelocity)
				velocity = minVelocity;
			
			double oldAngle = angle;
			angle += turn;
			
			if (oldAngle != angle) {
				radianAngle = Maths.toRadians(angle);
				sinAngle = Math.sin(radianAngle);
				cosAngle = Math.cos(radianAngle);
			}
			
			x += -sinAngle * velocity;
			y += cosAngle * velocity;
			
			rotated = (Path2D.Double) Maths.rotate(this, radianAngle);
		}
	}
	
	public void spawn() {
		// Creates new bullet(s) from a subbullet
		
		EnemyProjectile blt = Game.bulletMap.get(subBullet);
		
		if (!blt.aimed)
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, angle + blt.angle, blt.turn, blt.aimed, blt.velocity, blt.acceleration, blt.maxVelocity,
					blt.minVelocity, blt.homing, blt.lifetime, blt.gravity, blt.border, blt.subBullet), this));
		else
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, Maths.angleTo(getBounds(), Player.hitboxModel.getBounds()), blt.turn, false, blt.velocity,
					blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime, blt.gravity, blt.border, blt.subBullet), this));
	}
	
	@Override
	public void update() {
		// Updates the bullet
		
		move();
		lifetime--;
		
		if (border && Game.frameCount - spawnFrame > 30)
			if (Maths.checkInBounds(getBounds(), -10) != -1)
				kill();
			
		if (lifetime == 0 || Maths.checkInBounds(getBounds(), BORDERBUFFER) != -1)
			kill();
		
		if (Maths.distanceTo(getBounds(), Player.hitboxModel.getBounds()) < Math.max(width, height) + Player.grazeRadius)
			if (collides(Player.hitboxModel)) {
				kill();
				Player.hit();
			} else if (!grazed)
				graze();
	}
}