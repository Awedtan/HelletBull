import java.awt.*;
import java.awt.geom.*;

public class Player {
	
	static final double STARTSPEED = 4.0; // Base player speed
	static final int STARTPOSX = Game.PLAYSCREEN.width / 2;
	static final int STARTPOSY = Game.PLAYSCREEN.height / 3 * 2;
	
	static int playerWidth = 120; // Player model properties
	static int playerHeight = 120;
	
	static int hitboxSize = 4; // Hitbox properties
	
	static Ellipse2D.Double grazeModel = new Ellipse2D.Double(STARTPOSX, STARTPOSY, playerWidth, playerHeight); // Does not get hit by projectiles
	static Rectangle2D.Double hitboxModel = new Rectangle2D.Double(STARTPOSX + playerWidth / 2 - hitboxSize / 2, STARTPOSY + playerHeight / 2 - hitboxSize / 2, // Gets hit by projectiles
			hitboxSize, hitboxSize);
	
	static Image hitboxImage;
	
	static final int MAXPOWER = 10;
	static int points = 0;
	static int power = 0;
	static int shotPower = 0;
	
	static final int SHOTDELAY = 7; // Player shot cooldown
	static final int BOMBDELAY = 240; // Player bomb cooldown
	static int lastShot = 0; // Frame of last player shot
	static int lastBomb = 0; // Frame of last player bomb
	
	static double speed; // Actual player speed
	static boolean moveLeft; // Movement booleans
	static boolean moveRight;
	static boolean moveUp;
	static boolean moveDown;
	static boolean focus;
	static boolean shoot;
	
	static String pickupClip = "pickup";
	static String deathClip = "playerdeath";
	static String shotClip = "playershot";
	
	public static void addScore(int value) {
		
		power += value;
		shotPower = Maths.log(power / 2);
		
		Game.playClip(pickupClip);
	}
	
	public static void bomb() {
		// TODO: make this
		
		lastBomb = Game.frameCount;
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLUE);
		g2.fill(grazeModel);
		
		g2.setColor(Color.YELLOW);
		g2.fill(hitboxModel);
		
		g2.drawImage(hitboxImage, (int) grazeModel.x, (int) grazeModel.y, null);
	}
	
	public static void hit() {
		// TODO: make this
		
		Game.playClip(deathClip);
	}
	
	public static void move() {
		// Updates player position
		
		double mx = grazeModel.x;
		double my = grazeModel.y;
		double hx = hitboxModel.x;
		double hy = hitboxModel.y;
		
		if (focus)
			speed = (STARTSPEED * .5);
		else
			speed = STARTSPEED;
		
		if ((moveLeft || moveRight) && (moveUp || moveDown))
			speed *= 0.7;
		
		if (moveLeft && !moveRight) {
			grazeModel.x -= speed;
			hitboxModel.x -= speed;
		} else if (moveRight && !moveLeft) {
			grazeModel.x += speed;
			hitboxModel.x += speed;
		}
		
		if (moveUp && !moveDown) {
			grazeModel.y -= speed;
			hitboxModel.y -= speed;
		} else if (moveDown && !moveUp) {
			grazeModel.y += speed;
			hitboxModel.y += speed;
		}
		
		switch (Maths.checkInBounds(Player.grazeModel.getBounds(), 0)) {
			case (0):
				grazeModel.x = mx;
				hitboxModel.x = hx;
				break;
			case (1):
				grazeModel.y = my;
				hitboxModel.y = hy;
				break;
			case (2):
				grazeModel.x = mx;
				hitboxModel.x = hx;
				grazeModel.y = my;
				hitboxModel.y = hy;
				break;
		}
	}
	
	public static void setMove(int direction) {
		// Controls movement booleans if using a controller
		// Doesn't do anything if using keyboard
		
		switch (direction) {
			case (-1):
				moveLeft = false;
				moveRight = false;
				moveUp = false;
				moveDown = false;
				break;
			
			case (0):
				moveLeft = false;
				moveRight = false;
				moveUp = false;
				moveDown = true;
				break;
			
			case (45):
				moveLeft = false;
				moveRight = true;
				moveUp = false;
				moveDown = true;
				break;
			
			case (90):
				moveLeft = false;
				moveRight = true;
				moveUp = false;
				moveDown = false;
				break;
			
			case (135):
				moveLeft = false;
				moveRight = true;
				moveUp = true;
				moveDown = false;
				break;
			
			case (180):
				moveLeft = false;
				moveRight = false;
				moveUp = true;
				moveDown = false;
				break;
			
			case (-135):
				moveLeft = true;
				moveRight = false;
				moveUp = true;
				moveDown = false;
				break;
			
			case (-90):
				moveLeft = true;
				moveRight = false;
				moveUp = false;
				moveDown = false;
				break;
			
			case (-45):
				moveLeft = true;
				moveRight = false;
				moveUp = false;
				moveDown = true;
				break;
		}
	}
	
	public static void shoot() {
		
		PlayerProjectile.create(shotPower);
		lastShot = Game.frameCount;
		
		Game.playClip(shotClip);
	}
	
	public static void update() {
		
		move();
		if (shoot && Game.frameCount - lastShot > SHOTDELAY)
			shoot();
	}
}