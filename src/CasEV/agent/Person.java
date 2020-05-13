package CasEV.agent;

import CasEV.environment.Spawner;
import CasEV.environment.electric.Building;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * The person in the simulation. Making choioces on travel choice.
 * @author andrfo
 *
 */
public class Person extends Agent{

	public Building workPlace;
	private Spawner spawner;
	private int lastTimeUse = 0;
	private int parkedTimer = 0;
	
	public Person(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner) {
		super(space, grid);
		this.space = space;
		this.grid = grid;
		this.workPlace = null;
		this.spawner = spawner;

	}

	/**
	 * Gets the assigned work place for this person
	 * @return Building, the work place
	 */
	public Building getWorkPlace() {
		return workPlace;
	}
	
	/**
	 * Is called as a check each tick to see if the person is done and ready to leave. If not the times is decremented
	 * @return True if the person is ready to leave
	 */
	public boolean isWantToLeave(Vehicle v, Boolean check) {
		
		if(parkedTimer > 0) {
			parkedTimer--;
			return false;
		}
		
		if (check) {
			return true;
			
		} else {
			workPlace.removeOccupants(this, v);
			return true;
			
		}
		

	}
	
	/**
	 * Sets a timer for how long it will stay before it is ready to leave.
	 * @param time
	 */
	public void setParked(int time) {
		parkedTimer = time;
	}
	
	
	/**
	 * Gets the number of ticks for the last trip
	 * @return int, number of ticks
	 */
	public int getLastTimeUse() {
		return lastTimeUse;
	}

		
	/**
	 * Sets the nearest busstop and find the nearest busstop
	 * @param workPlace
	 */
	public void setWorkPlace(Building workPlace) {
		this.workPlace = workPlace;

	}
	
	/**
	 * Called from the vehicle when a goal is reached for the person (work place, shop, or despawn)
	 * @param Vehicle v, the vehicle used during the trip
	 * @param isEndGoal, Boolean, True if the goal reach is the despawn.
	 */
	public void setReachedGoal(Vehicle v, boolean isEndGoal) {
		if(isEndGoal) {
			spawner.returnWorker(this);
			return;
		}
		if(workPlace != null) {
				workPlace.addOccupants(this, v, determineParkingWorth(v, workPlace));
		}
	}
	
	public boolean determineParkingWorth(Vehicle v, Building workPlace) {
		
		System.out.println("vehicle type: " + v.type + ". Charge: " + v.charge);
		
		if (v.charge > 200 && workPlace.getLoadPrice() < 5d) {
			System.out.println("vehicle type: " + v.type + ". Parking worth it");
			return true;
		} else {
			System.out.println("vehicle type: " + v.type + ". Parking not worth it");
			return false;
		}
	} 

}
