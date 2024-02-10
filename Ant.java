import java.util.ArrayList;
import processing.core.PVector;
import java.util.Random;

public class Ant {
    
    Random rand = new Random();
    float x;
    float y;
    float rotation;
    int time;
    boolean hasFood;
    boolean goHome;
    boolean explorer;
    boolean exploreUp;

    ArrayList<PVector> trail = new ArrayList<PVector>();
    FoodTrail assignedTrail;

    public Ant(float x, float y, boolean explorer, FoodTrail assignedTrail, boolean exploreUp) {
        this.x = x;
        this.y = y;
        this.rotation = (float) (Math.random() * Math.PI * 2);
        this.trail.add(new PVector(x, y));
        this.time = 0;
        this.hasFood = false;
        this.goHome = false;
        this.explorer = true;
        this.exploreUp = exploreUp;
    }
    
}
