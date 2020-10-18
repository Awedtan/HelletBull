import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class EnemyActive extends Enemy {
	
	ArrayList<Point2D.Double> points; // The array of points the enemy will move through
	ArrayDeque<Subroutine> routine; // The array of subroutines the enemy will shoot through
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
}