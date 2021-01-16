import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Boss {
	
	String name;
	String sprite;
	int health;
	ArrayDeque<ArrayDeque<Subscript>> spellQueue;
	
	public Boss(String name, String sprite, int health) {
		
		this.name = name;
		this.sprite = sprite;
		this.health = health;
	}
}