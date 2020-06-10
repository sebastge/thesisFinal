package CasEV.agent;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import CasEV.environment.Spawner;
import CasEV.environment.roads.Road;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class EV extends Car{
	
	public double charge;
	public double distanceFromCentre = 0d;
	public double borrowedCharge = 0d;
	public double offeredCharge = 0d;
	
	private static final double chargeMax = 10d;
	private static final double distanceMax = 60d;
	
	public EV(ContinuousSpace<Object> space, Grid<Object> grid, int occupantLimit, List<Road> parkingNexi, Spawner spawner) {
		super(space, grid, occupantLimit, parkingNexi, spawner);
		setChargeAndDistance(this.type);

	}
	public void updateCharge(Double load) {
		this.charge += load;
	}
	
	protected void setChargeAndDistance(int type) {
		this.charge = (2*ThreadLocalRandom.current().nextDouble(0, chargeMax));
		this.distanceFromCentre = ThreadLocalRandom.current().nextDouble(0, distanceMax);
	}
	
	
	public Double getChargeAvailableForV2G() {
		
//		System.out.println("Distance: " + this.distanceFromCentre);
//		System.out.println("Charge: " + this.charge);
		
		if (this.distanceFromCentre/this.charge > 3d ) {
			//System.out.println("was bigger");
			return (-(this.distanceFromCentre/this.charge)*2);
		} else {
			//System.out.println("was smaller");
			return ((this.distanceFromCentre/this.charge)*2);
		}
	}

}
