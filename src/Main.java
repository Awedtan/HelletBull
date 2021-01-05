import javax.swing.*;

/* TODO
general
	level scripting
	routine scripting (0r0r)
	verify script before starting
	panel layout
	music
	FIX BULLET ROUTINES TO BEHAVE LIKE SCRIPTS FOR THEIR TIMING

projectiles
	ROTATING STUFF(MAKE EVERYTHING AN OCTAGON???)
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