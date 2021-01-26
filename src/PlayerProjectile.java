import java.awt.*;

public class PlayerProjectile extends DObject {
	
	static PlayerProjectile[] shotPowers = new PlayerProjectile[] { new PlayerProjectile(1, 180, 10), new PlayerProjectile(1, 182, 10), new PlayerProjectile(1, 178, 10),
			new PlayerProjectile(1, 184, 10), new PlayerProjectile(1, 176, 10) };
	
	static final int BORDERBUFFER = -10;
	
	String sprite = "pp";
	int damage;
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, +-180 is up
	double velocity; // The current speed of the proj
	
	public PlayerProjectile(int damage, double angle, double velocity) {
		
		image = Game.getImage(sprite);
		width = image.getWidth(null);
		height = image.getHeight(null);
		this.damage = damage;
		this.angle = angle;
		this.velocity = velocity;
	}
	
	public PlayerProjectile(PlayerProjectile pp, Point origin) {
		
		image = Game.getImage(sprite);
		damage = pp.damage;
		angle = pp.angle;
		velocity = pp.velocity;
		width = pp.width;
		height = pp.height;
		x = origin.x;
		y = origin.y;
	}
	
	public static void create() {
		
		for (PlayerProjectile pp : shotPowers)
			Game.activePlayerBullets.add(new PlayerProjectile(pp, new Point((int) Maths.centerX(Player.model.getBounds(), pp.width), (int) Maths.centerY(Player.model.getBounds(), pp.height))));
	}
	
	@Override
	public void kill() {
		// Marks the bullet for deletion
		
		Game.deadPlayerBullets.add(this);
	}
	
	@Override
	public void move() {
		
		x += Math.sin(Math.toRadians(angle)) * velocity;
		y += Math.cos(Math.toRadians(angle)) * velocity;
	}
	
	@Override
	public void update() {
		
		move();
		
		if (Maths.checkInBounds(getBounds(), BORDERBUFFER) != -1)
			kill();
		
		for (EnemyActive ea : Game.activeEnemies)
			if (Maths.distanceTo(getBounds(), ea.getBounds()) < Math.max(width, height) + Math.max(ea.width, ea.height))
				if (collides(ea)) {
					kill();
					ea.hit(damage);
				}
	}
}
