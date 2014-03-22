import java.util.ArrayList;

public class TestCase {

	public void go() {
		EventBarrier b = new EventBarrier();
		Gatekeeper g1 = new Gatekeeper(b);
		Gatekeeper g2 = new Gatekeeper(b);
		
		ArrayList<Minstrel> minstrels= new ArrayList<Minstrel>();
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < 100; i++) {
			Minstrel m = new Minstrel(b, i);
			minstrels.add(m);
			threads.add(new Thread(m));
		}
		Thread t4 = new Thread(g1);
		Thread t5 = new Thread(g2);
		for (int i = 0; i <10; i++) {
			threads.get(i).start();	
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		t4.start();

		/*
		Minstrel m1 = new Minstrel(b);
		Minstrel m2 = new Minstrel(b);
		Minstrel m3 = new Minstrel(b);
		Thread t1 = new Thread(m1);
		Thread t2 = new Thread(m2);
		Thread t3 = new Thread(m3);
		Thread t4 = new Thread(g1);
		t1.start();
		t2.start();
		t3.start();
		t4.start();

		*/
	}

		

	public static void main(String[] args) {
		TestCase tc = new TestCase();
		tc.go();
	}

	public class Minstrel implements Runnable {

		private EventBarrier myBarrier;
		private int myIndex;

		public Minstrel(EventBarrier b, int i) {
			this.myBarrier = b;
			this.myIndex = i;
		}

		@Override
		public void run() {
			int sleepDuration = (int) (5000 * Math.random());
			try {
				Thread.sleep(sleepDuration);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			System.out.println("Minstrel has arrived");
			myBarrier.arrive();
			System.out.println("Minstrel " + myIndex + " has crossed the bridge");
			myBarrier.complete();
		}
	}

	public class Gatekeeper implements Runnable {

		private EventBarrier myBarrier;

		public Gatekeeper(EventBarrier b) {
			myBarrier = b;
		}

		@Override
		public void run() {

			myBarrier.raise();
		}
	}
}
