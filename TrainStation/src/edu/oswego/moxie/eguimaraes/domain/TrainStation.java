package edu.oswego.moxie.eguimaraes.domain;

import java.util.Random;

import edu.oswego.moxie.eguimaraes.animation.GraphicElement;
import edu.oswego.moxie.eguimaraes.control.Control;

public class TrainStation extends GraphicElement implements Runnable {

	private static final String IMAGE_LOCATION = "img/train_station.png";

	private String name;

	private Line line;

	public TrainStation(String name) {
		super(IMAGE_LOCATION);
		this.name = name;
		line = new Line(Control.LINE_CAPACITY);
	}

	@Override
	public String toString() {
		return name.substring(0, 3);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(new Random().nextInt((int) Control.PASSENGER_GENERATION_TIME)*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < new Random().nextInt(Control.MAX_NUMBER_PASSENGERS_GENERATED); i++) {
				generatePassenger();
			}
		}
	}

	private void generatePassenger() {
		TrainStation destination;
		destination = Control.stations.get(new Random().nextInt(Control.stations.size()));
		while (destination.equals(this)) {
			destination = Control.stations.get(new Random().nextInt(Control.stations.size()));
		}
		try {
			if(line.offer(new Passenger(destination), 0))
				Control.counterPassengers.add(1);
		} catch (InterruptedException e) {
			// TODO??
			e.printStackTrace();
		}
	}

	public Line getPassengers() {
		return line;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfPassengers() {
		try {
			return line.size();
		} catch (java.util.ConcurrentModificationException e) {
			return 0;
		}
	}

}
