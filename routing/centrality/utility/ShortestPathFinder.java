package routing.centrality.utility;

import java.util.List;
import java.util.LinkedList;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.PriorityQueue;

/**
 * A Shortest Paht Finder besed on the Dijkstra Algorithm.
 */
public class ShortestPathFinder {

    private static final int PQ_INIT_SIZE = 11;
    // unvisited实际是一个大根堆（heap）存储最优的pi(n)，每次加入pi(n)值最小的节点n（贪婪）。
    private Queue<Node> unvisited;
    private Set<Node> visited;

    private DistanceMap distances;
    private Map<Node, Node> prevNodes;

    public ShortestPathFinder(){
    }

    private void initWith(Node node) {

        // create needed data structures
        this.visited = new HashSet<Node>();
        this.distances = new DistanceMap();
        this.prevNodes = new HashMap<Node, Node>();
        this.unvisited = new PriorityQueue<Node>(PQ_INIT_SIZE, new DistanceComparator(this.distances));

        // set distance to source 0 and initialize unvisited queue
        this.distances.put(node, 0);
        this.unvisited.add(node);
        this.visited.add(node);
    }

    /**
     * LinkedList基于双向链表：
     *      1. 随机访问集合元素时性能较差，因为需要在双向列表中找到要index的位置，复杂度为 O(n)。
     *      2. 在插入，删除操作更快（复杂度仅为 O(1)），因为只需要改变链表插入位置的两个指针。
     *      3. LinkedList需要更多的内存，因为除了储存数据本身还要存储前后节点的指针。
     * ArrayList是基于数组：
     *      1. 使用索引在数组中搜索和读取数据的时间复杂度为 O(1)，直接返回数组中index位置的元素。
     *      2. 在随机访问集合元素上有较好的性能。
     *      3. 要插入、删除数据却是开销很大的，因为这需要移动数组中插入位置之后的的所有元素，O(n)。
     */
    public List<Node> getShortestPath(Node from, Node to) {
        List<Node> path = new LinkedList<Node>();

        if (from.compareTo(to) == 0) {
            path.add(from);
            return path;
        }

        initWith(from);
        Node currentNode = null;

        // unvisited 是一个PriorityQueue，这里`.poll()`从优先队列中取出最小值（贪婪）
        // 在`setDistance()`中更新PriorityQueue。
        while ((currentNode = unvisited.poll()) != null) {
            if (currentNode == to) { // 比较两个对象的地址，是否为同一个对象
                break;
            }
            processCurrentNode(currentNode);
        }

        if (currentNode == to) { // found a path from `from` to `to`, and current node is `to`
            path.add(0, to);
            Node prev = prevNodes.get(to);
            while (prev != from) {
                path.add(0, prev); // always put previous node to beginning
                prev = prevNodes.get(prev);
            }
            path.add(0, from); // finally put the source node to first node
        }

        return path;
    }

    private void processCurrentNode(Node u) {
        double pi_u = distances.get(u);     // get the π(u)
        for (Node v : u.getNeighbors()) {   // v ∈ N(u) \ visited
            if (visited.contains(v)) {continue;}
            visited.add(v);

            /**
             * if π(v) > π(u) + distance(uv)
             *  then
             *      π(v) ← π(u) + distance(uv);
             *      p(v) ← u;
             */
            double pi_v = pi_u + getDistance(u, v);

            if (distances.get(v) > pi_v) {
                setDistance(v, pi_v);
                prevNodes.put(v, u);
            }
        }
    }

    // Sets the distance from source node to a node
    private void setDistance(Node node, double distance) {
        unvisited.remove(node);         // remove node from old place in the heap (PriorityQueue)
        distances.put(node, distance);  // update distance
        unvisited.add(node);            // insert node to the new place in the heap (PriorityQueue)
    }

    private double getDistance(Node from, Node to) {
        return from.getLocation().distance(to.getLocation());
    }
}
