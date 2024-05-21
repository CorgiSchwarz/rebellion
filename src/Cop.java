import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cop extends Movable {
    private final Random randomGenerator = new Random();

    public Cop(Location location, int id, PatchMap map){
        super(location, id, map);
        map.setPatchStatus(location, GridStatus.COP);
    }

    protected void move() {
        super.move();
        map.setPatchStatus(location, GridStatus.COP);
    }

    public Location getEnforceLocation() {
        List<Location> neighborhood = map.getAllPatchesWithinVision(location);
        List<Location> suspects = new ArrayList<>();
        for (Location l : neighborhood) {
            if (map.active(l)) {
                suspects.add(l);
            }
        }
        if (!suspects.isEmpty()) {
            map.resetPatchStatus(location);
            int suspectIndex = randomGenerator.nextInt(suspects.size());
            return suspects.get(suspectIndex);
        }
        return null;
    }

    public int getId() {
        return id;
    }
    public String toString() {
        return "Cop " + id + ": (" + location.x
            + ", " + location.y + ")";
    }
}