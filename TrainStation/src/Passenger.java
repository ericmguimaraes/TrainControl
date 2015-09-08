
public class Passenger {
	
	TrainStation currentStation;
	
	TrainStation destination;
	
	boolean waiting;
	
	public Passenger(TrainStation currentStation, TrainStation destination) {
		this.currentStation = currentStation;
		this.destination = destination;
		waiting = true;
	}

	boolean hasArrived(){
		return currentStation.equals(destination);
	}
	
	boolean isWaiting(){
		return waiting;
	}
	
}
