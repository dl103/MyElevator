import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Building extends AbstractBuilding{

	int numFloors;
	int numElevators;
	private ArrayList<Elevator> elevators;//arraylist to hold the elevators
	
	
	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		elevators = new ArrayList<>();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("Elevator.log"));
			int maxOccupancy=10;
			for (int i=0;i<numElevators;i++){
				//instantiate the correct number of elevators
				//then add them to arraylist elevators
				Elevator e = new Elevator(numFloors, i+1, maxOccupancy);
				e.setWriter(writer);
				elevators.add(e);
				Thread t = new Thread(e);
				t.start();
				t.setName("Elevator " + i+1);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
		//need a queue system of sort for the riders
	}

	public Elevator CallUp(int fromFloor){
//		System.out.println("just added " + fromFloor);
		//check for error cases such as rider on top floor calling up
		if ((fromFloor>=super.numFloors) || (fromFloor<0)){
			return null;
		}
		
		for (Elevator e: elevators){
//			System.out.println("before the sync");
			synchronized(e){
				//if the elevator is idle
				//or if the elevator is below the rider and it's going up
				if(e.getMyDirection()==Elevator.DIRECTION_NEUTRAL||(e.getMyDirection()==Elevator.DIRECTION_UP && e.getFloor()<=fromFloor)){
					//add in condition about space
//					System.out.println("before the return");
					e.addFloor(fromFloor);
//					System.out.println("between add & arrive");
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
		
		for (Elevator e: elevators){
			synchronized(e){
				//if the elevator is idle
				//or if the elevator is abobe the rider and it's going down
				if(e.getMyDirection()==Elevator.DIRECTION_NEUTRAL||(e.getMyDirection()==Elevator.DIRECTION_DOWN && e.getFloor()>=fromFloor)){//direction2 is going upwards
					//add in condition about space
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
