
public class ElevatorEventBarrier extends AbstractEventBarrier {

	private int myCount;
	private boolean myDoorsOpen;

	//instantiate the class
	public ElevatorEventBarrier() {
		myDoorsOpen = false;
		myCount = 0;
	}

	@Override
	public synchronized void arrive() {
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

	@Override
	public synchronized void raise() {//called by producer thread
		notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void complete() {//called by consumer thread
		myCount--;
		if (myCount == 0) {
			notifyAll();
		}
	}

	@Override
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
