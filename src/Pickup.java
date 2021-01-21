import java.awt.geom.*;

public class Pickup extends DObject {
	
	static final double MAXVELOCITY = 2;
	static final double ACCELERATION = 0.01;
	
	double velocity = -1;
	int value;
	
	public Pickup(int value, int size, Ellipse2D.Double ellipse) {
		
		this.value = value;
		width = size;
		height = size;
		x = Maths.centerX(ellipse.getBounds()) - size / 2;
		y = Maths.centerY(ellipse.getBounds()) - size / 2;
	}
	
	public static void create(int value, int size, Ellipse2D.Double ellipse) {
		
		Game.activePickups.add(new Pickup(value, size, ellipse));
	}
	
	@Override
	public void kill() {
		
		Game.deadPickups.add(this);
	}
	
	@Override
	public void move() {
		
		if (velocity <= MAXVELOCITY)
			velocity += ACCELERATION;
		y += velocity;
	}
	
	@Override
	public void update() {
		
		move();
		
		if (Maths.checkInBounds(getBounds(), -20) != -1)
			kill();
		
		if (Maths.distanceTo(getBounds(), Player.model.getBounds()) < 100)
			if (collides(Player.model)) {
				kill();
				Player.addPower(value);
			}
	}
}