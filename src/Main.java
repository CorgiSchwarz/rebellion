import java.io.File;

public class Main {
    public static void main(String[] args) {
        handleInput(args);
        PatchMap map = new PatchMap();
        createResultDir();

        int numOfCops = (int) (Params.MAP_LENGTH * Params.MAP_WIDTH * Params.COP_DENSITY);
        int numOfAgents = (int) (Params.MAP_LENGTH * Params.MAP_WIDTH * Params.AGENT_DENSITY);

        Coordinator coordinator = new Coordinator(numOfCops, numOfAgents, map);
        int tick = 0;

        while (tick++ < Params.TICKS) {
            System.out.println("tick: " + tick);
            coordinator.goTick();
        }

    }

    private static void createResultDir() {
        String currentDirectory = System.getProperty("user.dir");
        Params.dirPath = currentDirectory + File.separator + "result";

        File resultDir = new File(Params.dirPath);
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }
    }

    private static void handleInput(String[] args) {
        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--length" -> {
                    Params.MAP_LENGTH = Integer.parseInt(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--width" -> {
                    Params.MAP_WIDTH = Integer.parseInt(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--copDensity" -> {
                    Params.COP_DENSITY = Double.parseDouble(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--agentDensity" -> {
                    Params.AGENT_DENSITY = Double.parseDouble(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--vision" -> {
                    Params.VISION_RADIUS = Double.parseDouble(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--governmentLegitimacy" -> {
                    Params.GOVERNMENT_LEGITIMACY = Double.parseDouble(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--maxJailTerm" -> {
                    Params.MAX_JAIL_TERM = Integer.parseInt(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--movement" -> {
                    Params.MOVEMENT = Boolean.parseBoolean(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--ticks" -> {
                    Params.TICKS = Integer.parseInt(args[i + 1]);
                    i++; // Skip next argument
                }
                case "--extension" -> {
                    Params.EXTENSION = Boolean.parseBoolean(args[i + 1]);
                    i++; // Skip next argument
                }
            }
        }

    }

}