package edu.oswego.moxie.eguimaraes.domain;

public class Seat {
	
	Passenger passenger;

	boolean isOccuped(){
		return passenger!=null;
	}
}
