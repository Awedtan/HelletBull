import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Panel extends JPanel implements KeyListener, Runnable {
	
	static Line2D.Double[] grid = new Line2D.Double[] { new Line2D.Double(Maths.toWidth(1), 0, Maths.toWidth(1), Game.PLAYSCREEN.height),
			new Line2D.Double(Maths.toWidth(2), 0, Maths.toWidth(2), Game.PLAYSCREEN.height), new Line2D.Double(Maths.toWidth(3), 0, Maths.toWidth(3), Game.PLAYSCREEN.height),
			new Line2D.Double(Maths.toWidth(4), 0, Maths.toWidth(4), Game.PLAYSCREEN.height), new Line2D.Double(Maths.toWidth(5), 0, Maths.toWidth(5), Game.PLAYSCREEN.height),
			new Line2D.Double(Maths.toWidth(6), 0, Maths.toWidth(6), Game.PLAYSCREEN.height), new Line2D.Double(Maths.toWidth(7), 0, Maths.toWidth(7), Game.PLAYSCREEN.height),
			new Line2D.Double(Maths.toWidth(8), 0, Maths.toWidth(8), Game.PLAYSCREEN.height), new Line2D.Double(Maths.toWidth(9), 0, Maths.toWidth(9), Game.PLAYSCREEN.height),
			new Line2D.Double(0, 0, Game.PLAYSCREEN.width, 0), new Line2D.Double(0, Maths.toHeight(1), Game.PLAYSCREEN.width, Maths.toHeight(1)),
			new Line2D.Double(0, Maths.toHeight(2), Game.PLAYSCREEN.width, Maths.toHeight(2)), new Line2D.Double(0, Maths.toHeight(3), Game.PLAYSCREEN.width, Maths.toHeight(3)),
			new Line2D.Double(0, Maths.toHeight(4), Game.PLAYSCREEN.width, Maths.toHeight(4)), new Line2D.Double(0, Maths.toHeight(5), Game.PLAYSCREEN.width, Maths.toHeight(5)),
			new Line2D.Double(0, Maths.toHeight(6), Game.PLAYSCREEN.width, Maths.toHeight(6)), new Line2D.Double(0, Maths.toHeight(7), Game.PLAYSCREEN.width, Maths.toHeight(7)),
			new Line2D.Double(0, Maths.toHeight(8), Game.PLAYSCREEN.width, Maths.toHeight(8)), new Line2D.Double(0, Maths.toHeight(9), Game.PLAYSCREEN.width, Maths.toHeight(9)) };
	
	static JLabel scoreLabel = new JLabel() {
		{
			setBounds(1000, 200, 200, 50);
			setText(Integer.toString(Player.shotPower));
		}
	};
	
	public Panel() {
		
		MediaTracker tracker = new MediaTracker(this);
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
		
		setLayout(null);
		setPreferredSize(new Dimension(Game.SCREEN.width, Game.SCREEN.height));
		setBackground(Color.GRAY);
		setFocusable(true);
		addKeyListener(this);
		
		add(scoreLabel);
	}
	
	@Override
	public void run() {
		
		initializeImages();
		// initializeBosses();
		initializeBullets(); // Bullets are ellipses that move around and hit the player. Can spawn other bullets upon death
		initializeRoutines(); // Bullet routines tell individual enemies when to spawn bullets
		initializeEnemies(); // Enemies are ellipses that spawn bullets. Have built-in movement routines
		initializeScripts(); // Scripts control when and where enemies are spawned. Also specify the bullet routine for each enemy
		Controller.initialize();
		
		long time = System.currentTimeMillis();
		
		while (Game.run) { // Main game loop
			
			try {
				scoreLabel.setText(Integer.toString(Player.power));
				Game.update();
				Controller.update();
				this.repaint();
				
				Game.frameCount++;
				
				if (Game.frameCount % (Game.FPS * 2) == 0) {
					if (System.currentTimeMillis() - time > 2500)
						System.out.print("Slow! ");
					System.out.println(System.currentTimeMillis() - time + " ms/" + Game.FPS * 2 + " frames");
					time = System.currentTimeMillis();
				}
				
				Thread.sleep(1000 / (int) (Game.FPS * Game.gameSpeed));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		
		try {
			
			super.paintComponent(g);
			
			try {
				EnemyActive.drawAll(g);
				Player.draw(g);
				PlayerProjectile.drawAll(g);
				Pickup.drawAll(g);
				
				g.drawRect(Game.SIDESCREEN.x, Game.SIDESCREEN.y, Game.SIDESCREEN.width, Game.SIDESCREEN.height);
				
				for (Line2D.Double l : grid)
					g.drawLine((int) l.x1, (int) l.y1, (int) l.x2, (int) l.y2);
				
				EnemyProjectile.drawAll(g);
			} catch (ConcurrentModificationException e) {
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// public static void initializeBosses() {
	
	// try {
	
	// File folder = new File("data/bosses");
	
	// for (File f : folder.listFiles()) {
	
	// BufferedReader input = new BufferedReader(new FileReader(f));
	// String str = input.readLine();
	// String[] bossAccumulate = new String[3];
	// int count = 0;
	// Boss boss;
	
	// if (str.indexOf("boss") == 0)
	// while (str.indexOf(';') == -1) {
	// bossAccumulate[count] = str;
	// str = input.readLine();
	// count++;
	// }
	
	// str = input.readLine();
	
	// boss = Parser.parseBoss(bossAccumulate);
	
	// while (str.indexOf('?') == -1) {
	// ArrayList<String> accumulate = new ArrayList<String>();
	
	// if (str.indexOf("spell") == 0)
	// while (str.indexOf(';') == -1) {
	// if (str.length() > 1)
	// accumulate.add(str);
	// str = input.readLine();
	// }
	// else if (Game.scriptMap.containsKey(str))
	// Game.scriptQueue.addLast(Game.scriptMap.get(str).clone());
	
	// str = input.readLine();
	
	// if (accumulate.size() != 0)
	// boss.spellQueue.addLast(Parser.parseSpell(accumulate));
	// }
	
	// input.close();
	// }
	// } catch (FileNotFoundException e) {
	// System.out.println("The specified file was not found.");
	// } catch (IOException e) {
	// System.out.println("Something went wrong while reading a file.");
	// } catch (Exception e) {
	// System.out.println("Wow something went really wrong");
	// e.printStackTrace();
	// }
	// }
	
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
			}
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void initializeImages() {
		// TODO make this
		
		Player.hitboxImage = Game.imageMap.get("hitbox.png");
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
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_UP)
			Player.moveUp = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			Player.moveDown = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			Player.moveLeft = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			Player.moveRight = true;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			Player.focus = true;
		if (e.getKeyCode() == KeyEvent.VK_Z)
			Player.shoot = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_UP)
			Player.moveUp = false;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			Player.moveDown = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			Player.moveLeft = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			Player.moveRight = false;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			Player.focus = false;
		if (e.getKeyCode() == KeyEvent.VK_Z)
			Player.shoot = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}