import java.util.ArrayList;
import java.util.TreeSet;

public class Elevator extends AbstractElevator implements Runnable {

	public static final int DIRECTION_NEUTRAL = 0;
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_DOWN = -1;

	private ArrayList<ElevatorEventBarrier> upEventBarriers;
	private ArrayList<ElevatorEventBarrier> downEventBarriers;
	private int myDirectionState;
	private TreeSet<Integer> myDestinations;
	private int myFloor;

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		for (int i = 0; i < numFloors; i++) {
			upEventBarriers.add(new ElevatorEventBarrier());
			downEventBarriers.add(new ElevatorEventBarrier());
		}
		myDirectionState = DIRECTION_NEUTRAL;
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
		upEventBarriers.get(myFloor).openDoors();
		upEventBarriers.get(myFloor).raise(myFloor);
	}
	
	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 */
	@Override
	public void ClosedDoors() {
		upEventBarriers.get(myFloor).closeDoors();
	}

	@Override
	public void VisitFloor(int floor) {
		if (floor-myFloor==0){
			myDirectionState=DIRECTION_NEUTRAL;
		}
		else{
			myDirectionState = (floor - myFloor)/Math.abs(floor - myFloor);
		}
		myFloor = floor;
		myDestinations.remove(floor);
	}

	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
	 */
	@Override
	public boolean Enter(Rider rider) {
		upEventBarriers.get(myFloor).arrive(rider.getFloor(), rider);
		return true;
	}

	@Override
	public void Exit() {
		upEventBarriers.get(myFloor).complete();
	}

	@Override
	public void RequestFloor(int floor) {
		myDestinations.add(floor);
		while (myFloor != floor) {
			try {
				upEventBarriers.get(myFloor).wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	/**
	 * Looped in a separate thread
	 */
	public void run() {
		while (true) {
			if (myDestinations.size() > 0) {
				if (myDirectionState == DIRECTION_UP) {
					VisitFloor(myDestinations.first());
				}
				if (myDirectionState == DIRECTION_DOWN) {
					VisitFloor(myDestinations.last());
				}
				if (myDirectionState == DIRECTION_NEUTRAL && myDestinations.size() > 0) {
					VisitFloor(myDestinations.first());
				}
				OpenDoors();
				ClosedDoors();
				if (myDestinations.size() == 0) myDirectionState = DIRECTION_NEUTRAL;
			}
		}
	}
}
