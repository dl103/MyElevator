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
			t.setName("Elevator " + i+1);
		}
		
		//need a queue system of sort for the riders
	}

	/**
	 * We optimize the elevator scheduling by returning either the first idle elevator or the first elevator that's already going
	 * in the same direction as requested. Additionally, we can calculate the floor distance for all elevators that fulfill the
	 * criteria above and pick the one that's the closest. However, this calculation would take much time and might slow us down 
	 * instead of optimizing it.
	 */
	
	public Elevator CallUp(int fromFloor){
//		System.out.println("just added " + fromFloor);
		//check for error cases such as rider on top floor calling up
		if ((fromFloor>=super.numFloors) || (fromFloor<0)){
			return null;
		}
		//first we will send the first idle elevator
		for (Elevator e: elevators){
//			System.out.println("before the sync");
			synchronized(e){
				//if the elevator is idle
				//or if the elevator is below the rider and it's going up
				if(e.getMyDirection()==Elevator.DIRECTION_NEUTRAL){
					//add in condition about space
//					System.out.println("before the return");
					e.addFloor(fromFloor);
//					System.out.println("between add & arrive");
					e.getUpBarriers()[fromFloor].arrive();
					return e;
				}
			}
		}
		//else we will send the first elevator going in the correct elevator
		for (Elevator e: elevators){
			synchronized(e){
				if(e.getMyDirection()==Elevator.DIRECTION_UP && e.getFloor()<=fromFloor){
					e.addFloor(fromFloor);
					e.getUpBarriers()[fromFloor].arrive();
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
		//first we will send the first idle elevator
		for (Elevator e: elevators){
			synchronized(e){
				//if the elevator is idle
				//or if the elevator is above the rider and it's going down
				if(e.getMyDirection()==Elevator.DIRECTION_NEUTRAL){//direction2 is going upwards
					//add in condition about space
					e.addFloor(fromFloor);
					e.getDownBarriers()[fromFloor].arrive();
					return e;
				}
			}
		}
		//else we will send the first elevator going in the correct elevator
		for (Elevator e: elevators){
			synchronized(e){
				if (e.getMyDirection()==Elevator.DIRECTION_DOWN && e.getFloor()>=fromFloor){
					e.addFloor(fromFloor);
					e.getDownBarriers()[fromFloor].arrive();
					return e;
				}
			}
		}
		//else if there are no elevators
		return null;
	}
    
	/* Other methods as needed goes here */
}
