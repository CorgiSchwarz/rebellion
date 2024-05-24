import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents a grid-based map of patches with statuses.
 */
public class PatchMap {
    private GridStatus[][] grid;

    // For extension only, indicates the locations where there are more than one active agent
    private Stack<Location> emergencyLocations;

    /**
     * Constructor to initialize the grid with empty patches.
     */
    public PatchMap() {
        grid = new GridStatus[Params.MAP_LENGTH][Params.MAP_WIDTH];
        for (int i = 0; i < Params.MAP_LENGTH; i++) {
            for (int j = 0; j < Params.MAP_WIDTH; j++) {
                grid[i][j] = GridStatus.EMPTY;
            }
        }
        if (Params.EXTENSION) {
            emergencyLocations = new Stack<>();
        }
    }

    /**
     * Retrieves a list of available patches within the vision radius of a given location.
     * A patch is available if it is not occupied by a cop or free agent.
     * @param location The location from which vision is calculated.
     * @return List of available patches within vision.
     */
    public List<Location> getAvailablePatchesWithinVision(Location location) {
        List<Location> allPatchesWithinVision = getAllPatchesWithinVision(location);
        List<Location> result = new ArrayList<>();
        for (Location l: allPatchesWithinVision) {
            if (notOccupied(l)) {
                result.add(l);
            }
        }
        return result;
    }

    /**
     * Retrieves a list of all patches within the vision radius of a given location.
     * @param location The location from which vision is calculated.
     * @return List of all patches within vision.
     */
    public List<Location> getAllPatchesWithinVision (Location location) {
        List<Location> result = new ArrayList<>();
        for (int i = 0; i < Params.MAP_LENGTH; i++) {
            for (int j = 0; j < Params.MAP_WIDTH; j++) {
                int distanceSquare = (int) (Math.pow(getXDistance(location.x, j), 2) +
                                    Math.pow(getYDistance(location.y, i), 2));
                if (distanceSquare <= Params.VISION_RADIUS * Params.VISION_RADIUS) {
                    result.add(new Location(j, i));
                }
            }
        }
        return result;
    }

    /**
     * Calculates the x-axis distance between two points on the grid, considering wrap-around.
     * @param x1 X-coordinate of the first point.
     * @param x2 X-coordinate of the second point.
     * @return Minimum distance in the x-axis direction.
     */
    private int getXDistance(int x1, int x2) {
        return Math.min(Math.abs(x1 - x2), Params.MAP_WIDTH - Math.abs(x1 - x2));
    }

    /**
     * Calculates the y-axis distance between two points on the grid, considering wrap-around.
     * @param y1 Y-coordinate of the first point.
     * @param y2 Y-coordinate of the second point.
     * @return Minimum distance in the y-axis direction.
     */
    private int getYDistance(int y1, int y2) {
        return Math.min(Math.abs(y1 - y2), Params.MAP_LENGTH - Math.abs(y1 - y2));
    }

    /**
     * Sets the status of a patch at a specific location on the grid.
     * @param location The location of the patch.
     * @param status The status to set.
     */
    public void setPatchStatus(Location location, GridStatus status) {
        grid[location.y][location.x] = status;
    }

    /**
     * Resets the status of a patch at a specific location to EMPTY.
     * @param location The location of the patch to reset.
     */
    public void resetPatchStatus(Location location) {
        grid[location.y][location.x] = GridStatus.EMPTY;
    }

    /**
     * Checks if a patch at a specific location is not occupied by a cop or free agent.
     * @param location The location to check.
     * @return True if the patch is not occupied, false otherwise.
     */
    public boolean notOccupied(Location location) {
        return grid[location.y][location.x] == GridStatus.EMPTY ||
            grid[location.y][location.x] == GridStatus.AGENT_JAILED;
    }

    /**
     * Checks if a patch at a specific location has an active agent.
     * @param location The location to check.
     * @return True if the patch has an active agent, false otherwise.
     */
    public boolean haveActiveAgent(Location location) {
        return grid[location.y][location.x] == GridStatus.AGENT_ACTIVE;
    }

    /**
     * Checks if a patch at a specific location has a cop.
     * @param location The location to check.
     * @return True if the patch has a cop, false otherwise.
     */
    public boolean haveCop(Location location) {
        return grid[location.y][location.x] == GridStatus.COP;
    }

    /**
     * Prints the current state of the grid.
     */
    public void print() {
        for (int i = 0; i < Params.MAP_LENGTH; i++) {
            for (int j = 0; j < Params.MAP_WIDTH; j++) {
                System.out.print(grid[i][j].getValue() + ",");
            }
            System.out.print("\n");
        }
    }

    /**
     * For extension only.
     * Gets the location where there are more than one active agent
     */
    public Location getEmergencyLocation() {
        if (!emergencyLocations.isEmpty()) {
            return emergencyLocations.pop();
        } else {
            return null;
        }
    }

    /**
     * For extension only.
     * Adds the location where there are more than one active agent
     */
    public void addEmergencyLocation(Location l) {
        emergencyLocations.push(l);
    }
}
