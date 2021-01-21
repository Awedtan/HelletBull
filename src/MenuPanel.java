import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuPanel extends JPanel implements KeyListener {
	
	static Font font = new Font("Arial", Font.PLAIN, 22);
	
	static JLabel playLabel = new JLabel("Play ", SwingConstants.RIGHT) {
		{
			setFont(font);
			setBounds(1000, 200, 100, 50);
			setBackground(Color.GRAY);
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Main.runGame();
				}
				
				public void mouseEntered(MouseEvent e) {
					setOpaque(true);
					repaint();
				}
				
				public void mouseExited(MouseEvent e) {
					setOpaque(false);
					repaint();
				}
			});
		}
	};
	static JLabel scoreLabel = new JLabel("Scores ", SwingConstants.RIGHT) {
		{
			setFont(font);
			setBounds(1000, 300, 100, 50);
			setBackground(Color.GRAY);
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
				}
				
				public void mouseEntered(MouseEvent e) {
					setOpaque(true);
					repaint();
				}
				
				public void mouseExited(MouseEvent e) {
					setOpaque(false);
					repaint();
				}
			});
		}
	};
	static JLabel exitLabel = new JLabel("Exit ", SwingConstants.RIGHT) {
		{
			setFont(font);
			setBounds(1000, 400, 100, 50);
			setBackground(Color.GRAY);
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					System.exit(0);
				}
				
				public void mouseEntered(MouseEvent e) {
					setOpaque(true);
					repaint();
				}
				
				public void mouseExited(MouseEvent e) {
					setOpaque(false);
					repaint();
				}
			});
		}
	};
	
	public MenuPanel() {
		
		setLayout(null);
		setPreferredSize(new Dimension(Game.SCREEN.width, Game.SCREEN.height));
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		addKeyListener(this);
		
		add(playLabel);
		add(scoreLabel);
		add(exitLabel);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
}
