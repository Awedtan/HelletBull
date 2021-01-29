import java.awt.*;

public class Subscript {
	// Scripts are a queue of these
	// At the specified time, spawn an enemy at the origin and give it the bullet routine
	
	int time;
	String enemy;
	Point origin;
	String routine;
	String song;
	
	public Subscript(int time, String enemy, Point origin, String routine, String song) {
		
		this.time = time;
		this.enemy = enemy;
		this.origin = origin;
		this.routine = routine;
		this.song = song;
	}
	
	public String toString() {
		return time + ", " + enemy + ", " + origin + "," + routine;
	}
}