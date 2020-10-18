import com.studiohartman.jamepad.*;

public class controller {
	
	static ControllerManager controllers = new ControllerManager();
	
	final static double[] MOVEANGLES = { 27.5, 62.5, 117.5, 152.5 };
	final static double DEADZONE = 0.4;
	
	public static void initialize() {
		
		controllers.initSDLGamepad();
	}
	
	public static void update() {
		
		ControllerState state = controllers.getState(0);
		
		if (!state.isConnected)
			return;
		
		if (state.rightTrigger > 0)
			if (game.frameCount - player.lastShot > player.shotDelay) {
				player.shoot();
				player.lastShot = game.frameCount;
			}
		
		if (state.rb)
			if (game.frameCount - player.lastBombed > player.bombDelay) {
				player.bomb();
				player.lastBombed = game.frameCount;
			}
		
		if (state.leftTrigger > 0)
			player.focus = true;
		else
			player.focus = false;
		
		if (state.leftStickMagnitude > DEADZONE) {
			
			double angle = state.leftStickAngle;
			double abs = Math.abs(angle);
			
			if (abs < MOVEANGLES[0])
				player.setMove(90);
			
			else if (abs > MOVEANGLES[3])
				player.setMove(-90);
			
			else if (abs > MOVEANGLES[0] && abs < MOVEANGLES[1]) {
				if (angle > 0)
					player.setMove(135);
				else
					player.setMove(45);
			} else if (abs > MOVEANGLES[1] && abs < MOVEANGLES[2])
				if (angle > 0)
					player.setMove(180);
				else
					player.setMove(0);
				
			else if (abs > 112.5 && abs < MOVEANGLES[3])
				if (angle > 0)
					player.setMove(-135);
				else
					player.setMove(-45);
		} else
			player.setMove(-1);
	}
}