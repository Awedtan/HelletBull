import java.awt.*;
import java.awt.geom.*;

public class player {
	
	final static double STARTSPEED = 3.0;
	final static int STARTPOSX = 100;
	final static int STARTPOSY = 550;
	
	static int width = 40;
	static int height = width;
	static Color color = Color.BLUE;
	static int hbSize = 5;
	static Color hbColor = Color.YELLOW;
	
	static Ellipse2D.Double model = new Ellipse2D.Double(STARTPOSX, STARTPOSY, width, height);
	static Ellipse2D.Double hitbox = new Ellipse2D.Double(STARTPOSX + width / 2 - hbSize / 2, STARTPOSY + height / 2 - hbSize / 2, hbSize, hbSize);
	
	static int lastShot = 0;
	static int shotDelay = 10;
	static int lastBombed = 0;
	static int bombDelay = 240;
	
	static double speed = STARTSPEED;
	static boolean moveLeft;
	static boolean moveRight;
	static boolean moveUp;
	static boolean moveDown;
	static boolean focus;
	
	public static void bomb() {
		
	}
	
	public static boolean checkCollision(int bullet) {
		
		projectile blt = game.activeBullets.get(bullet);
		
		if (Math.abs(blt.x - hitbox.x) < 30 && Math.abs(blt.y - hitbox.y) < 30)
			if (new Rectangle2D.Double(hitbox.x, hitbox.y, hitbox.width, hitbox.height)
					.intersects(new Rectangle2D.Double(blt.x, blt.y, blt.width, blt.height))) {
				
				player.hit(bullet);
				return true;
			}
		
		return false;
	}
	
	public static void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(color);
		g2.fill(model);
		
		Image img = Toolkit.getDefaultToolkit().getImage("images/world-ship.png");
		g2.drawImage(img, (int) model.x, (int) model.y, null);
		
		g2.setColor(hbColor);
		g2.fill(hitbox);
	}
	
	public static void hit(int bullet) {
		
		System.out.println("You got hit!");
	}
	
	public static void move() {
		
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
		
	}
}