import java.awt.*;
import java.awt.geom.*;

public class enemy extends Ellipse2D.Double {
	
	String sprite;
	String path; // The array of points the enemy will travel through
	double velocity; // The current speed of the enemy
	double acceleration; // How much the speed of the enemy changes each frame
	double maxVelocity; // The max velocity
	double minVelocity; // The min velocity
	double health; // Enemy health
	boolean offset; // If the bezier curve coordinates should move along with the origin, or are fixed
	// String routine;
	
	public enemy() {
		
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
	
	public enemy(String sprite, String path, double velocity, double acceleration, double maxVelocity, double minVelocity, Dimension size, int health,
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
		// this.routine = routine;
	}
	
	public static void create(String enem, Point origin, String routine) {
		
		game.activeEnemies.add(new enemyActive(game.enemyTypes.get(enem), origin, routine));
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		
		for (int i = 0; i < game.activeEnemies.size(); i++)
			g2.fill(game.activeEnemies.get(i));
		for (int i = 0; i < game.activePaths.size(); i++)
			g2.draw(game.activePaths.get(i));
	}
	
	public static void kill(int enemy) {
		
		game.deadEnemies.add(enemy);
	}
	
	public static void move(int enemy) {
		
		enemyActive enem = game.activeEnemies.get(enemy);
		int index = game.frameCount - enem.startFrame;
		
		if (index < enem.points.size()) {
			enem.x = enem.points.get(index).x;
			enem.y = enem.points.get(index).y;
		} else
			kill(enemy);
	}
	
	public static void routine(int enemy) {
		enemyActive enem = game.activeEnemies.get(enemy);
		
		if (enem.routine.peekFirst() == null)
			return;
		
		if (enem.routine.peekFirst().time == game.frameCount - enem.startFrame)
			shoot(enemy);
	}
	
	public static void shoot(int enemy) {
		
		enemyActive enem = game.activeEnemies.get(enemy);
		subroutine routine = enem.routine.removeFirst();
		
		if (routine.angle > 0)
			projectile.create(routine.proj, enem, routine.amount, routine.angle);
		else if (routine.amount > 0)
			projectile.create(routine.proj, enem, routine.amount);
		else
			projectile.create(routine.proj, enem);
	}
	
	public static void update(int enemy) {
		
		move(enemy);
		routine(enemy);
	}
}