public class Subspell {
	
	String points;
	String routine;
	
	int lastSpawnFrame; // The frame the last shot was shot
	int pauseFrame = 0; // How long this boss will stay in one spot as determined by its script
	
	public Subspell(String points, String routine) {
		
		this.points = points;
		this.routine = routine;
	}
	
	public boolean isEmpty() {
		
		return points.isEmpty() && routine.isEmpty();
	}
}