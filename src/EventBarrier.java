
public class EventBarrier extends AbstractEventBarrier {

	private int count;
	private boolean isSignaled;

	//instantiate the class
	public EventBarrier() {
		isSignaled = false;
		count = 0;
	}

	@Override
	public synchronized void arrive() {
		count++;
		if (isSignaled){//if signaled, just return
			return;
		}
		else{//wait until an event is signaled
			try{
				wait();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void raise() {//called by producer thread
		//System.out.println("Gatekeeper calling raise");
		setSignal(true); //signal the event
		notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setSignal(false);//reverts to the unsignaled state
		//System.out.println("Closing the gate");
	}

	@Override
	public synchronized void complete() {//called by consumer thread
		count--;
		if (count == 0) {
			notifyAll();
		}
	}

	@Override
	public synchronized int waiters() {
		return count;
	}
	
	public boolean getSignal() {
		return isSignaled;
	}
	
	public void setSignal(boolean signal) {
		isSignaled = signal;
	}
}
