
public class ElevatorEventBarrier{

	private int myCount;
	private boolean myDoorsOpen;

	//instantiate the class
	public ElevatorEventBarrier() {
		myDoorsOpen = false;
		myCount = 0;
	}


	public synchronized void arrive(int requestedFloor, Runnable rider) {
		if (!myDoorsOpen){ //wait until an event is signaled
			try{
				System.out.println("Rider is sleeping");
				rider.wait();
				System.out.println("Rider woke up");
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		myCount++;
	}


	public synchronized void raise(int currentFloor) { //called by elevator thread as it arrives
		notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void complete() {//called by rider thread
		myCount--;
		if (myCount == 0) {
			notifyAll();
		}
	}

	public synchronized int waiters() {
		return myCount;
	}

	public boolean getDoorsOpen() {
		return myDoorsOpen;
	}

	public void openDoors() {
		myDoorsOpen = true;
	}

	public void closeDoors() {
		myDoorsOpen = false;
	}
	
	public synchronized void manualWait() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
