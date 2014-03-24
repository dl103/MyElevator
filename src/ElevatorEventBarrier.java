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


	public synchronized void arrive(int currentFloor, Runnable r) {
		if (riders.get(currentFloor) == null) {
			riders.put(currentFloor, new ArrayList<Runnable>());
		}
		riders.get(currentFloor).add(r);
		if (!myDoorsOpen){ //wait until an event is signaled
			try{
				wait();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		myCount++;
	}


	public synchronized void raise(int currentFloor) { //called by elevator thread as it arrives
		ArrayList<Runnable> floorRiders = riders.get(currentFloor);
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
