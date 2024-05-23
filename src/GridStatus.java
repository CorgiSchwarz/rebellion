public enum GridStatus {
    EMPTY(0),
    AGENT_ACTIVE(1),
    AGENT_INACTIVE(2),
    AGENT_JAILED(3),
    COP(4);

    private final int value;

    GridStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
