import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

public class Game {
	
	static HashMap<String, EnemyProjectile> bulletMap = new HashMap<>(); // Projectile types
	static ArrayList<EnemyProjectile> activeBullets = new ArrayList<>(); // Projectiles currently alive
	static ArrayList<EnemyProjectile> deadBullets = new ArrayList<>(); // Projectiles to be killed
	
	static HashMap<String, ArrayDeque<Subroutine>> routineMap = new HashMap<>(); // Routine types
	
	static HashMap<String, Enemy> enemyMap = new HashMap<>(); // Enemy types
	static ArrayList<EnemyActive> activeEnemies = new ArrayList<>(); // Enemies currently alive
	static ArrayList<EnemyActive> deadEnemies = new ArrayList<>(); // Enemies to be killed
	
	static ArrayList<Path2D.Double> activePaths = new ArrayList<>(); // All active enemy path curves
	
	static HashMap<String, ArrayDeque<Subscript>> scriptMap = new HashMap<>(); // Script types
	static ArrayDeque<ArrayDeque<Subscript>> scriptQueue = new ArrayDeque<>(); // Queue of scripts, scripts are queues of subscripts
	static ArrayDeque<Subscript> activeScript = new ArrayDeque<>(); // Current script
	
	static ArrayList<Pickup> activePickups = new ArrayList<>(); // Pickups currently alive
	static ArrayList<Pickup> deadPickups = new ArrayList<>(); // Pickups currently alive
	
	static final Rectangle SCREEN = new Rectangle(0, 0, 1280, 960);
	static final Rectangle PLAYSCREEN = new Rectangle(0, 0, 900, 960);
	static final Rectangle SIDESCREEN = new Rectangle(900, 0, 1280 - 900, 960);
	static final int GRIDLINES = 20;
	static final int FPS = 120;
	static double gameSpeed = 1.0;
	static int frameCount = 0;
	static int lastSpawnFrame = 0;
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
			String str = input.readLine().trim();
			
			while (str.indexOf('!') == -1)
				str = input.readLine().trim();
			
			while (str.indexOf('?') == -1) {
				ArrayList<String> accumulate = new ArrayList<String>();
				
				if (str.indexOf("script") == 0)
					while (str.indexOf(';') == -1) {
						if (str.length() > 1)
							accumulate.add(str);
						str = input.readLine();
					}
				else if (scriptMap.containsKey(str))
					scriptQueue.addLast(scriptMap.get(str).clone());
				
				str = input.readLine();
				
				if (accumulate.size() != 0)
					Parser.parseScript(accumulate);
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void playClip(String s) {
		
		try {
			
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("sounds/" + s + ".wav")));
			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	public static void purgeBullets() {
		// Removes all bullets marked for deletion
		
		for (EnemyProjectile p : deadBullets) {
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
	
	public static void purgePickups() {
		// Removes all enemies marked for deletion
		
		for (Pickup pu : deadPickups)
			activePickups.remove(pu);
		
		deadPickups.clear();
	}
	
	public static void purgeScript() {
		// Transitions into next script
		// Resets all relevant delay values
		
		frameCount = 0;
		lastSpawnFrame = 0;
		Player.lastShot = 0;
		Player.lastBomb = 0;
		
		activeScript = scriptQueue.removeFirst();
	}
	
	public static void script() {
		// Runs game scripts
		
		if (activeScript.peekFirst() == null)
			if (scriptQueue.peekFirst() != null && activeEnemies.isEmpty())
				purgeScript();
			else
				return;
			
		if (frameCount - lastSpawnFrame == activeScript.peekFirst().time) {
			
			Subscript current = activeScript.removeFirst();
			EnemyActive.create(current.enemy, current.origin, current.routine);
			lastSpawnFrame = frameCount;
		}
	}
	
	public static void update() {
		// Updates all aspects of the game
		
		Player.update();
		
		for (EnemyProjectile p : activeBullets)
			p.update();
		for (EnemyActive ea : activeEnemies)
			ea.update();
		for (PlayerProjectile pp : PlayerProjectile.activeBullets)
			pp.update();
		for (Pickup pu : activePickups)
			pu.update();
		
		if (deadEnemies.size() > 0)
			purgeEnemies();
		if (deadBullets.size() > 0)
			purgeBullets();
		if (PlayerProjectile.deadBullets.size() > 0)
			PlayerProjectile.purge();
		if (deadPickups.size() > 0)
			purgePickups();
		
		script();
	}
}