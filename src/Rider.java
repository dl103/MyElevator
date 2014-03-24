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
	
	@Override
	public void run() {
		Elevator elevator = null;
		if (this.currentFloor > this.requestedFloor){
			elevator = this.building.CallDown(currentFloor);
		}
		
		if (this.currentFloor < this.requestedFloor){
			elevator = this.building.CallUp(currentFloor);
		}
		System.out.println("Rider has figured its elevator");
		elevator.Enter();
		System.out.println("Rider has entered");
		elevator.RequestFloor(requestedFloor);
		System.out.println("Rider has requested floor");
		elevator.Exit();
		System.out.println("Rider has exited");
		
	}
	
}
