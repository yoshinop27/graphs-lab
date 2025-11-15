import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

// Record class for edge
record edge<T>(T v1, T v2) {};

// Class for our graph
class Graph <T> {

    // Variables for our graph
    HashMap<T,ArrayList<T>> adjacencyList;
    int doubleEdges;

    // Graph Constructor always start empty
    public Graph() {
        this.doubleEdges = 0;
        this.adjacencyList = new HashMap<>();
    } 

    /**
     * Adds two vertices to adjacency list if not already present. Update edge sets and count
     * @param v1 the first vertex
     * @param v2 the second vertex
     */
    public void add(T v1, T v2) {
        // Make sure they aren't the same vertex
        if (v1.equals(v2)) {
            return;
        }
        // Is v1 in the graph?
        if (!adjacencyList.containsKey(v1)) {
            // Create entry for v1, add v2 to edgeSet
            adjacencyList.put(v1, new ArrayList<>());
        }
        // Check we are not adding the same edge twice
        if (!adjacencyList.get(v1).contains(v2)) {
            // Add v2 to v1's edgeSet
            adjacencyList.get(v1).add(v2);
            doubleEdges++;
        }

        // Is v2 in the graph?
        if (!adjacencyList.containsKey(v2)) {
            // Create entry for v2, add v1 to edgeSet
            adjacencyList.put(v2, new ArrayList<>());
        }
        // Check we are not adding the same edge twice
        if (!adjacencyList.get(v2).contains(v1)) {
            // Add v1 to v2's edgeSet
            adjacencyList.get(v2).add(v1);
            doubleEdges++;
        }
    }

    /**
     * Checks if a vertex is in the graph
     * @param v the vertex to check
     * @return true if the vertex is in the graph, false otherwise
     */
    public boolean isNode(T v) {
        return adjacencyList.containsKey(v);
    }

    /**
     * Returns a list of all vertices in the graph
     * @return a list of all vertices in the graph
     */
    public List<T> getNodes(){
        return new ArrayList<>(adjacencyList.keySet());
    }

    /**
     * Returns a list of all edges in the graph
     * @return a list of all edges in the graph
     */
    public List<edge<T>> getEdges(){
        List<edge<T>> edges = new ArrayList<>();
        for (T v1: adjacencyList.keySet()){
            for (T v2: adjacencyList.get(v1)){
                edges.add(new edge<>(v1, v2));
            }
        }
        return edges;
    }

    /**
     * 
     * Recursive dfs search of our graph for a walk of length <= n from v1 to v2
     * @param v1 starting vertex
     * @param v2 target vertex
     * @param n degrees of seperation
     * @param s set of see vertices
     * @return true if there exists walk of length <= n
     */
    public boolean areConnectedH(T v1, T v2, int n, Set<T> s) {

        // get adjacency list of v1
        List<T> adjacents = adjacencyList.get(v1);

        // Check for equality
        if (v1.equals(v2)) return true;
        // check for neighbors
        if (adjacents == null || adjacents.size() == 0) return false;
        // Base Case:
        if (n == 0) return false;

        // Update seen set
        s.add(v1);

        // loop through each neighbor, run dfs recursively
        for (T neighbor : adjacents){
            if (!s.contains(neighbor)){
                if(areConnectedH(neighbor, v2, n-1, s)) return true;
            }
        }
        return false;
    }

    /**
     * Husk for helper function
     * @param v1 starting vertex
     * @param v2 target vertex
     * @param n degrees of seperation
     * @return true if there exists walk of length <= n
     */
    public boolean areConnected(T v1, T v2, int n){
        // Create set 
        Set<T> seen = new HashSet<>();
        // Run dfs search
        return areConnectedH(v1, v2, n, seen);
    }
}

public class SocialNetworks {
    public static void main(String[] args) {
        // Check if the user has provided a filename
        if (args.length != 4) {
            System.exit(1);
        }
        // Get the filename from the command line arguments
        String filename = args[0];
        Graph<String> graph = new Graph<>();
        // Read the file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Add edges to adjacency list
            while ((line = reader.readLine()) != null) {
                String[] vertices = line.split(" ");
                graph.add(vertices[0], vertices[1]);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }

        // Run areConnected function
        boolean res = graph.areConnected(args[1], args[2], Integer.parseInt(args[3]));
        System.out.printf(
    "%s and %s are connected within %d degrees of seperation: %b", 
            args[1], args[2], Integer.parseInt(args[3]), res);
    }
}
