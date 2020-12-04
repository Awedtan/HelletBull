import java.awt.*;
import java.awt.geom.*;

public class Pickup extends Rectangle2D.Double {
	
	static final double MAXVELOCITY = 2;
	static final double ACCELERATION = 0.01;
	double velocity = -1;
	int value;
	
	public Pickup(int value, int size, Ellipse2D.Double ellipse) {
		
		this.value = value;
		width = size;
		height = size;
		x = Maths.centerX(ellipse) - size / 2;
		y = Maths.centerY(ellipse) - size / 2;
	}
	
	public boolean collides(Ellipse2D.Double ellipse) {
		// Checks for pickup collision
		// Returns true if collided
		
		return Maths.ellipseArea(ellipse).intersects(this);
	}
	
	public static void create(int value, int size, Ellipse2D.Double ellipse) {
		
		Game.activePickups.add(new Pickup(value, size, ellipse));
	}
	
	public static void drawAll(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.MAGENTA);
		
		for (Pickup pu : Game.activePickups)
			g2.fill(pu);
	}
	
	public void kill() {
		
		Game.deadPickups.add(this);
	}
	
	public void move() {
		
		if (velocity <= MAXVELOCITY)
			velocity += ACCELERATION;
		y += velocity;
	}
	
	public void update() {
		
		this.move();
		
		if (Maths.distanceTo(this, Player.model) < 50)
			if (this.collides(Player.model)) {
				this.kill();
				Player.addScore(this.value);
			}
	}
}