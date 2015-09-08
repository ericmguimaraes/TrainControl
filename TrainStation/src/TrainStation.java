import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrainStation implements Runnable {

	String name;

	volatile List<Passenger> passengers; // TODO take care of parallelism

	public TrainStation(String name) {
		this.name = name;
		passengers = Collections.synchronizedList(new ArrayList<Passenger>());
	}

	@Override
	public String toString() {
		return name.substring(0, 3);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < new Random().nextInt(Control.MAX_NUMBER_PASSENGERS_GENERATED); i++) {
				generatePassenger();
			}
		}
	}

	private void generatePassenger() {
		//synchronized (Control.counterPassengers) {
			Control.counterPassengers++;
			TrainStation destination = Control.stations.get(new Random().nextInt(Control.stations.size()));
			passengers.add(new Passenger(destination));
		//}
	}
}
