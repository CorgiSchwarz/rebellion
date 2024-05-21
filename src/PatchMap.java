import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PatchMap {
    private GridStatus[][] grid;

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    private final int length;
    private final int width;


    public PatchMap(int length, int width) {
        grid = new GridStatus[length][width];
        this.length = length;
        this.width = width;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
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
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                double distanceSquare = Math.pow(getXDistance(location.x, i), 2) +
                    Math.pow(getYDistance(location.y, j), 2);
                if (distanceSquare < Params.VISION_RADIUS * Params.VISION_RADIUS) {
                    result.add(new Location(i, j));
                }
            }
        }
        return result;
    }


    private int getXDistance(int x1, int x2) {
        return Math.min(Math.abs(x1 - x2), width - Math.abs(x1 - x2));
    }

    private int getYDistance(int y1, int y2) {
        return Math.min(Math.abs(y1 - y2), length - Math.abs(y1 - y2));
    }

    public void setPatchStatus(Location location, GridStatus status) {
        grid[location.y][location.x] = status;
    }

    public void resetPatchStatus(Location location) {
        grid[location.y][location.x] = GridStatus.EMPTY;
    }

    public boolean notOccupied(Location location) {
        return grid[location.y][location.x] == GridStatus.EMPTY ||
            grid[location.y][location.x] == GridStatus.AGENT_INACTIVE;
    }

    public boolean active(Location location) {
        return grid[location.y][location.x] == GridStatus.AGENT_ACTIVE;
    }

    public boolean haveCop(Location location) {
        return grid[location.y][location.x] == GridStatus.COP;
    }


    public void print() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j].getValue() + ",");
            }
            System.out.print("\n");
        }
    }
}
