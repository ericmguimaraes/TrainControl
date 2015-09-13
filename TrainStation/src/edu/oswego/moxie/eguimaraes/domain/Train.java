package edu.oswego.moxie.eguimaraes.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.oswego.moxie.eguimaraes.control.Control;

public class Train extends GraphicElement implements Runnable {

	public static enum TrainState {
		boarding, disembark, stopped, motion, departing, arriving
	}

	private static final String IMAGE_LOCATION = "img/train.png";

	private String name;

	List<Seat> seats;

	private TrainState state;

	Track track;

	private TrainStation currentStation;

	private boolean direction;

	private boolean start;

	public Train(int numberSeats, Track track, TrainStation currentStation) {
		super(IMAGE_LOCATION);
		seats = new ArrayList<Seat>();
		for (int i = 0; i < numberSeats; i++) {
			seats.add(new Seat());
		}
		this.track = track;
		this.currentStation = currentStation;
		name = track.toString();
		direction = true;
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

	public void updatePosition() {
		int speed = 2;
		int x1 = track.location1.getX(), y1 = track.location1.getY(), x2 = track.location2.getX(),
				y2 = track.location2.getY();
		double angle = Math.atan2(y2 - y1, x2 - x1);
		
		
		
		
		if(getTrainDestination().equals(track.location1)){
			if (state.equals(TrainState.motion) && Math.abs(getX() - x1) > 0 && Math.abs(getY() - y1) > 0){
				setX((int) (getX() + speed));//*Math.sin(angle)));
				setY((int) (getY() + speed));//*Math.cos(angle)));
			}
		} else {
			if (state.equals(TrainState.motion) && Math.abs(getX() - x2) > 0 && Math.abs(getY() - y2) > 0){
				setX((int) (getX() - speed));//*Math.sin(angle)));
				setY((int) (getY() - speed));//*Math.cos(angle)));
			}
		}
	}

	public void upgradeDirection() {
		// TODO Auto-generated method stub
	}

	public double getAngle() {
		int x1 = track.location1.getX(), y1 = track.location1.getY(), x2 = track.location2.getX(),
				y2 = track.location2.getY();
		double angle = Math.atan2(y2 - y1, x2 - x1);
		return angle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public TrainState getState() {
		return state;
	}

	public void setState(TrainState state) {
		this.state = state;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public TrainStation getCurrentStation() {
		return currentStation;
	}

	public void setCurrentStation(TrainStation currentStation) {
		if (this.currentStation != currentStation)
			direction = !direction;
		this.currentStation = currentStation;
	}

	public boolean isDirection() {
		return direction;
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public static String getImageLocation() {
		return IMAGE_LOCATION;
	}

}
