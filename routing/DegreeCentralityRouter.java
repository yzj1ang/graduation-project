package routing;

import java.util.List;

import core.Settings;
import core.DTNHost;
import core.MessageListener;
import routing.centrality.DegreeCalculator;

public class DegreeCentralityRouter extends CentralityRouter{

    public DegreeCentralityRouter(Settings s) {
		super(s);
	}

    protected DegreeCentralityRouter(DegreeCentralityRouter r) {
		super(r);
	}

    @Override
    public void init(DTNHost host, List<MessageListener> mListeners) {
        super.init(host, mListeners);
        this.calculator = new DegreeCalculator(host);
    }

    @Override
    public DegreeCentralityRouter replicate() {
        return new DegreeCentralityRouter(this);
    }

    @Override
    public String toString() {
        String name = "Degree";
        return super.toString(name);
    }
}
