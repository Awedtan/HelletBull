import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class EnemyActive extends Enemy {
	
	ArrayList<Point2D.Double> points; // The array of points the enemy will move through
	ArrayDeque<Subroutine> routine; // The queue of subroutines the enemy will shoot through
	int startFrame; // The frame the enemy was created
	
	public EnemyActive(Enemy enem, Point origin, String routine) {
		
		sprite = enem.sprite;
		path = enem.path;
		velocity = enem.velocity;
		acceleration = enem.acceleration;
		maxVelocity = enem.maxVelocity;
		minVelocity = enem.minVelocity;
		width = enem.width;
		height = enem.height;
		health = enem.health;
		offset = enem.offset;
		
		this.routine = Game.routineMap.get(routine).clone();
		points = Parser.parsePathing(path, origin, offset);
		startFrame = Game.frameCount;
	}
	
	public boolean checkCollision() {
		return false;
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
	
	public void hit(int bullet) {
		
	}
	
	public void kill() {
		// Marks the enemy for deletion
		
		Game.deadEnemies.add(this);
	}
	
	public void move() {
		// Updates the enemy's position
		
		int index = Game.frameCount - this.startFrame;
		
		if (index < this.points.size()) {
			this.x = this.points.get(index).x;
			this.y = this.points.get(index).y;
		} else
			this.kill();
	}
	
	public void routine() {
		// Updates the enemy's routine
		
		if (this.routine.peekFirst() == null)
			return;
		
		if (this.routine.peekFirst().time == Game.frameCount - this.startFrame)
			shoot();
	}
	
	public void shoot() {
		// Creates the specified projectile type at the enemy
		
		Subroutine routine = this.routine.removeFirst();
		
		if (routine.angle > 0)
			Projectile.create(routine.proj, this, routine.amount, routine.angle);
		else if (routine.amount > 0)
			Projectile.create(routine.proj, this, routine.amount);
		else
			Projectile.create(routine.proj, this);
	}
	
	public void update() {
		// Updates the enemy
		
		this.move();
		this.routine();
	}
}