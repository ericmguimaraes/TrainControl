
public class Passenger {

	TrainStation destination;

	boolean waiting;

	public Passenger(TrainStation destination) {
		this.destination = destination;
		waiting = true;
	}

	boolean hasArrived(TrainStation currentStation) {
		return currentStation.equals(destination);
	}

	boolean isWaiting() {
		return waiting;
	}

}
