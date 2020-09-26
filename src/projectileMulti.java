import java.awt.geom.*;

public class projectileMulti {
	
	public static void create(String[] projArr, Ellipse2D.Double origin) {
		
		for (int i = 0; i < projArr.length; i++) {
			
			game.activeBullets.add(new projectileSingle(game.bulletTypes.get(projArr[i]), origin));
		}
	}
}