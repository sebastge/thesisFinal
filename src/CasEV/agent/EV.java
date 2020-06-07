package CasEV.agent;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import CasEV.environment.Spawner;
import CasEV.environment.roads.Road;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class EV extends Car{
	
	public double charge;
	

	public EV(ContinuousSpace<Object> space, Grid<Object> grid, int occupantLimit, List<Road> parkingNexi, Spawner spawner) {
		super(space, grid, occupantLimit, parkingNexi, spawner);
		this.charge = setCharge(this.type);

	}
	public void updateCharge(Double load) {
		System.out.println("Ev update charge");
		this.charge += load;
	}
	
	protected Double setCharge(int type) {
		return (2*ThreadLocalRandom.current().nextDouble(0, 10));

	}
}
