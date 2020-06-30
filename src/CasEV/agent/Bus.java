package CasEV.agent;

import java.util.List;

import CasEV.Spawner;
import CasEV.physical.roads.Road;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Bus extends Vehicle{

	public Bus(ContinuousSpace<Object> space, Grid<Object> grid, int occupantLimit, List<Road> parkingNexi, Spawner spawner) {
		super(space, grid, occupantLimit, parkingNexi, spawner);
		// TODO Auto-generated constructor stub
	}
	

}
