package CasEV.physical.electric;

import CasEV.Spawner;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Substation extends ElectricEntity{

	GridPoint location; //Location of the top-right GridPoint
	private Spawner spawner;
	private int experimentNum;
	
	public Substation(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner, int experimentNum) {
		super(space, grid);
		this.totalLoad = 0d;
		this.grid = grid;
		this.space = space;
		this.spawner = spawner;
		this.experimentNum = experimentNum;
	}
	

	
	public void setSubsationLoadForReporter() {
		//spawner.getReporter().setTotalLoad(this.totalLoad);
	}

	@Override
	protected int getV2GCharging() {
		// TODO Auto-generated method stub
		return 0;
	}
}
