import java.util.ArrayList;

public class Building{

	int numFloors;
	int numElevators;
	private ArrayList<Elevator> elevators;//arraylist to hold the elevators
	
	
	public Building(int numFloors, int numElevators) {
		this.numFloors = numFloors;
		this.numElevators = numElevators;
		elevators = new ArrayList<>();
			
		/*
		public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
			super(numFloors, elevatorId, maxOccupancyThreshold);
			myEventBarrier = new EventBarrier();
			myDoorsOpen = false;
			myDirectionState = DIRECTION_NEUTRAL;
			myDestinations = new boolean[numFloors];
			myFloor = 1;
		}
		*/
		int maxOccupancy=10;
		for (int i=0;i<numElevators;i++){
			//instantiate the correct number of elevators
			//then add them to arraylist elevators
			elevators.add(new Elevator(numFloors, i+1, maxOccupancy));
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
				if(e.getMyDirection()==1 && e.getFloor()<=fromFloor){//direction1 is going upwards
					//add in condition about space
					return e;
				}
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
				if(e.getMyDirection()==2 && e.getFloor()>=fromFloor){//direction2 is going upwards
					//add in condition about space
					return e;
				}
			}
		}
		//else if there are no elevators
		return null;
	}
    
	/* Other methods as needed goes here */
}
