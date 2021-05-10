package routing;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import core.Settings;
import core.Connection;
import core.DTNHost;
import core.DTNSim;
import core.Message;
import core.MessageListener;
import routing.centrality.CentralityCalculator;

public abstract class CentralityRouter extends ActiveRouter{

    private static int count;
    private static HashMap<Integer, Double> CValue;
    static {
        DTNSim.registerForReset(CentralityRouter.class.getCanonicalName());
        reset();
    }
    public static void reset() {
        count = 13;
        CValue = new HashMap<Integer, Double>();
    }

    public void printC() {
        if (count > 0 && CValue.get(this.getHost().getAddress()) == null) {
            CValue.put(this.getHost().getAddress(), calculator.getCentrality());
            count--;
        }
        if (count == 0) {
            for (Integer i : CValue.keySet()) {
                System.out.println("Node: " + i + " \t- " + CValue.get(i));
            }
            count--;
        }
    }

    /**
     * 每一个CentralityRouter都包含一个含有`Node`成员变量的 CentralityCalculator(父类)。
     * 对应到不同CentralityCalculator的实例：Degree, Betweenness, Closeness.
     */
    protected CentralityCalculator calculator;

    public CentralityRouter(Settings s) {
		super(s);
	}

    protected CentralityRouter(CentralityRouter r) {
		super(r);
	}

    /**
     * TODO: Need to implement in the subclass.
     */
    @Override
    public abstract MessageRouter replicate();

    /**
     * TODO: Need to initialize the `calculator` with a concrete centrality calculator.
     */
    @Override
    public void init(DTNHost host, List<MessageListener> mListeners) {
        super.init(host, mListeners);
    }

    @Override
    public void update() {
        super.update();
        if (isTransferring() || !canStartTransfer()) {
            return; // can't start a new transfer
        }

        // Try only the messages that can be delivered to final recipient
        if (exchangeDeliverableMessages() != null) {
            return; // started a transfer
        }
        CentralityTransfer();
        printC();
    }

    @Override
    public void changedConnection(Connection con) {
        /**
         * OtherNode必须是 `CentralityRouter` 才能保证有 `calculator`实例。
         * 同时，`CentralityCalculator`作为抽象类，保证其子类（Degree, Betweenness, Closeness）
         * 都有 `addContactNode()`, `endContactNode()`，`getCentrality()` 和 `getNode()`方法
         */
        MessageRouter mr = con.getOtherNode(getHost()).getRouter();
        CentralityRouter cr = null;
        if (mr instanceof CentralityRouter) {
            cr = (CentralityRouter) mr;
        } else {
            return;
        }
        assert cr != null : "CentralityRouter should not be null";

        if (con.isUp()) {
            calculator.addContactNode(cr.getCalculator().getNode());
        } else {
            calculator.endContactNode(cr.getCalculator().getNode());
        }
    }

    public void CentralityTransfer() {
        double maxDegree = 0;
        Connection targetConnection = null;

        List<Connection> connections = getConnections();
        for (Connection con : connections) {
            double degree = 0;
            MessageRouter neighborRouter = con.getOtherNode(getHost()).getRouter();

            if (neighborRouter instanceof CentralityRouter) {
                CentralityRouter cr = (CentralityRouter) neighborRouter;
                degree = cr.getCalculator().getCentrality();
            }
            if (degree > maxDegree) {
                maxDegree = degree;
                targetConnection = con;
            }
        }

        if (targetConnection != null) {
            List<Message> messages = new ArrayList<Message>(this.getMessageCollection());
            tryAllMessages(targetConnection, messages);
        }
    }

    public CentralityCalculator getCalculator() {
        return this.calculator;
    }

    // use one-copy or not (comment this function.)
    @Override
    protected void transferDone(Connection con) {
        /* don't leave a copy for the sender */
        this.deleteMessage(con.getMessage().getId(), false);
    }

    /**
     *  Override the toString method.
     * @param type : type of Centrality : Degree, Betweenness, Closeness
     * @return The description string of this CentralityRouter.
     */
    public String toString(String type) {
        return "The "+type+" centrality of node " + this.getHost().toString() + " is:"
                + Double.toString(getCalculator().getCentrality());
    }
}
