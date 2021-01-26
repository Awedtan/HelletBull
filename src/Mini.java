import java.awt.*;
import javax.swing.*;

public class Mini extends JFrame {
	// A small preset jframe
	
	public Mini(String title) {
		
		setTitle(title);
		setPreferredSize(new Dimension(300, 200));
		setBackground(new Color(45, 42, 46));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFocusable(true);
		setResizable(false);
		setVisible(true);
		pack();
	}
	
	public Mini(String title, int width, int height) {
		
		setTitle(title);
		setPreferredSize(new Dimension(width, height));
		setBackground(new Color(45, 42, 46));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFocusable(true);
		setResizable(false);
		setVisible(true);
		pack();
	}
}