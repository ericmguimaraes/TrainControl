import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrainStation implements Runnable {

	String name;

	private volatile CopyOnWriteArrayList<Passenger> passengers; // TODO take care of parallelism

	public TrainStation(String name) {
		this.name = name;
		passengers = new CopyOnWriteArrayList<Passenger>();
	}

	@Override
	public String toString() {
		return name.substring(0, 3);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < new Random().nextInt(Control.MAX_NUMBER_PASSENGERS_GENERATED); i++) {
				generatePassenger();
			}
		}
	}

	private void generatePassenger() {
		synchronized (passengers) {
			Control.counterPassengers++;
			TrainStation destination;
			destination = Control.stations.get(new Random().nextInt(Control.stations.size()));
			while(destination.equals(this)){
				destination = Control.stations.get(new Random().nextInt(Control.stations.size()));
			}
			passengers.add(new Passenger(destination));
		}
	}
	
	boolean addPassenger(Passenger passenger){
		synchronized (passengers) {
			return passengers.add(passenger);
		}
	}
	
	boolean removerPassenger(Passenger passenger){
		synchronized (passengers) {
			return passengers.remove(passenger);
		}
	}
	
	CopyOnWriteArrayList<Passenger> getPassengers(){
		return passengers;
	}
}
