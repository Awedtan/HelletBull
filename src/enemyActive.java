import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class enemyActive extends enemy {
	
	ArrayList<Point2D.Double> points;
	ArrayDeque <subroutine> routine;
	int startFrame;
	
	public enemyActive(enemy enem, Point origin, String routine) {
		
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
		
		this.routine = game.routineTypes.get(routine).clone();
		points = parser.parsePathing(path, origin, offset);
		startFrame = game.frameCount;
	}
}