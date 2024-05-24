import java.util.List;
import java.util.Random;

/**
 * Represents a movable turtle in the simulation.
 */
public class Movable {
    protected final Random randomGenerator = new Random();

    protected final int id;
    protected PatchMap map;
    protected Location location;

    /**
     * Constructor to initialize a Movable entity with a location, ID, and map.
     * @param l The initial location of the movable entity.
     * @param id The unique identifier for the movable entity.
     * @param map The PatchMap instance representing the simulation map.
     */
    public Movable(Location l, int id, PatchMap map) {
        this.map = map;
        location = new Location(l);
        this.id = id;
    }

    /**
     * Method to set the location of the movable entity.
     * @param location The new location to set for the movable entity.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Method that defines how the movable entity moves within the simulation.
     * It randomly selects an available patch within vision and moves to it.
     */
    protected void move() {
        List<Location> neighborhood = map.getAvailablePatchesWithinVision(location);
        if (!neighborhood.isEmpty()) {
            map.resetPatchStatus(location);
            Location target = neighborhood.get(randomGenerator.nextInt(neighborhood.size()));
            setLocation(target);
        }
    }
}

