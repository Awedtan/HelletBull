import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

public class ScorePanel extends JPanel {
	
	static Font font = new Font("Arial", Font.PLAIN, 22);
	
	static JLabel backLabel = new JLabel("Back ", SwingConstants.RIGHT) {
		{
			setFont(font);
			setBackground(Color.GRAY);
			
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
			add(new JLabel(s.toString()));
		}
	}
	
	public static ArrayList<Score> readScores() {
		
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