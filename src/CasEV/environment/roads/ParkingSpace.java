package CasEV.environment.roads;

import java.util.concurrent.ThreadLocalRandom;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class ParkingSpace extends Road {

	public static final int DEFAULT_TIME = 90;
	private boolean isReserved = false;
	public double charge;
	
	public ParkingSpace(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid);
		this.charge = ThreadLocalRandom.current().nextDouble(0, 10);
		// TODO Auto-generated constructor stub
	}
	
	public boolean reserve() {
		if(isReserved) {return false;}
		else {
			isReserved = true;
			return true;
		}
	}
	
	public void vacate() {
		isReserved = false;
	}
	
	public boolean isReserved() {
		return isReserved;
	}
	
	public void addCharge(double chargeFromCar) {
		this.charge += chargeFromCar;

	}

}
