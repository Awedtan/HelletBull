import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Parser {
	// Class of methods that read through collected information and create objects based on them
	// Nothing really important here, just a lot of string manipulation
	
	public static void parseBullet(String[] arr) {
		// Parses through bullet data
		// Creates a new bullet type with the given attributes
		// Adds the bullet type to the global bullet map
		
		String name = arr[0].split(" ")[1];
		String sprite = "zy";
		int inaccuracy = 0;
		double angle = 0;
		double turn = 0;
		boolean aimed = false;
		double velocity = 1;
		double variance = 0;
		double acceleration = 0;
		double max = 5;
		double min = 0;
		int homing = 0;
		int lifetime = -1;
		boolean gravity = false;
		boolean border = false;
		String secondary = "";
		
		for (int i = 1; i < arr.length; i++) {
			
			if (arr[i] == null)
				continue;
			
			String variable = arr[i].split("\t")[1].trim();
			String value = arr[i].split("\t")[arr[i].split("\t").length - 1].trim();
			
			try {
				switch (variable) {
					case "sprite":
						sprite = value;
						break;
					case "inaccuracy":
						inaccuracy = Integer.parseInt(value);
						break;
					case "angle":
						angle = java.lang.Double.parseDouble(value);
						break;
					case "turn":
						turn = java.lang.Double.parseDouble(value);
						break;
					case "aimed":
						aimed = Boolean.parseBoolean(value);
						break;
					case "velocity":
						velocity = java.lang.Double.parseDouble(value);
						break;
					case "variance":
						variance = java.lang.Double.parseDouble(value);
						break;
					case "acceleration":
						acceleration = java.lang.Double.parseDouble(value);
						break;
					case "maxspeed":
						max = java.lang.Double.parseDouble(value);
						break;
					case "minspeed":
						min = java.lang.Double.parseDouble(value);
						break;
					case "homing":
						homing = Integer.parseInt(value);
						break;
					case "gravity":
						gravity = Boolean.parseBoolean(value);
						break;
					case "border":
						border = Boolean.parseBoolean(value);
						break;
					case "lifetime":
						lifetime = Integer.parseInt(value);
						break;
					case "secondary":
						secondary = value;
						break;
					default:
						System.out.printf("WARN: %s is an invalid field for bullet %s%n", variable.toUpperCase(), name.toUpperCase());
				}
			} catch (Exception e) {
				System.out.printf("WARN: An error occured when creating field %s for bullet %s%n", variable.toUpperCase(), name.toUpperCase());
			}
		}
		
		Game.bulletMap.put(name, new EnemyProjectile(sprite, inaccuracy, angle, turn, aimed, velocity, variance, acceleration, max, min, homing, lifetime, gravity, border, secondary));
	}
	
	public static void parseEnemy(String[] arr) {
		// Parses through enemy data
		// Creates a new enemy type with the given attributes
		// Adds the enemy type to the global enemy map
		
		String name = arr[0].split(" ")[1];
		String sprite = "";
		String path = "";
		int health = 1;
		double flat = 0.003;
		boolean offset = false;
		boolean boss = false;
		
		for (int i = 1; i < arr.length; i++) {
			
			if (arr[i] == null)
				continue;
			
			String variable = arr[i].split("\t")[1].trim();
			String value = arr[i].split("\t")[arr[i].split("\t").length - 1].trim();
			
			try {
				switch (variable) {
					case "sprite":
						sprite = value;
						break;
					case "pathing":
						path = value;
						break;
					case "health":
						health = Integer.parseInt(value);
						break;
					case "flatness":
						flat = Double.parseDouble(value);
						break;
					case "offset":
						offset = Boolean.parseBoolean(value);
						break;
					case "boss":
						boss = Boolean.parseBoolean(value);
						break;
					default:
						System.out.printf("WARN: %s is an invalid field for enemy %s%n", variable.toUpperCase(), name.toUpperCase());
				}
			} catch (Exception e) {
				System.out.printf("WARN: An error occured when creating field %s for enemy %s%n", variable.toUpperCase(), name.toUpperCase());
			}
		}
		
		try {
			parsePathing(path, new Point(0, 0), flat, offset, true);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating pathing for enemy %s%n", name.toUpperCase());
		}
		
		Game.enemyMap.put(name, new Enemy(sprite, path, health, flat, offset, boss));
	}
	
	public static ArrayDeque<Point2D.Double> parsePathing(String str, Point origin, double flatness, boolean offset, boolean check) {
		// Parses through a string of coordinates
		// Creates the corresponding curve(s) and/or line(s)
		// Returns an array of points iterating through the curve(s) and/or line(s)
		
		String[] arr = str.split("/");
		ArrayDeque<Point2D.Double> points = new ArrayDeque<Point2D.Double>();
		Path2D.Double path = new Path2D.Double();
		Shape pathShape;
		PathIterator iterator;
		
		path.moveTo(origin.x, origin.y);
		
		Point prevPoint = new Point(origin.x, origin.y);
		
		for (int i = 0; i < arr.length; i++) {
			
			String[] coords = arr[i].split(",");
			int[] ints = new int[coords.length];
			boolean bool = true;
			
			if (coords.length != 1)
				for (int j = 0; j < coords.length; j++) {
					
					coords[j] = coords[j].trim();
					
					if (bool)
						ints[j] = Maths.toWidth(Integer.parseInt(coords[j]));
					else
						ints[j] = Maths.toHeight(Integer.parseInt(coords[j]));
					
					bool = !bool;
				}
			else
				ints[0] = Integer.parseInt(coords[0].trim());
			
			if (offset) { // Yes offset
				if (ints.length == 6) { // Two point curve
					path.curveTo(ints[0] + origin.x, ints[1] + origin.y, ints[2] + origin.x, ints[3] + origin.y, ints[4] + origin.x, ints[5] + origin.y);
					prevPoint = new Point(ints[4] + origin.x, ints[5] + origin.y);
					
				} else if (ints.length == 4) { // One point curve
					
					path.quadTo(ints[0] + origin.x, ints[1] + origin.y, ints[2] + origin.x, ints[3] + origin.y);
					prevPoint = new Point(ints[2] + origin.x, ints[3] + origin.y);
					
				} else if (ints.length == 3) { // Straight line, custom velocity
					
					int velocity = Maths.fromWidth(ints[2]);
					
					double min = 0.3, multiplier = 1, ratio = 3;
					double x = prevPoint.x, y = prevPoint.y, tarx = ints[0] + origin.x, tary = ints[1] + origin.y;
					double angle = Maths.angleTo(x, y, tarx, tary);
					double startDist = Maths.distanceTo(x, y, tarx, tary);
					double lastDist = startDist;
					
					while (Maths.distanceTo(x, y, tarx, tary) <= lastDist) {
						
						lastDist = Maths.distanceTo(x, y, tarx, tary);
						x += Math.sin(Math.toRadians(angle)) * velocity * multiplier;
						y += Math.cos(Math.toRadians(angle)) * velocity * multiplier;
						
						path.lineTo(x, y);
						
						if (velocity * multiplier > min)
							if (lastDist / startDist < 1 / ratio)
								multiplier = lastDist / (startDist / ratio);
					}
					
					prevPoint = new Point(ints[0] + origin.x, ints[1] + origin.y);
					
				} else if (ints.length == 2) { // Straight line, default velocity
					
					int velocity = 2;
					
					double min = 0.3, multiplier = 1, ratio = 3;
					double x = prevPoint.x, y = prevPoint.y, tarx = ints[0] + origin.x, tary = ints[1] + origin.y;
					double angle = Maths.angleTo(x, y, tarx, tary);
					double startDist = Maths.distanceTo(x, y, tarx, tary);
					double lastDist = startDist;
					
					while (Maths.distanceTo(x, y, tarx, tary) <= lastDist) {
						
						lastDist = Maths.distanceTo(x, y, tarx, tary);
						x += Math.sin(Math.toRadians(angle)) * velocity * multiplier;
						y += Math.cos(Math.toRadians(angle)) * velocity * multiplier;
						
						path.lineTo(x, y);
						
						if (velocity * multiplier > min)
							if (lastDist / startDist < 1 / ratio)
								multiplier = lastDist / (startDist / ratio);
					}
					
					prevPoint = new Point(ints[0] + origin.x, ints[1] + origin.y);
					
				} else if (ints.length == 1) { // Pause for ints[0] frames
					path.lineTo(ints[0], EnemyActive.POINTPAUSEVALUE);
					path.moveTo(prevPoint.x, prevPoint.y);
					
				} else
					throw new RuntimeException();
				
			} else if (ints.length == 6) { // No offset, two point curve
				path.curveTo(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5]);
				prevPoint = new Point(ints[4], ints[5]);
				
			} else if (ints.length == 4) { // No offset, one point curve
				path.quadTo(ints[0], ints[1], ints[2], ints[3]);
				prevPoint = new Point(ints[2], ints[3]);
				
			} else if (ints.length == 3) { // No offset, straight line, custom velocity
				
				int velocity = Maths.fromWidth(ints[2]);
				
				double min = 0.3, multiplier = 1, ratio = 3;
				double x = prevPoint.x, y = prevPoint.y, tarx = ints[0], tary = ints[1];
				double angle = Maths.angleTo(x, y, tarx, tary);
				double startDist = Maths.distanceTo(x, y, tarx, tary);
				double lastDist = startDist;
				
				while (Maths.distanceTo(x, y, tarx, tary) <= lastDist) {
					
					lastDist = Maths.distanceTo(x, y, tarx, tary);
					x += Math.sin(Math.toRadians(angle)) * velocity * multiplier;
					y += Math.cos(Math.toRadians(angle)) * velocity * multiplier;
					
					path.lineTo(x, y);
					
					if (velocity * multiplier > min)
						if (lastDist / startDist < 1 / ratio)
							multiplier = lastDist / (startDist / ratio);
				}
				
				prevPoint = new Point(ints[0], ints[1]);
				
			} else if (ints.length == 2) { // No offset, straight line
				
				int velocity = 2;
				
				double min = 0.3, multiplier = 1, ratio = 3;
				double x = prevPoint.x, y = prevPoint.y;
				double angle = Maths.angleTo(x, y, ints[0], ints[1]);
				double startDist = Maths.distanceTo(x, y, ints[0], ints[1]);
				double lastDist = startDist;
				
				while (Maths.distanceTo(x, y, ints[0], ints[1]) <= lastDist) {
					
					lastDist = Maths.distanceTo(x, y, ints[0], ints[1]);
					x += Math.sin(Math.toRadians(angle)) * velocity * multiplier;
					y += Math.cos(Math.toRadians(angle)) * velocity * multiplier;
					
					path.lineTo(x, y);
					
					if (velocity * multiplier > min)
						if (lastDist / startDist < 1 / ratio)
							multiplier = lastDist / (startDist / ratio);
				}
				
				prevPoint = new Point(ints[0], ints[1]);
				
			} else if (ints.length == 1) { // Pause for ints[0] frames
				path.lineTo(ints[0], EnemyActive.POINTPAUSEVALUE);
				path.moveTo(prevPoint.x, prevPoint.y);
				
			} else
				throw new RuntimeException();
		}
		
		pathShape = path;
		iterator = pathShape.getPathIterator(null, flatness);
		
		while (!iterator.isDone()) {
			
			double[] coords = new double[6];
			
			switch (iterator.currentSegment(coords)) {
				case PathIterator.SEG_MOVETO:
				case PathIterator.SEG_LINETO:
					points.add(new Point2D.Double(coords[0], coords[1]));
					break;
			}
			
			iterator.next();
		}
		
		return points;
	}
	
	public static void parseRoutine(ArrayList<String> arr) {
		// Parses through routine data
		// Creates a queue of subroutines (routine)
		// Adds the routine to the global routine map
		
		String title = arr.get(0).split(" ")[1];
		
		try {
			
			ArrayDeque<Subroutine> queue = new ArrayDeque<Subroutine>();
			
			for (int i = 1; i < arr.size(); i++) {
				
				if (arr.get(i) == null)
					continue;
				
				StringTokenizer st = new StringTokenizer(arr.get(i));
				int time = -1;
				String name = "";
				int amount = -1;
				int angle = -1;
				int repeat = 1;
				
				if (st.hasMoreTokens()) {
					String[] line = st.nextToken().split("r");
					time = Integer.parseInt(line[0]);
					
					if (line.length == 2)
						repeat = Integer.parseInt(line[1]);
				}
				if (st.hasMoreTokens())
					name = st.nextToken();
				if (st.hasMoreTokens())
					amount = Integer.parseInt(st.nextToken());
				if (st.hasMoreTokens())
					angle = Integer.parseInt(st.nextToken());
				
				if (name == null || time == -1)
					continue;
				
				for (int j = 0; j < repeat; j++)
					queue.addLast(new Subroutine(time, name, amount, angle));
			}
			
			Game.routineMap.put(title, queue);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating routine %s%n", title.toUpperCase());
		}
	}
	
	public static void parseScript(ArrayList<String> arr) {
		// Parses through script data
		// Creates a queue of subscripts (script)
		// Adds the script to the global script queue
		
		String name = arr.get(0).split(" ")[1];
		
		try {
			
			ArrayDeque<Subscript> script = new ArrayDeque<Subscript>();
			
			for (int i = 1; i < arr.size(); i++) {
				
				if (arr.get(i) == null)
					continue;
				
				StringTokenizer st = new StringTokenizer(arr.get(i));
				int time = Integer.parseInt(st.nextToken());
				String enemy = st.nextToken();
				String routine = st.nextToken();
				String[] str = st.nextToken().split(",");
				Point origin = new Point(Maths.toWidth(Integer.parseInt(str[0].trim())), Maths.toHeight(Integer.parseInt(str[1].trim())));
				String song = "stage" + name.charAt(0);
				
				if (name.length() > 4)
					if (name.substring(0, 4).equalsIgnoreCase("boss"))
						song = "boss" + name.charAt(4);
					
				script.addLast(new Subscript(time, enemy, origin, routine, song));
			}
			
			Game.scriptQueue.addLast(script);
			Game.scriptMap.put(name, script);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating script %s%n", name.toUpperCase());
		}
	}
}