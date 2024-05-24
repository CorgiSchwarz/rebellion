import java.util.List;

/**
 * Represents an agent entity in the simulation, extending from Movable.
 */
public class Agent extends Movable{
    private double grievance;
    private final double perceivedHardship;
    private boolean active;
    private int jailTerm = 0;
    private final double risk_aversion;

    /**
     * Constructor to initialize an Agent with a location, ID, and map.
     * Initializes perceived hardship, risk aversion, grievance, and sets initial status.
     * @param location The initial location of the agent.
     * @param id The unique identifier for the agent.
     * @param map The PatchMap instance representing the simulation map.
     */
    public Agent(Location location, int id, PatchMap map) {
        super(location, id, map);
        perceivedHardship = Math.random();
        risk_aversion = Math.random();
        grievance = perceivedHardship * (1 - Params.GOVERNMENT_LEGITIMACY);
        active = false;
        map.setPatchStatus(location, GridStatus.AGENT_INACTIVE);
    }

    /**
     * Override of the move method to handle agent-specific movement logic.
     * Moves the agent and updates its status on the map.
     */
    protected void move() {
        if (Params.MOVEMENT) {
            super.move();
            map.setPatchStatus(location,
                active ? GridStatus.AGENT_ACTIVE : GridStatus.AGENT_INACTIVE);
        }
    }

    /**
     * Calculates the ratio of cops to active agents in the agent's vision.
     * @return The ratio of cops to active agents in the neighborhood.
     */
    private double getCopRatio() {
        int copCount = 0, activeCount = 1;
        List<Location> neighborhood = map.getAllPatchesWithinVision(location);
        for (Location l: neighborhood) {
            if (map.haveCop(l)) {
                copCount++;
            }
            if (map.haveActiveAgent(l)) {
                activeCount++;
            }
        }
        return Math.floor((double) copCount / activeCount);
    }

    /**
     * Determines the behavior of the agent based on perceived risk and grievance.
     * Updates agent's activity status and map status accordingly.
     * @return True if the agent becomes active, false otherwise.
     */
    boolean determineBehavior() {
        double copR = getCopRatio();
        double estimatedArrestProbability = 1 - Math.exp(
            -Params.K * copR);
        double netRisk = grievance - risk_aversion * estimatedArrestProbability;
        if (netRisk > Params.THRESHOLD) {
            active = true;
            map.setPatchStatus(location, GridStatus.AGENT_ACTIVE);
            return true;
        } else {
            active = false;
            map.setPatchStatus(location, GridStatus.AGENT_INACTIVE);
            return false;
        }
    }

    /**
     * Sends the agent to jail with a specified jail term.
     * Updates agent's activity status and map status accordingly.
     * @param jailTerm The jail term to set for the agent.
     */
    public void sendToJail(int jailTerm) {
        if (jailTerm > 0) {
            active = false;
            this.jailTerm = jailTerm;
            map.setPatchStatus(location, GridStatus.AGENT_JAILED);
        }
    }

    /**
     * Decreases the jail term of the agent by one.
     * If the jail term reaches zero and the location is not occupied, update map status.
     */
    public void decreaseJailTerm() {
        this.jailTerm -= 1;
        if (this.jailTerm == 0 && map.notOccupied(location)) {
            map.setPatchStatus(location, GridStatus.AGENT_INACTIVE);
        }
    }

    /**
     * Checks if the agent is active.
     * @return True if the agent is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Retrieves the remaining jail term of the agent.
     * @return The remaining jail term of the agent.
     */
    public int getJailTerm() {
        return jailTerm;
    }
}
