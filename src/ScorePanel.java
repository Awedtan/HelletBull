import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

public class ScorePanel extends JPanel {
	// Panel for displaying high scores
	
	static Font font = new Font("Arial", Font.PLAIN, 22);
	
	static JLabel backLabel = new JLabel("Back ", SwingConstants.RIGHT) { // Goes back to menu panel
		{
			setFont(new Font("Arial", Font.PLAIN, 30));
			setBackground(Color.BLACK);
			setForeground(Color.LIGHT_GRAY);
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Main.showMenu();
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
	
	public ScorePanel() {
		
		setPreferredSize(new Dimension(Game.SCREEN.width, Game.SCREEN.height));
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(backLabel);
		
		ArrayList<Score> list = readScores();
		for (Score s : list) {
			add(new JLabel(s.toString()) {// Adds all scores to the panel
				{
					setFont(font);
					setForeground(Color.LIGHT_GRAY);
				}
			});
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(Game.getImage("title"), 0, 0, this);
	}
	
	public static ArrayList<Score> readScores() {
		// Reads in scores from save file
		// Sorts high to low
		// Returns a sorted list of scores
		
		Scanner input;
		ArrayList<Score> list = new ArrayList<>();
		
		try {
			input = new Scanner(new File("data/score.txt"));
			while (input.hasNextLine())
				list.add(new Score(input.nextLine()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Collections.sort(list);
		return list;
	}
}