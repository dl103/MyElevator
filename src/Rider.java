import java.io.BufferedWriter;
import java.io.IOException;


public class Rider implements Runnable{

	protected int requestedFloor;
	protected int currentFloor;
	protected Building building;
	protected int riderID;
	protected boolean haveRequested;
	protected boolean haveExited;
	private BufferedWriter myFileWriter;

	public Rider(int riderID, int requestedFloor, int currentFloor, Building building) {
        
       
        
		this.riderID = riderID;
		this.requestedFloor = requestedFloor;
		this.currentFloor = currentFloor;
		this.building = building;
		this.haveRequested = false;
		this.haveExited = false;
		
	}

	public int getFloor() {
			return this.requestedFloor;
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
	
	
	@SuppressWarnings("deprecation")
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
			write("Rider " + riderID + " has entered " + "E" + elevator.elevatorId);
			elevator.RequestFloor(this, requestedFloor);
			/**
			 * if the rider doesn't make a request, we force him out of the elevator
			 */
			if (this.haveRequested = false) {
				elevator.Exit(this);
			}
		}
		System.out.println("Rider " + riderID+ " has requested floor " + requestedFloor);
		elevator.Exit(this);
		/**
		 * if the rider never exits the elevator, we kill the corresponding thread or make it enter in another request again
		 */
		if(this.haveExited = false) {
			Thread.currentThread().destroy();
			System.out.println("rider refuse to exit the elevator");
		}
		System.out.println("Rider " + riderID+ " has exited " + "E" + elevator.elevatorId);
		write("Rider " + riderID+ " has exited " + "E" + elevator.elevatorId);
	}

}
