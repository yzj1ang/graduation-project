package routing.centrality.utility;

public class Duration {
    // start time
	double start;

    // end time
	double end;

	public Duration(){
		start = 0;
		end = 0;
	}
	public Duration(double s, double e){
		start = s;
		end = e;
	}
	public double getStart() {
		return start;
	}
	public void setStart(double start) {
		this.start = start;
	}
	public double getEnd() {
		return end;
	}
	public void setEnd(double end) {
		this.end = end;
	}

	public double calInterval(){
		return end-start;
	}

	public double calRestTime(double time){
		return end - time;
	}

	public boolean inDuration(double time){
		return time < end && time > start;
	}

	public boolean isAfter(double time){
		return time > end;
	}
}
