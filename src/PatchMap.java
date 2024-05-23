import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PatchMap {
    private GridStatus[][] grid;
    public PatchMap() {
        grid = new GridStatus[Params.MAP_LENGTH][Params.MAP_WIDTH];
        for (int i = 0; i < Params.MAP_LENGTH; i++) {
            for (int j = 0; j < Params.MAP_WIDTH; j++) {
                grid[i][j] = GridStatus.EMPTY;
            }
        }
    }

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


    private int getXDistance(int x1, int x2) {
        return Math.min(Math.abs(x1 - x2), Params.MAP_WIDTH - Math.abs(x1 - x2));
    }

    private int getYDistance(int y1, int y2) {
        return Math.min(Math.abs(y1 - y2), Params.MAP_LENGTH - Math.abs(y1 - y2));
    }

    public void setPatchStatus(Location location, GridStatus status) {
        grid[location.y][location.x] = status;
    }

    public void resetPatchStatus(Location location) {
        grid[location.y][location.x] = GridStatus.EMPTY;
    }

    public boolean notOccupied(Location location) {
        return grid[location.y][location.x] == GridStatus.EMPTY ||
            grid[location.y][location.x] == GridStatus.AGENT_JAILED;
    }

    public boolean active(Location location) {
        return grid[location.y][location.x] == GridStatus.AGENT_ACTIVE;
    }

    public boolean haveCop(Location location) {
        return grid[location.y][location.x] == GridStatus.COP;
    }

    public void print() {
        for (int i = 0; i < Params.MAP_LENGTH; i++) {
            for (int j = 0; j < Params.MAP_WIDTH; j++) {
                System.out.print(grid[i][j].getValue() + ",");
            }
            System.out.print("\n");
        }
    }
}
