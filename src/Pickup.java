import java.awt.*;
import java.awt.geom.*;

public class Pickup extends Rectangle2D.Double {

    static final double MAXVELOCITY = 0;
    static final double ACCELERATION = 0;
    double velocity;
    int size;
    int value;
    
    public Pickup(int value, int size, Ellipse2D.Double ellipse){
        
        this.value = value;
        this.size = size;
        x = Maths.centerX(ellipse) - size / 2;
		y = Maths.centerY(ellipse) - size / 2;
    }
    
    public boolean collides(Ellipse2D.Double ellipse){
        //Checks for pickup collision
        // Returns true if collided
        
        return Maths.ellipseArea(ellipse).intersects(this);
    }
    
    public static void create(int value, int size, Ellipse2D.Double ellipse){
        
        Game.activePickups.add(new Pickup(value, size, ellipse));
    }
    
    public static void draw(Graphics g){
        
        Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.ORANGE);
		
		for (Pickup pu : Game.activePickups)
			g2.fill(pu);
    }
    
    public void kill(){
        
    }
    
    public void update(){
        
    }
}
