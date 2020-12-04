import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class PlayerProjectile extends Ellipse2D.Double {
	
	static HashMap<String, PlayerProjectile[]> bulletMap = new HashMap<>(); // Projectile types
	static ArrayList<PlayerProjectile> activeBullets = new ArrayList<>(); // Projectiles currently alive
	static ArrayList<PlayerProjectile> deadBullets = new ArrayList<>(); // Projectiles to be killed
	
	String sprite;
	int damage;
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, 180 is up
	double velocity; // The current speed of the proj
	int homing; // If greater than 0, the proj will follow the player until this many pixels away
	
	public PlayerProjectile(String sprite, int damage, double angle, double velocity, int homing, Dimension size) {
		
		this.sprite = sprite;
		this.damage = damage;
		this.angle = angle;
		this.velocity = velocity;
		this.homing = homing;
		width = size.width;
		height = size.height;
	}
	
	public PlayerProjectile(PlayerProjectile pp, Point origin) {
		
		this.sprite = pp.sprite;
		this.damage = pp.damage;
		this.angle = pp.angle;
		this.velocity = pp.velocity;
		this.homing = pp.homing;
		width = pp.width;
		height = pp.height;
		x = origin.x;
		y = origin.y;
	}
	
	public boolean checkCollision(Ellipse2D.Double ellipse) {
		// Checks for bullet collision
		// Returns true if collided
		
		return Maths.intersects(this, ellipse);
	}
	
	public static void create(String name, int power) {
		
		for (int i = 0; i <= power; i++)
			if (i < bulletMap.get(name).length)
				activeBullets.add(new PlayerProjectile(bulletMap.get(name)[i], new Point((int) Maths.centerX(Player.model), (int) Maths.centerY(Player.model))));
	}
	
	public static void drawAll(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		
		for (PlayerProjectile pp : activeBullets)
			g2.fill(pp);
	}
	
	public static void initialize() {
		
		bulletMap.put("test", new PlayerProjectile[] { new PlayerProjectile("", 1, 180, 8, 0, new Dimension(10, 10)), new PlayerProjectile("", 1, 185, 8, 0, new Dimension(10, 10)),
				new PlayerProjectile("", 1, 175, 8, 0, new Dimension(10, 10)), new PlayerProjectile("", 1, 170, 8, 0, new Dimension(10, 10)),new PlayerProjectile("", 1, 190, 8, 0, new Dimension(10, 10))});
	}
	
	public void kill() {
		// Marks the bullet for deletion
		
		deadBullets.add(this);
	}
	
	public void move() {
		
		this.x += Math.sin(Math.toRadians(this.angle)) * this.velocity;
		this.y += Math.cos(Math.toRadians(this.angle)) * this.velocity;
	}
	
	public static void purge() {
		// Removes all bullets marked for deletion
		
		for (PlayerProjectile pp : deadBullets)
			activeBullets.remove(pp);
		
		deadBullets.clear();
	}
	
	public void update() {
		
		this.move();
		
		if (!Maths.checkInBounds(this))
			this.kill();
		
		for (EnemyActive ea : Game.activeEnemies)
			if (Maths.distanceTo(this, ea) < 20)
				if (this.checkCollision(ea)) {
					this.kill();
					ea.hit(this.damage);
				}
	}
}
