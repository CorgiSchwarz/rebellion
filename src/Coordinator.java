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

    Coordinator(int numOfCops, int numOfAgents, PatchMap map) {
        this.map = map;
        random = new Random();
        init(numOfCops, numOfAgents);
    }

    void init(int numOfCops, int numOfAgents) {
        agents = new ArrayList<>(numOfAgents);
        cops = new ArrayList<>(numOfCops);

        List<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < map.getLength() * map.getWidth(); i++) {
            permutation.add(i);
        }
        Collections.shuffle(permutation);

        for (int i = 0; i < numOfAgents; i++) {
            Location location = new Location(permutation.get(i) / map.getWidth(),
                permutation.get(i) % map.getWidth());
            agents.add(new Agent(location, i, map));
        }

        for (int i = agents.size(); i <numOfAgents + numOfCops; i++) {
            Location location = new Location(permutation.get(i) / map.getWidth(),
                permutation.get(i) % map.getWidth());
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

    Agent getAgentByLocation(Location location) {
        for (Agent agent: agents) {
            if (agent.location.equals(location)) {
                return agent;
            }
        }
        return null;
    }

    public void goTick() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < agents.size() + cops.size(); i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        int arrest = 0;
        int arise = 0;
        for (int i = 0; i < numbers.size(); i++) {
            int id = numbers.get(i);
            if (id < agents.size()) {
                agents.get(id).move();
                boolean becomeActive = agents.get(id).determineBehavior();
                if (becomeActive) {
                    arise++;
                }
            } else {
                cops.get(id - agents.size()).move();
                Location arrestLocation = cops.get(id - agents.size()).getEnforceLocation();
                if (arrestLocation != null) {
                    Agent arrestAgent = getAgentByLocation(arrestLocation);
                    if (arrestAgent != null) {
                        arrestAgent.sendToJail(random.nextInt(Params.MAX_JAIL_TERM));
                        map.setPatchStatus(arrestLocation, GridStatus.COP);
                        arrest++;
                    }
                }
            }
        }
        System.out.print("arise: " + arise + " ");

        System.out.print("arrest: " + arrest + " ");
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
