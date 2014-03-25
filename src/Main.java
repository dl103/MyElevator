
public class Main {

	public static void main (String args[]) {
		args = new String[3];
		args[0]="1";
		if (args[0].equals("0")) {
			TestCase tc = new TestCase();
			tc.go();
		}
		if (args[0].equals("1")) {
			TestCaseElevator tc = new TestCaseElevator();
			tc.go();
		}
	}
	
}
