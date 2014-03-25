public class Rider implements Runnable{

	protected int requestedFloor;
	protected int currentFloor;
	protected Building building;
	protected int riderID;

	public Rider(int riderID, int requestedFloor, int currentFloor, Building building) {
		this.riderID = riderID;
		this.requestedFloor = requestedFloor;
		this.currentFloor = currentFloor;
		this.building = building;
	}

	public int getFloor() {
		return this.requestedFloor;
	}

	@Override
	public void run() {
		Elevator elevator = null;
		while (elevator == null) {
			if (this.currentFloor > this.requestedFloor){
				elevator = this.building.CallDown(currentFloor);
			}

			if (this.currentFloor < this.requestedFloor){
				elevator = this.building.CallUp(currentFloor);
			}
		}
		synchronized(elevator) {
			System.out.println("Rider " + riderID+ " has figured its elevator");
			boolean entered = false;
			while (!entered) {
				entered = elevator.Enter(this);
				if (!entered) System.out.println("Denied from elevator");
			}
			System.out.println("Rider " + riderID+ " has entered");
			elevator.RequestFloor(requestedFloor);
		}
		System.out.println("Rider " + riderID+ " has requested floor " + requestedFloor);
		elevator.Exit();
		System.out.println("Rider " + riderID+ " has exited");
	}

}
