import java.util.ArrayList;
import processing.core.PVector;

public class FoodTrail {

    ArrayList<PVector> trail;
    int time; 

    public FoodTrail(ArrayList<PVector> trail) {
        this.trail = trail;
        this.time = 0;
    }
}
