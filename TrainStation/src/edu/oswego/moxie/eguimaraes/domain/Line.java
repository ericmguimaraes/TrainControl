package edu.oswego.moxie.eguimaraes.domain;

import edu.oswego.moxie.eguimaraes.concurrent.BoundedBuffer;

public class Line extends BoundedBuffer {

	public Line(int capacity) {
		super(capacity);
	}

	public Passenger poll(TrainStation destination, long msecs) throws InterruptedException {
		if (Thread.interrupted())
			throw new InterruptedException();
		Passenger old = null;
		synchronized (this) {
			long start = (msecs <= 0) ? 0 : System.currentTimeMillis();
			long waitTime = msecs;

			while (usedSlots_ <= 0) {
				if (waitTime <= 0)
					return null;
				try {
					wait(waitTime);
				} catch (InterruptedException ex) {
					notify();
					throw ex;
				}
				waitTime = msecs - (System.currentTimeMillis() - start);
			}
			old = (Passenger) extract();
			incEmptySlots();
			int count = 0, limit = usedSlots_;
			while (old != null && !old.destination.equals(destination) && count < limit) {
				insert(old);
				incUsedSlots();
				old = (Passenger) extract();
				incEmptySlots();
				count++;
			}
			if (old != null && old.destination.equals(destination)) {
				return old;
			} else {
				if (old != null){
					insert(old);
					incUsedSlots();
				}
				return null;
			}
		}
	}

}
