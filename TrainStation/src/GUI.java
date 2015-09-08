public class GUI {

	public static void main(String[] args) {
		Control control = new Control();
		Thread controlThread = new Thread(control);
		controlThread.start();
		while (true)
			System.out.println(Control.stations.get(0).passengers.size());
	}
}
