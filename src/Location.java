import java.util.Objects;

/**
 * Represents a location with x and y coordinates.
 */
public class Location {
    public int x;
    public int y;

    /**
     * Constructor to initialize a Location with specified x and y coordinates.
     * @param x The x-coordinate of the location.
     * @param y The y-coordinate of the location.
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor to create a Location from another Location object.
     * @param l The Location object to copy.
     */
    public Location(Location l) {
        this.x = l.x;
        this.y = l.y;
    }

    /**
     * Checks if this Location object is equal to another object.
     * @param o The object to compare with this Location.
     * @return True if the objects are equal (same class and same coordinates), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    /**
     * Computes the hash code for this Location object based on its coordinates.
     * @return The hash code of the Location object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
