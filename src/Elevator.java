import java.util.ArrayList;
import java.util.TreeSet;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Elevator extends AbstractElevator implements Runnable {

	public static final int DIRECTION_NEUTRAL = 0;
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_DOWN = -1;

	public static final int MAX_CAPACITY = 1;

	private ElevatorEventBarrier[] myUpBarriers;
	private ElevatorEventBarrier[] myDownBarriers;
	private ElevatorEventBarrier[] myOutBarriers;
	private Integer myDirection;
	private TreeSet<Integer> myDestinations;
	private Integer myFloor;
	
	private BufferedWriter myFileWriter;

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
	
	public void setWriter(BufferedWriter writer) {
        myFileWriter = writer;
    }
    
    /**
     * Writes to output file.
     */
    public void write(String string) {
        synchronized (myFileWriter) {
            try {
            	System.out.println("writing");
                myFileWriter.write(string);
                myFileWriter.newLine();
                myFileWriter.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	public void addFloor(int floor) {
		synchronized(myDestinations) {
			myDestinations.add(floor);
//			System.out.println("Adding floor " + floor + " to elevator");
		}	
	}
	
	public ElevatorEventBarrier[] getUpBarriers() {
		synchronized(myUpBarriers) {
			return myUpBarriers;
		}
	}
	
	public ElevatorEventBarrier[] getDownBarriers() {
		synchronized(myDownBarriers) {
			return myDownBarriers;
		}
	}
	
	public ElevatorEventBarrier[] getOutBarriers() {
		synchronized(myOutBarriers) {
			return myOutBarriers;
		}
	}

	/**
	 * Elevator control interface: invoked by Elevator thread.
	 */
	@Override
	public void OpenDoors() {
		int dir = getMyDirection();
		int flr = getFloor();
		if (dir == DIRECTION_UP && myUpBarriers[flr].waiters() > 0) {
//			System.out.println("Waking up " + myUpBarriers[flr].waiters() +
//					" on floor " + myFloor);
			myUpBarriers[flr].raise();
		}
		if (dir == DIRECTION_DOWN && myDownBarriers[flr].waiters() > 0) {
			myDownBarriers[flr].raise();
		}
		// Elevator is in neutral and there is someone waiting to go up. Consider
		// the edge cases. If there are people waiting to go up and down, the
		// elevator will take the person going up first.
		if (dir == DIRECTION_NEUTRAL) {
			if (myUpBarriers[flr].waiters() == 0) myDownBarriers[flr].raise();
			else myUpBarriers[flr].raise();
		}
		if (myOutBarriers[flr].waiters() > 0) myOutBarriers[flr].raise();
	}

	/**
	 * CheckDoors handles practical jokers who call the elevator and don't wait for it by checking the 
	 * waitCount for both upBarriers and downBarrier of that floor to determine if its necessary to stop
	 */
	
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
//		System.out.println("Closing doors");
	}

	@Override
	public void VisitFloor(int floor) {
		System.out.println("Visiting floor " + floor + " from " + getFloor());
		write("Visiting floor " + floor + " from " + getFloor());
		int dir = getMyDirection();
		if (floor-getFloor()==0){
			myDirection = DIRECTION_NEUTRAL;
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

	public boolean Enter(Rider rider) {
		addFloor(rider.requestedFloor);
		System.out.println("The list of destinations is " + myDestinations);
		int sumRiders = 0;
		for (int i = 0; i < myOutBarriers.length; i++) {
			sumRiders += myOutBarriers[i].waiters();
		}
		if (myFloor < rider.getFloor()) {
			//System.out.println("Added Rider " + rider.riderID + " to " + myUpBarriers[rider.requestedFloor].toString() + 
			//		"[" + rider.getFloor() + "]");
			//System.out.println(myUpBarriers[rider.requestedFloor]);
			if (sumRiders < MAX_CAPACITY) {
				myUpBarriers[rider.currentFloor].complete();
				return true;
			}
		} else {
			if (sumRiders < MAX_CAPACITY) {
				myDownBarriers[rider.currentFloor].complete();
				return true;
			}
			myUpBarriers[rider.currentFloor].complete();
		}
		return false;
	}

	@Override
	public void Exit(Rider r) {
		// Possible concurrency issue
		myOutBarriers[myFloor].complete();
		r.haveExited = true;
	}

	@Override
	public void RequestFloor(Rider rider, int floor) {
//		System.out.println("Adding floor " + floor + " to elevator");
		myDestinations.add(floor);
		rider.haveRequested = true;
//		System.out.println("The list of destinations from RequestFloor is " + myDestinations);
		myOutBarriers[floor].arrive();
	}

	/**
	 * Custom methods
	 */
	public int getMyDirection() {
		synchronized(myDirection) {
			return myDirection;
		}
	}

	public void setMyDirection(int direction) {
		myDirection = direction;
	}

	public int getFloor() {
		synchronized(myFloor) {
			return myFloor;
		}
	}

	/**
	 * Looped in a separate thread
	 */
	public void run() {
		while (true) {
			
			if (myDestinations.size() > 0) {
//				System.out.println("About to visit floor");
				int dir = getMyDirection();
				if (dir == DIRECTION_UP || dir == DIRECTION_NEUTRAL) {
					VisitFloor(myDestinations.first());
				}
				if (dir == DIRECTION_DOWN) {
					VisitFloor(myDestinations.last());
				}
//				System.out.println("Finished visiting floor");
			}
			if (CheckDoors(myFloor)) {
//				System.out.println("About to open doors");
				OpenDoors();
				ClosedDoors();
			}
			if (myDestinations.size() == 0) myDirection = DIRECTION_NEUTRAL;
		}
	}
}
