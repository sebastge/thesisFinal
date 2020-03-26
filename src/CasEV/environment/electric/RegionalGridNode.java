package CasEV.environment.electric;

import CasEV.environment.Spawner;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class RegionalGridNode extends ElectricEntity{

	
	private Spawner spawner;
	
	public RegionalGridNode(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner) {
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
		setTotalLoadForReporter();
	}
	
	public void setTotalLoadForReporter() {
		spawner.getReporter().setTotalLoad(this.totalLoad);
	}
	
}
