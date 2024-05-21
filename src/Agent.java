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
        map.setPatchStatus(location, GridStatus.AGENT_INACTIVE);
    }

    protected void move() {
        if (jailTerm == 0) {
            if (Params.MOVEMENT) {
                super.move();
                map.setPatchStatus(location,
                    active ? GridStatus.AGENT_ACTIVE : GridStatus.AGENT_INACTIVE);
            }
        } else {
            jailTerm--;
        }
    }

    private double getCopRatio() {
        int copCount = 0, activeCount = 1;
        List<Location> neighborhood = map.getAllPatchesWithinVision(location);
        for (Location l: neighborhood) {
            if (map.haveCop(l)) {
                copCount++;
            }
            if (map.active(l)) {
                activeCount++;
            }
        }
        return (double) copCount / activeCount;
    }


    void determineBehavior() {
        double copR = getCopRatio();
        double estimatedArrestProbability = 1 - Math.exp(
            -Params.K * Math.floor(copR));
        double netRisk = grievance - risk_aversion * estimatedArrestProbability;
        if (netRisk > Params.THRESHOLD) {
            active = true;
            map.setPatchStatus(location, GridStatus.AGENT_ACTIVE);
        }
    }

    public void sendToJail(int jailTerm) {
        active = false;
        this.jailTerm = jailTerm;
        map.setPatchStatus(location, GridStatus.AGENT_JAILED);
    }

    void onParamChange() {
        grievance = perceivedHardship * (1 - Params.GOVERNMENT_LEGITIMACY);
    }
}
