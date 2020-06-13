package CasEV.agent;

import CasEV.environment.Spawner;
import CasEV.environment.electric.Building;
import CasEV.environment.roads.BusStop;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * The person in the simulation. Making choioces on travel choice.
 * @author andrfo
 *
 */
public class Person extends Agent{

	public Building workPlace;
	private Building shop;
	private Spawner spawner;
	private int lastTimeUse = 0;
	private int parkedTimer = 0;
	private Double travelTime = 0d;
	
	private BusStop nearestBusStop;
	
	public Person(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner) {
		super(space, grid);
		this.space = space;
		this.grid = grid;
		this.workPlace = null;
		this.spawner = spawner;

	}
	
	/**
	 * Runs every step
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		//setSubsationLoadForReporter();
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
			//spawner.getReporter().removeParkedCar(v.type);
			return true;	
		}
	}
	
	/**
	 * Sets a timer for how long it will stay before it is ready to leave.
	 * @param time
	 */
	public void setParked(int time) {
		parkedTimer = time;
		//System.out.println("Travel time: " + this.travelTime);
		spawner.getMarket().addToAvgTravelTimeList(this.travelTime);
	}
	
	public String getTravelChoice() {
		

		return "car";
		
		
	}
	
	public void setShoppingPlace(Building shop) {
		this.shop = shop;
	}
	
	public Building getShoppingPlace() {
		return shop;
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
	public BusStop getNearestBusStop() {
		return nearestBusStop;
	}
		

	/**
	 * Sets the nearest busstop and find the nearest busstop
	 * @param workPlace
	 */
	public void setWorkPlace(Building workPlace) {
		this.workPlace = workPlace;
		this.nearestBusStop = workPlace.getNearestBusStop();
	}
	
	/**
	 * Called from the vehicle when a goal is reached for the person (work place, shop, or despawn)
	 * @param Vehicle v, the vehicle used during the trip
	 * @param isEndGoal, Boolean, True if the goal reach is the despawn.
	 */
	public void setReachedGoal(Vehicle v, boolean isEndGoal) {
		System.out.println(v + " caled setReachedGoal");
		if(isEndGoal) {
			if (workPlace == null) {
				spawner.returnShopper(this);
			} else {
				spawner.returnWorker(this); 
			}

			return;
		}
		if(workPlace != null && v instanceof EV) {
				workPlace.addOccupants(this, (EV) v, determineParkingWorth((EV) v, workPlace));
		} else if (workPlace != null && v instanceof Car) {

			workPlace.addOccupants(this, (Car) v, true);
		}
	}
	
	public boolean determineParkingWorth(Vehicle v, Building workPlace) {
		
		
		double priceLevel = Math.abs(spawner.getMarket().getPriceLevel());
	
		if (((EV)v).charge > 7 && priceLevel < 50d) {
			return true;
		} else {
			return false;
		}
	} 

}
