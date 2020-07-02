package CasEV.agent;

import java.util.concurrent.ThreadLocalRandom;

import CasEV.Spawner;
import CasEV.physical.electric.Aggregator;
import CasEV.physical.roads.BusStop;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * The person in the simulation. Making choioces on travel choice.
 * @author andrfo
 *
 */
public class Prosumer extends Agent{

	public Aggregator workPlace;
	private Aggregator shop;
	private Spawner spawner;
	private int lastTimeUse = 0;
	private int parkedTimer = 0;
	private Double travelTime = 0d;
	Parameters params = RunEnvironment.getInstance().getParameters();


	
	private Double experiment2ProsumerConfidence = 1.0d;

	
	private Double experiment2Mulitplier = params.getDouble("price_multiplication");
	
	private Double prosumerMinPriceLevel;
	
	private BusStop nearestBusStop;
	
	
	public Prosumer(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner) {
		super(space, grid);
		this.space = space;
		this.grid = grid;
		this.workPlace = null;
		this.spawner = spawner;
		//randomized to account for unknowns
		this.prosumerMinPriceLevel = ThreadLocalRandom.current().nextDouble(35, 50);
		Parameters params = RunEnvironment.getInstance().getParameters();


	}

	/**
	 * Gets the assigned work place for this person
	 * @return Building, the work place
	 */
	public Aggregator getWorkPlace() {
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
			workPlace.disconnectProsumer(this, v);
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

		spawner.getMarket().addToAvgTravelTimeList(this.travelTime);
	}
	
	public String getTravelChoice() {
		

		return "car";
		
		
	}
	
	public void setShoppingPlace(Aggregator shop) {
		this.shop = shop;
	}
	
	public Aggregator getShoppingPlace() {
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
	public void setWorkPlace(Aggregator workPlace) {
		this.workPlace = workPlace;
		this.nearestBusStop = workPlace.getNearestBusStop();
	}
	
	/**
	 * Called from the vehicle when a goal is reached for the person (work place, shop, or despawn)
	 * @param Vehicle v, the vehicle used during the trip
	 * @param isEndGoal, Boolean, True if the goal reach is the despawn.
	 */
	public void setReachedGoal(Vehicle v, boolean isEndGoal) {
		if(isEndGoal) {
			if (workPlace == null) {
				spawner.returnShopper(this);
			} else {
				spawner.returnWorker(this); 
			}

			return;
		}
		if(workPlace != null && v instanceof EV) {
				workPlace.connectProsumer(this, (EV) v, determineParkingWorth((EV) v, workPlace));
		} else if (workPlace != null && v instanceof Car) {
			workPlace.connectProsumer(this, (Car) v, true);
		}
	}
	
	public boolean determineParkingWorth(Vehicle v, Aggregator workPlace) {
		

		double priceLevel = (Math.abs(spawner.getMarket().getPriceLevel()) * this.experiment2Mulitplier);

		
		if (priceLevel > this.prosumerMinPriceLevel) {
			return true;
		} else {
			return false;
		}
	} 

}
