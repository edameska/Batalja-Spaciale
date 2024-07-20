import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Player {
    static BufferedWriter fileOut = null;
    static Random rand = new Random();

    // Q-learning constants
    private static final double LEARNING_RATE = 0.1;
    private static final double DISCOUNT_FACTOR = 0.9; // importance of future rewards
    private static double EXPLORATION_RATE = 1.0; // how often to choose a random action
    private static final double EXPLORATION_DECAY = 0.995; // how much exploration rate decreases over time
    private static final double MIN_EXPLORATION_RATE = 0.01; // minimum exploration rate
    
    private static Map<String, Map<String, Double>> qTable = new HashMap<>(); // tracks how good each action is in each state

    // Game state variables
    public static int universeWidth;
    public static int universeHeight;
    public static String myColor;

    public static String[] myPlanets;
    public static String[] enemyPlanets;

    public static String[] bluePlanets;
    public static String[] cyanPlanets;
    public static String[] greenPlanets;
    public static String[] yellowPlanets;
    public static String[] neutralPlanets;

	public static String[] blueFleets;
	public static String[] cyanFleets;
	public static String[] greenFleets;
	public static String[] yellowFleets;

    public static void main(String[] args) throws Exception {
        try {
            while (true) {
                getGameState();

                // Determine the state
                String currentState = generateState();
                
                // Choose action based on Q-table
                String action = chooseAction(currentState, rand);
    
                // Execute action
                executeAction(action);

				logToFile(action);
    
                // Getting reward and next state
                double reward = getReward(action); 
                String nextState = generateState(); // Get the new state after the action
    
                // Update Q-table with the received reward and next state
                updateQTable(currentState, action, reward, nextState);
    
                // Decay exploration rate
                EXPLORATION_RATE = Math.max(EXPLORATION_RATE * EXPLORATION_DECAY, MIN_EXPLORATION_RATE);
    
                // End of turn
                System.out.println("E");
            }
        } catch (Exception e) {
            logToFile("ERROR: ");
            logToFile(e.getMessage());
            e.printStackTrace();
        }
        if (fileOut != null) {
            fileOut.close();
        }
    }

    public static void logToFile(String line) throws IOException {
        if (fileOut == null) {
            FileWriter fstream = new FileWriter("Igralec.log");
            fileOut = new BufferedWriter(fstream);
        }
        if (line.charAt(line.length() - 1) != '\n') {
            line += "\n";
        }
        fileOut.write(line);
        fileOut.flush();
    }

    public static void getGameState() throws NumberFormatException, IOException {
        BufferedReader stdin = new BufferedReader(new java.io.InputStreamReader(System.in));

        LinkedList<String> bluePlanetsList = new LinkedList<>();
        LinkedList<String> cyanPlanetsList = new LinkedList<>();
        LinkedList<String> greenPlanetsList = new LinkedList<>();
        LinkedList<String> yellowPlanetsList = new LinkedList<>();
        LinkedList<String> neutralPlanetsList = new LinkedList<>();

        LinkedList<String> myPlanetsList = new LinkedList<>();
        LinkedList<String> enemyPlanetsList = new LinkedList<>();

		LinkedList<String> blueFleetsList = new LinkedList<>();
		LinkedList<String> cyanFleetsList = new LinkedList<>();
		LinkedList<String> greenFleetsList = new LinkedList<>();
		LinkedList<String> yellowFleetsList = new LinkedList<>();

        String line = "";
        while (!(line = stdin.readLine()).equals("S")) {
            logToFile(line);

            String[] tokens = line.split(" ");
            char firstLetter = line.charAt(0);
            if (firstLetter == 'U') {
                universeWidth = Integer.parseInt(tokens[1]);
                universeHeight = Integer.parseInt(tokens[2]);
                myColor = tokens[3];
            } else if (firstLetter == 'P') {
                String planetName = tokens[1];
                String planetColor = tokens[6];
                if (planetColor.equals(myColor)) {
                    myPlanetsList.add(planetName);
                } else {
                    enemyPlanetsList.add(planetName);
                }

                switch (planetColor) {
                    case "blue":
                        bluePlanetsList.add(planetName);
                        break;
                    case "cyan":
                        cyanPlanetsList.add(planetName);
                        break;
                    case "green":
                        greenPlanetsList.add(planetName);
                        break;
                    case "yellow":
                        yellowPlanetsList.add(planetName);
                        break;
                    case "null":
                        neutralPlanetsList.add(planetName);
                        break;
                }
            }
        }

        bluePlanets = bluePlanetsList.toArray(new String[0]);
        cyanPlanets = cyanPlanetsList.toArray(new String[0]);
        greenPlanets = greenPlanetsList.toArray(new String[0]);
        yellowPlanets = yellowPlanetsList.toArray(new String[0]);
        neutralPlanets = neutralPlanetsList.toArray(new String[0]);

        myPlanets = myPlanetsList.toArray(new String[0]);
        enemyPlanets = enemyPlanetsList.toArray(new String[0]);

        blueFleets = blueFleetsList.toArray(new String[0]);
        cyanFleets = cyanFleetsList.toArray(new String[0]);
        greenFleets = greenFleetsList.toArray(new String[0]);
        yellowFleets = yellowFleetsList.toArray(new String[0]);
    }

    public static String generateState() {
        return Arrays.toString(bluePlanets) + Arrays.toString(cyanPlanets) +
               Arrays.toString(greenPlanets) + Arrays.toString(yellowPlanets) +
               Arrays.toString(neutralPlanets);
    }

    public static String chooseAction(String state, Random rand) {
        Map<String, Double> stateActions = qTable.getOrDefault(state, new HashMap<>());
        if (rand.nextDouble() < EXPLORATION_RATE) {
            // explore -> choose a random action
            return getRandomAction();
        } else {
            // exploit -> choose the best action
            return stateActions.entrySet()
                               .stream()
                               .max(Map.Entry.comparingByValue())
                               .map(Map.Entry::getKey)
                               .orElse(getRandomAction());
        }
    }

    public static String getRandomAction() {
		if (myPlanets.length == 0 || enemyPlanets.length == 0) {
			//no valid action if either array is empty
			return "";
		}

        // Randomly choose an origin planet from the player's planets
        String originPlanet = myPlanets[rand.nextInt(myPlanets.length)];
        // Randomly choose a destination planet from the enemy's planets
        String destinationPlanet = enemyPlanets[rand.nextInt(enemyPlanets.length)];
        
        // Send half troops for now
        return "A " + originPlanet + " " + destinationPlanet;
   
    }

    public static void executeAction(String action) {
        // Print the action to be executed
        System.out.println(action);
    }

    public static void updateQTable(String state, String action, double reward, String nextState) {
        Map<String, Double> stateActions = qTable.computeIfAbsent(state, k -> new HashMap<>());
        double oldQValue = stateActions.getOrDefault(action, 0.0);
        double maxNextQValue = qTable.getOrDefault(nextState, new HashMap<>())
                                      .values()
                                      .stream()
                                      .max(Double::compare)
                                      .orElse(0.0);

        double newQValue = oldQValue + LEARNING_RATE * (reward + DISCOUNT_FACTOR * maxNextQValue - oldQValue);
        stateActions.put(action, newQValue);
    }
    public static double getReward(String action) {
		//yet to implement
        //default
        return 0.1;
    }
}
