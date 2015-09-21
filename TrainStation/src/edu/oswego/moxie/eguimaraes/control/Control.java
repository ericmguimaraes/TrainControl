package edu.oswego.moxie.eguimaraes.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.oswego.moxie.eguimaraes.animation.Board;
import edu.oswego.moxie.eguimaraes.concurrent.SynchronizedInt;
import edu.oswego.moxie.eguimaraes.domain.Track;
import edu.oswego.moxie.eguimaraes.domain.Train;
import edu.oswego.moxie.eguimaraes.domain.TrainStation;

public class Control implements Runnable {

	public static List<TrainStation> stations;

	List<Track> tracks;

	public List<Train> trains;

	public static final int NUMBER_SEATS = 50;

	public static final int MAX_NUMBER_PASSENGERS_GENERATED = 250;

	public final static int LINE_CAPACITY = 150;

	public static final int TRAVEL_TIME = 5000;

	public static final int BOARDING_TIME = 2000;

	public static final int DISEMBARK_TIME = 2000;

	public static final int DEPARTING_TIME = 1000;

	public static final int ARRIVING_TIME = 1000;

	public static final int STOPPING_TIME = 2000;

	public static final long PASSENGER_GENERATION_TIME = 5; //seconds

	public static volatile SynchronizedInt counterPassengers;

	private List<Thread> threads;

	public Control() {
		counterPassengers = new SynchronizedInt(0);
		initStations();
		initTracks();
		initTrains();
		initThreads();
	}

	private void initTrains() {
		trains = new ArrayList<Train>();
		for (Track track : tracks) {
			trains.add(new Train(NUMBER_SEATS, track,
					new Random().nextInt(2) % 2 == 0 ? track.location1 : track.location2));
		}
	}

	private void initTracks() {
		tracks = new ArrayList<Track>();
		boolean alreadyExist;
		for (TrainStation station1 : stations) {
			for (TrainStation station2 : stations) {
				if (station1.equals(station2))
					continue;
				alreadyExist = false;
				for (Track track : tracks) {
					if (track.compareTrack(station1, station2))
						alreadyExist = true;
				}
				if (!alreadyExist)
					tracks.add(new Track(station1, station2));
			}
		}
	}

	private void initStations() {
		stations = new ArrayList<TrainStation>();
		stations.add(new TrainStation("Oswego"));
		stations.add(new TrainStation("NYC"));
		
		stations.add(new TrainStation("Albany"));
		stations.add(new TrainStation("Buffalo"));
		
		stations.add(new TrainStation("Niagara Falls"));
		stations.add(new TrainStation("Plattsburgh"));
		
		stations.add(new TrainStation("Rochester"));
		stations.add(new TrainStation("Syracuse"));
		
		stations.add(new TrainStation("San Diego"));
		stations.add(new TrainStation("Barbara"));
		
		stations.add(new TrainStation("Los Angeles"));
		stations.add(new TrainStation("Sacramento"));
		
		stations.add(new TrainStation("Miami"));
		stations.add(new TrainStation("Chicago"));
		
		stations.add(new TrainStation("Boston"));
		stations.add(new TrainStation("Philladelphia"));
	}

	private void initThreads() {
		threads = new ArrayList<Thread>();
		for (TrainStation station : stations) {
			threads.add(new Thread(station));
		}
		for (Train train : trains) {
			threads.add(new Thread(train));
		}
	}

	private void startThreads() {
		for (Thread thread : threads) {
			thread.start();
		}
	}

	private void interruptAllThreads() {
		for (Thread thread : threads) {
			thread.interrupt();
		}
	}

	@Override
	public void run() {
		startThreads();
		while (true) {
			if (Thread.interrupted()) {
				interruptAllThreads();
				break;
			}
		}
	}

	public void loadImages() {
		for (TrainStation station : stations) {
			station.loadImage();
		}
		for (Train train : trains) {
			train.loadImage();
		}
	}

	public void initStartLocations() {
		initStationLocations();
		initTrainLocations();
	}

	private void initTrainLocations() {
		for (Train train : trains) {
			train.setX(train.getCurrentStation().getX());
			train.setY(train.getCurrentStation().getY());
		}
	}

	private void initStationLocations() {
		int numberOfStations = stations.size();
		int baseDistance = (Board.WIDTH / numberOfStations) / 2;
		int i = 0, j = 1;
		for (TrainStation station : stations) {
			station.setX((baseDistance * j * 4) - (baseDistance * 2 + 20));
			if (i % 2 == 0) {
				station.setY(Board.HEIGHT / 3 / 2);
			} else {
				station.setY((Board.HEIGHT / 3 / 2) * 4);
				j++;
			}
			i++;
		}
		for (Train train : trains) {
			train.loadImage();
		}
	}

}