import java.util.ArrayList;
import java.util.List;

public class Control implements Runnable {
	
	List<TrainStation> stations;
	
	List<Track> tracks;
	
	List<Train> trains;
	
	Thread passengersGenerator;
	
	public static final int numberSeats = 300;
	
	public Control() {
		initStations();
		initTracks();
		initTrains();
	}

	private void initTrains() {
		trains = new ArrayList<Train>();
		for (Track track : tracks) {
			trains.add(new Train(numberSeats, track, track.location1));
		}
	}

	private void initTracks() {
		tracks = new ArrayList<Track>();
		boolean alreadyExist;
		for (TrainStation station1 : stations) {
			for (TrainStation station2 : stations) {
				alreadyExist = false;
				for (Track track : tracks) {
					if(track.compareTrack(station1, station2))
						alreadyExist=true;
				}
				if(!alreadyExist)
					tracks.add(new Track(station1, station2));
			}
		}
	}

	private void initStations() {
		stations = new ArrayList<TrainStation>();
		stations.add(new TrainStation("Oswego"));
		stations.add(new TrainStation("New York"));
		stations.add(new TrainStation("Albany"));
		stations.add(new TrainStation("Buffalo"));
		stations.add(new TrainStation("Niagara Falls"));
		stations.add(new TrainStation("Plattsburgh"));
		stations.add(new TrainStation("Rochester"));
		stations.add(new TrainStation("Syracuse"));
	}

	@Override
	public void run() {
		
	}
	
}