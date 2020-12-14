import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Parser {
	
	public static void parseBullet(String[] arr) {
		// Parses through bullet data
		// Creates a new bullet type with the given attributes
		// Adds the bullet type to the global bullet map
		
		String nameLocal = arr[0].split(" ")[1];
		String spriteLocal = "";
		int inaccuracyLocal = 0;
		double angleLocal = 0;
		double turnLocal = 0;
		boolean aimedLocal = false;
		double velocityLocal = 1;
		double accelerationLocal = 0;
		double maxLocal = 1;
		double minLocal = 0;
		int homingLocal = 0;
		int lifetimeLocal = 120;
		int widthLocal = 10;
		int heightLocal = 10;
		String secondaryLocal = "";
		
		for (int i = 1; i < arr.length; i++) {
			
			if (arr[i] == null)
				continue;
			
			String variable = arr[i].split("\t")[1].trim();
			String value = arr[i].split("\t")[arr[i].split("\t").length - 1].trim();
			
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
					case "maxspeed":
						maxLocal = java.lang.Double.parseDouble(value);
						break;
					case "minspeed":
						minLocal = java.lang.Double.parseDouble(value);
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
					default:
						System.out.printf("WARN: %s is an invalid field for bullet %s%n", variable.toUpperCase(), nameLocal.toUpperCase());
				}
			} catch (Exception e) {
				System.out.printf("WARN: An error occured when creating field %s for bullet %s%n", variable.toUpperCase(), nameLocal.toUpperCase());
			}
		}
		
		Game.bulletMap.put(nameLocal, new EnemyProjectile(spriteLocal, inaccuracyLocal, angleLocal, turnLocal, aimedLocal, velocityLocal, accelerationLocal, maxLocal, minLocal, homingLocal,
				lifetimeLocal, new Dimension(widthLocal, heightLocal), secondaryLocal));
	}
	
	public static void parseEnemy(String[] arr) {
		// Parses through enemy data
		// Creates a new enemy type with the given attributes
		// Adds the enemy type to the global enemy map
		
		String nameLocal = arr[0].split(" ")[1];
		String spriteLocal = "";
		String pathLocal = "";
		int widthLocal = 10;
		int heightLocal = 10;
		int healthLocal = 0;
		double flatLocal = 0.003;
		boolean offsetLocal = false;
		
		for (int i = 1; i < arr.length; i++) {
			
			if (arr[i] == null)
				continue;
			
			String variable = arr[i].split("\t")[1].trim();
			String value = arr[i].split("\t")[arr[i].split("\t").length - 1].trim();
			
			try {
				switch (variable) {
					case "sprite":
						spriteLocal = value;
						break;
					case "pathing":
						pathLocal = value;
						break;
					case "width":
						widthLocal = Integer.parseInt(value);
						break;
					case "height":
						heightLocal = Integer.parseInt(value);
						break;
					case "health":
						healthLocal = Integer.parseInt(value);
						break;
					case "flatness":
						flatLocal = Double.parseDouble(value);
						break;
					case "offset":
						offsetLocal = Boolean.parseBoolean(value);
						break;
					default:
						System.out.printf("WARN: %s is an invalid field for enemy %s%n", variable.toUpperCase(), nameLocal.toUpperCase());
				}
			} catch (Exception e) {
				System.out.printf("WARN: An error occured when creating field %s for enemy %s%n", variable.toUpperCase(), nameLocal.toUpperCase());
			}
		}
		
		try {
			parsePathing(pathLocal, new Point(0, 0), flatLocal, offsetLocal, true);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating pathing for enemy %s%n", nameLocal.toUpperCase());
		}
		
		Game.enemyMap.put(nameLocal, new Enemy(spriteLocal, pathLocal, new Dimension(widthLocal, heightLocal), healthLocal, flatLocal, offsetLocal));
	}
	
	public static ArrayDeque<Point2D.Double> parsePathing(String str, Point origin, double flatness, boolean offset, boolean check) {
		// Parses through a string of bezier curve coordinates
		// Creates the corresponding curve(s)
		// Returns an array of points iterating through the curve(s)
		
		String[] arr = str.split("/");
		ArrayDeque<Point2D.Double> points = new ArrayDeque<Point2D.Double>();
		Path2D.Double path = new Path2D.Double();
		Shape pathShape;
		PathIterator iterator;
		
		path.moveTo(origin.x, origin.y);
		
		Point prevPoint = new Point();
		
		for (int i = 0; i < arr.length; i++) {
			
			String[] coords = arr[i].split(",");
			int[] ints = new int[coords.length];
			boolean bool = true;
			
			if (coords.length == 1 && i == 0)
				throw new RuntimeException();
			
			if (coords.length != 1)
				for (int j = 0; j < coords.length; j++) {
					
					coords[j] = coords[j].trim();
					
					if (Math.abs(Integer.parseInt(coords[j])) > Game.GRIDLINES)
						throw new RuntimeException();
					
					if (!offset && Integer.parseInt(coords[j]) < 0)
						throw new RuntimeException();
					
					if (bool)
						ints[j] = Maths.toWidth(Integer.parseInt(coords[j]));
					else
						ints[j] = Maths.toHeight(Integer.parseInt(coords[j]));
					
					bool = !bool;
				}
			else
				ints[0] = Integer.parseInt(coords[0].trim());
			
			if (offset) // Yes offset
				if (ints.length == 6) { // Two point curve
					path.curveTo(ints[0] + origin.x, ints[1] + origin.y, ints[2] + origin.x, ints[3] + origin.y, ints[4] + origin.x, ints[5] + origin.y);
					prevPoint = new Point(ints[4] + origin.x, ints[5] + origin.y);
					
				} else if (ints.length == 4) { // One point curve
					path.quadTo(ints[0] + origin.x, ints[1] + origin.y, ints[2] + origin.x, ints[3] + origin.y);
					prevPoint = new Point(ints[2] + origin.x, ints[3] + origin.y);
					
				} else if (ints.length == 1) { // Signals for the enemy to pause movement for ints[0] frames
					path.lineTo(ints[0], EnemyActive.POINTPAUSEVALUE);
					path.moveTo(prevPoint.x, prevPoint.y);
					
				} else
					throw new RuntimeException();
				
			else if (ints.length == 6) { // No offset, Two point curve
				path.curveTo(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5]);
				prevPoint = new Point(ints[4], ints[5]);
				
			} else if (ints.length == 4) { // No offset, One point curve
				path.quadTo(ints[0], ints[1], ints[2], ints[3]);
				prevPoint = new Point(ints[2], ints[3]);
				
			} else if (ints.length == 1) { // Signals for the enemy to pause movement for ints[0] frames
				path.lineTo(ints[0], EnemyActive.POINTPAUSEVALUE);
				path.moveTo(prevPoint.x, prevPoint.y);
				
			} else
				throw new RuntimeException();
		}
		
		pathShape = path;
		iterator = pathShape.getPathIterator(null, flatness);
		
		if (!check)
			Game.activePaths.add(path);
		
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
		
		String nameLocal = arr.get(0).split(" ")[1];
		
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
				
				if (st.hasMoreTokens())
					time = Integer.parseInt(st.nextToken());
				if (st.hasMoreTokens())
					name = st.nextToken();
				if (st.hasMoreTokens())
					amount = Integer.parseInt(st.nextToken());
				if (st.hasMoreTokens())
					angle = Integer.parseInt(st.nextToken());
				
				if (name == null || time == -1)
					throw new java.lang.RuntimeException();
				
				queue.addLast(new Subroutine(time, name, amount, angle));
			}
			
			Game.routineMap.put(nameLocal, queue);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating routine %s%n", nameLocal.toUpperCase());
		}
	}
	
	public static void parseScript(ArrayList<String> arr) {
		// Parses through script data
		// Creates a queue of subscripts (script)
		// Adds the script to the global script queue
		
		String nameLocal = arr.get(0).split(" ")[1];
		
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
				
				script.addLast(new Subscript(time, enemy, origin, routine));
			}
			
			Game.scriptQueue.addLast(script);
			Game.scriptMap.put(nameLocal, script);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating script %s%n", nameLocal.toUpperCase());
		}
	}
}
