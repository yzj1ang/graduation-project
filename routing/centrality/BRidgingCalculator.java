package routing.centrality;

import core.DTNHost;
import routing.centrality.utility.Node;

public class BRidgingCalculator extends BetweennessCalculator{

    public BRidgingCalculator(DTNHost host) {
        super(host);
    }

    @Override
    public double getCentrality(){
        double betweenness = super.getCentrality();

        double neighbourDegreeInverse = 0.0;

        for(Node i : self.getNeighbors()){
            neighbourDegreeInverse += 1.0 / i.getNeighbors().size();
        }

        double ridgingCoefficient = 1.0 / (self.getNeighbors().size() * neighbourDegreeInverse);

        return betweenness * ridgingCoefficient;
    }

}
