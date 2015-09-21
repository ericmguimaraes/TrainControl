package edu.oswego.moxie.eguimaraes.domain;

import edu.oswego.moxie.eguimaraes.concurrent.BoundedBuffer;

public class Line extends BoundedBuffer {

	public Line(int capacity) {
		super(capacity);
	}

	public synchronized Passenger poll(TrainStation destination, long timeout) throws InterruptedException {
		if (Thread.interrupted())
			throw new InterruptedException();
		synchronized (this) {
			Passenger result = (Passenger) poll(timeout);
			int count = 0;
			while (result != null && !result.destination.equals(destination) && count < size()) {
				put(result);
				result = (Passenger) poll(100);
				count++;
			}
			if (result != null && result.destination.equals(destination))
				return result;
			return null;
		}
	}

}
