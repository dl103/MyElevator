
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
		
		synchronized(waiterLock) {
            while (numCrossed < maxCapacity && numWaiters > 0) {
                try {
                    waiterLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        synchronized(exitLock) {
            canExit = true;
            exitLock.notifyAll(); //allows the riders to complete complete() by releasing the exitLock
        }

        synchronized(enterLock) {
            canPass = false;
        }
	}

	public void arrive() {
		synchronized(waiterLock) {
			numWaiters++;
		}
		synchronized(enterLock) {
			while(!canPass) {
				try {
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
//			System.out.println("complete once");
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

	public void decrementCounter() {
		synchronized(waiterLock) {
			numWaiters--;
			numCrossed++;
		}
	}
	
}

