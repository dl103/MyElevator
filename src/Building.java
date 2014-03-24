import java.util.ArrayList;

public class Building extends AbstractBuilding{

	int numFloors;
	int numElevators;
	private ArrayList<Elevator> elevators;//arraylist to hold the elevators
	
	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		elevators = new ArrayList<>();
			
		int maxOccupancy=10;
		for (int i=0;i<numElevators;i++){
			//instantiate the correct number of elevators
			//then add them to arraylist elevators
			Elevator e = new Elevator(numFloors, i+1, maxOccupancy);
			elevators.add(e);
			Thread t = new Thread(e);
			t.start();
		}
		
		//need a queue system of sort for the riders
	}

	public Elevator CallUp(int fromFloor){
		//check for error cases such as rider on top floor calling up
		if ((fromFloor>=super.numFloors) || (fromFloor<0)){
			return null;
		}
		
		for (Elevator e: elevators){
			synchronized(e){
				//if the elevator is idle
				//or if the elevator is below the rider and it's going up
				if(e.getMyDirection()==Elevator.DIRECTION_NEUTRAL||(e.getMyDirection()==Elevator.DIRECTION_UP && e.getFloor()<=fromFloor)){
					//add in condition about space
					e.addFloor(fromFloor);
					System.out.println("Added Floor " + fromFloor + " to elevator");
					return e;
				}
			}
		}
		//else if there are no elevators
		return null;
	}

	public Elevator CallDown(int fromFloor){
		//check for error cases
		if ((fromFloor>super.numFloors) || (fromFloor<=0)){
			return null;
		}
		
		for (Elevator e: elevators){
			synchronized(e){
				//if the elevator is idle
				//or if the elevator is abobe the rider and it's going down
				if(e.getMyDirection()==Elevator.DIRECTION_NEUTRAL||(e.getMyDirection()==Elevator.DIRECTION_DOWN && e.getFloor()>=fromFloor)){//direction2 is going upwards
					//add in condition about space
					e.addFloor(fromFloor);
					return e;
				}
			}
		}
		//else if there are no elevators
		return null;
	}
    
	/* Other methods as needed goes here */
}
