package routing.centrality.utility;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;
import java.util.Comparator;

public class BFSearch {
    private List<Node> visited;
    private Queue<Node> unvisited;

    public BFSearch() {
    }

    private void initWith(Node node) {
        this.visited = new LinkedList<Node>();
        this.unvisited = new LinkedList<Node>();
        unvisited.offer(node);
        visited.add(node);
    }

    public List<Node> getMapSet(Node root) {
        initWith(root);
        Node node = null;

        while ((node = unvisited.poll()) != null) {
            for (Node n : node.getNeighbors()) {
                if (visited.contains(n)) {continue;}

                visited.add(n); //向visited中添加已经经过的节点，注意位置：标记为visited，然后放入unvisited队列。
                unvisited.offer(n);
            }
        }
        Collections.sort(visited, new seqComparator());
        return visited;
    }

    // 每个节点都有一个唯一个address (DTNHost: private static int nextAddress)
    // 根绝节点的address从小到大进行排序。
    private class seqComparator implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            int ad1 = node1.getAddress();
            int ad2 = node2.getAddress();

            if (ad1 > ad2) {
                return 1;
            } else if (ad1 < ad2) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}