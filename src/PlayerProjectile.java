import java.awt.*;
import java.awt.geom.*;

public class PlayerProjectile extends Ellipse2D.Double {
	
	static PlayerProjectile[] shotPowers = new PlayerProjectile[] { new PlayerProjectile("", 1, 180, 10, 0, new Dimension(10, 10)), new PlayerProjectile("", 1, 185, 10, 0, new Dimension(10, 10)),
			new PlayerProjectile("", 1, 175, 10, 0, new Dimension(10, 10)), new PlayerProjectile("", 1, 170, 10, 0, new Dimension(10, 10)),
			new PlayerProjectile("", 1, 190, 10, 0, new Dimension(10, 10)) };
	
	static final int BORDERBUFFER = -10;
	
	String sprite;
	int damage;
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, +-180 is up
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
		
		sprite = pp.sprite;
		damage = pp.damage;
		angle = pp.angle;
		velocity = pp.velocity;
		homing = pp.homing;
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
	
	public static void create(int power) {
		
		for (int i = 0; i <= 2; i++) {
			PlayerProjectile pp = shotPowers[i];
			Game.activePlayerBullets.add(new PlayerProjectile(pp, new Point((int) Maths.centerX(Player.grazeModel.getBounds(), pp.width), (int) Maths.centerY(Player.grazeModel.getBounds(), pp.height))));
		}
	}
	
	public static void drawAll(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		
		for (PlayerProjectile pp : Game.activePlayerBullets)
			g2.fill(pp);
	}
	
	public void kill() {
		// Marks the bullet for deletion
		
		Game.deadPlayerBullets.add(this);
	}
	
	public void move() {
		
		x += Math.sin(Math.toRadians(angle)) * velocity;
		y += Math.cos(Math.toRadians(angle)) * velocity;
	}
	
	public static void purgeAll() {
		// Removes all bullets marked for deletion
		
		for (PlayerProjectile pp : Game.deadPlayerBullets)
		Game.activePlayerBullets.remove(pp);
		
		Game.deadPlayerBullets.clear();
	}
	
	public void update() {
		
		move();
		
		if (Maths.checkInBounds(this.getBounds(), BORDERBUFFER) != -1)
			kill();
		
		for (EnemyActive ea : Game.activeEnemies)
			if (Maths.distanceTo(this.getBounds(), ea.getBounds()) < 20)
				if (checkCollision(ea)) {
					kill();
					ea.hit(damage);
				}
	}
}
