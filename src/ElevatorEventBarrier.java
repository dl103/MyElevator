import java.util.ArrayList;


public class ElevatorEventBarrier{

	private int myCount;
	private boolean myDoorsOpen;
	private int[] myCounts;

	//instantiate the class
	public ElevatorEventBarrier(int numFloors) {
		myDoorsOpen = false;
		myCount = 0;
		myCounts = new int[numFloors+1];
	}


	public synchronized void arrive(int requestedFloor, Runnable rider) {
		if (!myDoorsOpen){ //wait until an event is signaled
			try{
				System.out.println("Rider is sleeping");
				myCounts[requestedFloor]++;
				wait();
				myCounts[requestedFloor]--;
				if (myCounts[requestedFloor] == 0) notifyAll();
				System.out.println("Rider woke up");
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		myCount++;
	}


	public synchronized void raise(int currentFloor) { //called by elevator thread as it arrives
		System.out.println("Calling raise");
		notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void complete() {//called by rider thread
		
	}

	public synchronized int waiters() {
		return myCount;
	}

	public boolean getDoorsOpen() {
		return myDoorsOpen;
	}

	public void openDoors() {
		System.out.println("Opening elevator doors");
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
