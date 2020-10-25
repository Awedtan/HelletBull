import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

public class Game {
	
	static HashMap<String, Projectile> bulletMap = new HashMap<>(); // Projectile types
	static ArrayList<Projectile> activeBullets = new ArrayList<>(); // Projectiles currently alive
	static ArrayList<Projectile> deadBullets = new ArrayList<>(); // Projectiles to be killed
	
	static HashMap<String, ArrayDeque<Subroutine>> routineMap = new HashMap<>(); // Routine types
	
	static HashMap<String, Enemy> enemyMap = new HashMap<>(); // Enemy types
	static ArrayList<EnemyActive> activeEnemies = new ArrayList<>(); // Enemies currently alive
	static ArrayList<EnemyActive> deadEnemies = new ArrayList<>(); // Enemies to be killed
	
	static ArrayList<Path2D.Double> activePaths = new ArrayList<>(); // All active enemy path curves
	
	static ArrayDeque<ArrayDeque<Subscript>> scriptQueue = new ArrayDeque<>(); // Queue of scripts, scripts are queues of subscripts
	static ArrayDeque<Subscript> activeScript = new ArrayDeque<>(); // Current script
	
	static ArrayDeque<Pickup> activePickups = new ArrayDeque<>(); // Pickups currently alive
	
	static final int SCREENWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static final int SCREENHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	static final int FPS = 120;
	static double gameSpeed = 1.0;
	static int frameCount = 0;
	static boolean run = true;
	
	public static void initializeBullets() {
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
	
	public static void initializeEnemies() {
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
	
	public static void initializeRoutines() {
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
	
	public static void initializeScripts() {
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
	
	public static void purgeBullets() {
		// Removes all bullets marked for deletion
		
		for (Projectile p : deadBullets) {
			if (bulletMap.get(p.subbullet) != null)
				p.spawn();
			activeBullets.remove(p);
		}
		
		deadBullets.clear();
	}
	
	public static void purgeEnemies() {
		// Removes all enemies marked for deletion
		// TODO: clear paths as well
		
		for (EnemyActive ea : deadEnemies)
			activeEnemies.remove(ea);
		
		deadEnemies.clear();
	}
	
	public static void script() {
		// Runs game scripts
		
		if (activeScript.peekFirst() == null)
			if (scriptQueue.peekFirst() != null)
				activeScript = scriptQueue.removeFirst();
			else
				return;
			
		if (frameCount == activeScript.peekFirst().time) {
			
			Subscript current = activeScript.removeFirst();
			EnemyActive.create(current.enemy, current.origin, current.routine);
		}
	}
	
	public static void update() {
		// Updates all aspects of the game
		
		Player.update();
		
		for (Projectile p : activeBullets)
			p.update();
		for (EnemyActive ea : activeEnemies)
			ea.update();
		for (PlayerProjectile pp : PlayerProjectile.activeBullets)
			pp.update();
		
		if (deadEnemies.size() > 0)
			purgeEnemies();
		if (deadBullets.size() > 0)
			purgeBullets();
		if (PlayerProjectile.deadBullets.size() > 0)
			PlayerProjectile.purge();
		
		script();
	}
}
