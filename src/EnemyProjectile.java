import java.awt.*;
import java.awt.geom.*;

public class EnemyProjectile extends Ellipse2D.Double {
	
	static final int BORDERBUFFER = -250;
	
	String spriteName;
	Image sprite;
	int inaccuracy; // How much deviation there can be when initially shooting the proj
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, +-180 is up
	double turn; // How much the angle of the proj changes each frame
	boolean aimed; // When true, the angle will initially be directed at the player. Inaccuracy and angle calculations will come afterwards
	double velocity; // The current speed of the proj
	double acceleration; // How much the speed of the proj changes each frame
	double maxVelocity; // The maximum speed
	double minVelocity; // The minimum speed
	int homing; // If greater than 0, the proj will follow the player until this many pixels away
	int lifetime; // How many frames the proj will last, -1 for forever
	String subBullet; // The proj to be created when the current proj dies
	
	boolean grazed = false;
	Path2D.Double rotated = null;
	double radianAngle;
	double sinAngle;
	double cosAngle;
	
	static String grazeClip = "graze";
	
	public EnemyProjectile() {
		
		sprite = null;
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
		subBullet = "";
	}
	
	public EnemyProjectile(String sprite, int inaccuracy, double angle, double turn, boolean aimed, double velocity, double acceleration, double maxVelocity, double minVelocity, int homing,
			int lifetime, String subBullet) { // Stored projectile types
		
		this.spriteName = sprite;
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
		this.subBullet = subBullet;
	}
	
	public EnemyProjectile(EnemyProjectile proj, Ellipse2D.Double origin) { // Active projectiles
		
		sprite = Game.getImage(proj.spriteName);
		inaccuracy = proj.inaccuracy;
		turn = proj.turn;
		aimed = proj.aimed;
		velocity = proj.velocity;
		acceleration = proj.acceleration;
		maxVelocity = proj.maxVelocity;
		minVelocity = proj.minVelocity;
		homing = proj.homing;
		lifetime = proj.lifetime;
		width = sprite.getWidth(null);
		height = sprite.getHeight(null);
		x = Maths.centerX(origin.getBounds()) - width / 2;
		y = Maths.centerY(origin.getBounds()) - height / 2;
		subBullet = proj.subBullet;
		
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
	
	public boolean collides(Shape shape) {
		// Checks for bullet collision
		// Returns true if collided
		
		return Maths.intersects(this, shape, radianAngle);
	}
	
	public static void create(String proj, Ellipse2D.Double origin) {
		// Creates a single bullet
		
		Game.activeEnemyBullets.add(new EnemyProjectile(Game.bulletMap.get(proj), origin));
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount) {
		// Creates a circle of bullets
		
		for (int i = 0; i < amount; i++) {
			EnemyProjectile blt = Game.bulletMap.get(proj);
			
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.spriteName, blt.inaccuracy, blt.angle + i * (360.0 / amount), blt.turn, blt.aimed, blt.velocity, blt.acceleration,
					blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime, blt.subBullet), origin));
		}
	}
	
	public static void create(String proj, Ellipse2D.Double origin, int amount, int angle) {
		// Creates a arc of bullets
		
		for (int i = 0; i < amount; i++) {
			EnemyProjectile blt = Game.bulletMap.get(proj);
			
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.spriteName, blt.inaccuracy, blt.angle + i * (angle / (amount - 1)) - (angle / 2.0), blt.turn, blt.aimed,
					blt.velocity, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime, blt.subBullet), origin));
		}
	}
	
	public static void drawAll(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		
		for (EnemyProjectile p : Game.activeEnemyBullets) {
			
			AffineTransform at = AffineTransform.getTranslateInstance(Maths.centerX(p.getBounds(), p.sprite.getWidth(null)), Maths.centerY(p.getBounds(), p.sprite.getHeight(null)));
			
			g2.rotate(p.radianAngle + Math.PI, Maths.centerX(p.getBounds()), Maths.centerY(p.getBounds()));
			g2.drawImage(p.sprite, at, null);
			g2.rotate(-(p.radianAngle + Math.PI), Maths.centerX(p.getBounds()), Maths.centerY(p.getBounds()));
			
			// g2.setColor(Color.BLACK);
			// g2.fill(p);
			// g2.setColor(Color.BLUE);
			// g2.fill(p.getShape());
			// g2.setColor(Color.RED);
			// g2.draw(Maths.rotate(Maths.ellipseHitbox(p), p.radianAngle));
		}
		
		g2.setColor(Color.BLACK);
	}
	
	public Shape getShape() {
		
		if (rotated != null)
			return rotated;
		return this;
	}
	
	public void graze() {
		
		Game.playClip(grazeClip);
		grazed = true;
	}
	
	public void kill() {
		// Marks the bullet for deletion
		
		Game.deadEnemyBullets.add(this);
	}
	
	public void move() {
		// Updates the bullet's position
		
		if (homing > 0) { // Homing
			
			velocity += acceleration;
			
			if (velocity > maxVelocity)
				velocity = maxVelocity;
			if (velocity < minVelocity)
				velocity = minVelocity;
			
			double oldAngle = angle;
			angle = Maths.angleTo(this.getBounds(), Player.hitboxModel.getBounds());
			
			if (oldAngle != angle) {
				radianAngle = Maths.toRadians(angle);
				sinAngle = Math.sin(radianAngle);
				cosAngle = Math.cos(radianAngle);
			}
			
			x += -sinAngle * velocity;
			y += cosAngle * velocity;
			
			rotated = (Path2D.Double) Maths.rotate(this, radianAngle);
			
			if (Maths.distanceTo(this.getBounds(), Player.hitboxModel.getBounds()) < homing)
				homing = 0;
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
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.spriteName, blt.inaccuracy, angle, blt.turn, blt.aimed, blt.velocity, blt.acceleration, blt.maxVelocity,
					blt.minVelocity, blt.homing, blt.lifetime, blt.subBullet), this));
		else
			Game.activeEnemyBullets.add(new EnemyProjectile(new EnemyProjectile(blt.spriteName, blt.inaccuracy, Maths.angleTo(this.getBounds(), Player.hitboxModel.getBounds()), blt.turn, false,
					blt.velocity, blt.acceleration, blt.maxVelocity, blt.minVelocity, blt.homing, blt.lifetime, blt.subBullet), this));
	}
	
	public void update() {
		// Updates the bullet
		
		move();
		lifetime--;
		
		if (lifetime == 0 || Maths.checkInBounds(this.getBounds(), BORDERBUFFER) != -1)
			kill();
		
		if (Maths.distanceTo(this.getBounds(), Player.hitboxModel.getBounds()) < Math.max(this.width, this.height) + Player.grazeRadius)
			if (collides(Player.hitboxModel)) {
				kill();
				Player.hit();
			} else if (!grazed)
				graze();
	}
}