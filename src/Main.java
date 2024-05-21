import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static ArrayList<Agent> agents;
    private static ArrayList<Cop> cops;
    public static void main(String[] args) {
        int length, width;
        Scanner scanner = new Scanner(System.in);
        length = scanner.nextInt();
        width = scanner.nextInt();
        PatchMap map = new PatchMap(length, width);

        double copDensity = scanner.nextDouble();
        double agentDensity = scanner.nextDouble();
        if (agentDensity + copDensity > 1) {
            System.out.println(
                "The sum of AgentDensity and CopDensity should not be greater than 1");
        }
        int numOfAgents = (int) (length * width * agentDensity);
        int numOfCops = (int) (length * width * copDensity);
        Params.VISION_RADIUS = scanner.nextDouble();
        Params.GOVERNMENT_LEGITIMACY = scanner.nextDouble();
        Params.MAX_JAIL_TERM = scanner.nextInt();
        Params.MOVEMENT = scanner.nextBoolean();

        Coordinator coordinator = new Coordinator(numOfCops, numOfAgents, map);
        int tick = 0;

        while (tick++ < 10) {
            System.out.println("tick: " + tick);
            coordinator.goTick();
        }
    }
}