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

	public Train(int numberSeats, Track track, TrainStation currentStation) {
		seats = new ArrayList<Seat>();
		for (int i = 0; i < numberSeats; i++) {
			seats.add(new Seat());
		}
		this.track = track;
		this.currentStation = currentStation;
		name = track.toString();
		state = TrainState.stopped;
	}

	public boolean isReadyToDepart() {
		return state == TrainState.stopped && occupedSeats() == Control.numberSeats;
	}

	public int occupedSeats() {
		int counter = 0;
		for (Seat seat : seats) {
			if (seat.isOccuped())
				counter++;
		}
		return counter;
	}

	public boolean disembark() {
		if (state == TrainState.stopped)
			for (Seat seat : seats) {
				if (seat.passenger.hasArrived())
					seats.iterator().remove();
			}
		return state == TrainState.stopped;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub	
	}

}
