import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class EnemyActive extends Enemy {
	
	static final int POINTPAUSEVALUE = -10;
	
	static String damageClip = "enemydamage";
	static String deathClip = "enemydeath";
	
	ArrayDeque<Point2D.Double> points; // The array of points the enemy will move through
	ArrayDeque<Subroutine> routine; // The queue of subroutines the enemy will shoot through
	int startFrame; // The frame the enemy was created
	int pauseFrame = 0; // How long this enemy will stay in one spot as determined by its script
	
	public EnemyActive(Enemy enem, Point origin, String routine) {
		
		sprite = enem.sprite;
		path = enem.path;
		width = enem.width;
		height = enem.height;
		health = enem.health;
		flatness = enem.flatness;
		offset = enem.offset;
		
		this.routine = Game.routineMap.get(routine).clone();
		points = Parser.parsePathing(path, origin, flatness, offset, false);
		
		Point2D.Double p = points.removeFirst();
		x = p.x;
		y = p.y;
		
		startFrame = Game.frameCount;
	}
	
	public static void create(String enem, Point origin, String routine) {
		// Adds a new enemy to the enemy array
		
		Game.activeEnemies.add(new EnemyActive(Game.enemyMap.get(enem), origin, routine));
	}
	
	public static void drawAll(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		
		for (EnemyActive ea : Game.activeEnemies)
			g2.fill(ea);
		// for (Path2D.Double d : Game.activePaths)
		// g2.draw(d);
	}
	
	public void hit(int damage) {
		
		health -= damage;
		
		if (health <= 0) {
			
			kill();
			Game.playClip(deathClip);
			
			switch ((int) (Math.random() * 100)) {
				case (0):
					Pickup.create(5, 20, this);
					break;
				case (1):
				case (2):
				case (3):
				case (4):
				case (5):
				case (6):
				case (7):
				case (8):
				case (9):
				case (10):
					Pickup.create(1, 10, this);
					break;
			}
		}
		
		Game.playClip(damageClip);
	}
	
	public void kill() {
		// Marks the enemy for deletion
		
		Game.deadEnemies.add(this);
	}
	
	public void move() {
		// Updates the enemy's position
		
		if (pauseFrame > 0) {
			pauseFrame--;
			return;
		}
		
		if (points.peekFirst() != null) {
			Point2D.Double p = points.removeFirst();
			
			if (p.y == POINTPAUSEVALUE) {
				pauseFrame = (int) p.x;
				return;
			}
			
			x = p.x - width / 2;
			y = p.y - height / 2;
		} else
			kill();
	}
	
	public void routine() {
		// Updates the enemy's routine
		
		if (routine.peekFirst() == null)
			return;
		
		if (routine.peekFirst().time == Game.frameCount - startFrame)
			shoot();
	}
	
	public void shoot() {
		// Creates the specified projectile type at the enemy
		
		Subroutine routine = this.routine.removeFirst();
		
		if (routine.angle > 0)
			EnemyProjectile.create(routine.proj, this, routine.amount, routine.angle);
		else if (routine.amount > 0)
			EnemyProjectile.create(routine.proj, this, routine.amount);
		else
			EnemyProjectile.create(routine.proj, this);
	}
	
	public void update() {
		// Updates the enemy
		
		move();
		routine();
	}
}