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
	
	public static void create(String enem, Point origin, String routine) {
		// Adds a new enemy to the enemy array
		
		Game.activeEnemies.add(new EnemyActive(Game.enemyMap.get(enem), origin, routine));
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		
		for (int i = 0; i < Game.activeEnemies.size(); i++)
			g2.fill(Game.activeEnemies.get(i));
		for (int i = 0; i < Game.activePaths.size(); i++)
			g2.draw(Game.activePaths.get(i));
	}
	
	public static void kill(int enemy) {
		// Marks an enemy for deletion
		
		Game.deadEnemies.add(enemy);
	}
	
	public static void move(int enemy) {
		// Updates an enemy's position
		
		EnemyActive enem = Game.activeEnemies.get(enemy);
		int index = Game.frameCount - enem.startFrame;
		
		if (index < enem.points.size()) {
			enem.x = enem.points.get(index).x;
			enem.y = enem.points.get(index).y;
		} else
			kill(enemy);
	}
	
	public static void routine(int enemy) {
		// Updates an enemy's routine
		
		EnemyActive enem = Game.activeEnemies.get(enemy);
		
		if (enem.routine.peekFirst() == null)
			return;
		
		if (enem.routine.peekFirst().time == Game.frameCount - enem.startFrame)
			shoot(enemy);
	}
	
	public static void shoot(int enemy) {
		// Creates the specified projectile type at the enemy
		
		EnemyActive enem = Game.activeEnemies.get(enemy);
		Subroutine routine = enem.routine.removeFirst();
		
		if (routine.angle > 0)
			Projectile.create(routine.proj, enem, routine.amount, routine.angle);
		else if (routine.amount > 0)
			Projectile.create(routine.proj, enem, routine.amount);
		else
			Projectile.create(routine.proj, enem);
	}
	
	public static void update(int enemy) {
		// Updates an enemy
		
		move(enemy);
		routine(enemy);
	}
}