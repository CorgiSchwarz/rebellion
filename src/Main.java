import java.io.File;

/**
 * Main class for our model.
 */
public class Main {
    /**
     * Main method to initiate the simulation.
     *
     * @param args Command line arguments for configuring simulation parameters.
     */
    public static void main(String[] args) {
        handleInput(args);
        createResultDir();

        int numOfCops = (int) (Params.MAP_LENGTH * Params.MAP_WIDTH * Params.COP_DENSITY);
        int numOfAgents = (int) (Params.MAP_LENGTH * Params.MAP_WIDTH * Params.AGENT_DENSITY);
        PatchMap map = new PatchMap();
        Coordinator coordinator = new Coordinator(numOfCops, numOfAgents, map);

        int tick = 0;
        while (tick++ < Params.TICKS) {
            System.out.println("tick: " + tick);
            coordinator.goTick();
        }
    }

    /**
     * Creates a directory for storing simulation results if it doesn't already exist.
     */
    private static void createResultDir() {
        String currentDirectory = System.getProperty("user.dir");
        Params.dirPath = currentDirectory + File.separator + "result";

        File resultDir = new File(Params.dirPath);
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }
    }

    /**
     * Parses command line arguments to configure simulation parameters.
     *
     * @param args Command line arguments passed to the program.
     */
    private static void handleInput(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--length" -> {
                    Params.MAP_LENGTH = Integer.parseInt(args[i + 1]);
                    i++;
                }
                case "--width" -> {
                    Params.MAP_WIDTH = Integer.parseInt(args[i + 1]);
                    i++;
                }
                case "--copDensity" -> {
                    Params.COP_DENSITY = Double.parseDouble(args[i + 1]);
                    i++;
                }
                case "--agentDensity" -> {
                    Params.AGENT_DENSITY = Double.parseDouble(args[i + 1]);
                    i++;
                }
                case "--vision" -> {
                    Params.VISION_RADIUS = Double.parseDouble(args[i + 1]);
                    i++;
                }
                case "--governmentLegitimacy" -> {
                    Params.GOVERNMENT_LEGITIMACY = Double.parseDouble(args[i + 1]);
                    i++;
                }
                case "--maxJailTerm" -> {
                    Params.MAX_JAIL_TERM = Integer.parseInt(args[i + 1]);
                    i++;
                }
                case "--movement" -> {
                    Params.MOVEMENT = Boolean.parseBoolean(args[i + 1]);
                    i++;
                }
                case "--ticks" -> {
                    Params.TICKS = Integer.parseInt(args[i + 1]);
                    i++;
                }
                case "--extension" -> {
                    Params.EXTENSION = Boolean.parseBoolean(args[i + 1]);
                    i++;
                }
            }
        }
    }
}