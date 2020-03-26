package citySim.environment.electric;

import citySim.environment.Spawner;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Substation extends ElectricEntity{

GridPoint location; //Location of the top-right GridPoint
private Spawner spawner;
	
	public Substation(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner) {
		super(space, grid);
		this.totalLoad = 0d;
		this.grid = grid;
		this.space = space;
		this.spawner = spawner;
	}
	
	/**
	 * Runs every step
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		//setSubsationLoadForReporter();
	}
	
	public void setSubsationLoadForReporter() {
		spawner.getReporter().setTotalLoad(this.totalLoad);
	}
}
