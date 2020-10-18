import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class parser {
	
	public static void parseBullet(String[] arr) {
		
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
				}
			} catch (Exception e) {
				System.out.printf("WARN: An error occured when creating field %s for bullet %s%n", variable.toUpperCase(), nameLocal.toUpperCase());
			}
		}
		
		game.bulletTypes.put(nameLocal, new projectile(spriteLocal, inaccuracyLocal, angleLocal, turnLocal, aimedLocal, velocityLocal, accelerationLocal,
				maxLocal, minLocal, homingLocal, lifetimeLocal, new Dimension(widthLocal, heightLocal), secondaryLocal));
	}
	
	public static void parseEnemy(String[] arr) {
		
		String nameLocal = arr[0].split(" ")[1];
		String spriteLocal = "";
		String pathLocal = "";
		double velocityLocal = 1;
		double accelerationLocal = 0;
		double maxLocal = 0;
		double minLocal = 0;
		int widthLocal = 10;
		int heightLocal = 10;
		int healthLocal = 0;
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
				case "width":
					widthLocal = Integer.parseInt(value);
					break;
				case "height":
					heightLocal = Integer.parseInt(value);
					break;
				case "health":
					healthLocal = Integer.parseInt(value);
					break;
				case "offset":
					offsetLocal = Boolean.parseBoolean(value);
					break;
				// case "routine":
				// routineLocal = value;
				// break;
				}
			} catch (Exception e) {
				System.out.printf("WARN: An error occured when creating field %s for enemy %s%n", variable.toUpperCase(), nameLocal.toUpperCase());
			}
		}
		
		try {
			parsePathing(pathLocal, new Point(0, 0), false);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating pathing for enemy %s%n", nameLocal.toUpperCase());
		}
		
		game.enemyTypes.put(nameLocal, new enemy(spriteLocal, pathLocal, velocityLocal, accelerationLocal, maxLocal, minLocal,
				new Dimension(widthLocal, heightLocal), healthLocal, offsetLocal));
	}
	
	public static ArrayList<Point2D.Double> parsePathing(String str, Point origin, boolean offset) {
		
		String[] arr = str.split("/");
		ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
		Path2D.Double path = new Path2D.Double();
		Shape pathShape;
		PathIterator iterator;
		
		path.moveTo(origin.x, origin.y);
		
		if (!offset)
			for (int i = 0; i < arr.length; i++) {
				
				String[] coords = arr[i].split(",");
				
				if (coords.length == 6) // Two point curve
					path.curveTo(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()), Integer.parseInt(coords[2].trim()),
							Integer.parseInt(coords[3].trim()), Integer.parseInt(coords[4].trim()), Integer.parseInt(coords[5].trim()));
				else if (coords.length == 4) // One point curve
					path.quadTo(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()), Integer.parseInt(coords[2].trim()),
							Integer.parseInt(coords[3].trim()));
				else
					throw new java.lang.RuntimeException();
			}
		else
			for (int i = 0; i < arr.length; i++) {
				
				String[] coords = arr[i].split(",");
				
				if (coords.length == 6) // Two point curve
					path.curveTo(Integer.parseInt(coords[0].trim()) + origin.x, Integer.parseInt(coords[1].trim()) + origin.y,
							Integer.parseInt(coords[2].trim()) + origin.x, Integer.parseInt(coords[3].trim()) + origin.y,
							Integer.parseInt(coords[4].trim()) + origin.x, Integer.parseInt(coords[5].trim()) + origin.y);
				else if (coords.length == 4) // One point curve
					path.quadTo(Integer.parseInt(coords[0].trim()) + origin.x, Integer.parseInt(coords[1].trim()) + origin.y,
							Integer.parseInt(coords[2].trim()) + origin.x, Integer.parseInt(coords[3].trim()) + origin.y);
				else
					throw new java.lang.RuntimeException();
			}
		
		pathShape = path;
		iterator = pathShape.getPathIterator(null, 0.001);
		
		game.activePaths.add(path);
		
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
		
		String nameLocal = arr.get(0).split(" ")[1];
		
		try {
			
			ArrayDeque<subroutine> stack = new ArrayDeque<subroutine>();
			
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
				
				stack.addLast(new subroutine(time, name, amount, angle));
			}
			game.routineTypes.put(nameLocal, stack);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating routine %s%n", nameLocal.toUpperCase());
		}
	}
	
	public static void parseScript(ArrayList<String> arr) {
		
		String nameLocal = arr.get(0).split(" ")[1];
		
		try {
			
			ArrayDeque<subscript> script = new ArrayDeque<subscript>();
			
			for (int i = 1; i < arr.size(); i++) {
				
				if (arr.get(i) == null)
					continue;
				
				StringTokenizer st = new StringTokenizer(arr.get(i));
				int time = Integer.parseInt(st.nextToken());
				String enemy = st.nextToken();
				String routine = st.nextToken();
				String[] str = st.nextToken().split(",");
				Point origin = new Point(Integer.parseInt(str[0].trim()), Integer.parseInt(str[1].trim()));
				
				script.addLast(new subscript(time, enemy, origin, routine));
			}
			game.scriptList.addLast(script);
		} catch (Exception e) {
			System.out.printf("WARN: An error occured when creating script %s%n", nameLocal.toUpperCase());
		}
	}
}
