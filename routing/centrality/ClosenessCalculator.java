package routing.centrality;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

import core.DTNHost;
import routing.centrality.utility.Node;
import routing.centrality.utility.DistanceMap;
import routing.centrality.utility.MinSpanningTree;


public class ClosenessCalculator extends CentralityCalculator{

    private static MinSpanningTree STPaser;

    static {
        ClosenessCalculator.STPaser = new MinSpanningTree();
    }

    public ClosenessCalculator(DTNHost host) {
        super(host);
    }

    @Override
    public double getCentrality(){

        // Construct the Minimum Spanning Tree
        Node mstRoot = STPaser.getMinSpanTree(self);

        double MSTDistance = 0.0;
        double rootDistance = 0.0;
        DistanceMap dm = new DistanceMap();

        List<Node> visited = new LinkedList<Node>();
        Queue<Node> unvisited = new LinkedList<Node>();
        dm.put(mstRoot, 0);
        visited.add(mstRoot);
        unvisited.offer(mstRoot);

        Node u = null;

        while((u=unvisited.poll()) != null){
            for (Node v : u.getNeighbors()) {
                if (visited.contains(v)) {
                    continue;
                }
                visited.add(v);
                unvisited.offer(v);
                MSTDistance += getDistance(u, v);
                double dist= dm.get(u)+getDistance(u, v);
                dm.put(v, dist);
                rootDistance += dist;
            }
        }
        return MSTDistance/rootDistance;
    }

    private double getDistance(Node from, Node to) {
        return from.getLocation().distance(to.getLocation());
    }
}
