
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class TestCaseElevator {

	public static void main(String[] args) {
		
		BufferedReader br = null;
		 
		try {
			int numFloors;
			int numElevators;
			int numRiders;
			int maxCapacity;
			
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("inputs"));
			
			if ((sCurrentLine = br.readLine()) != null) {
				//This is the first line 
				String[] inputs = sCurrentLine.split(" ");
				System.out.println(Arrays.toString(inputs));
				
			}
			
			while ((sCurrentLine = br.readLine()) != null) {
				String[] inputs = sCurrentLine.split(" ");
				System.out.println(Arrays.toString(inputs));
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
