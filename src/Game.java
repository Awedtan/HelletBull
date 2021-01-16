import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

public class Game {
	
	// static final ArrayDeque<Subscript> BOSSSCRIPT = new ArrayDeque<>();
	
	static HashMap<String, Image> imageMap = new HashMap<>();
	
	// static HashMap<String, Boss> bossMap = new HashMap<>();
	// static HashMap<String, ArrayDeque<Subscript>> spellMap = new HashMap<>();
	
	static HashMap<String, EnemyProjectile> bulletMap = new HashMap<>(); // Projectile types
	static ArrayList<EnemyProjectile> activeEnemyBullets = new ArrayList<>(); // Projectiles currently alive
	static ArrayList<EnemyProjectile> deadEnemyBullets = new ArrayList<>(); // Projectiles to be killed
	
	static ArrayList<PlayerProjectile> activePlayerBullets = new ArrayList<>(); // Projectiles currently alive
	static ArrayList<PlayerProjectile> deadPlayerBullets = new ArrayList<>(); // Projectiles to be killed
	
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
	
	public static Image getImage(String s) {
		
		if (imageMap.containsKey(s + ".png"))
			return imageMap.get(s + ".png");
		
		String color = s.split("\\d+")[1];
		String name = s.substring(0, s.length() - color.length());
		Image image = imageMap.get(name.substring(0, name.length() - 1) + ".png");
		int xOffset = 0, yOffset = 0, width = 0, height = 0;
		
		switch (name.substring(0, name.length() - 1)) {
			case ("sb"):
				width = 16;
				height = 16;
				switch (color) {
					case ("grey"):
						break;
					case ("red"):
						xOffset = -16;
						break;
					case ("pink"):
						xOffset = -48;
						break;
					case ("blue"):
						xOffset = -96;
						break;
					case ("teal"):
						yOffset = -16;
						break;
					case ("green"):
						yOffset = -16;
						xOffset = -16;
						break;
					case ("yellow"):
						yOffset = -16;
						xOffset = -64;
						break;
					case ("orange"):
						yOffset = -16;
						xOffset = -96;
						break;
				}
				break;
			case ("mb"):
				switch (Integer.parseInt(Character.toString(name.charAt(name.length() - 1)))) {
					case (1):
						width = 28;
						height = 30;
						break;
					case (2):
						yOffset = -30;
						width = 32;
						height = 32;
						break;
					case (3):
						yOffset = -62;
						width = 32;
						height = 32;
						break;
					case (4):
						yOffset = -96;
						width = 16;
						height = 28;
						break;
					case (5):
						yOffset = -126;
						width = 16;
						height = 32;
						break;
					case (6):
						yOffset = -158;
						width = 16;
						height = 28;
						break;
					case (7):
						yOffset = -190;
						width = 28;
						height = 32;
						break;
					case (8):
						yOffset = -222;
						width = 16;
						height = 32;
						break;
					case (9):
						yOffset = 254;
						width = 16;
						height = 32;
						break;
					case (10):
						yOffset = -286;
						width = 32;
						height = 30;
						break;
					case (11):
						yOffset = -318;
						width = 32;
						height = 32;
						break;
				}
				
				switch (color) {
					case ("grey"):
						break;
					case ("red"):
						xOffset = -64;
						break;
					case ("pink"):
						xOffset = -128;
						break;
					case ("blue"):
						xOffset = -192;
						break;
					case ("teal"):
						xOffset = -256;
						break;
					case ("green"):
						xOffset = -320;
						break;
					case ("yellow"):
						xOffset = -416;
						break;
					case ("orange"):
						xOffset = -448;
						break;
				}
				xOffset -= (32 - width) / 2;
				break;
			case ("bb"):
				switch (Integer.parseInt(Character.toString(name.charAt(name.length() - 1)))) {
					case (1):
						width = 64;
						height = 58;
						break;
					case (2):
						yOffset = -66;
						width = 60;
						height = 60;
						break;
					case (3):
						yOffset = -128;
						width = 64;
						height = 58;
						break;
					case (4):
						yOffset = -190;
						width = 26;
						height = 62;
						break;
					case (5):
						yOffset = -256;
						width = 28;
						height = 60;
						break;
					case (6):
						yOffset = -328;
						width = 44;
						height = 44;
						break;
				}
				
				switch (color) {
					case ("grey"):
						break;
					case ("red"):
						xOffset = -64;
						break;
					case ("pink"):
						xOffset = -128;
						break;
					case ("blue"):
						xOffset = -192;
						break;
					case ("teal"):
						xOffset = -256;
						break;
					case ("green"):
						xOffset = -320;
						break;
					case ("yellow"):
						xOffset = -384;
						break;
				}
				xOffset -= (64 - width) / 2;
				break;
			case ("hb"):
				width = 124;
				height = 124;
				switch (color) {
					case ("red"):
						break;
					case ("blue"):
						xOffset = -128;
						break;
					case ("green"):
						xOffset = -256;
						break;
					case ("yellow"):
						xOffset = -384;
						break;
				}
				break;
		}
		
		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bImage.getGraphics();
		
		g.drawImage(image, xOffset, yOffset, null);
		
		return bImage;
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
		
		for (EnemyProjectile p : deadEnemyBullets) {
			if (bulletMap.get(p.subBullet) != null)
				p.spawn();
			activeEnemyBullets.remove(p);
		}
		
		deadEnemyBullets.clear();
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
	
	public static void runScript() {
		// Runs game scripts
		
		if (activeScript.peekFirst() == null)
			if (scriptQueue.peekFirst() != null && activeEnemies.isEmpty())
				purgeScript();
			else
				return;
		
		// if(scriptQueue.peekFirst() == BOSSSCRIPT){}
		
		if (frameCount - lastSpawnFrame == activeScript.peekFirst().time) {
			
			Subscript current = activeScript.removeFirst();
			EnemyActive.create(current.enemy, current.origin, current.routine);
			lastSpawnFrame = frameCount;
		}
	}
	
	public static void update() {
		// Updates all aspects of the game
		
		Player.update();
		
		for (EnemyProjectile p : activeEnemyBullets)
			p.update();
		for (EnemyActive ea : activeEnemies)
			ea.update();
		for (PlayerProjectile pp : activePlayerBullets)
			pp.update();
		for (Pickup pu : activePickups)
			pu.update();
		
		if (deadEnemies.size() > 0)
			purgeEnemies();
		if (deadEnemyBullets.size() > 0)
			purgeBullets();
		if (deadPlayerBullets.size() > 0)
			PlayerProjectile.purgeAll();
		if (deadPickups.size() > 0)
			purgePickups();
		
		runScript();
	}
}