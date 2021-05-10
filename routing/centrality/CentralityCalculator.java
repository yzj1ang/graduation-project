package routing.centrality;

import core.DTNHost;
import routing.centrality.utility.Node;


public abstract class CentralityCalculator {

    protected Node self;

    public CentralityCalculator(DTNHost host){
        this.self = new Node(host);
    }

    public Node getNode(){
        return this.self;
    }

    public void addContactNode(Node encountered){
        self.addNeighbor(encountered);
    }

    public void endContactNode(Node left){
        self.removeNeighbor(left);
    }

    public abstract double getCentrality();

    @Override
    public String toString() {
        return "Node: " + self.getAddress() + "\tCentrality: " + getCentrality();
    }
}
