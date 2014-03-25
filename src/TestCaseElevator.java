
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCaseElevator {

	public static void main(String[] args) {
		
		BufferedReader br = null;
		BufferedWriter out = null;
		 
		try {
			int numFloors;
			int numElevators;
			int numRiders;
			int maxCapacity;
			List<Rider> myRiders = new ArrayList<Rider>();
			
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("inputs"));
			out = new BufferedWriter(new FileWriter("Elevator.log"));
			Building b = null;
			
			if ((sCurrentLine = br.readLine()) != null) {
				//This is the first line 
				String[] inputs = sCurrentLine.split(" ");
				System.out.println(Arrays.toString(inputs));
				b = new Building(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]), out);
			}
			
			while ((sCurrentLine = br.readLine()) != null) {
				String[] inputs = sCurrentLine.split(" ");
				System.out.println(Arrays.toString(inputs));
				int riderID = Integer.parseInt(inputs[0]);
				int currentFloor = Integer.parseInt(inputs[1]);
				int requestedFloor = -1;
				try {
					requestedFloor = Integer.parseInt(inputs[2]); 
				} catch(NumberFormatException e) {
					System.out.println("The rider does not have a valid request, bad behavior detected!!");
				}
				if (requestedFloor != -1) {
					Rider r = new Rider(riderID, requestedFloor, currentFloor, b);
					r.setWriter(out);
					Thread t = new Thread(r);
					t.start();
					t.setName("Rider " + r.riderID);
				} 
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		
	}
}
