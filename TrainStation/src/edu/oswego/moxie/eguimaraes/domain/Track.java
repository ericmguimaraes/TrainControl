package edu.oswego.moxie.eguimaraes.domain;

public class Track {

	Boolean busy;

	public TrainStation location1;

	public TrainStation location2;

	public Track(TrainStation location1, TrainStation location2) {
		this.location1 = location1;
		this.location2 = location2;
		busy = false;
	}

	public boolean compareTrack(TrainStation station1, TrainStation station2) {
		if ((station1.equals(location1) && station2.equals(location2))
				|| (station2.equals(location1) && station1.equals(location2)))
			return true;
		return false;
	}
	
	boolean isBusy(){
		return busy;
	}
	
	@Override
	public String toString() {
		return location1.toString()+"/"+location2.toString();
	}
	
	public int getXPace(int speed){
		return Math.abs((location1.getX()-location2.getX())/(50/speed));
	}
	
	public int getYPace(int speed){
		return Math.abs((location1.getY()-location2.getY())/(50/speed));
	}

}