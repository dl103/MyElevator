public class Elevator extends AbstractElevator {
	
	public static final int DIRECTION_NEUTRAL = 0;
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_DOWN = 2;
	
	private AbstractEventBarrier myEventBarrier;
	private boolean myDoorsOpen;
	private int myDirectionState;
	private boolean[] myDestinations;
	private int myFloor;
	
	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		myEventBarrier = new EventBarrier();
		myDoorsOpen = false;
		myDirectionState = DIRECTION_NEUTRAL;
		myDestinations = new boolean[numFloors];
		myFloor = 1;
	}

	/**
	 * Elevator control interface: invoked by Elevator thread.
 	 */
	@Override
	public void OpenDoors() {
		myDoorsOpen = true;
		myEventBarrier.raise();
	}

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
 	 */
	@Override
	public void ClosedDoors() {
		myDoorsOpen = false;
	}

	@Override
	public void VisitFloor(int floor) {
		myFloor = floor;
	}

	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
  	 */
	@Override
	public boolean Enter() {
		if (myDoorsOpen) {
			myEventBarrier.arrive();
			return true;
		}
		return false;
	}

	@Override
	public void Exit() {
		if (myDoorsOpen) {
			myEventBarrier.complete();
		}
	}

	@Override
	public void RequestFloor(int floor) {
		myDestinations[floor] = true;
	}
	
	public int getMyDirection() {
		return myDirectionState;
	}
	
	public void setMyDirection(int direction) {
		myDirectionState = direction;
	}
	
	public int getFloor() {
		return myFloor;
	}
}
