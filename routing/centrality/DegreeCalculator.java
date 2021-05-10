package routing.centrality;

import core.DTNHost;

public class DegreeCalculator extends CentralityCalculator{

    public DegreeCalculator(DTNHost host) {
        super(host);
    }

    @Override
    public double getCentrality() {
        return self.getNeighbors().size();
    }
}
