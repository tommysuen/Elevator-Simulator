package PartB;
import java.util.concurrent.Semaphore;

public class Cleaning extends Thread {
	
	static Semaphore NeedToClean = new Semaphore(0, false);
	static Semaphore Cleaner = new Semaphore(1, false);
	static int current = 1;
	
	
	public void run()
	{
		while(true)
		{
			//Takes mutual exclusion and takes control
			//so no other people can enter elevator
		try {
			Master.mutex.acquire();
			NeedToClean.acquire();
			Cleaner.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Custodian needs elevator
		Master.NeedElevator[current] += 1;
		System.out.println("Custodian calls elevator on floor " + current);
		Master.mutex.release();
		
		
		try {
			
			Master.floors[current].acquire();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Custodian is now in elevator. Current floor " + current);
		//Needs to go to his destination
		try {
			Master.mutex.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Master.Destination[current] += 1;
		System.out.println("Custodian wants to go to floor " + current);
		Master.mutex.release();
		//
		try {
			Master.Location[current].acquire();
			Thread.sleep(100);
			System.out.println("Cleaning");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Gets off elevator
		Cleaner.release();
		System.out.println("Custodian exits on floor " + current);
		}
		
}
}
