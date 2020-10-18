import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

public class Game {
	
	static HashMap<String, Projectile> bulletMap = new HashMap<String, Projectile>(); // Projectile types
	static ArrayList<Projectile> activeBullets = new ArrayList<Projectile>(); // Projectiles currently alive
	
	static HashMap<String, ArrayDeque<Subroutine>> routineMap = new HashMap<String, ArrayDeque<Subroutine>>(); // Routine types
	
	static HashMap<String, Enemy> enemyMap = new HashMap<String, Enemy>(); // Enemy types
	static ArrayList<EnemyActive> activeEnemies = new ArrayList<EnemyActive>(); // Enemies currently alive
	static ArrayList<Integer> deadEnemies = new ArrayList<Integer>(); // Enemies to be killed
	
	static ArrayList<Path2D.Double> activePaths = new ArrayList<Path2D.Double>(); // All active enemy path curves
	
	static ArrayDeque<ArrayDeque<Subscript>> scriptQueue = new ArrayDeque<ArrayDeque<Subscript>>(); // Queue of scripts, scripts are queues of subscripts
	static ArrayDeque<Subscript> activeScript = new ArrayDeque<Subscript>(); // Active script
	
	final static int SCREENWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	final static int SCREENHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	final static int FPS = 120;
	static double gameSpeed = 1.0;
	static int frameCount = 0;
	static boolean run = true;
	
	public static double angleTo(Ellipse2D.Double origin, Ellipse2D.Double target) {
		// Finds the angle from the origin to the target
		// FOR ALL ANGLES IN THIS GAME: down is 0, up is +-180, right is 90, left is -90
		
		double angle;
		
		if (target.x > origin.x && target.y > origin.y) { // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x < origin.x && target.y > origin.y) { // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x < origin.x && target.y < origin.y) { // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x > origin.x && target.y < origin.y) { // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x == origin.x)
			if (target.y > origin.y)
				angle = 0;
			else
				angle = 180;
		else if (target.y == origin.y)
			if (target.x > origin.x)
				angle = 90;
			else
				angle = -90;
		else
			angle = 0;
		
		return angle;
	}
	
	public static double angleTo(double originX, double originY, double tarCenterX, double tarCenterY) {
		// Finds the angle from the origin coordinates to the target coordinates
		// FOR ALL ANGLES IN THIS GAME: down is 0, up is +-180, right is 90, left is -90
		
		double angle;
		
		if (tarCenterX > originX && tarCenterY > originY) { // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
			
		} else if (tarCenterX < originX && tarCenterY > originY) { // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
			
		} else if (tarCenterX < originX && tarCenterY < originY) { // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
			
		} else if (tarCenterX > originX && tarCenterY < originY) { // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(tarCenterX - originX) / Math.abs(tarCenterY - originY)));
			
		} else if (tarCenterX == originX)
			if (tarCenterY > originY)
				angle = 0;
			else
				angle = 180;
		else if (tarCenterY == originY)
			if (tarCenterX > originX)
				angle = 90;
			else
				angle = -90;
		else
			angle = 0;
		
		return angle;
	}
	
	public static double centerX(Ellipse2D.Double ellipse) {
		// Returns the x coordinate of the center of the given ellipse
		
		return ellipse.x + ellipse.width / 2;
	}
	
	public static double centerY(Ellipse2D.Double ellipse) {
		// Returns the y coordinate of the center of the given ellipse
		
		return ellipse.y + ellipse.height / 2;
	}
	
	public static void createBullets() {
		// Reads in data from bullet files
		// Collects and sends the data to be parsed
		
		try {
			
			BufferedReader input = new BufferedReader(new FileReader("data/bullet.txt"));
			String str = input.readLine();
			
			while (str.indexOf('?') == -1) {
				String[] accumulate = new String[15];
				int count = 0;
				
				if (str.indexOf("bullet") == 0)
					while (str.indexOf(';') == -1) {
						accumulate[count] = str;
						str = input.readLine();
						count++;
					}
				
				str = input.readLine();
				if (accumulate[0] != null)
					Parser.parseBullet(accumulate);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void createEnemies() {
		// Reads in data from enemy files
		// Collects and sends the data to be parsed
		
		try {
			
			BufferedReader input = new BufferedReader(new FileReader("data/enemy.txt"));
			String str = input.readLine();
			
			while (str.indexOf('?') == -1) {
				String[] accumulate = new String[11];
				int count = 0;
				
				if (str.indexOf("enemy") == 0)
					while (str.indexOf(';') == -1) {
						accumulate[count] = str;
						str = input.readLine();
						count++;
					}
				
				str = input.readLine();
				
				if (accumulate[0] != null)
					Parser.parseEnemy(accumulate);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void createRoutines() {
		// Reads in data from routine files
		// Collects and sends the data to be parsed
		
		try {
			
			BufferedReader input = new BufferedReader(new FileReader("data/routine.txt"));
			String str = input.readLine();
			
			while (str.indexOf('?') == -1) {
				ArrayList<String> accumulate = new ArrayList<String>();
				
				if (str.indexOf("routine") == 0)
					while (str.indexOf(';') == -1) {
						if (str.length() > 0)
							accumulate.add(str);
						str = input.readLine();
					}
				
				str = input.readLine();
				
				if (accumulate.size() != 0)
					Parser.parseRoutine(accumulate);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void createScripts() {
		// Reads in data from script files
		// Collects and sends the data to be parsed
		
		try {
			
			BufferedReader input = new BufferedReader(new FileReader("data/script.txt"));
			String str = input.readLine();
			
			ArrayList<String> accumulate = new ArrayList<String>();
			
			if (str.indexOf("script") == 0)
				while (str.indexOf('?') == -1) {
					if (str.length() > 0)
						accumulate.add(str);
					str = input.readLine();
				}
			
			if (accumulate.size() != 0)
				Parser.parseScript(accumulate);
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static double distanceTo(Ellipse2D.Double origin, Ellipse2D.Double target) {
		// Returns the distance from the center of the origin to the center of the target
		
		return Math.sqrt(Math.pow(centerY(target) - centerY(target), 2) + Math.pow(centerX(target) - centerX(target), 2));
	}
	
	public static void purgeEnemies() {
		// Removes all enemies marked for deletion
		// TODO: clear paths as well
		
		int increment = 0;
		
		for (int i = 0; i < deadEnemies.size(); i++) {
			activeEnemies.remove(deadEnemies.get(i) + increment);
			increment++;
		}
		
		deadEnemies.clear();
	}
	
	public static void script() {
		// Creates and runs game scripts
		
		if (activeScript.peekFirst() == null)
			if (scriptQueue.peekFirst() != null)
				activeScript = scriptQueue.removeFirst();
			else
				return;
			
		if (frameCount == activeScript.peekFirst().time) {
			
			Subscript current = activeScript.removeFirst();
			Enemy.create(current.enemy, current.origin, current.routine);
		}
	}
	
	public static void update() {
		// Updates all aspects of the game
		
		Player.move();
		
		for (int i = 0; i < activeBullets.size(); i++)
			Projectile.update(i);
		
		for (int i = 0; i < activeBullets.size(); i++)
			if (activeBullets.get(i).lifetimeInt == 0 || !Projectile.checkInBounds(i) || Player.checkCollision(i)) {
				Projectile.kill(i);
				i--;
			}
		
		for (int i = 0; i < activeEnemies.size(); i++)
			Enemy.update(i);
		
		script();
		
		if (deadEnemies.size() > 0)
			purgeEnemies();
	}
}