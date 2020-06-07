package CasEV.environment.electric;

import CasEV.Market;
import CasEV.environment.Spawner;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import utils.Tools;

import java.util.concurrent.ThreadLocalRandom;

public class RegionalGridNode extends ElectricEntity{

	
	private Spawner spawner;
	
	
	private Double loadPrice;
	
	//Time of day translation from ticks and schedule
	private static final int[] NIGHT = {0, 2160}; 				//00:00 - 06:00
	private static final int[] MORNING = {2160, 4320}; 			//06:00 - 12:00
	private static final int[] AFTERNOON = {4320, 6480}; 		//12:00 - 18:00
	private static final int[] EVENING = {6480, 8640}; 			//18:00 - 00:00
	private static final int[] MORNING_RUSH = {2520, 3060}; 	//07:00 - 08:30
	private static final int[] AFTERNOON_RUSH = {5580, 6120}; 	//15:30 - 17:00
	
	
	public RegionalGridNode(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner) {
		super(space, grid);
		this.totalLoad = 0d;
		this.grid = grid;
		this.space = space;
		this.spawner = spawner;
		this.loadPrice = 0d;
	}
	
	/**
	 * Runs every step
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		setTotalLoadForReporter();
		setLoadPrice();
		setLoadPriceForReporter();
		//setWeatherProfileForReporter();
		//System.out.println("Load price: " + this.loadPrice);
		//System.out.println("Total load: " + this.totalLoad);
	}
	
	public void setTotalLoadForReporter() {

		spawner.getReporter().setTotalLoad(this.totalLoad);
		spawner.getMarket().setTotalLoad(this.totalLoad);
		
		
	}
	
	public void setWeatherProfileForReporter() {
		spawner.getReporter().setWeatherProfile(ThreadLocalRandom.current().nextInt(1, 4 + 1));
	}
	
	public void setLoadPriceForReporter() {
		spawner.getReporter().setElPrice(this.loadPrice);
	}
	
	public void setLoadPrice() {
		int time = Tools.getTime();
		if(isInInterval(time, NIGHT)) {
			this.loadPrice = 2d;
			//spawner.getReporter().setWeatherProfile(3);
		} else if(isInInterval(time, MORNING)) {
			this.loadPrice = 10d;
			//spawner.getReporter().setWeatherProfile(2);
		} else if(isInInterval(time, AFTERNOON)) {
			this.loadPrice = 5d;
			//spawner.getReporter().setWeatherProfile(4);
		} else if(isInInterval(time, EVENING)) {
			this.loadPrice = 2d;
			//spawner.getReporter().setWeatherProfile(1);
		} else if(isInInterval(time, MORNING_RUSH)) {
			this.loadPrice = 20d;
			//spawner.getReporter().setWeatherProfile(2);
		} else if(isInInterval(time, AFTERNOON_RUSH)) {
			this.loadPrice = 15d;
			//spawner.getReporter().setWeatherProfile(1);
		}
	}
	
	private boolean isInInterval(int n, int[] interval) {
		return n >= interval[0] && n < interval[1];
	}
	
	
}
