import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cop entity in the simulation, extending from Movable.
 */
public class Cop extends Movable {
    /**
     * Constructor to initialize a Cop with a location, ID, and map.
     * Sets the initial patch status to COP on the map.
     * @param location The initial location of the cop.
     * @param id The unique identifier for the cop.
     * @param map The PatchMap instance representing the simulation map.
     */
    public Cop(Location location, int id, PatchMap map){
        super(location, id, map);
        map.setPatchStatus(location, GridStatus.COP);
    }

    /**
     * Overrides the move method to move the cop and update its status on the map.
     */
    @Override
    protected void move() {
        super.move();
        map.setPatchStatus(location, GridStatus.COP);
    }

    /**
     * Determines the location to enforce based on nearby active agents.
     * @return The location of a suspect agent within vision, or null if none found.
     */
    public Location getEnforceLocation() {
        List<Location> neighborhood = map.getAllPatchesWithinVision(location);
        List<Location> suspects = new ArrayList<>();
        for (Location l : neighborhood) {
            if (map.haveActiveAgent(l)) {
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
}