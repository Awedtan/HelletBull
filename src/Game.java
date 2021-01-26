import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class Game {
	
	static HashMap<String, Image> imageMap = new HashMap<>();
	
	static HashMap<String, EnemyProjectile> bulletMap = new HashMap<>(); // Projectile types
	static ArrayList<EnemyProjectile> activeEnemyBullets = new ArrayList<>(); // Projectiles currently alive
	static ArrayList<EnemyProjectile> deadEnemyBullets = new ArrayList<>(); // Projectiles to be killed
	
	static ArrayList<PlayerProjectile> activePlayerBullets = new ArrayList<>(); // Projectiles currently alive
	static ArrayList<PlayerProjectile> deadPlayerBullets = new ArrayList<>(); // Projectiles to be killed
	
	static HashMap<String, ArrayDeque<Subroutine>> routineMap = new HashMap<>(); // Routine types
	
	static HashMap<String, Enemy> enemyMap = new HashMap<>(); // Enemy types
	static ArrayList<EnemyActive> activeEnemies = new ArrayList<>(); // Enemies currently alive
	static ArrayList<EnemyActive> deadEnemies = new ArrayList<>(); // Enemies to be killed
	
	static HashMap<String, ArrayDeque<Subscript>> scriptMap = new HashMap<>(); // Script types
	static ArrayDeque<ArrayDeque<Subscript>> scriptQueue = new ArrayDeque<>(); // Queue of scripts, scripts are queues of subscripts
	static ArrayDeque<Subscript> activeScript = new ArrayDeque<>(); // Current script
	
	static ArrayList<Pickup> activePickups = new ArrayList<>(); // Pickups currently alive
	static ArrayList<Pickup> deadPickups = new ArrayList<>(); // Pickups currently alive
	
	static HashMap<String, Integer> clipMap = new HashMap<>();
	static int clipDelay = 7;
	
	static final Rectangle SCREEN = new Rectangle(0, 0, 1280, 960);
	static final Rectangle PLAYSCREEN = new Rectangle(0, 0, 900, 960);
	static final Rectangle SIDESCREEN = new Rectangle(900, 0, 1280 - 900, 960);
	static final int GRIDLINES = 20;
	static final int FPS = 120;
	static double gameSpeed = 1.0;
	static int frameCount = 0;
	static int lastSpawnFrame = 0;
	static boolean run = true;
	
	public static void end() {
		
		Game.run = false;
		Main.showMenu();
		
		Mini name = new Mini("Enter your name");
		JLabel label = new JLabel("Enter your name:");
		JTextField field = new JTextField("Name");
		JButton button = new JButton("Confirm");
		
		name.add(label);
		name.add(field);
		name.add(button);
		
		label.setBounds(10, 0, 200, 30);
		field.setBounds(10, 50, 200, 30);
		button.setBounds(10, 100, 200, 30);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				try {
					
					FileWriter writer = new FileWriter("data/score.txt", true);
					
					writer.write('\n' + field.getText() + ", " + Player.score);
					writer.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				name.dispose();
			}
		});
	}
	
	public static boolean isEmpty() {
		
		return activeEnemyBullets.isEmpty() && activePlayerBullets.isEmpty() && activeEnemies.isEmpty() && activePickups.isEmpty();
	}
	
	public static void incrementClipDelay() {
		for (String s : clipMap.keySet())
			clipMap.put(s, clipMap.get(s) - 1);
	}
	
	public static void playClip(String s) {
		
		try {
			
			if (clipMap.containsKey(s)) {
				if (clipMap.get(s) < 0) {
					
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(new File("sounds/" + s + ".wav")));
					clip.start();
					
					FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(-30);
					
					clipMap.put(s, clipDelay);
				}
			} else {
				
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(new File("sounds/" + s + ".wav")));
				clip.start();
				
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-30);
				
				clipMap.put(s, clipDelay);
			}
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
	
	public static void purgePlayerBullets() {
		// Removes all bullets marked for deletion
		
		for (PlayerProjectile pp : Game.deadPlayerBullets)
			Game.activePlayerBullets.remove(pp);
		
		Game.deadPlayerBullets.clear();
	}
	
	public static void purgeEnemies() {
		// Removes all enemies marked for deletion
		
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
			else if (Game.isEmpty()) {
				Game.end();
				return;
			} else
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
			purgePlayerBullets();
		if (deadPickups.size() > 0)
			purgePickups();
		
		runScript();
		incrementClipDelay();
	}
	
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
					case ("orange"):
						xOffset = -16;
						break;
					case ("yellow"):
						xOffset = -48;
						break;
					case ("green"):
						xOffset = -96;
						break;
					case ("teal"):
						yOffset = -16;
						break;
					case ("blue"):
						yOffset = -16;
						xOffset = -16;
						break;
					case ("pink"):
						yOffset = -16;
						xOffset = -48;
						break;
					case ("red"):
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
						yOffset = -254;
						width = 16;
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
		Graphics2D g2 = (Graphics2D) bImage.getGraphics();
		
		g2.rotate(Math.PI, width / 2, height / 2);
		g2.drawImage(image, xOffset, yOffset, null);
		
		return bImage;
	}
}