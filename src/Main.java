import javax.swing.*;

/* TODO
general
	level scripting
	verify everything before starting
	panel layout
	sfx
	music

projectiles
	fix grazing
	sprites

player
	shot power
	sprites

enemies
	speed
	sprites

bosses
	create
*/

public class Main {
	
	static JFrame frame = new JFrame("Hellet Bull");
	static Panel panel = new Panel();
	
	public static void main(String[] args) throws Exception {
		
		// frame.setUndecorated(true);
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setVisible(true);
		panel.run();
	}
}