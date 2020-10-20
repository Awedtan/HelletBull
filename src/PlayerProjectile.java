import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class PlayerProjectile extends Ellipse2D.Double {
	
	static HashMap<String, PlayerProjectile[]> bulletMap = new HashMap<>();
	static ArrayList<PlayerProjectile> activeBullets = new ArrayList<>();
	
	String sprite;
	int inaccuracy; // How much deviation there can be when initially shooting the proj
	double angle; // The angle of the proj, 0 is down, 90 is right, -90 is left, 180 is up
	double velocity; // The current speed of the proj
	int homing; // If greater than 0, the proj will follow the player until this many pixels away
	
	public PlayerProjectile(String sprite, int inaccuracy, double angle, double velocity, int homing, Dimension size) {
		
		this.sprite = sprite;
		this.inaccuracy = inaccuracy;
		this.angle = angle;
		this.velocity = velocity;
		this.homing = homing;
		width = size.width;
		height = size.height;
	}
	
	public PlayerProjectile(PlayerProjectile pp, Point origin) {
		
		this.sprite = pp.sprite;
		this.inaccuracy = pp.inaccuracy;
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
			activeBullets.add(new PlayerProjectile(bulletMap.get(name)[i], new Point((int) Maths.centerX(Player.model), (int) Maths.centerY(Player.model))));
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		
		for (int i = 0; i < activeBullets.size(); i++)
			g2.fill(activeBullets.get(i));
	}
	
	public static void initialize() {
		
		bulletMap.put("test", new PlayerProjectile[] { new PlayerProjectile("", 0, 180, 3, 0, new Dimension(10, 10)) });
	}
	
	public void kill() {
		
	}
	
	public void move() {
		
		this.x += Math.sin(Math.toRadians(this.angle)) * this.velocity;
		this.y += Math.cos(Math.toRadians(this.angle)) * this.velocity;
	}
	
	public void update() {
		
		this.move();
		
		if (Maths.distanceTo(this, Player.model) < 20)
			if (this.checkCollision(Player.model)) {
				this.kill();
				Player.hit();
			}
	}
}
