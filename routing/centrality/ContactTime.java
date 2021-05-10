package routing.centrality;

import core.DTNHost;
import core.SimClock;
import routing.centrality.utility.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ContactTime {

    private int timeRange;
    private HashMap<Integer, List<Duration>> contactDurations;

    public ContactTime (int timeRange){
        this.timeRange = timeRange;
        contactDurations = new HashMap<Integer, List<Duration>>();
    }

    public void addContactTime(DTNHost encountered){
        assert contactDurations!=null : "Errors: contactDurations has not been initialized";

        Duration duration = new Duration();
        duration.setStart(SimClock.getTime());

        Integer nodeAddress = encountered.getAddress();
        List<Duration> aDurations;
        if(!contactDurations.keySet().contains(nodeAddress)){
            aDurations = new ArrayList<Duration>();
            aDurations.add(duration);

            contactDurations.put(nodeAddress, aDurations);
        }
        else{
            aDurations = contactDurations.get(nodeAddress);
            aDurations.add(duration);
        }
    }

    public void endContactTime(DTNHost left) {
        assert contactDurations != null : "Errors: contactDurations has not been initialized";

        Integer nodeAddress = left.getAddress();
        List<Duration> aDurations = contactDurations.getOrDefault(nodeAddress, null);
        assert aDurations != null : "Error";

        Duration duration = aDurations.get(aDurations.size() - 1);
        duration.setEnd(SimClock.getTime());
    }

    public double getContactTimeMeasure(DTNHost node) {
        double totalContactTime = 0;
        double earliest = SimClock.getTime() - timeRange;
        Integer index = node.getAddress();

        List<Duration> aDurations = contactDurations.getOrDefault(index, null);
        assert aDurations != null : "Error";

        for (Duration duration : aDurations) {
            if (duration.getEnd() == 0) {
                duration.setEnd(SimClock.getTime());
            }
            if (duration.isAfter(earliest)) {
                aDurations.remove(duration);
            }
            if (duration.inDuration(earliest)) {
                totalContactTime += duration.calRestTime(earliest);
            } else {
                totalContactTime += duration.calInterval();
            }
        }
        return totalContactTime / timeRange;
    }

    public void setTimeRange(int timeRange) {
        this.timeRange = timeRange;
    }
}
