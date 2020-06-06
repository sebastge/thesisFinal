package CasEV;



import CasEV.agent.Person;
import CasEV.environment.Spawner;
import CasEV.environment.roads.Road;
import repast.simphony.engine.schedule.ScheduledMethod;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import structures.Goals;

public class Market {
	
	private int occupantLimit = 5;

	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	
	private Goals goals;
	
	private Spawner spawner;

	public Market() {
		super();

	}
	
	/**
	 * Runs every step. Checks for "dead" after each main step because model destruction does not trigger until next step.
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		
		System.out.println("Market innit");
		
	}
		
		
	}