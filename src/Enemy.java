import java.awt.*;
import java.awt.geom.*;

public class Enemy extends Ellipse2D.Double {
	
	String sprite;
	String path; // The string representation of the bezier curve coordinates
	int health; // Enemy health
	double flatness;
	boolean offset; // If the bezier curve coordinates should move along with the origin, or are fixed
	
	public Enemy() {
		
		sprite = "";
		path = "";
		width = 0;
		height = 0;
		health = 0;
		offset = false;
	}
	
	public Enemy(String sprite, String path, Dimension size, int health, double flatness, boolean offset) {
		
		this.sprite = sprite;
		this.path = path;
		width = size.width;
		height = size.height;
		this.health = health;
		this.flatness = flatness;
		this.offset = offset;
	}
}