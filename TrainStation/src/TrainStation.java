import java.util.ArrayList;
import java.util.List;


public class TrainStation {
	
	String name;
	
	List<Passenger> passengers;
	
	public TrainStation(String name) {
		this.name = name;
		passengers = new ArrayList<Passenger>();
	}
	
	@Override
	public String toString() {
		return name.substring(0, 4);
	}
	
}
