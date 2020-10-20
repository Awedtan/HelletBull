import java.awt.*;
import java.awt.geom.*;

public class Enemy extends Ellipse2D.Double {
	
	String sprite;
	String path; // The string representation of the bezier curve coordinates
	double velocity; // The current speed of the enemy
	double acceleration; // How much the speed of the enemy changes each frame
	double maxVelocity; // The max velocity
	double minVelocity; // The min velocity
	double health; // Enemy health
	boolean offset; // If the bezier curve coordinates should move along with the origin, or are fixed
	
	public Enemy() {
		
		sprite = "";
		path = "";
		velocity = 0;
		acceleration = 0;
		maxVelocity = 0;
		minVelocity = 0;
		width = 0;
		height = 0;
		health = 0;
		offset = false;
	}
	
	public Enemy(String sprite, String path, double velocity, double acceleration, double maxVelocity, double minVelocity, Dimension size, int health,
			boolean offset) {
		
		this.sprite = sprite;
		this.path = path;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;
		this.minVelocity = minVelocity;
		width = size.width;
		height = size.height;
		this.health = health;
		this.offset = offset;
	}
}
