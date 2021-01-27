public class Enemy extends AObject {
	// Class for an uninitialized enemy
	// Gets stored in a map
	// Gets turned into an EnemyActive once spawned in the game
	
	String path; // The string representation of the bezier curve coordinates
	int health; // Enemy health
	double flatness; // Bezier curve tolerance
	boolean offset; // If the bezier curve coordinates should move along with the origin, or are fixed
	boolean isBoss;
	
	public Enemy() {
	}
	
	public Enemy(String sprite, String path, int health, double flatness, boolean offset, boolean isBoss) {
		// These values are specified in data/enemies/filename.txt
		
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