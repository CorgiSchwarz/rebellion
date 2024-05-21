import java.util.List;
import java.util.Random;
public class Movable {

    protected final int id;
    protected PatchMap map;

    public void setLocation(Location location) {
        this.location = location;
    }

    protected Location location;

    public Movable(Location l, int id, PatchMap map) {
        this.map = map;
        location = new Location(l);
        this.id = id;
    }

    protected void move() {
        map.resetPatchStatus(location);
        List<Location> neighborhood = map.getAvailablePatchesWithinVision(location);
        if (!neighborhood.isEmpty()) {
            Location target = neighborhood.get(new Random().nextInt(neighborhood.size()));
            setLocation(target);
        }
    }
}

