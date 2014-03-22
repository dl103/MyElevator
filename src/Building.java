import java.util.ArrayList;

public class Building{

	int numFloors;
	int numElevators;
	private ArrayList<Elevator> elevators;//arraylist to hold the elevators
	
	
	
	public Building(int numFloors, int numElevators) {
		this.numFloors = numFloors;
		this.numElevators = numElevators;
		elevators = new ArrayList<>();
			
		for (int i=0;i<numElevators;i++){
			//instantiate the correct number of elevators
			//then add them to arraylist elevators
			
		}
	}



    /**
     * Signal an elevator that we want to go up
     *
     * @param fromFloor  floor from which elevator is called
     * @return           instance of the elevator to use to go up
     */
	
	public Elevator CallUp(int fromFloor){
		//check for error cases
		if ((fromFloor>numFloors) || (fromFloor<0)){
			return null;
		}
		
		for (Elevator e: elevators){
			synchronized(e){
				//check for conditions
				
				return e;
			}
		}
		//else if there are no elevators
		return null;
	}

    /**
     * Signal an elevator that we want to go down
     *
     * @param fromFloor  floor from which elevator is called
     * @return           instance of the elevator to use to go down
     */
	public Elevator CallDown(int fromFloor){
		if ((fromFloor>numFloors) || (fromFloor<0)){
			return null;
		}
		
		for (Elevator e: elevators){
			synchronized(e){
				//check for conditions
				
				return e;
			}
		}
		//else if there are no elevators
		return null;
	}
    
	/* Other methods as needed goes here */
}
