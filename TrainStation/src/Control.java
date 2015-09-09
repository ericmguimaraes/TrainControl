import java.util.ArrayList;
import java.util.List;

public class Control implements Runnable {

	public static List<TrainStation> stations;

	List<Track> tracks;

	List<Train> trains;

	public static final int NUMBER_SEATS = 200;

	public static final int MAX_NUMBER_PASSENGERS_GENERATED = 2000;

	public static final int TRAVEL_TIME = 3000;

	public static final int BOARDING_TIME = 2000;

	public static final int DISEMBARK_TIME = 2000;
	
	public static final int DEPARTING_TIME = 1000;
	
	public static final int ARRIVING_TIME = 1000;
	
	public static final int STOPPING_TIME = 2000;
	
	public static volatile int counterPassengers = 0;

	private List<Thread> threads;

	public Control() {
		initStations();
		initTracks();
		initTrains();
		initThreads();
	}

	private void initTrains() {
		trains = new ArrayList<Train>();
		for (Track track : tracks) {
			trains.add(new Train(NUMBER_SEATS, track, track.location1));
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

}