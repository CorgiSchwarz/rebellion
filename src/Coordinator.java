import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Coordinator class that manages the simulation of agents and cops on the map.
 */
public class Coordinator {
    private ArrayList<Agent> agents;
    private ArrayList<Cop> cops;
    private PatchMap map;
    private Random random;
    private ArrayList<Integer> permutation;

    /**
     * Constructor to initialize the Coordinator with a number of cops, agents, and a map.
     * @param numOfCops Number of cops in the simulation.
     * @param numOfAgents Number of agents in the simulation.
     * @param map The PatchMap instance representing the simulation map.
     */
    Coordinator(int numOfCops, int numOfAgents, PatchMap map) {
        this.map = map;
        random = new Random();
        init(numOfCops, numOfAgents);
    }

    /**
     * Initializes the simulation by setting up agents, cops, and their starting locations.
     * @param numOfCops Number of cops to initialize.
     * @param numOfAgents Number of agents to initialize.
     */
    void init(int numOfCops, int numOfAgents) {
        // uses a permutation to determine the order of acting within one tick
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

        // Initialize agents with shuffled starting locations
        for (int i = 0; i < numOfAgents; i++) {
            Location location = new Location(locationPermutation.get(i) % Params.MAP_WIDTH,
                locationPermutation.get(i) % Params.MAP_LENGTH);
            agents.add(new Agent(location, i, map));
        }

        // Initialize cops with shuffled starting locations
        for (int i = numOfAgents; i < numOfAgents + numOfCops; i++) {
            Location location = new Location(locationPermutation.get(i) % Params.MAP_WIDTH,
                locationPermutation.get(i) % Params.MAP_LENGTH);
            cops.add(new Cop(location, i, map));
        }

        // Initialize a CSV file for recording agent states
        String csvFile = Params.dirPath + File.separator + "agent.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println("quiet,jailed,active");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the active agent at a specific location.
     * @param location The location to check for an active agent.
     * @return The active agent at the specified location, or null if none found.
     */
    Agent getActiveAgentByLocation(Location location) {
        for (Agent agent: agents) {
            if (agent.location.equals(location) && agent.isActive()) {
                return agent;
            }
        }
        return null;
    }

    /**
     * Executes one simulation tick where agents and cops perform actions.
     */
    public void goTick() {
        Collections.shuffle(permutation);
        int arrest = 0;
        int arise = 0;
        int release = 0;
        // Turtles take actions in random order
        for (int i = 0; i < permutation.size(); i++) {
            int id = permutation.get(i);
            if (id >= agents.size()) {
                // The cop moves and arrest an active agent within vision (if exists).
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
                // The free agent moves and determines whether to change her/his behavior.
                agents.get(id).move();
                boolean becomeActive = agents.get(id).determineBehavior();
                if (becomeActive) {
                    arise++;
                }
            }
        }

        // Handle releasing agents from jail at the end of their jail term
        for (Agent agent: agents) {
            if (agent.getJailTerm() > 0) {
                agent.decreaseJailTerm();
                if (agent.getJailTerm() == 0) {
                    release++;
                }
            }
        }
        System.out.print("arise: " + arise + " ");
        System.out.print("arrest: " + arrest + " ");
        System.out.print("release: " + release + " ");

        analyze();
    }

    /**
     * Analyzes the current state of agents and records statistics to a CSV file.
     */
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
