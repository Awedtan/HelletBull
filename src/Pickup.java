import java.awt.geom.*;

public class Pickup extends DObject {
	// Stuff that enemies drop that grant points to the player
	// Only moves down
	
	static final double MAXVELOCITY = 2; // Terminal velocity
	static final double ACCELERATION = 0.01; // Basically just gravity
	
	double velocity = -1;
	int value;
	
	static String pickupClip = "pickup";
	
	public Pickup(int value, int size, Ellipse2D.Double origin) {
		// Pickups only have values of 5 or 200
		// Size is always 15 pixels
		// Origin is the enemy that dropped it
		
		image = Game.getImage("pickup");
		this.value = value;
		width = size;
		height = size;
		x = Maths.centerX(origin.getBounds()) - size / 2;
		y = Maths.centerY(origin.getBounds()) - size / 2;
	}
	
	public static void create(int value, int size, Ellipse2D.Double ellipse) {
		// Creates a pickup with the specified values at the specified origin
		
		Game.activePickups.add(new Pickup(value, size, ellipse));
	}
	
	@Override
	public void kill() {
		// Marks the pickup for deletion
		
		Game.deadPickups.add(this);
	}
	
	@Override
	public void move() {
		// Moves the pickup
		
		if (velocity <= MAXVELOCITY)
			velocity += ACCELERATION;
		y += velocity;
	}
	
	@Override
	public void update() {
		
		move();
		
		if (Maths.checkInBounds(getBounds(), -20) != -1)
			kill();
		
		if (Maths.distanceTo(getBounds(), Player.model.getBounds()) < Player.grazeRadius) // Only checks for collision when within the player's graze radius
			if (getBounds().intersects(Player.model.getBounds())) {
				kill();
				Player.addScore(value);
				Game.playClip(pickupClip);
			}
	}
}