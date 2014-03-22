public class Elevator extends AbstractElevator implements Runnable {
	
	public static final int DIRECTION_NEUTRAL = 0;
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_DOWN = -1;
	
	private ElevatorEventBarrier myEventBarrier;
	private int myDirectionState;
	private boolean[] myDestinations;
	private int myFloor;
	
	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		myEventBarrier = new ElevatorEventBarrier();
		myDirectionState = DIRECTION_NEUTRAL;
		myDestinations = new boolean[numFloors];
		myFloor = 1;
	}

	/**
	 * Elevator control interface: invoked by Elevator thread.
 	 */
	@Override
	public void OpenDoors() {
		myEventBarrier.openDoors();
		myEventBarrier.raise();
	}

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
 	 */
	@Override
	public void ClosedDoors() {
		myEventBarrier.closeDoors();
	}

	@Override
	public void VisitFloor(int floor) {
		myDirectionState = (floor - myFloor)/Math.abs(floor - myFloor);
		myFloor = floor;
	}

	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
  	 */
	@Override
	public boolean Enter() {
		myEventBarrier.arrive();
		return false;
	}

	@Override
	public void Exit() {
		myEventBarrier.complete();
	}

	@Override
	public void RequestFloor(int floor) {
		myDestinations[floor] = true;
	}
	
	/**
	 * Custom methods
	 */
	public int getMyDirection() {
		return myDirectionState;
	}
	
	public void setMyDirection(int direction) {
		myDirectionState = direction;
	}
	
	public int getFloor() {
		return myFloor;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
