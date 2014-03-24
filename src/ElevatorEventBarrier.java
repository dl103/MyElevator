import java.util.ArrayList;
import java.util.HashMap;


public class ElevatorEventBarrier{

	private int myCount;
	private boolean myDoorsOpen;
	private HashMap<Integer, ArrayList<Runnable>> riders;

	//instantiate the class
	public ElevatorEventBarrier() {
		myDoorsOpen = false;
		myCount = 0;
		riders = new HashMap<Integer, ArrayList<Runnable>>();
	}


	public synchronized void arrive(int requestedFloor, Runnable rider) {
		if (riders.get(requestedFloor) == null) {
			riders.put(requestedFloor, new ArrayList<Runnable>());
		}
		riders.get(requestedFloor).add(rider);
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
		ArrayList<Runnable> floorRiders = riders.get(currentFloor);
		System.out.println("Elevator raising from Floor " + currentFloor);
		if (floorRiders != null) {
			for (Runnable r : floorRiders) {
				r.notify();
			}
			floorRiders.clear();
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
}
