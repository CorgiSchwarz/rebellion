import java.util.ArrayList;
import java.util.List;

public class Cop extends Movable {
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
            Location suspectLocation = suspects.get(suspectIndex);
            setLocation(suspectLocation);
            return suspectLocation;
        }
        return null;
    }

    public String toString() {
        return "Cop " + id + ": (" + location.x
            + ", " + location.y + ")";
    }
}