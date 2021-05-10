package routing.centrality.utility;

import java.util.Comparator;

/**
 * Comparator that compares two map nodes by their distance from the source
 * node.
 */
public class DistanceComparator implements Comparator<Node> {

    private DistanceMap distances;

    public DistanceComparator(DistanceMap distances){
        this.distances = distances;
    }

    /**
     * Compares two map nodes by their distance from the source node
     *
     * @return -1, 0 or 1 if node1's distance is smaller, equal to, or bigger than
     *         node2's distance
     */
    public int compare(Node node1, Node node2) {
        double dist1 = distances.get(node1);
        double dist2 = distances.get(node2);

        if (dist1 > dist2) {
            return 1;
        } else if (dist1 < dist2) {
            return -1;
        } else {
            return node1.compareTo(node2);
        }
    }
}