package PartB;
import java.util.concurrent.Semaphore;
import java.lang.Math;

public class ElevatorSyn extends Thread
{
	//Set up Variables
	static Semaphore Max_People = new Semaphore(3,false);
	static boolean DirectionUP = true;
	static boolean TopFloor;
	static int currentfloor = 0;
	static int i;
	
	
	public boolean CanTakePeople()
	{
		if (Max_People.availablePermits() > 0 && Master.NeedElevator[currentfloor] > 0)
			return true;
		else
			return false;
	}
	public int SleepTime()
	{
		int sleep = Math.abs(i - currentfloor) * 10;
		return sleep;
	}
	
	public boolean DirectionUP()
	{
		return true;
	}
	
	public boolean TopFloor()
	{
		return false;
	}
	public boolean DestinationOver0()
	{
		if (Master.Destination[currentfloor] >0)
			return true;
		else
			return false;
	}
	public void ReleasePeople()
	{
		Master.Destination[currentfloor] -= 1;
		Master.mutex.release();
		Master.Location[currentfloor].release();
		Max_People.release();
	}
	public void TakePeople()
	{
		Master.NeedElevator[currentfloor] -= 1;
		Master.floors[currentfloor].release();
		try {
			Max_People.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(true)
		{
		Cleaning.NeedToClean.release();
		
		if (DirectionUP == true)
		{
			//Checks each floor going up
			for (i = currentfloor; i < 5; i++)
			{	
				//If someone needs to go on the floor, increase
				if (Master.Destination[i] > 0)
				{
					try {
						Thread.sleep(SleepTime());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					currentfloor = i;
					//If there are people who still needs to go
					//to their destination, create mutex
					while(DestinationOver0())
					{
						try {
							Master.mutex.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//Let them off their destination
						ReleasePeople();
						
					}
				}
				while(CanTakePeople() == true)
				{
					TakePeople();
				}
				try {
					Thread.sleep(SleepTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
			DirectionUP = false;
		}
		if (DirectionUP == false)
		{
			for (int i = currentfloor; i >= 0; i--)
			{
				if (Master.Destination[i] > 0)
				{
					try {
						Thread.sleep(SleepTime());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					currentfloor = i;
					
					while(DestinationOver0())
					{
						try {
							Master.mutex.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ReleasePeople();
					}
					try {
						Thread.sleep(10 * 8);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				while(CanTakePeople() == true)
				{
					TakePeople();
				}
			}	
			DirectionUP = true;
		}
	}
}
}




