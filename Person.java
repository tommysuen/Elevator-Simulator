package PartB;
import java.util.Random;

public class Person extends Thread {

	private int id;
	private int current;
	private int floor;
	Random x = new Random();
	int call = x.nextInt(5);
	int ToGo = x.nextInt(5);
	
	
	
	public Person (int i)
	{
		while(call == ToGo)
		{
			ToGo = x.nextInt(5);
		}
		this.id = i;
		this.floor = ToGo;
		current = call;
	}
	
	public void run()
	{
		while(true)
		{
		try {
			Master.mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Master.NeedElevator[current] += 1;
		System.out.println("Person " + id + " calls elevator on floor " + current);
		Master.mutex.release();
		
		
		try {
			Master.floors[current].acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			Cleaning.Cleaner.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Cleaning.Cleaner.release(); */
		
		System.out.println("Person " + id + " is now in elevator. Current floor " + current);
		try {
			Master.mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Master.Destination[floor] += 1;
		System.out.println("Person " + id + " wants to go to floor " + floor);
		Master.mutex.release();
		
		
		try {
			Master.Location[floor].acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Person " + id + " exits floor " + floor);
		
	}
}
}


