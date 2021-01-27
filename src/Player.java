import java.awt.*;
import java.awt.geom.*;

public class Player {
	
	static final double STARTSPEED = 5.0; // Base player speed
	static final int STARTPOSX = Game.PLAYSCREEN.width / 2;
	static final int STARTPOSY = Game.PLAYSCREEN.height / 3 * 2;
	
	static int playerWidth = 60; // Player model properties
	static int playerHeight = 60;
	static int hitboxSize = 6; // Hitbox properties
	static int grazeRadius = 30;
	
	static Ellipse2D.Double model = new Ellipse2D.Double(STARTPOSX, STARTPOSY, playerWidth, playerHeight); // Just some ellipse to represent the player
	static Rectangle2D.Double hitboxModel = new Rectangle2D.Double(STARTPOSX + playerWidth / 2 - hitboxSize / 2, STARTPOSY + playerHeight / 2 - hitboxSize / 2, // Ellipse that gets hit by bullets
			hitboxSize, hitboxSize);
	
	static Image sprite;
	
	static int score = 0;
	
	static final int SHOTDELAY = 6; // Player shot cooldown
	static final int BOMBDELAY = 1200; // Player bomb cooldown
	static int lastShot = 0; // Frame of last player shot
	static long lastBomb = 0; // Frame of last player bomb
	
	static double speed; // Current player speed
	static boolean moveLeft; // Movement booleans
	static boolean moveRight;
	static boolean moveUp;
	static boolean moveDown;
	static boolean focus; // Action booleans
	static boolean shoot;
	
	static String deathClip = "playerdeath";
	static String shotClip = "playershot";
	static String bombClip = "bomb";
	
	public static void addScore(int value) {
		// Adds the value to the player score
		
		score += value;
	}
	
	public static void bomb() {
		// Clears all bullets on screen
		// Can be used once every 10 seconds, has a 30 point cost
		
		score -= 30;
		Game.activeEnemyBullets.clear();
		Game.playClip(bombClip);
		
		lastBomb = Game.globalFrameCount;
	}
	
	public static void draw(Graphics2D g2) {
		// Draws the player sprite
		// Also draws hitbox and model if needed
		
		if (Main.debug) {
			g2.setColor(Color.RED);
			g2.fill(model);
		}
		
		g2.drawImage(sprite, (int) (model.x - ((sprite.getWidth(null) - model.width) / 2)), (int) (model.y - ((sprite.getHeight(null) - model.height) / 2)) + 2, null);
		
		if (Main.debug) {
			g2.setColor(Color.MAGENTA);
			g2.fill(hitboxModel);
		}
		
	}
	
	public static void hit() {
		// If the player gets hit, subtract 100 points
		// Infinite lives, negative points are possible and almost a certainty
		
		score -= 100;
		Game.playClip(deathClip);
	}
	
	public static void move() {
		// Updates player position
		
		double mx = model.x;
		double my = model.y;
		double hx = hitboxModel.x;
		double hy = hitboxModel.y;
		
		if (focus)
			speed = (STARTSPEED * .5); // Slows the player
		else
			speed = STARTSPEED;
		
		if ((moveLeft || moveRight) && (moveUp || moveDown)) // Diagonal movement speed
			speed *= 0.7;
		
		if (moveLeft && !moveRight) {
			model.x -= speed;
			hitboxModel.x -= speed;
		} else if (moveRight && !moveLeft) {
			model.x += speed;
			hitboxModel.x += speed;
		}
		
		if (moveUp && !moveDown) {
			model.y -= speed;
			hitboxModel.y -= speed;
		} else if (moveDown && !moveUp) {
			model.y += speed;
			hitboxModel.y += speed;
		}
		
		switch (Maths.checkInBounds(model.getBounds(), 0)) { // Border collision check
			case (0):
				model.x = mx;
				hitboxModel.x = hx;
				break;
			case (1):
				model.y = my;
				hitboxModel.y = hy;
				break;
			case (2):
				model.x = mx;
				hitboxModel.x = hx;
				model.y = my;
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
		// Shoots bullets
		
		PlayerProjectile.create();
		lastShot = Game.frameCount;
		
		Game.playClip(shotClip);
	}
	
	public static void update() {
		// Main update method
		
		move();
		if (shoot && Game.frameCount - lastShot > SHOTDELAY)
			shoot();
	}
}