// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2021T2, Assignment 6
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class BusNetworks {

    /**
     * Map of towns, indexed by their names
     */
    private Map<String, Town> busNetwork = new HashMap<String, Town>();

    /**
     * CORE
     * Loads a network of towns from a file.
     * Constructs a Set of Town objects in the busNetwork field
     * Each town has a name and a set of neighbouring towns
     * First line of file contains the names of all the towns.
     * Remaining lines have pairs of names of towns that are connected.
     */
    public void loadNetwork(String filename) {
        try {
            busNetwork.clear();
            UI.clearText();
            List<String> lines = Files.readAllLines(Path.of(filename));
            String firstLine = lines.remove(0);
            /*# YOUR CODE HERE */
            String[] towns = firstLine.split(" ");
            String[] route;
            for (String townName : towns) {
                busNetwork.put(townName, new Town(townName));
            }
            for (String neighbours : lines) {
                route = neighbours.split(" ");
                busNetwork.get(route[0]).addNeighbour(busNetwork.get(route[1]));
                busNetwork.get(route[1]).addNeighbour(busNetwork.get(route[0]));
            }
            UI.println("Loaded " + busNetwork.size() + " towns:");

        } catch (IOException e) {
            throw new RuntimeException("Loading data.txt failed" + e);
        }
    }

    /**
     * CORE
     * Print all the towns and their neighbours:
     * Each line starts with the name of the town, followed by
     * the names of all its immediate neighbours,
     */
    public void printNetwork() {
        UI.println("The current network: \n====================");
        /*# YOUR CODE HERE */
        Object[] keys = busNetwork.keySet().toArray();
        Arrays.sort(keys);
        for (Object string : keys) {
            ArrayList<String> neighbours = new ArrayList<>();
            for (Town s : busNetwork.get(string).getNeighbours()) {
                neighbours.add(s.getName());
            }
            UI.println(string + "-> " + neighbours);
        }
    }

    /**
     * COMPLETION
     * Return a set of all the nodes that are connected to the given node.
     * Traverse the network from this node in the standard way, using a
     * visited set, and then return the visited set
     */
    public Set<Town> findAllConnected(Town town) {
        /*# YOUR CODE HERE */
        Set<Town> temp = findAllConnected(town, new HashSet<Town>());
        temp.remove(town);
        return temp;
    }

    public Set<Town> findAllConnected(Town town, Set<Town> visited) {
        visited.add(town);
        for (Town name : town.getNeighbours()) {
            if (!visited.contains(name)) {
                findAllConnected(name, visited);
            }
        }
        return visited;
    }

    /**
     * COMPLETION
     * Print all the towns that are reachable through the network from
     * the town with the given name.
     * Note, do not include the town itself in the list.
     */
    public void printReachable(String name) {
        Town town = busNetwork.get(name);
        if (town == null) {
            UI.println(name + " is not a recognised town");
        } else {
            UI.println("\nFrom " + town.getName() + " you can get to:");
            /*# YOUR CODE HERE */
            for (Town i : findAllConnected(town)) {
                UI.println(i.getName());
            }
        }

    }

    /**
     * COMPLETION
     * Print all the connected sets of towns in the busNetwork
     * Each line of the output should be the names of the towns in a connected set
     * Works through busNetwork, using findAllConnected on each town that hasn't
     * yet been printed out.
     */
    public void printConnectedGroups() {
        UI.println("Groups of Connected Towns: \n================");
        int groupNum = 1;
        /*# YOUR CODE HERE */
        Set<Set<Town>> groupSet = new HashSet<>();
        Set<Town> temp;
        for (Town town : busNetwork.values()) {
            temp = findAllConnected(town);
            temp.add(town);
            groupSet.add(temp);
        }
        for (Set<Town> group : groupSet) {
            UI.println("Group " + groupNum + ":");
            for (Town town : group) {
                UI.print(" " + town.getName());
            }
            UI.println();
            groupNum++;
        }
    }

    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI() {
        UI.addButton("Load", () -> {
            loadNetwork(UIFileChooser.open());
        });
        UI.addButton("Print Network", this::printNetwork);
        UI.addTextField("Reachable from", this::printReachable);
        UI.addButton("All Connected Groups", this::printConnectedGroups);
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100, 500);
        UI.setDivider(1.0);
        loadNetwork("data-small.txt");
    }

    // Main
    public static void main(String[] arguments) {
        BusNetworks bnw = new BusNetworks();
        bnw.setupGUI();
    }

}
