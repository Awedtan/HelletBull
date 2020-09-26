import com.studiohartman.jamepad.*;

public class controller {
	
	static ControllerManager controllers = new ControllerManager();
	
	final static double[] MOVEANGLE = { 27.5, 62.5, 117.5, 152.5 };
	static double deadZone = 0.2;
	
	public static void initialize() {
		
		controllers.initSDLGamepad();
	}
	
	public static void update() {
		
		ControllerState state = controllers.getState(0);
		
		if (state.rightTrigger > 0) {
			
			if (game.frameCount % 100 == 0) {
				// projectileCircle.create("LCurveBlt", 16, player.hitbox);
			}
		}
		
		if (state.rb) {
			
			if (game.frameCount - player.lastBombed > player.bombDelay) {
				// projectileCircle.create("LCurveBlt", 48, player.hitbox);
				player.lastBombed = game.frameCount;
			}
		}
		
		if (state.leftTrigger > 0)
			player.focus = true;
		else
			player.focus = false;
		
		if (state.leftStickMagnitude > deadZone) {
			
			double angle = state.leftStickAngle;
			double abs = Math.abs(angle);
			
			if (abs < MOVEANGLE[0])
				player.setMove(90);
			
			else if (abs > MOVEANGLE[3])
				player.setMove(-90);
			
			else if (abs > MOVEANGLE[0] && abs < MOVEANGLE[1]) {
				if (angle > 0)
					player.setMove(135);
				else
					player.setMove(45);
			} else if (abs > MOVEANGLE[1] && abs < MOVEANGLE[2])
				if (angle > 0)
					player.setMove(180);
				else
					player.setMove(0);
				
			else if (abs > 112.5 && abs < MOVEANGLE[3])
				if (angle > 0)
					player.setMove(-135);
				else
					player.setMove(-45);
				
		} else
			player.setMove(-1);
	}
}