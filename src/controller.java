import com.studiohartman.jamepad.*;

public class Controller {
	
	static ControllerManager controllers = new ControllerManager();
	
	final static double[] MOVEANGLES = { 27.5, 62.5, 117.5, 152.5 };
	final static double DEADZONE = 0.4;
	
	public static void initialize() {
		
		controllers.initSDLGamepad();
	}
	
	public static void update() {
		// Updates the controller state and does stuff accordingly
		
		ControllerState state = controllers.getState(0); // State of first controller
		
		if (!state.isConnected)
			return;
		
		if (state.rightTrigger > 0)
			if (Game.frameCount - Player.lastShot > Player.shotDelay) { // Player shooting
				Player.shoot();
				Player.lastShot = Game.frameCount;
			}
		
		if (state.rb)
			if (Game.frameCount - Player.lastBomb > Player.bombDelay) { // Player bombing
				Player.bomb();
				Player.lastBomb = Game.frameCount;
			}
		
		if (state.leftTrigger > 0) // Player focusing
			Player.focus = true;
		else
			Player.focus = false;
		
		if (state.leftStickMagnitude > DEADZONE) { // Player movement
			
			double angle = state.leftStickAngle;
			double abs = Math.abs(angle);
			
			if (abs < MOVEANGLES[0])
				Player.setMove(90);
			
			else if (abs > MOVEANGLES[3])
				Player.setMove(-90);
			
			else if (abs > MOVEANGLES[0] && abs < MOVEANGLES[1]) {
				if (angle > 0)
					Player.setMove(135);
				else
					Player.setMove(45);
			} else if (abs > MOVEANGLES[1] && abs < MOVEANGLES[2])
				if (angle > 0)
					Player.setMove(180);
				else
					Player.setMove(0);
				
			else if (abs > 112.5 && abs < MOVEANGLES[3])
				if (angle > 0)
					Player.setMove(-135);
				else
					Player.setMove(-45);
		} else
			Player.setMove(-1);
	}
}