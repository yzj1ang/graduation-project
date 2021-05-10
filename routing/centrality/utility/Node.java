package routing.centrality.utility;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import core.Coord;
import core.DTNHost;

public class Node implements Comparable<Node> {

    // DTN host 中的数据在不断更新，必须通过`DTNHost`的getter获得更新后的数据，如：host.getLocation()。
    private int address;
    private DTNHost host;
    private Vector<Node> neighbors;

    public Node(DTNHost host) {
		this.host = host;
        this.address = host.getAddress();
		this.neighbors = new Vector<Node>();
	}

    public Node(Node node){
        this.address = node.getAddress();
        this.host = node.getHost();
        this.neighbors = new Vector<Node>();
    }

    public boolean addNeighbor(Node node) {
        if (node == null) {
            return false;
        }

        if (!this.neighbors.contains(node) && node != this) {
            this.neighbors.add(node);
            node.addNeighbor(this);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeNeighbor(Node node){
        if (node == null) {
            return false;
        }

        if (this.neighbors.contains(node)) {
            this.neighbors.remove(node);
            node.removeNeighbor(this);
            return true;
        } else {
            return false;
        }
    }

    public int getAddress() {
        return this.address;
    }

    public DTNHost getHost(){
        return this.host;
    }

    public Coord getLocation() {
        return host.getLocation();
    }

    public List<Node> getNeighbors() {
        return this.neighbors;
    }

    @Override
    public String toString() {
        List<Integer> neighborsAddress = new LinkedList<>();
        for (Node i : neighbors) {
            neighborsAddress.add(i.getAddress());
        }
        return "Node: " + this.address + " Contains:" + neighborsAddress + "\n";
    }

    public int compareTo(Node o) {
        return getLocation().compareTo((o).getLocation());
    }
}
