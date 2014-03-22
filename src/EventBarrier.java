
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
		System.out.println("Gatekeeper calling raise");
		isSignaled = true;//signal the event
		notifyAll();

		//System.out.println(this);
		/*while(count>0){//block until all threads that wait for this event have responded
			//do nothing
		}*/

		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isSignaled = false;//reverts to the unsignaled state
		System.out.println("Closing the gate");
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
}
