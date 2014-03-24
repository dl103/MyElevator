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
		return this.currentFloor;
	}
	
	@Override
	public void run() {
		Elevator elevator = null;
		if (this.currentFloor > this.requestedFloor){
			elevator = this.building.CallDown(currentFloor);
		}
		
		if (this.currentFloor < this.requestedFloor){
			elevator = this.building.CallUp(currentFloor);
		}
		System.out.println("Rider " + riderID+ " has figured its elevator");
		elevator.Enter(this);
		System.out.println("Rider " + riderID+ " has entered");
		elevator.RequestFloor(requestedFloor);
		System.out.println("Rider " + riderID+ " has requested floor " + requestedFloor);
		elevator.Exit();
		System.out.println("Rider " + riderID+ " has exited");
		
	}
	
}
