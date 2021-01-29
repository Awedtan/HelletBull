import java.awt.*;
import java.awt.geom.*;

public class EnemyProjectile extends DObject {
	// Bullets that enemies shoot at you
	// This class covers both stored bullets and active bullets because the difference is just 1 variable and whatever i just did it this way
	
	static final int BORDERBUFFER = -1000; // Bullets are killed this many units outside the border unless otherwise specified
	static final double GRAVITY = 0.01; // Gravity vector
	
	int inaccuracy; // How much deviation there can be when initially shooting the proj
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, +-180 is up
	double turn; // How much the angle of the proj changes each frame
	boolean aimed; // When true, the angle will initially be directed at the player. Inaccuracy and angle calculations will come afterwards
	double velocity; // The current speed of the proj
	double variance; // Randomness in the starting velocity
	double acceleration; // How much the speed of the proj changes each frame
	double maxVelocity; // The maximum speed
	double minVelocity; // The minimum speed
	int homing; // If greater than 0, the proj will follow the player until this many pixels away
	boolean gravity; // If this bullet is affected by gravity
	boolean border; // If this bullet will die right at the border
	int lifetime; // How many frames the proj will last, -1 for forever
	String subBullet; // The proj to be created when the current proj dies
	
	long spawnFrame;
	boolean grazed = false; // Bullets can only be grazed once
	Path2D.Double rotated = null; // This will be not null if the bullet is being rotated
	
	static String grazeClip = "graze";
	
	public EnemyProjectile(String sprite, int inaccuracy, double angle, double turn, boolean aimed, double velocity, double variance, double acceleration, double maxVelocity, double minVelocity,
			int homing, int lifetime, boolean gravity, boolean border, String subBullet) { 
		// Stored projectile types
		// These values are specified in data/bullets/filename.txt
		
		this.sprite = sprite;
		this.inaccuracy = inaccuracy;
		this.angle = angle;
		this.turn = turn;
		this.aimed = aimed;
		this.velocity = velocity;
		this.variance = variance;
		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;
		this.minVelocity = minVelocity;
		this.homing = homing;
		this.lifetime = lifetime;
		this.gravity = gravity;
		this.border = border;
		this.subBullet = subBullet;
	}
	
	public EnemyProjectile(EnemyProjectile proj, Ellipse2D.Double origin) { 
		// Active projectiles
		// Basically the same but with a set location
		
		image = Game.getImage(proj.sprite); // Retrieves the sprite
		inaccuracy = proj.inaccuracy;
		turn = proj.turn;
		aimed = proj.aimed;
		velocity = proj.velocity;
		variance = proj.variance;
		acceleration = proj.acceleration;
		maxVelocity = proj.maxVelocity;
		minVelocity = proj.minVelocity;
		homing = proj.homing;
		lifetime = proj.lifetime;
		gravity = proj.gravity;
		border = proj.border;
		subBullet = proj.subBullet;
		
		width = image.getWidth(null); // Size is tied to sprite size
		height = image.getHeight(null);
		x = Maths.centerX(origin.getBounds()) - width / 2;
		y = Maths.centerY(origin.getBounds()) - height / 2;
		spawnFrame = Game.globalFrameCount;
		
		if (aimed) // Aims the bullet towards the player
			angle = Maths.angleTo(x, y, Maths.centerX(Player.hitboxModel.getBounds()) - width / 2, Maths.centerY(Player.hitboxModel.getBounds()) - height / 2);
		angle += proj.angle;
		
		if ((int) (Math.random() * 2) == 0) // Randomizes the starting angle
			angle += (Math.random() * inaccuracy);
		else
			angle -= (Math.random() * inaccuracy);
		
		velocity -= variance / 2.0; // Randomizes the starting velocity
		velocity += variance * Math.random();
		
		radianAngle = Maths.toRadians(angle);
		sinAngle = Math.sin(radianAngle);
		cosAngle = Math.cos(radianAngle);
		
		if (angle != 0)
			rotated = (Path2D.Double) Maths.rotate(this, radianAngle); // Creates a rotated version of this shape
		
	}
	
	public static void create(String proj, Ellipse2D.Double origin) {
		// Creates a single bullet
		
		Game.activeEnemyBullets.add(new EnemyProjectile(Game.bulletMap.get(proj), origin));
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount) {
		// Creates a circle of bullets
		
		create(proj, origin, amount, 360 - (360.0 / amount));
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount, double angle) {
		// Creates a arc of bullets
		
		for (int i = 0; i < amount; i++) {
			EnemyProjectile blt = Game.bulletMap.get(proj);
			
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, blt.angle + i * (angle / (amount - 1)) - (angle / 2.0), blt.turn, blt.aimed, blt.velocity,
					blt.variance, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime, blt.gravity, blt.border, blt.subBullet), origin));
		}
	}
	
	public Shape getShape() {
		// Returns the shape and orientation of the bullet
		
		if (rotated != null)
			return rotated;
		return this;
	}
	
	public void graze() {
		// Graze is when the player gets close but doesn't touch the bullet
		// Gives some points
		
		Game.playClip(grazeClip);
		Player.addScore(2);
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
			
			if (oldAngle != angle) { // Only do all this if the angle has changed
				radianAngle = Maths.toRadians(angle);
				sinAngle = Math.sin(radianAngle);
				cosAngle = Math.cos(radianAngle);
			}
			
			x += -sinAngle * velocity; // Moves the bullet
			y += cosAngle * velocity;
			
			rotated = (Path2D.Double) Maths.rotate(this, radianAngle); // Updates the rotated version
			
			if (Maths.distanceTo(getBounds(), Player.hitboxModel.getBounds()) < homing) // Checks if the player is close enough to make this bullet not be homing anymore
				homing = 0;
			
		} else if (gravity) { // Gravity
			
			Point2D.Double p1 = new Point2D.Double(this.x - Math.sin(Maths.toRadians(angle)) * velocity, this.y + Math.cos(Maths.toRadians(angle)) * velocity); // Finds the velocity vector of the bullet
			p1 = new Point2D.Double(p1.x, p1.y + GRAVITY); // Adds gravity to the vector
			
			angle = Maths.angleTo(this.x, this.y, p1.x, p1.y); // Calculates the new angle
			velocity = Maths.distanceTo(this.x, this.y, p1.x, p1.y); // Calculates the new velocity
			
			if (velocity > maxVelocity)
				velocity = maxVelocity;
			if (velocity < minVelocity)
				velocity = minVelocity;
			
			radianAngle = Maths.toRadians(angle);
			sinAngle = Math.sin(radianAngle);
			cosAngle = Math.cos(radianAngle);
			
			x += -sinAngle * velocity; // Moves the bullet
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
		// Creates a new bullet at this bullet's location
		
		EnemyProjectile blt = Game.bulletMap.get(subBullet); // Retrieves the bullet
		
		if (!blt.aimed)
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, angle + blt.angle, blt.turn, blt.aimed, blt.velocity, blt.variance, blt.acceleration,
					blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime, blt.gravity, blt.border, blt.subBullet), this));
		else
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.sprite, blt.inaccuracy, Maths.angleTo(getBounds(), Player.hitboxModel.getBounds()), blt.turn, false, blt.velocity,
					variance, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime, blt.gravity, blt.border, blt.subBullet), this));
	}
	
	@Override
	public void update() {
		// Updates the bullet
		
		move();
		lifetime--;
		
		if (border && Game.globalFrameCount - spawnFrame > 30) // Border check
			if (Maths.checkInBounds(getBounds(), -10) != -1)
				kill();
			
		if (lifetime == 0 || Maths.checkInBounds(getBounds(), BORDERBUFFER) != -1) // Border + buffer check
			kill();
		
		if (Maths.distanceTo(getBounds(), Player.hitboxModel.getBounds()) < Math.max(width, height) + Player.grazeRadius) // Only checks for player colliison if within graze range
			if (collides(Player.hitboxModel)) {
				kill();
				Player.hit();
			} else if (!grazed)
				graze();
	}
}