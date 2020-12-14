import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
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
		
		setLayout(null);
		setPreferredSize(new Dimension(Game.SCREEN.width, Game.SCREEN.height));
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);
		
		add(scoreLabel);
	}
	
	@Override
	public void run() {
		
		Game.initializeBullets(); // Bullets are ellipses that move around and hit the player. Can spawn other bullets upon death
		Game.initializeRoutines(); // Bullet routines tell individual enemies when to spawn bullets
		Game.initializeEnemies(); // Enemies are ellipses that spawn bullets. Have built-in movement routines
		Game.initializeScripts(); // Scripts control when and where enemies are spawned. Also specify the bullet routine for each enemy
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
			
			EnemyActive.drawAll(g);
			Player.draw(g);
			PlayerProjectile.drawAll(g);
			Pickup.drawAll(g);
			EnemyProjectile.drawAll(g);
			
			g.drawRect(Game.SIDESCREEN.x, Game.SIDESCREEN.y, Game.SIDESCREEN.width, Game.SIDESCREEN.height);
			
			for (Line2D.Double l : grid)
				g.drawLine((int) l.x1, (int) l.y1, (int) l.x2, (int) l.y2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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