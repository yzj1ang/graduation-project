package routing.centrality.utility;

import java.util.HashMap;

/**
 * Simple Map implementation for storing distances.
 */
public class DistanceMap {
    private static final double INFINITY = Double.MAX_VALUE;

    private HashMap<Node, Double> map;

    /**
     * Constructor. Creates an empty distance map.
     */
    public DistanceMap() {
        this.map = new HashMap<Node, Double>();
    }

    /**
     * Returns the distance to a node. If no distance value is found, returns MAX.
     */
    public double get(Node node) {
        Double value = map.get(node);
        if (value != null) {
            return value;
        } else {
            return INFINITY;
        }
    }

    /**
     * Puts a new distance value for a map node
     */
    public void put(Node node, double distance) {
        map.put(node, distance);
    }

    /**
     * Returns a string representation of the map's contents
     */
    public String toString() {
        return map.toString();
    }
}
