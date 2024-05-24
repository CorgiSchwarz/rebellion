import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


// Agent class
class Agent {
    private double riskAversion;
    private double perceivedHardship;
    private boolean active;
    private int jailTerm;
    private int x;
    private int y;

    // Constructor
    public Agent(double riskAversion, double perceivedHardship) {
        this.riskAversion = riskAversion;
        this.perceivedHardship = perceivedHardship;
    }

    // Getters and setters
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveToRandomFreeLocation(int[][] grid) {
        Random random = new Random();
        int newX, newY;
        do {
            newX = random.nextInt(grid.length);
            newY = random.nextInt(grid[0].length);
        } while (grid[newX][newY] != 0); // Keep generating new coordinates until a free location is found
        setCoordinates(newX, newY);
    }
}

// Cop class
class Cop {
    private int x;
    private int y;
    private List<Cop> linkedCops;
    private int targetX;
    private int targetY;

    // Constructor
    public Cop(int x, int y) {
        this.x = x;
        this.y = y;
        this.linkedCops = new ArrayList<>();
    }

    // Method to add linked cop
    public void addLinkedCop(Cop cop) {
        linkedCops.add(cop);
    }

    // Method to share coordinates with another cop
    public void shareCoordinates(Cop receiver) {
        receiver.receiveCoordinates(this.x, this.y);
    }

    // Method to receive coordinates from another cop
    public void receiveCoordinates(int x, int y) {
        // Process received coordinates
        this.targetX = x;
        this.targetY = y;
    }

    // Method for cop movement
    public void moveToTarget() {
        setCoordinates(targetX, targetY);
    }

    // Getters and setters
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get the number of cops from the user
        System.out.println("Enter the number of cops: ");
        int numCops = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Create a list to store the Cop objects
        List<Cop> cops = new ArrayList<>();

        // Create a 2D grid to represent the environment
        int[][] grid = new int[10][10]; // Assuming a 10x10 grid for simplicity

        // Prompt the user to enter coordinates for each cop
        for (int i = 0; i < numCops; i++) {
            System.out.println("Enter initial coordinates of cop" + (i + 1) + " (x y): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            Cop cop = new Cop(x, y);
            cops.add(cop);
            grid[x][y] = 1; // Mark the location of cop as occupied
        }

        // Establish communication links between cops
        for (int i = 0; i < numCops; i++) {
            for (int j = i + 1; j < numCops; j++) {
                cops.get(i).addLinkedCop(cops.get(j));
                cops.get(j).addLinkedCop(cops.get(i));
            }
        }

        // Share coordinates between cops
        for (int i = 0; i < numCops; i++) {
            for (int j = i + 1; j < numCops; j++) {
                cops.get(i).shareCoordinates(cops.get(j));
            }
        }

        // Move cops to their target coordinates
        for (Cop cop : cops) {
            cop.moveToTarget();
        }

        // Move agents to random free locations
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < n; i++) { 
            Agent agent = new Agent(riskAversion,percievedHardship); // Example riskAversion and perceivedHardship values
            agents.add(agent);
            agent.moveToRandomFreeLocation(grid);
            grid[agent.x][agent.y] = 1; // Mark the location of agent as occupied
        }

        // Display final positions of cops and agents
        for (Cop cop : cops) {
            System.out.println("Cop at (" + cop.x + ", " + cop.y + ")");
        }
        for (Agent agent : agents) {
            System.out.println("Agent at (" + agent.x + ", " + agent.y + ")");
        }

        scanner.close();
    }
}
