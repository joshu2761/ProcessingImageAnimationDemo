import java.util.ArrayList;
import processing.core.PVector;

public class Ant {
    
    float x;
    float y;
    float rotation;
    int time;
    boolean hasFood;
    boolean goHome;

    ArrayList<PVector> trail = new ArrayList<PVector>();

    public Ant(float x, float y) {
        this.x = x;
        this.y = y;
        this.rotation = (float) (Math.random() * Math.PI * 2);
        this.trail.add(new PVector(x, y));
        this.time = 0;
        this.hasFood = false;
        this.goHome = false;
    }
}
