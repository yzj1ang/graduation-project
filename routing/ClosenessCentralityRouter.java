package routing;

import java.util.List;

import core.Settings;
import core.DTNHost;
import core.MessageListener;
import routing.centrality.ClosenessCalculator;

public class ClosenessCentralityRouter extends CentralityRouter{

    public ClosenessCentralityRouter(Settings s) {
		super(s);
	}

    protected ClosenessCentralityRouter(ClosenessCentralityRouter r) {
		super(r);
	}

    @Override
    public void init(DTNHost host, List<MessageListener> mListeners) {
        super.init(host, mListeners);
        this.calculator = new ClosenessCalculator(host);
    }

    @Override
    public ClosenessCentralityRouter replicate() {
        return new ClosenessCentralityRouter(this);
    }

    @Override
    public String toString() {
        String name = "Closeness";
        return super.toString(name);
    }
}
