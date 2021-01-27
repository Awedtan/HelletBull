import java.awt.*;

public class Subscript {
	// Scripts are a queue of these
	// At the specified time, spawn an enemy at the origin and give it the bullet routine
	
	int time;
	String enemy;
	Point origin;
	String routine;
	
	public Subscript(int time, String enemy, Point origin, String routine) {
		
		this.time = time;
		this.enemy = enemy;
		this.origin = origin;
		this.routine = routine;
	}
	
	public String toString() {
		return time + ", " + enemy + ", " + origin + "," + routine;
	}
}