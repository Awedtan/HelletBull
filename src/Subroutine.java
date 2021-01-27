public class Subroutine {
	// Bullet routines are a queue of these
	// At the specified time, shoot the bullet with the other optional modifiers
	
	int time;
	String proj;
	int amount; // Optional, for a circle
	int angle; // Optional, for an arc
	
	public Subroutine(int time, String proj, int amount, int angle) {
		
		this.time = time;
		this.proj = proj;
		this.amount = amount;
		this.angle = angle;
	}
}