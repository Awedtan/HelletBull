import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuPanel extends JPanel {
	// Panel that shows the main menu
	
	static Font font = new Font("Arial", Font.PLAIN, 22);
	
	static JLabel playLabel = new JLabel("Play ", SwingConstants.RIGHT) { // Starts the game
		{
			setFont(font);
			setBounds(1000, 200, 100, 50);
			setBackground(Color.GRAY);
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Main.showGame();
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
	static JLabel scoreLabel = new JLabel("Scores ", SwingConstants.RIGHT) { // Shows the score panel
		{
			setFont(font);
			setBounds(1000, 300, 100, 50);
			setBackground(Color.GRAY);
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Main.showScores();
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
	static JLabel exitLabel = new JLabel("Exit ", SwingConstants.RIGHT) { // Quits the program
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
		
		add(playLabel);
		add(scoreLabel);
		add(exitLabel);
	}
}