import java.util.ArrayList;
import java.util.List;

public class Train implements Runnable {

	public static enum TrainState {
		boarding, stopped, motion, departing
	}

	String name;

	List<Seat> seats;

	TrainState state;

	Track track;

	TrainStation currentStation;

	boolean start;

	public Train(int numberSeats, Track track, TrainStation currentStation) {
		seats = new ArrayList<Seat>();
		for (int i = 0; i < numberSeats; i++) {
			seats.add(new Seat());
		}
		this.track = track;
		this.currentStation = currentStation;
		name = track.toString();
		start = false;
		state = TrainState.stopped;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			embark();
			travel();
			disembark();
		}
	}

	public boolean isReadyToDepart() {
		return (state == TrainState.stopped && occupedSeats() == Control.NUMBER_SEATS) || start;
	}

	public int occupedSeats() {
		int counter = 0;
		for (Seat seat : seats) {
			if (seat.isOccuped())
				counter++;
		}
		return counter;
	}

	public void disembark() {
		if (state == TrainState.stopped)
			for (Seat seat : seats) {
				if (seat.isOccuped())
					if (seat.passenger.hasArrived())
						seat.passenger = null;
			}
	}

	public void embark() {
		if (state == TrainState.stopped)
			synchronized (currentStation.passengers) {
				for (Passenger passenger : new ArrayList<>(currentStation.passengers)) {
					if (passenger.destination.equals(getTrainDestination())) {
						for (Seat seat : seats) {
							if (!seat.isOccuped()) {
								seat.passenger = passenger;
								currentStation.passengers.remove(passenger);
							}
						}
						start = isReadyToDepart();
					}
				}
			}
	}

	private void travel() {
		if (start) {
			state = TrainState.motion;
			track.busy = true;
			try {
				Thread.sleep(Control.TRAVEL_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentStation = getTrainDestination();
			track.busy = false;
			start = false;
			state = TrainState.stopped;
		}
	}

	TrainStation getTrainDestination() {
		if (currentStation.equals(track.location1))
			return track.location2;
		return track.location1;
	}

}
