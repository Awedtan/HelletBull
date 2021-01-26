public class Enemy extends AObject {
	
	String path; // The string representation of the bezier curve coordinates
	int health; // Enemy health
	double flatness;
	boolean offset; // If the bezier curve coordinates should move along with the origin, or are fixed
	boolean isBoss;
	
	public Enemy() {
	}
	
	public Enemy(String sprite, String path, int health, double flatness, boolean offset, boolean isBoss) {
		
		this.sprite = sprite;
		this.path = path;
		this.health = health;
		this.flatness = flatness;
		this.offset = offset;
		this.isBoss = isBoss;
	}
	
	@Override
	void kill() {
	}
	
	@Override
	void move() {
	}
	
	@Override
	void update() {
	}
}