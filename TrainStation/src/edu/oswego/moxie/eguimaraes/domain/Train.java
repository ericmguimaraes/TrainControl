package edu.oswego.moxie.eguimaraes.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.oswego.moxie.eguimaraes.animation.GraphicElement;
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
			waitFor(new Random().nextInt(Control.BOARDING_TIME));
			board();
			if (isReadyToDepart()) {
				state = TrainState.departing;
				waitFor(Control.DEPARTING_TIME);
				travel();
				state = TrainState.arriving;
				waitFor(Control.ARRIVING_TIME);
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
		state = TrainState.disembark;
		currentStation = getTrainDestination();
		waitFor(Control.DISEMBARK_TIME);
		for (Seat seat : seats) {
			if (seat.isOccuped())
				if (seat.passenger.hasArrived(currentStation))
					seat.passenger = null;
		}
		start = false;
	}

	public void board() {
		try {
			if (state == TrainState.boarding || state == TrainState.stopped) {
				boolean found = false;
				Passenger passenger = null;
				passenger = currentStation.getPassengers().poll(getTrainDestination(), 100);
				while (passenger != null) {
					found = false;
					for (Seat seat : seats) {
						if (!seat.isOccuped()) {
							seat.passenger = passenger;
							found = true;
							break;
						}
					}
					if (!found) {
						currentStation.getPassengers().put(passenger);
						break;
					}
					passenger = currentStation.getPassengers().poll(getTrainDestination(), 100);
				}
			}
		} catch (InterruptedException e) {
			// TODO ???
			e.printStackTrace();
		}
		start = isReadyToDepart();
	}

	private void travel() {
		state = TrainState.motion;
		track.busy = true;
		long initialTime = System.currentTimeMillis(), timeSpent = 0;
		while (!updatePosition()) {
			waitFor(100);
			timeSpent = System.currentTimeMillis() - initialTime;
			if (timeSpent > Control.TRAVEL_TIME) {
				setX(getTrainDestination().getX());
				setY(getTrainDestination().getY());
				break;
			}
		}
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

	private boolean updatePosition() {
		int speed = 2;
		int xdiff = getX() - getTrainDestination().getX(), ydiff = getY() - getTrainDestination().getY();
		int range = 10;
		if (xdiff > -range && xdiff < range && ydiff > -range && ydiff < range) {
			setX(getTrainDestination().getX());
			setY(getTrainDestination().getY());
			return true;
		} else {
			if (state.equals(TrainState.motion)) {
				setY(getY() + ((ydiff > 0 ? -1 : 1) * track.getYPace(speed)));
				setX(getX() + ((xdiff > 0 ? -1 : 1) * track.getXPace(speed)));
				return false;
			} else {
				setX(getTrainDestination().getX());
				setY(getTrainDestination().getY());
				return true;
			}
		}
	}

	public double getAngle() {
		int x1 = track.location1.getX(), y1 = track.location1.getY(), x2 = track.location2.getX(),
				y2 = track.location2.getY();
		double angle = Math.toRadians(90) + Math.atan2(y2 - y1, x2 - x1);
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
