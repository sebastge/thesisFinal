package CasEV.physical;

import java.math.BigDecimal;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import utils.Tools;

public class ParkingLabel extends Entity{
	
	private int numParking;
	private String label = "hei der";

	public ParkingLabel(ContinuousSpace<Object> space, Grid<Object> grid, Context<Object> context) {
		super(space, grid);
		// TODO Auto-generated constructor stub
	}
	
	public void addParking() {

		numParking += 1;
	}
	
	public String getNumParking() {
		return label;
	}
}
