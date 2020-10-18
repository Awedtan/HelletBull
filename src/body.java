import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class body extends JPanel implements KeyListener, Runnable {
	
	static JFrame frame;
	static body panel = new body();
	
	public body() {
		
		setLayout(null);
		setPreferredSize(new Dimension(game.SCREENWIDTH, game.SCREENHEIGHT));
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);
	}
	
	@Override
	public void run() {
		
		game.createBullets();
		game.createRoutines();
		game.createEnemies();
		game.createScripts();
		controller.initialize();
		
		while (game.run) {
			
			update();
			controller.update();
			this.repaint();
			
			game.frameCount++;
			
			try {
				Thread.sleep(1000 / (int) (game.FPS * game.gameSpeed));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
		
		player.move();
		
		for (int i = 0; i < game.activeBullets.size(); i++)
			projectile.update(i);
		
		for (int i = 0; i < game.activeBullets.size(); i++)
			if (game.activeBullets.get(i).lifetime == 0 || !projectile.checkInBounds(i) || player.checkCollision(i)) {
				projectile.kill(i);
				i--;
			}
		
		for (int i = 0; i < game.activeEnemies.size(); i++)
			enemy.update(i);
		
		game.script();
	}
	
	public void paintComponent(Graphics g) {
		
		try {
			
			super.paintComponent(g);
			
			player.draw(g);
			projectile.draw(g);
			enemy.draw(g);
			
			testDummy.draw(g);
			
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
			player.moveLeft = true;
		
		else if (e.getKeyCode() == KeyEvent.VK_D)
			player.moveRight = true;
		
		else if (e.getKeyCode() == KeyEvent.VK_W)
			player.moveUp = true;
		
		else if (e.getKeyCode() == KeyEvent.VK_S)
			player.moveDown = true;
		
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			player.focus = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_A)
			player.moveLeft = false;
		
		else if (e.getKeyCode() == KeyEvent.VK_D)
			player.moveRight = false;
		
		else if (e.getKeyCode() == KeyEvent.VK_W)
			player.moveUp = false;
		
		else if (e.getKeyCode() == KeyEvent.VK_S)
			player.moveDown = false;
		
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			player.focus = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}