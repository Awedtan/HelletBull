import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/* TODO
general
	level scripting
	verify everything before starting
	panel layout
	sfx
	music

projectiles
	fix grazing

player
	shot power
	sprites

enemies
	sprites
*/

public class Main {
	
	static boolean enableController = false;
	static boolean debug = false;
	
	static JFrame frame = new JFrame("Hellet Bull");
	static MenuPanel menuPanel = new MenuPanel();
	static GamePanel gamePanel;
	
	public static void runGame() {
		
		frame.dispose();
		
		frame = new JFrame("Hellet Bull");
		gamePanel = new GamePanel();
		
		frame.add(gamePanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws Exception {
		
		initializeImages();
		initializeBullets(); // Bullets are ellipses that move around and hit the player. Can spawn other bullets upon death
		initializeRoutines(); // Bullet routines tell individual enemies when to spawn bullets
		initializeEnemies(); // Enemies are ellipses that spawn bullets. Have built-in movement routines
		initializeScripts(); // Scripts control when and where enemies are spawned. Also specify the bullet routine for each enemy
		
		if (enableController)
			Controller.initialize();
		
		frame.add(menuPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void initializeBullets() {
		// Reads in data from bullet files
		// Collects and sends the data to be parsed
		
		try {
			
			File folder = new File("data/bullets");
			
			for (File f : folder.listFiles()) {
				
				BufferedReader input = new BufferedReader(new FileReader(f));
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
			}
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
			
			File folder = new File("data/enemies");
			
			for (File f : folder.listFiles()) {
				
				BufferedReader input = new BufferedReader(new FileReader(f));
				String str = input.readLine();
				
				while (str.indexOf('?') == -1) {
					String[] accumulate = new String[9];
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
			}
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void initializeImages() {
		// TODO make this
		
		MediaTracker tracker = new MediaTracker(menuPanel);
		File folder = new File("images");
		
		for (File f : folder.listFiles()) {
			
			Image img = Toolkit.getDefaultToolkit().getImage(f.toString());
			tracker.addImage(img, 0);
			Game.imageMap.put(f.getName(), img);
		}
		
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
		}
		
		Player.hitboxImage = Game.getImage("hitbox");
	}
	
	public static void initializeRoutines() {
		// Reads in data from routine files
		// Collects and sends the data to be parsed
		
		try {
			
			File folder = new File("data/routines");
			
			for (File f : folder.listFiles()) {
				
				BufferedReader input = new BufferedReader(new FileReader(f));
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
			}
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
			
			File folder = new File("data/scripts");
			
			for (File f : folder.listFiles()) {
				
				BufferedReader input = new BufferedReader(new FileReader(f));
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
					else if (Game.scriptMap.containsKey(str))
						Game.scriptQueue.addLast(Game.scriptMap.get(str).clone());
					// else if (Game.bossMap.containsKey(str))
					// Game.scriptQueue.addLast(Game.BOSSSCRIPT);
					
					str = input.readLine();
					
					if (accumulate.size() != 0)
						Parser.parseScript(accumulate);
				}
				input.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
}