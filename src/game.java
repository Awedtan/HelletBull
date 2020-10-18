import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

public class game {
	
	final static int FPS = 120;
	final static int SCREENWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	final static int SCREENHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	static HashMap<String, projectile> bulletTypes = new HashMap<String, projectile>();
	static ArrayList<projectile> activeBullets = new ArrayList<projectile>();
	
	static HashMap<String, enemy> enemyTypes = new HashMap<String, enemy>();
	static ArrayList<enemyActive> activeEnemies = new ArrayList<enemyActive>();
	static ArrayList<Integer> deadEnemies = new ArrayList<Integer>();
	
	static HashMap<String, ArrayDeque<subroutine>> routineTypes = new HashMap<String, ArrayDeque<subroutine>>();
	
	static ArrayList<Path2D.Double> activePaths = new ArrayList<Path2D.Double>();
	
	static ArrayDeque<ArrayDeque<subscript>> scriptList = new ArrayDeque<ArrayDeque<subscript>>();
	static ArrayDeque<subscript> activeScript = new ArrayDeque<subscript>();
	
	static double gameSpeed = 1.0;
	static int frameCount = 0;
	static boolean run = true;
	
	public static double angleTo(Ellipse2D.Double origin, Ellipse2D.Double target) {
		
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
	
	public static double angleTo(double originX, double originY, double tarcenterX, double tarcenterY) {
		
		double angle;
		
		if (tarcenterX > originX && tarcenterY > originY) { // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(tarcenterX - originX) / Math.abs(tarcenterY - originY)));
			
		} else if (tarcenterX < originX && tarcenterY > originY) { // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(tarcenterX - originX) / Math.abs(tarcenterY - originY)));
			
		} else if (tarcenterX < originX && tarcenterY < originY) { // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(tarcenterX - originX) / Math.abs(tarcenterY - originY)));
			
		} else if (tarcenterX > originX && tarcenterY < originY) { // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(tarcenterX - originX) / Math.abs(tarcenterY - originY)));
			
		} else if (tarcenterX == originX)
			if (tarcenterY > originY)
				angle = 0;
			else
				angle = 180;
		else if (tarcenterY == originY)
			if (tarcenterX > originX)
				angle = 90;
			else
				angle = -90;
		else
			angle = 0;
		
		return angle;
	}
	
	public static double centerX(Ellipse2D.Double ellipse) {
		
		return ellipse.x + ellipse.width / 2;
	}
	
	public static double centerY(Ellipse2D.Double ellipse) {
		
		return ellipse.y + ellipse.height / 2;
	}
	
	public static void createBullets() {
		
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
					parser.parseBullet(accumulate);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void createEnemies() {
		
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
					parser.parseEnemy(accumulate);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void createRoutines() {
		
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
					parser.parseRoutine(accumulate);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void createScripts() {
		
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
				parser.parseScript(accumulate);
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static double distanceTo(Ellipse2D.Double origin, Ellipse2D.Double target) {
		
		return Math.sqrt(Math.pow(target.y - origin.y, 2) + Math.pow(target.x - origin.x, 2));
	}
	
	public static void purgeEnemies() {
		
		int increment = 0;
		
		for (int i = 0; i < deadEnemies.size(); i++) {
			activeEnemies.remove(deadEnemies.get(i) + increment);
			increment++;
		}
		
		deadEnemies.clear();
	}
	
	public static void script() {
		
		if (activeScript.peekFirst() == null)
			if (scriptList.peekFirst() != null)
				activeScript = scriptList.removeFirst();
			else
				return;
			
		if (game.frameCount == activeScript.peekFirst().time) {
			
			subscript current = activeScript.removeFirst();
			enemy.create(current.enemy, current.origin, current.routine);
		}
	}
}