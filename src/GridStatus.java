/**
 * Enum representing different statuses of patches on the grid.
 */
public enum GridStatus {
    EMPTY(0),            // Represents an empty patch
    AGENT_ACTIVE(1),     // Represents a patch with an active agent
    AGENT_INACTIVE(2),   // Represents a patch with an inactive agent
    AGENT_JAILED(3),     // Represents a patch with an agent in jail
    COP(4);              // Represents a patch with a cop


    private final int value;

    /**
     * Constructor to initialize GridStatus enum constants with their respective integer values.
     * @param value The integer value associated with the enum constant.
     */
    GridStatus(int value) {
        this.value = value;
    }

    /**
     * Getter method to retrieve the integer value associated with the enum constant.
     * @return The integer value of the enum constant.
     */
    public int getValue() {
        return value;
    }
}
