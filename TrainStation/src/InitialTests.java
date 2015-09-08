import java.util.Date;

public class InitialTests {

	public static void main(String[] args) {
		Control control = new Control();
		Thread controlThread = new Thread(control);
		controlThread.start();
		Train.TrainState lastState = Train.TrainState.boarding;
		long tempo = new Date().getTime();
		while (true) {
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			if (lastState != control.trains.get(0).state || tempo - (new Date().getTime()) < -5000) {
				tempo = new Date().getTime();
				lastState = control.trains.get(0).state;
				System.out.println("Number of passengers Station0: " + Control.stations.get(0).passengers.size());
				System.out.println("State of Train0 " + control.trains.get(0).name + ": " + control.trains.get(0).state);
			}
		}
	}
	
}
