
public class ElevatorEventBarrier {

	private boolean canPass;
	private boolean canExit;
	private int numWaiters;
	private int numCrossed;
	private int maxCapacity;
	private Object enterLock, waiterLock, exitLock, caller, callerLock;

	public ElevatorEventBarrier(int cap) {
		canPass = false;
		canExit = false;
		enterLock = new Object();
		waiterLock = new Object();
		exitLock = new Object();
		callerLock = new Object();
		numWaiters = 0;
		numCrossed = 0;
		maxCapacity = cap;
	}

	public void raise() {
		synchronized(exitLock) {
			canExit = false;
			numCrossed = 0;
		}

		synchronized(enterLock) {
			canPass = true;
			enterLock.notifyAll();
		}
	}

	public void arrive() {
		synchronized(waiterLock) {
			numWaiters++;
		}
		System.out.println("Added to numWaiters");
		synchronized(enterLock) {
			while(!canPass) {
				try {
					System.out.println("Rider going to wait for enterLock");
					enterLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		/*
       	 synchronized(caller) {
            	return caller;
        }	
		 */
	}	
	public void complete() {
		synchronized(waiterLock) {
			numWaiters--;
			numCrossed++;
			waiterLock.notifyAll();
		}
		synchronized(exitLock){
			while(!canExit) {
				try {
					exitLock.wait();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
		}
	}

	public int waiters() {
		synchronized(waiterLock) {
			return numWaiters;
		}
	}

}

