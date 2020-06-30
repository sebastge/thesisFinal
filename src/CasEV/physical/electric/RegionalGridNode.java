package CasEV.physical.electric;

import CasEV.Market;
import CasEV.Spawner;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import utils.Tools;

import java.util.concurrent.ThreadLocalRandom;

public class RegionalGridNode extends ElectricEntity{

	
	private Spawner spawner;
	private Market market;
	private int experimentNum;
	private int v2gCharging;
	private String timeOfDay;
	private Double biggestLoad;
	
	
	private Double loadPrice;
	
	//Time of day translation from ticks and schedule
	private static final int[] NIGHT = {0, 2160}; 				//00:00 - 06:00
	private static final int[] MORNING = {2160, 4320}; 			//06:00 - 12:00
	private static final int[] AFTERNOON = {4320, 6480}; 		//12:00 - 18:00
	private static final int[] EVENING = {6480, 8640}; 			//18:00 - 00:00
	private static final int[] MORNING_RUSH = {2520, 3060}; 	//07:00 - 08:30
	private static final int[] AFTERNOON_RUSH = {5580, 6120}; 	//15:30 - 17:00
	
	private static final int[] AM0 = {0, 180}; 				//00:00 - 06:00
	private static final int[] AM1 = {180, 360}; 			//06:00 - 12:00
	private static final int[] AM2 = {360, 540}; 		//12:00 - 18:00
	private static final int[] AM3 = {540, 720}; 			//18:00 - 00:00
	private static final int[] AM4 = {720, 900}; 	//07:00 - 08:30
	private static final int[] AM5 = {900, 1080}; 	//15:30 - 17:00
	private static final int[] AM6 = {1080, 1260}; 				//00:00 - 06:00
	private static final int[] AM7 = {1260, 1440}; 			//06:00 - 12:00
	private static final int[] AM8 = {1440, 1620}; 		//12:00 - 18:00
	private static final int[] AM9 = {1620, 1800}; 			//18:00 - 00:00
	private static final int[] AM10 = {1800, 1980}; 	//07:00 - 08:30
	private static final int[] AM11 = {1980, 2160}; 	//15:30 - 17:00
	private static final int[] PM0 = {2160, 2340}; 				//00:00 - 06:00
	private static final int[] PM1 = {2340, 2520}; 			//06:00 - 12:00
	private static final int[] PM2 = {2520, 2700}; 		//12:00 - 18:00
	private static final int[] PM3 = {2700, 2880}; 			//18:00 - 00:00
	private static final int[] PM4 = {2880, 3060}; 	//07:00 - 08:30
	private static final int[] PM5 = {3060, 3240}; 	//15:30 - 17:00
	private static final int[] PM6 = {3240, 3420}; 				//00:00 - 06:00
	private static final int[] PM7 = {3420, 3600}; 			//06:00 - 12:00
	private static final int[] PM8 = {3600, 3780}; 		//12:00 - 18:00
	private static final int[] PM9 = {3780, 3960}; 			//18:00 - 00:00
	private static final int[] PM10 = {3960, 4140}; 	//07:00 - 08:30
	private static final int[] PM11 = {4140, 4320}; 	//15:30 - 17:00

	
	
	public RegionalGridNode(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner, int experimentNum, int v2GCharging) {
		super(space, grid);
		this.totalLoad = 0d;
		this.grid = grid;
		this.space = space;
		this.spawner = spawner;
		this.loadPrice = 0d;
		this.experimentNum = experimentNum;
		this.v2gCharging = v2gCharging;
		this.biggestLoad = 0d;
		this.timeOfDay = "";
	}
	
	/**
	 * Runs every step
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		setTotalLoadForReporter();
		setLoadPriceForReporter();
		setLoad(this.totalLoad);
		
		//System.out.println(this + " v2g: " + this.v2gCharging );
	}
	
	public int getV2GCharging () {
		return this.v2gCharging;
	}
	public void setV2GCharging (int v2gCharging) {
		 this.v2gCharging = v2gCharging;
	}
	

	
	public void setTotalLoadForReporter() {

		spawner.getReporter().setTotalLoad(this.totalLoad, this);
		spawner.getMarket().setTotalLoad(this.totalLoad);
		
		
	}
	
	private void setLoad(double load) {
		
		
		
		int time = Tools.getTime();

		if(isInInterval(time, AM0)) {
			if (this.timeOfDay.equals("PM11")) {
				this.timeOfDay = "AM0";
			}
			if (!this.timeOfDay.equals("AM0")) {
				this.timeOfDay = "AM0";
				this.totalLoad = load*0.63;

			}
			
		} else if(isInInterval(time, AM1)) {
			if (!this.timeOfDay.equals("AM1")) {
				this.timeOfDay = "AM1";
				this.totalLoad = load*0.99;

			}
			
		} else if(isInInterval(time, AM2)) {
			if (!this.timeOfDay.equals("AM2")) {
				this.timeOfDay = "AM2";
				this.totalLoad = load*1.0;

			}
			
		} else if(isInInterval(time, AM3)) {
			if (!this.timeOfDay.equals("AM3")) {
				this.timeOfDay = "AM3";
				this.totalLoad = load*0.99;

			}
		} 
		else if(isInInterval(time, AM4)) {
			if (!this.timeOfDay.equals("AM4")) {
				this.timeOfDay = "AM4";
				this.totalLoad = load*1.01;

			}
		} 
		else if(isInInterval(time, AM5)) {
			if (!this.timeOfDay.equals("AM5")) {
				this.timeOfDay = "AM5";
				this.totalLoad = load*1.06;

			}
		} 
		else if(isInInterval(time, AM6)) {
			if (!this.timeOfDay.equals("AM6")) {
				this.timeOfDay = "AM6";
				this.totalLoad = load*1.07;

			}
		} 
		else if(isInInterval(time, AM7)) {
			if (!this.timeOfDay.equals("AM7")) {
				this.timeOfDay = "AM7";
				this.totalLoad = load*1.10;

			}
		} 
		else if(isInInterval(time, AM8)) {
			if (!this.timeOfDay.equals("AM8")) {
				this.timeOfDay = "AM8";
				this.totalLoad = load*1.03;

			}
		}
		
		else if(isInInterval(time, AM9)) {
			if (!this.timeOfDay.equals("AM9")) {
				this.timeOfDay = "AM9";
				this.totalLoad = load*1.03;

			}
		} 
		else if(isInInterval(time, AM10)) {
			if (!this.timeOfDay.equals("AM10")) {
				this.timeOfDay = "AM10";
				this.totalLoad = load*1.015;

			}
		} 
		else if(isInInterval(time, AM11)) {
			if (!this.timeOfDay.equals("AM11")) {
				this.timeOfDay = "AM11";
				this.totalLoad = load*1.0215;

			}
		}
		else if(isInInterval(time, PM0)) {
			if (!this.timeOfDay.equals("PM0")) {
				this.timeOfDay = "PM0";
				this.totalLoad = load*0.97;

			}
		} 
		else if(isInInterval(time, PM1)) {
			if (!this.timeOfDay.equals("PM1")) {
				this.timeOfDay = "PM1";
				this.totalLoad = load*1.08;

			}
		} 
		else if(isInInterval(time, PM2)) {
			if (!this.timeOfDay.equals("PM2")) {
				this.timeOfDay = "PM2";
				this.totalLoad = load*0.99;

			}
		} 
		else if(isInInterval(time, PM3)) {
			if (!this.timeOfDay.equals("PM3")) {
				this.timeOfDay = "PM3";
				this.totalLoad = load*1.03;

			}
		} 
		else if(isInInterval(time, PM4)) {
			if (!this.timeOfDay.equals("PM4")) {
				this.timeOfDay = "PM4";
				this.totalLoad = load*1.03;

			}
		} 
		else if(isInInterval(time, PM5)) {
			if (!this.timeOfDay.equals("PM5")) {
				this.timeOfDay = "PM5";
				this.totalLoad = load*1.03;

			}
		} 
		else if(isInInterval(time, PM6)) {
			if (!this.timeOfDay.equals("PM6")) {
				this.timeOfDay = "PM6";
				this.totalLoad = load*1.03;

			}
		} 
		
		else if(isInInterval(time, PM7)) {
			if (!this.timeOfDay.equals("PM7")) {
				this.timeOfDay = "PM7";
				this.totalLoad = load*0.93;

			}
		} 
		else if(isInInterval(time, PM8)) {
			if (!this.timeOfDay.equals("PM8")) {
				this.timeOfDay = "PM8";
				this.totalLoad = load*0.915;

			}
		} 
		else if(isInInterval(time, PM9)) {
			if (!this.timeOfDay.equals("PM9")) {
				this.timeOfDay = "PM9";
				this.totalLoad = load*0.93;

			}
		} 
		else if(isInInterval(time, PM10)) {
			if (!this.timeOfDay.equals("PM10")) {
				this.timeOfDay = "PM10";
				this.totalLoad = load*0.89;
				

			}
		} 
		else if(isInInterval(time, PM11)) {
			if (!this.timeOfDay.equals("PM11")) {
				this.timeOfDay = "PM11";
				this.totalLoad = load*0.89;

			}
		}
		if((time % 8640) == 0 ) {
			//setLoad2(1d);
		}
		
	}
	
	public void setWeatherProfileForReporter() {
		spawner.getReporter().setWeatherProfile(ThreadLocalRandom.current().nextInt(1, 4 + 1));
	}
	
	public void setLoadPriceForReporter() {
		spawner.getReporter().setElPrice(this.loadPrice);
	}

	
	
	private boolean isInInterval(int n, int[] interval) {
		return n >= interval[0] && n < interval[1];
	}
	
	
}
