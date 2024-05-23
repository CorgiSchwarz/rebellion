import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Coordinator {
    private ArrayList<Agent> agents;
    private ArrayList<Cop> cops;
    private PatchMap map;

    private Random random;
    private ArrayList<Integer> permutation;
    private int totalJail = 0;
    private int totalRelease = 0;

    Coordinator(int numOfCops, int numOfAgents, PatchMap map) {
        this.map = map;
        random = new Random();
        init(numOfCops, numOfAgents);
    }

    void init(int numOfCops, int numOfAgents) {
        permutation= new ArrayList<>();
        for (int i = 0; i < numOfAgents + numOfCops; i++) {
            permutation.add(i);
        }

        agents = new ArrayList<>(numOfAgents);
        cops = new ArrayList<>(numOfCops);

        List<Integer> locationPermutation = new ArrayList<>();
        for (int i = 0; i < Params.MAP_WIDTH * Params.MAP_LENGTH; i++) {
            locationPermutation.add(i);
        }
        Collections.shuffle(locationPermutation);

        for (int i = 0; i < numOfAgents; i++) {
            Location location = new Location(locationPermutation.get(i) % Params.MAP_WIDTH,
                locationPermutation.get(i) % Params.MAP_LENGTH);
            agents.add(new Agent(location, i, map));
        }

        for (int i = numOfAgents; i < numOfAgents + numOfCops; i++) {
            Location location = new Location(locationPermutation.get(i) % Params.MAP_WIDTH,
                locationPermutation.get(i) % Params.MAP_LENGTH);
            cops.add(new Cop(location, i, map));
        }
        System.out.println("init map");
//        map.print();

        String csvFile = Params.dirPath + File.separator + "agent.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println("quiet,jailed,active");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Agent getActiveAgentByLocation(Location location) {
        for (Agent agent: agents) {
            if (agent.location.equals(location) && agent.isActive()) {
                return agent;
            }
        }
        return null;
    }

    public void goTick() {
        Collections.shuffle(permutation);
        int arrest = 0;
        int arise = 0;
        int release = 0;
        for (int i = 0; i < permutation.size(); i++) {
            int id = permutation.get(i);
            if (id >= agents.size()) {
                cops.get(id - agents.size()).move();
                Location arrestLocation = cops.get(id - agents.size()).getEnforceLocation();
                if (arrestLocation != null) {
                    Agent arrestAgent = getActiveAgentByLocation(arrestLocation);
                    if (arrestAgent != null) {
                        arrestAgent.sendToJail(random.nextInt(Params.MAX_JAIL_TERM));
                        map.setPatchStatus(arrestLocation, GridStatus.COP);
                        arrest++;
                    }
                }
            } else if (agents.get(id).getJailTerm() == 0) {
                agents.get(id).move();
                boolean becomeActive = agents.get(id).determineBehavior();
                if (becomeActive) {
                    arise++;
                }
//                boolean becomeActive = agents.get(id).determineBehavior();
//                if (becomeActive) {
//                    arise++;
//                }
            }
        }
        for (Agent agent: agents) {
            if (agent.getJailTerm() > 0) {
                agent.decreaseJailTerm();
                if (agent.getJailTerm() == 0) {
                    release++;
                }
            }
        }
        totalJail += arrest;
        totalRelease += release;
        System.out.print("arise: " + arise + " ");
        System.out.print("arrest: " + arrest + " ");
        System.out.print("release: " + release + " ");
        System.out.print("total release: " + totalRelease + " ");
        System.out.print("total jail: " + totalJail + " ");

//        map.print();
        analyze();
    }

    private void analyze() {
        int quietAgent = 0, jailedAgent = 0, activeAgent = 0;
        for (Agent agent: agents) {
            if (agent.getJailTerm() > 0) {
                jailedAgent++;
            } else if (agent.isActive()) {
                activeAgent++;
            } else {
                quietAgent++;
            }
        }
        System.out.println("quiet: " + quietAgent + " jailed: " +
            jailedAgent + " active: " + activeAgent);
        String csvFile = Params.dirPath + File.separator + "agent.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) {
            writer.println(quietAgent + "," +
                jailedAgent + "," + activeAgent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
