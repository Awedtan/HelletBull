import java.awt.*;
import java.awt.geom.*;

public class Player {
	
	final static double STARTSPEED = 3.0; // Default player speed
	final static int STARTPOSX = 100;
	final static int STARTPOSY = 550;
	
	static int playerWidth = 40; // Player model properties
	static int playerHeight = playerWidth;
	static Color playerColor = Color.BLUE;
	
	static int hitboxSize = 5; // Hitbox properties
	static Color hitboxColor = Color.YELLOW;
	
	static Ellipse2D.Double model = new Ellipse2D.Double(STARTPOSX, STARTPOSY, playerWidth, playerHeight); // Does not get hit by projectiles
	static Ellipse2D.Double hitbox = new Ellipse2D.Double(STARTPOSX + playerWidth / 2 - hitboxSize / 2, STARTPOSY + playerHeight / 2 - hitboxSize / 2, // Gets hit by projectiles
			hitboxSize, hitboxSize);
	
	static int lastShot = 0; // Frame of last player shot
	static int shotDelay = 10; // Player shot cooldown
	static int lastBomb = 0; // Frame of last player bomb
	static int bombDelay = 240; // Player bomb cooldown
	
	static double speed; // Actual player speed
	static boolean moveLeft; // Movement booleans
	static boolean moveRight;
	static boolean moveUp;
	static boolean moveDown;
	static boolean focus;
	
	public static void bomb() {
		// TODO: make this
	}
	
	public static boolean checkCollision(int bullet) {
		// Checks for bullet collision with the player
		// Returns true if collided
		
		Projectile blt = Game.activeBullets.get(bullet);
		
		if (Math.abs(blt.x - hitbox.x) < 30 && Math.abs(blt.y - hitbox.y) < 30)
			if (new Rectangle2D.Double(hitbox.x, hitbox.y, hitbox.width, hitbox.height)
					.intersects(new Rectangle2D.Double(blt.x, blt.y, blt.width, blt.height))) {
				
				Player.hit(bullet);
				return true;
			}
		
		return false;
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(playerColor);
		g2.fill(model);
		
		Image img = Toolkit.getDefaultToolkit().getImage("images/world-ship.png");
		g2.drawImage(img, (int) model.x, (int) model.y, null);
		
		g2.setColor(hitboxColor);
		g2.fill(hitbox);
	}
	
	public static void hit(int bullet) {
		// TODO: make this
		
		System.out.println("You got hit!");
	}
	
	public static void move() {
		// Updates player position
		
		if (focus)
			speed = (STARTSPEED * .5);
		else
			speed = STARTSPEED;
		
		if ((moveLeft || moveRight) && (moveUp || moveDown))
			speed *= 0.7;
		
		if (moveLeft && !moveRight) {
			model.x -= speed;
			hitbox.x -= speed;
		} else if (moveRight && !moveLeft) {
			model.x += speed;
			hitbox.x += speed;
		}
		
		if (moveUp && !moveDown) {
			model.y -= speed;
			hitbox.y -= speed;
		} else if (moveDown && !moveUp) {
			model.y += speed;
			hitbox.y += speed;
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
		// TODO: make this
	}
}