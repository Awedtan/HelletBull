import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuPanel extends JPanel {
	// Panel that shows the main menu
	
	static Font font = new Font("Arial", Font.PLAIN, 28);
	
	static JLabel playLabel = new JLabel("Play ", SwingConstants.RIGHT) { // Starts the game
		{
			setFont(font);
			setBounds(1050, 250, 120, 50);
			setBackground(Color.BLACK);
			setForeground(Color.LIGHT_GRAY);
			
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
			setBounds(1050, 350, 120, 50);
			setBackground(Color.BLACK);
			setForeground(Color.LIGHT_GRAY);
			
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
			setBounds(1050, 450, 120, 50);
			setBackground(Color.BLACK);
			setForeground(Color.LIGHT_GRAY);
			
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
	static JLabel titleLabel = new JLabel("touhou 9.6 embroidery of google max") {
		{
			setFont(font);
			setForeground(Color.LIGHT_GRAY);
			setBounds(20, 400, 500, 50);
		}
	};
	
	public MenuPanel() {
		
		setLayout(null);
		setPreferredSize(new Dimension(Game.SCREEN.width, Game.SCREEN.height));
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		
		if (Game.currentSong == null)
			Game.playSong("title");
		
		add(titleLabel);
		add(playLabel);
		add(scoreLabel);
		add(exitLabel);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(Game.getImage("title"), 0, 0, this);
	}
}