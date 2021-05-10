package routing.centrality;

import java.util.List;
import java.util.HashMap;

import core.DTNHost;
import routing.centrality.utility.Node;
import routing.centrality.utility.BFSearch;
import routing.centrality.utility.ShortestPathFinder;

public class BetweennessCalculator extends CentralityCalculator{

    private static boolean outdated;
    private static HashMap<Node, Boolean> isUpdated;
    private static ShortestPathFinder PathFinder;
    private static BFSearch NodeSearch;

    static {
        BetweennessCalculator.outdated = true; // initialization is outdated, need to be updated
        BetweennessCalculator.isUpdated = new HashMap<Node, Boolean>();
        BetweennessCalculator.PathFinder = new ShortestPathFinder();
        BetweennessCalculator.NodeSearch = new BFSearch();
    }

    private double centrality;

    public BetweennessCalculator(DTNHost host) {
        super(host);
        this.centrality = 0;
        BetweennessCalculator.isUpdated.put(self, false);
    }

    // Set cache outdate
    private static void cacheOutDate() {
        BetweennessCalculator.outdated = true;
        for (Node i : isUpdated.keySet()) {
            isUpdated.put(i, false);
        }
    }

    // check all Nodes have been updated.
    private static void verifyUpdate() {
        boolean hasUpdated = true;
        for (Node i : isUpdated.keySet()) {
            hasUpdated = hasUpdated && isUpdated.get(i);
        }
        if (hasUpdated) {
            BetweennessCalculator.outdated = false;    // if all nodes have been updated, it it not outdate.
        }
    }

    @Override
    public void addContactNode(Node encountered) {
        self.addNeighbor(encountered);
        cacheOutDate();
    }

    @Override
    public void endContactNode(Node left) {
        self.removeNeighbor(left);
        cacheOutDate();
    }

    @Override
    public double getCentrality(){
        if(outdated){
            // obtain connected Network through BF-Search
            List<Node> currentNodes = NodeSearch.getMapSet(self);
            currentNodes.remove(self);   // Remove currentNode itself.

            int count = 0;
            int nodeSize = currentNodes.size();
            if(nodeSize == 1){
                return 1.0;  // No other hosts, only can forward the message to this node;
            }

            for(int i=0; i<nodeSize; i++){
                for(int j=i+1; j<nodeSize; j++){

                    List<Node> sp = PathFinder.getShortestPath(currentNodes.get(i), currentNodes.get(j));

                    if(sp.contains(self)){
                        count++;
                    }
                }
            }

            double numOfShortestPath = (nodeSize*(nodeSize-1))/2.0;
            this.centrality = (double)count/numOfShortestPath;
            BetweennessCalculator.isUpdated.put(self, true);
            verifyUpdate();
        }
        return this.centrality;
    }

    @Override
    public String toString() {
        return super.toString() + "\tisUpdate: " + isUpdated.get(self);
    }
}
