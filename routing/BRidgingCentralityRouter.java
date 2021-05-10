package routing;

import java.util.List;

import core.Settings;
import core.DTNHost;
import core.MessageListener;
import routing.centrality.BRidgingCalculator;

public class BRidgingCentralityRouter extends CentralityRouter {

    public BRidgingCentralityRouter(Settings s) {
        super(s);
    }

    protected BRidgingCentralityRouter(BRidgingCentralityRouter r) {
        super(r);
    }

    @Override
    public void init(DTNHost host, List<MessageListener> mListeners) {
        super.init(host, mListeners);
        this.calculator = new BRidgingCalculator(host);
    }

    @Override
    public BRidgingCentralityRouter replicate() {
        return new BRidgingCentralityRouter(this);
    }

    @Override
    public String toString() {
        String name = "BRidging";
        return super.toString(name);
    }
}
