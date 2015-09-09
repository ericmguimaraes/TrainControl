import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Train implements Runnable {

	public static enum TrainState {
		boarding, disembark, stopped, motion, departing, arriving
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
		state = TrainState.stopped;
		waitFor(new Random().nextInt(Control.STOPPING_TIME) * 4);
		while (!Thread.interrupted()) {
			state = TrainState.boarding;
			waitFor(Control.BOARDING_TIME);
			board();
			if (isReadyToDepart()) {
				state = TrainState.departing;
				waitFor(Control.DEPARTING_TIME);
				state = TrainState.motion;
				waitFor(Control.TRAVEL_TIME);
				travel();
				state = TrainState.arriving;
				waitFor(Control.ARRIVING_TIME);
				state = TrainState.disembark;
				waitFor(Control.DISEMBARK_TIME);
				disembark();
				state = TrainState.stopped;
				waitFor(new Random().nextInt(Control.STOPPING_TIME));
			}
		}
	}

	public boolean isReadyToDepart() {
		return ((state == TrainState.stopped || state == TrainState.boarding) && occupedSeats() == Control.NUMBER_SEATS)
				|| start;
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
		if (state == TrainState.disembark || state == TrainState.stopped) {
			for (Seat seat : seats) {
				if (seat.isOccuped())
					if (seat.passenger.hasArrived(currentStation))
						seat.passenger = null;
			}
			start = false;
		}
	}

	public void board() {
		if (state == TrainState.boarding || state == TrainState.stopped) {
			synchronized (currentStation.getPassengers()) {
				for (Passenger passenger : currentStation.getPassengers()) {
					if (passenger.destination.equals(getTrainDestination())) {
						boolean found = false;
						for (Seat seat : seats) {
							if (!seat.isOccuped()) {
								if (currentStation.removerPassenger(passenger)) {
									seat.passenger = passenger;
								}
								found = true;
							}
						}
						if (!found)
							break;
					}
					start = isReadyToDepart();
				}
			}
		}
	}

	private void travel() {
		state = TrainState.motion;
		track.busy = true;
		currentStation = getTrainDestination();
		track.busy = false;
		start = false;
	}

	TrainStation getTrainDestination() {
		if (currentStation.equals(track.location1))
			return track.location2;
		return track.location1;
	}

	public void waitFor(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
