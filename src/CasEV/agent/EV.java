package CasEV.agent;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import CasEV.Spawner;
import CasEV.physical.roads.Road;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class EV extends Car{
	
	public double charge;
	public double distanceFromCentre = 0d;
	public double borrowedCharge = 0d;
	public double offeredCharge = 0d;
	
	private static final double chargeMax = 10d;
	private static final double distanceMax = 60d;
	
	private static final double distanceChargeRatio = 5d;
	private static final double chargeMultiplier = 3d;
	
	public EV(ContinuousSpace<Object> space, Grid<Object> grid, int occupantLimit, List<Road> parkingNexi, Spawner spawner) {
		super(space, grid, occupantLimit, parkingNexi, spawner);
		setChargeAndDistance(this.type);

	}
	public void updateCharge(Double load) {
		this.charge += load;
	}
	
	
	//Set based on type and random variable to acvcount for differences in distance and charge
	
	protected void setChargeAndDistance(int type) {
		this.charge = ((this.type+1)*ThreadLocalRandom.current().nextDouble(0, chargeMax));
		this.distanceFromCentre = ThreadLocalRandom.current().nextDouble(0, distanceMax);
	}
	
	
	public Double getChargeAvailableForV2G() {
		

		if (this.distanceFromCentre/this.charge > distanceChargeRatio) {
			return ((this.distanceFromCentre/this.charge)*chargeMultiplier);
		} else if (this.distanceFromCentre/this.charge < distanceChargeRatio) {

			return (-(this.distanceFromCentre/this.charge)*chargeMultiplier);
		} else {
			return 0d;
		}
	}

}
