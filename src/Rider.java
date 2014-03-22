import java.util.*;

public class Rider implements Runnable{
	
	protected int requestedFloor;
	protected Elevator elevator;
	protected int currentFloor;
	protected Building building;
	
	public Rider(int requestedFloor, int currentFloor, Elevator elevator, Building building) {
		this.requestedFloor = requestedFloor;
		this.currentFloor = currentFloor;
		this.elevator = elevator;
		this.building = building;
	}
	
	@Override
	public void run() {
		if (this.currentFloor > this.requestedFloor){
			this.elevator = this.building.CallDown(currentFloor);
		}
		
		if (this.currentFloor < this.requestedFloor){
			this.elevator = this.building.CallUp(currentFloor);
		}
		this.elevator.Enter();
		this.elevator.RequestFloor(requestedFloor);
		this.elevator.Exit();
	}
	
}
