package routing.centrality.utility;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.PriorityQueue;

public class MinSpanningTree {

    private static final int PQ_INIT_SIZE = 11;

    private Queue<Node> unvisited;
    private Set<Node> visited;

    private DistanceMap distances;
    private Map<Node, Node> prevNode;

    public MinSpanningTree() {
    }

    private void initWith(Node node){

        // create needed data structures
        this.visited = new HashSet<Node>();
        this.distances = new DistanceMap();
        this.prevNode = new HashMap<Node, Node>();
        this.unvisited = new PriorityQueue<Node>(PQ_INIT_SIZE, new DistanceComparator(distances));

        // set distance to source 0 and initialize unvisited queue
        this.distances.put(node, 0);
        this.unvisited.add(node);
        this.visited.add(node);
    }

    public Node getMinSpanTree(Node node){
        Map<Integer, Node> mst = new HashMap<Integer, Node>();

        initWith(node);
        // u            currentNode
        // U            visited
        // N(u) \ U     unvisited
        // v            v ∈ N(u) \ U - 当前节点的临节点，且不在visited
        Node u = null;

        while((u = unvisited.poll()) != null){

            for (Node v : u.getNeighbors()) {   // v ∈ N(u) \ visited
                if (visited.contains(v)) {continue;}
                visited.add(v);

                double l_uv = getDistance(u, v);

                if (distances.get(v) > l_uv){
                    setDistance(v, l_uv);
                    prevNode.put(v, u);
                }
            }
        }

        // Construct the Minimum Spanning Tree.
        for(Node i : visited){
            Node temp = new Node(i);
            mst.put(temp.getAddress(), temp);
        }
        for(Node i : prevNode.keySet()){
            Node father = prevNode.get(i);
            mst.get(father.getAddress()).addNeighbor(mst.get(i.getAddress()));
        }

        return mst.get(Integer.valueOf(node.getAddress()));
    }

    // Sets the distance from source node to a node
    private void setDistance(Node node, double distance) {
        unvisited.remove(node); // remove node from old place in the heap (PriorityQueue)
        distances.put(node, distance); // update distance
        unvisited.add(node); // insert node to the new place in the heap (PriorityQueue)
    }

    private double getDistance(Node from, Node to) {
        return from.getLocation().distance(to.getLocation());
    }
}
