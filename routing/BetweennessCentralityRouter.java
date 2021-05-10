package routing;

import java.util.List;

import core.Settings;
import core.DTNHost;
import core.MessageListener;
import routing.centrality.BetweennessCalculator;

public class BetweennessCentralityRouter extends CentralityRouter{

    public BetweennessCentralityRouter(Settings s) {
		super(s);
	}

    protected BetweennessCentralityRouter(BetweennessCentralityRouter r) {
		super(r);
	}

    @Override
    public void init(DTNHost host, List<MessageListener> mListeners) {
        super.init(host, mListeners);
        this.calculator = new BetweennessCalculator(host);
    }

    @Override
    public BetweennessCentralityRouter replicate() {
        return new BetweennessCentralityRouter(this);
    }

    @Override
    public String toString() {
        String name = "Betweenness";
        return super.toString(name);
    }
}
