
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCaseElevator {

	public static void main(String[] args) {
		
		BufferedReader br = null;
		 
		try {
			int numFloors;
			int numElevators;
			int numRiders;
			int maxCapacity;
			List<Rider> myRiders = new ArrayList<Rider>();
			
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("inputs"));
			Building b = null;
			
			if ((sCurrentLine = br.readLine()) != null) {
				//This is the first line 
				String[] inputs = sCurrentLine.split(" ");
				System.out.println(Arrays.toString(inputs));
				b = new Building(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]));
			}
			
			while ((sCurrentLine = br.readLine()) != null) {
				String[] inputs = sCurrentLine.split(" ");
				System.out.println(Arrays.toString(inputs));
				Rider r = new Rider(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[1]), b);
				Thread t = new Thread(r);
				t.start();
				t.setName("Rider " + r.riderID);
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
