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
	
	public EV(ContinuousSpace<Object> space, Grid<Object> grid, int occupantLimit, List<Road> parkingNexi, Spawner spawner) {
		super(space, grid, occupantLimit, parkingNexi, spawner);
		setChargeAndDistance(this.type);

	}
	public void updateCharge(Double load) {
		this.charge += load;
	}
	
	protected void setChargeAndDistance(int type) {
		this.charge = ((this.type+1)*ThreadLocalRandom.current().nextDouble(0, chargeMax));
		this.distanceFromCentre = ThreadLocalRandom.current().nextDouble(0, distanceMax);
	}
	
	
	public Double getChargeAvailableForV2G() {
		

		if (this.distanceFromCentre/this.charge > 3d) {

			return ((this.distanceFromCentre/this.charge)*3);
		} else {

			return (-(this.distanceFromCentre/this.charge)*3);
		}
	}

}
