import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener, Runnable {
	
	static Thread gameThread;
	
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
	
	public GamePanel() {
		
		gameThread = new Thread(this);
		gameThread.start();
		
		setLayout(null);
		setPreferredSize(new Dimension(Game.SCREEN.width, Game.SCREEN.height));
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		addKeyListener(this);
		
		add(scoreLabel);
	}
	
	@Override
	public void run() {
		
		System.out.println("Running game...");
		long time = System.currentTimeMillis();
		
		while (Game.run) { // Main game loop
			
			try {
				
				scoreLabel.setText(Integer.toString(Player.power));
				Game.update();
				
				if (Main.enableController)
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
			Graphics2D g2 = (Graphics2D) g;
			
			try {
				
				g2.setColor(Color.RED);
				for (EnemyActive ea : Game.activeEnemies) { // Enemies
					// g2.fill(ea);
					ea.draw(g2);
				}
				
				g2.setColor(new Color(255, 100, 100, 150));
				for (PlayerProjectile pp : Game.activePlayerBullets) { // Player bullets
					// g2.fill(pp);
					pp.draw(g2);
				}
				
				g2.setColor(Color.MAGENTA);
				for (Pickup pu : Game.activePickups) { // Pickups
					g2.fill(pu);
					// pu.draw(g2);
				}
				
				g2.setColor(Color.BLACK);
				for (Line2D.Double l : grid)
					g2.drawLine((int) l.x1, (int) l.y1, (int) l.x2, (int) l.y2);
				g2.drawRect(Game.SIDESCREEN.x, Game.SIDESCREEN.y, Game.SIDESCREEN.width, Game.SIDESCREEN.height);
				
				Player.draw(g2);
				
				for (EnemyProjectile p : Game.activeEnemyBullets) { // Enemy bullets
					p.draw(g2);
					// g2.setColor(Color.BLACK);
					// g2.fill(p);
					// g2.setColor(Color.BLUE);
					// g2.fill(p.getShape());
					// g2.setColor(Color.RED);
					// g2.draw(Maths.rotate(Maths.ellipseHitbox(p), p.radianAngle));
				}
				
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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