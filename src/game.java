import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

public class game {
	
	final static int FPS = 120;
	final static int SCREENWIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth());
	final static int SCREENHEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight());
	
	static ArrayList<projectileSingle> activeBullets = new ArrayList<projectileSingle>();
	static HashMap<String, projectile> bulletTypes = new HashMap<String, projectile>();
	
	static double GAMESPEED = 1.0;
	static int frameCount = 0;
	static boolean run = true;
	
	public static double getAngle(Ellipse2D.Double origin, Ellipse2D.Double target) {
		
		double angle;
		
		if (target.x > origin.x && target.y > origin.y) { // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x < origin.x && target.y > origin.y) { // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x < origin.x && target.y < origin.y) { // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x > origin.x && target.y < origin.y) { // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(target.x - origin.x) / Math.abs(target.y - origin.y)));
			
		} else if (target.x == origin.x)
			if (target.y > origin.y)
				angle = 0;
			else
				angle = 180;
		else if (target.y == origin.y)
			if (target.x > origin.x)
				angle = 90;
			else
				angle = -90;
		else
			angle = 0;
		
		return angle;
	}
	
	public static double getX(Ellipse2D.Double ellipse) {
		
		return ellipse.x + ellipse.width / 2;
	}
	
	public static double getY(Ellipse2D.Double ellipse) {
		
		return ellipse.y + ellipse.height / 2;
	}
	
	public static double getAngle(double originX, double originY, double targetX, double targetY) {
		
		double angle;
		
		if (targetX > originX && targetY > originY) { // Quadrant 4
			angle = Math.toDegrees(Math.atan(Math.abs(targetX - originX) / Math.abs(targetY - originY)));
			
		} else if (targetX < originX && targetY > originY) { // Quadrant 3
			angle = -Math.toDegrees(Math.atan(Math.abs(targetX - originX) / Math.abs(targetY - originY)));
			
		} else if (targetX < originX && targetY < originY) { // Quadrant 2
			angle = -180 + Math.toDegrees(Math.atan(Math.abs(targetX - originX) / Math.abs(targetY - originY)));
			
		} else if (targetX > originX && targetY < originY) { // Quadrant 1
			angle = 180 - Math.toDegrees(Math.atan(Math.abs(targetX - originX) / Math.abs(targetY - originY)));
			
		} else if (targetX == originX)
			if (targetY > originY)
				angle = 0;
			else
				angle = 180;
		else if (targetY == originY)
			if (targetX > originX)
				angle = 90;
			else
				angle = -90;
		else
			angle = 0;
		
		return angle;
	}
	
	public static void createBullets() {
		
		try {
			
			BufferedReader input = new BufferedReader(new FileReader("bulletTypes.txt"));
			String str = input.readLine();
			
			while (str.indexOf('?') == -1) {
				String[] accumulate = new String[13];
				int count = 0;
				
				if (str.indexOf("bullet") == 0)
					while (str.indexOf(';') == -1) {
						accumulate[count] = str;
						str = input.readLine();
						count++;
					}
				
				str = input.readLine();
				if (accumulate[0] != null)
					parseBullet(accumulate);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("The specified file was not found.");
		} catch (IOException e) {
			System.out.println("Something went wrong while reading a file.");
		}
	}
	
	public static void parseBullet(String[] arr) {
		
		String nameLocal = arr[0].split(" ")[1];
		String spriteLocal = "";
		int inaccuracyLocal = 0;
		double angleLocal = 0;
		double turnLocal = 0;
		boolean aimedLocal = false;
		double velocityLocal = 1;
		double accelerationLocal = 0;
		int homingLocal = 0;
		int lifetimeLocal = 120;
		int widthLocal = 10;
		int heightLocal = 10;
		String secondaryLocal = "";
		
		for (int i = 1; i < arr.length; i++) {
			String variable = arr[i].split("\t")[1];
			String value = arr[i].split("\t")[arr[i].split("\t").length - 1];
			
			try {
				switch (variable) {
				case "sprite":
					spriteLocal = value;
					break;
				case "inaccuracy":
					inaccuracyLocal = Integer.parseInt(value);
					break;
				case "angle":
					angleLocal = java.lang.Double.parseDouble(value);
					break;
				case "turn":
					turnLocal = java.lang.Double.parseDouble(value);
					break;
				case "aimed":
					aimedLocal = Boolean.parseBoolean(value);
					break;
				case "velocity":
					velocityLocal = java.lang.Double.parseDouble(value);
					break;
				case "acceleration":
					accelerationLocal = java.lang.Double.parseDouble(value);
					break;
				case "homing":
					homingLocal = Integer.parseInt(value);
					break;
				case "lifetime":
					lifetimeLocal = Integer.parseInt(value);
					break;
				case "width":
					widthLocal = Integer.parseInt(value);
					break;
				case "height":
					heightLocal = Integer.parseInt(value);
					break;
				case "secondary":
					secondaryLocal = value;
					break;
				}
			} catch (Exception e) {
				System.out.printf("WARN: An error occured when creating the field %s for bullet %s%n", variable, nameLocal);
			}
		}
		
		bulletTypes.put(nameLocal, new projectile(spriteLocal, inaccuracyLocal, angleLocal, turnLocal, aimedLocal, velocityLocal, accelerationLocal,
				homingLocal, lifetimeLocal, new Dimension(widthLocal, heightLocal), secondaryLocal));
	}
}
