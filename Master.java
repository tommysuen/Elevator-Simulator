package PartB;
import java.util.concurrent.Semaphore;


public class Master {
	//initialize semaphores for mutex, floors, location
	static Semaphore mutex = new Semaphore(1,false);
	static Semaphore[] floors = new Semaphore[5];
	static Semaphore[] Location = new Semaphore[5];
	
	//For people who need the elevator and their destination
	static int[] NeedElevator = new int[5];
	static int[] Destination = new int[5];
	
	
	public static void main(String[] args) {
		
		//Each floor, check to see if anyone needs it
		for(int j = 0; j < 5; j++)
		{
			floors[j] = new Semaphore(0, false);
			Location[j] = new Semaphore(0, false);
		}
		//Custodian declares he needs elevator and starts
		Cleaning Custodian = new Cleaning();
		Custodian.start();
		//Once released, people can start going back in
		Person[] p = new Person[20];
		for (int i = 0; i < 20; i++)
		{	
			p[i] = new Person(i);
			p[i].start();
		}
		//Start Elevator
		ElevatorSyn SCAN = new ElevatorSyn();
		SCAN.start();

	}

}
