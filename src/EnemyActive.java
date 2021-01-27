import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class EnemyActive extends Enemy {
	// An enemy that has been spawn in the game
	// Has coordinates, a path, a bullet routine, and can be drawn
	
	static final int POINTPAUSEVALUE = -123456; // Some arbitrary number, if the next coordinate in the enemy's path has this as the y-value, the enemy will stop in place for the x-value # of frames
	
	static String damageClip = "enemydamage"; // Sound clip strings
	static String deathClip = "enemydeath";
	static String shotClip = "enemyshot";
	static String bossClip = "bossdeath";
	
	ArrayDeque<Point2D.Double> points; // The queue of points the enemy will move through
	ArrayDeque<Subroutine> routine; // The queue of subroutines the enemy will shoot through
	int lastSpawnFrame; // The frame the last shot was shot
	int pauseFrame = 0; // How long this enemy will stay in one spot as determined by its script
	
	public EnemyActive(Enemy enem, Point origin, String routine) {
		// Creates an EnemyActive from a stored Enemy, an origin coordinate, and a bullet routine name
		
		image = Game.getImage(enem.sprite); // Retrieves the sprite
		path = enem.path;
		width = image.getWidth(null) / 2; // Size is tied to sprite size
		height = image.getHeight(null);
		health = enem.health;
		flatness = enem.flatness;
		offset = enem.offset;
		isBoss = enem.isBoss;
		
		this.routine = Game.routineMap.get(routine).clone(); // Retrieves the bullet routine
		points = Parser.parsePathing(path, origin, flatness, offset, false); // Creates the queue of coordinates from the pathing string
		
		Point2D.Double p = points.removeFirst(); // Moves to the first point
		x = p.x;
		y = p.y;
		
		lastSpawnFrame = Game.frameCount;
	}
	
	public static void create(String enem, Point origin, String routine) {
		// Adds a new EnemyActive to the game array
		
		Game.activeEnemies.add(new EnemyActive(Game.enemyMap.get(enem), origin, routine));
	}
	
	public void hit(int damage) {
		// Subtracts the number from the enemy's health
		// Checks if health is below 0
		
		health -= damage;
		
		if (health <= 0) {
			
			kill();
			Player.addScore(1);
			Game.playClip(deathClip);
			
			if (isBoss)
				Pickup.create(200, 15, this); // Bosses drop more points because they just do
			else
				Pickup.create(5, 15, this);
		}
		
		Game.playClip(damageClip);
	}
	
	@Override
	public void kill() {
		// Marks the enemy for deletion
		
		if (isBoss) {
			Game.playClip(bossClip);
			Game.activeEnemyBullets.clear();
		}
		
		Game.deadEnemies.add(this);
	}
	
	@Override
	public void move() {
		// Moves to the next point in the coordinate queue
		
		if (pauseFrame > 0) { // Updating the pause duration
			pauseFrame--;
			return;
		}
		
		if (points.peekFirst() != null) {
			Point2D.Double p = points.removeFirst();
			
			if (p.y == POINTPAUSEVALUE) { // Stopping in place
				pauseFrame = (int) p.x;
				return;
			}
			
			x = p.x - width / 2;
			y = p.y - height / 2;
		} else
			kill();
	}
	
	public void routine() {
		// Runs the enemy's bullet routine
		
		if (routine.peekFirst() == null)
			return;
		
		if (routine.peekFirst().time == Game.frameCount - lastSpawnFrame) // Shoots the next bullet in the routine
			shoot();
	}
	
	public void shoot() {
		// Creates the specified projectile type at the enemy's location
		
		Subroutine routine = this.routine.removeFirst();
		
		if (routine.angle > 0)
			EnemyProjectile.create(routine.proj, this, routine.amount, routine.angle); // Creates an arc of bullets
		else if (routine.amount > 0)
			EnemyProjectile.create(routine.proj, this, routine.amount); // Creates a circle of bullets
		else
			EnemyProjectile.create(routine.proj, this); // Creates a single bullet
		
		Game.playClip(shotClip);
		lastSpawnFrame = Game.frameCount;
	}
	
	@Override
	public void update() {
		// Updates the enemy
		
		move();
		routine();
	}
}