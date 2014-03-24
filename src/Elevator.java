import java.util.ArrayList;
import java.util.TreeSet;

public class Elevator extends AbstractElevator implements Runnable {

	public static final int DIRECTION_NEUTRAL = 0;
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_DOWN = -1;

	public static final int MAX_CAPACITY = 1000;

	private ElevatorEventBarrier[] myUpBarriers;
	private ElevatorEventBarrier[] myDownBarriers;
	private ElevatorEventBarrier[] myOutBarriers;
	private int myDirection;
	private TreeSet<Integer> myDestinations;
	private int myFloor;

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		myUpBarriers = new ElevatorEventBarrier[numFloors+1];
		myDownBarriers = new ElevatorEventBarrier[numFloors+1];
		myOutBarriers = new ElevatorEventBarrier[numFloors+1];
		for(int i = 0; i < numFloors+1; i++){
			myUpBarriers[i] = new ElevatorEventBarrier(maxOccupancyThreshold);
			myDownBarriers[i] = new ElevatorEventBarrier(maxOccupancyThreshold);
			myOutBarriers[i] = new ElevatorEventBarrier(maxOccupancyThreshold);
		}
		myDirection = DIRECTION_NEUTRAL;
		myDestinations = new TreeSet<Integer>();
		myFloor = 1;
	}

	public void addFloor(int floor) {
		myDestinations.add(floor);
	}

	/**
	 * Elevator control interface: invoked by Elevator thread.
	 */
	@Override
	public void OpenDoors() {
		if (myDirection == DIRECTION_UP && myUpBarriers[myFloor].waiters() > 0) {
			myUpBarriers[myFloor].raise();
		}
		if (myDirection == DIRECTION_DOWN && myDownBarriers[myFloor].waiters() > 0) {
			myDownBarriers[myFloor].raise();
		}
		if (myOutBarriers[myFloor].waiters() > 0) myOutBarriers[myFloor].raise();
	}

	public boolean CheckDoors(int floor) {
		//System.out.println(myUpBarriers[floor].toString() + " " + myUpBarriers[floor].waiters());
		if (myUpBarriers[floor].waiters() > 0 || myDownBarriers[floor].waiters() > 0 ||
				myOutBarriers[floor].waiters() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 */
	@Override
	public void ClosedDoors() {
		System.out.println("Closing doors");
	}

	@Override
	public void VisitFloor(int floor) {
		System.out.println("Visiting floor " + floor);
		if (floor-myFloor==0){
			myDirection=DIRECTION_NEUTRAL;
		}
		else{
			myDirection = (floor - myFloor)/Math.abs(floor - myFloor);
		}
		myFloor = floor;
		myDestinations.remove(floor);
	}

	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
	 */
	@Override
	public synchronized boolean Enter(Rider rider) {
		addFloor(rider.getFloor());
		if (myFloor < rider.getFloor()) {
			//System.out.println("Added Rider " + rider.riderID + " to " + myUpBarriers[rider.requestedFloor].toString() + 
			//		"[" + rider.getFloor() + "]");
			//System.out.println(myUpBarriers[rider.requestedFloor]);
			myUpBarriers[rider.currentFloor].arrive();
			myUpBarriers[rider.currentFloor].complete();
		} else {
			myDownBarriers[rider.currentFloor].arrive();
			myDownBarriers[rider.currentFloor].complete();
		}
		return true;
	}

	@Override
	public void Exit() {
		// Possible concurrency issue
		myOutBarriers[myFloor].complete();
	}

	@Override
	public void RequestFloor(int floor) {
		myDestinations.add(floor);
		System.out.println("Added floor " + floor);
		myOutBarriers[floor].arrive();
	}

	/**
	 * Custom methods
	 */
	public int getMyDirection() {
		return myDirection;
	}

	public void setMyDirection(int direction) {
		myDirection = direction;
	}

	public int getFloor() {
		return myFloor;
	}

	/**
	 * Looped in a separate thread
	 */
	public void run() {
		while (true) {
			if (myDestinations.size() > 0) {
				System.out.println("About to visit floor");
				if (myDirection == DIRECTION_UP || myDirection == DIRECTION_NEUTRAL) {
					VisitFloor(myDestinations.first());
				}
				if (myDirection == DIRECTION_DOWN) {
					VisitFloor(myDestinations.last());
				}
				System.out.println("Finished visitng floor");
			}
			if (CheckDoors(myFloor)) {
				System.out.println("About to open doors");
				OpenDoors();
				ClosedDoors();
			}
			if (myDestinations.size() == 0) myDirection = DIRECTION_NEUTRAL;
		}
	}
}
