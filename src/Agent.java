import java.util.List;

public class Agent extends Movable{
    private double grievance;
    private final double perceivedHardship;

    public boolean isActive() {
        return active;
    }

    private boolean active;

    public int getJailTerm() {
        return jailTerm;
    }

    private int jailTerm = 0;
    private final double risk_aversion;

    public Agent(Location location, int id, PatchMap map) {
        super(location, id, map);
        perceivedHardship = Math.random();
        risk_aversion = Math.random();
        grievance = perceivedHardship * (1 - Params.GOVERNMENT_LEGITIMACY);
        active = false;
        map.setPatchStatus(location, GridStatus.AGENT_INACTIVE);
    }

    protected void move() {
        if (Params.MOVEMENT) {
            super.move();
            map.setPatchStatus(location,
                active ? GridStatus.AGENT_ACTIVE : GridStatus.AGENT_INACTIVE);
        }
    }

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

    public void sendToJail(int jailTerm) {
        if (jailTerm > 0) {
            active = false;
            this.jailTerm = jailTerm;
            map.setPatchStatus(location, GridStatus.AGENT_JAILED);
        }
    }

    public void decreaseJailTerm() {
        this.jailTerm -= 1;
        if (this.jailTerm == 0 && map.notOccupied(location)) {
            map.setPatchStatus(location, GridStatus.AGENT_INACTIVE);
        }
    }
}
