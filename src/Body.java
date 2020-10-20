import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Body extends JPanel implements KeyListener, Runnable {
	
	private static JFrame frame;
	private static Body panel = new Body();
	
	public Body() {
		
		setLayout(null);
		setPreferredSize(new Dimension(Game.SCREENWIDTH, Game.SCREENHEIGHT));
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);
	}
	
	@Override
	public void run() {
		PlayerProjectile.initialize();
		Game.initializeBullets(); // Bullets are ellipses that move around and hit the player. Can spawn other bullets upon death
		Game.initializeRoutines(); // Bullet routines tell individual enemies when to spawn bullets
		Game.initializeEnemies(); // Enemies are ellipses that spawn bullets. Have built-in movement routines
		Game.initializeScripts(); // Scripts control when and where enemies are spawned. Also specify the bullet routine for each enemy
		Controller.initialize();
		
		long time = System.currentTimeMillis();
		
		while (Game.run) { // Main game loop
			
			Game.update();
			Controller.update();
			this.repaint();
			
			Game.frameCount++;
			
			if (Game.frameCount % 120 == 0){
				System.out.println(System.currentTimeMillis() - time + "ms/120fps");
				time = System.currentTimeMillis();
			}
				
			try {
				Thread.sleep(1000 / (int) (Game.FPS * Game.gameSpeed));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		
		try {
			
			super.paintComponent(g);
			
			Player.draw(g);
			Projectile.draw(g);
			EnemyActive.draw(g);
			PlayerProjectile.draw(g);
			
			TestDummy.draw(g);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		frame = new JFrame("Hellet Bull");
		frame.setUndecorated(true);
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setVisible(true);
		panel.run();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_A)
			Player.moveLeft = true;
		if (e.getKeyCode() == KeyEvent.VK_D)
			Player.moveRight = true;
		if (e.getKeyCode() == KeyEvent.VK_W)
			Player.moveUp = true;
		if (e.getKeyCode() == KeyEvent.VK_S)
			Player.moveDown = true;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			Player.focus = true;
		if (e.getKeyCode() == KeyEvent.VK_Z)
			Player.shoot = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_A)
			Player.moveLeft = false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			Player.moveRight = false;
		if (e.getKeyCode() == KeyEvent.VK_W)
			Player.moveUp = false;
		if (e.getKeyCode() == KeyEvent.VK_S)
			Player.moveDown = false;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			Player.focus = false;
		if (e.getKeyCode() == KeyEvent.VK_Z)
			Player.shoot = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
