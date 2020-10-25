import java.awt.*;
import java.awt.geom.*;

public class Player {
	
	private static final double STARTSPEED = 3.0; // Default player speed
	private static final int STARTPOSX = Game.SCREENWIDTH / 2;
	private static final int STARTPOSY = Game.SCREENHEIGHT / 2;
	
	private static int playerWidth = 40; // Player model properties
	private static int playerHeight = playerWidth;
	private static Color playerColor = Color.BLUE;
	
	private static int hitboxSize = 5; // Hitbox properties
	private static Color hitboxColor = Color.YELLOW;
	
	static Ellipse2D.Double model = new Ellipse2D.Double(STARTPOSX, STARTPOSY, playerWidth, playerHeight); // Does not get hit by projectiles
	static Ellipse2D.Double hitbox = new Ellipse2D.Double(STARTPOSX + playerWidth / 2 - hitboxSize / 2, STARTPOSY + playerHeight / 2 - hitboxSize / 2, // Gets hit by projectiles
			hitboxSize, hitboxSize);
	
	static int points = 0;
	static int power = 0;
	static int shotPower = 3;
	static final int MAXPOWER = 10;
	
	static int lastShot = 0; // Frame of last player shot
	static final int SHOTDELAY = 10; // Player shot cooldown
	static int lastBomb = 0; // Frame of last player bomb
	static final int BOMBDELAY = 240; // Player bomb cooldown
	
	static double speed; // Actual player speed
	static boolean moveLeft; // Movement booleans
	static boolean moveRight;
	static boolean moveUp;
	static boolean moveDown;
	static boolean focus;
	
	static boolean shoot;
	
	public static void bomb() {
		// TODO: make this
		
		lastBomb = Game.frameCount;
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
	
	public static void hit() {
		// TODO: make this
		
		// System.out.println("You got hit!");
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
		
		PlayerProjectile.create("test", shotPower);
		lastShot = Game.frameCount;
	}
	
	public static void update() {
		
		move();
		if (shoot && Game.frameCount - lastShot > SHOTDELAY)
			shoot();
	}
}
