package CasEV.environment.electric;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import CasEV.agent.Car;
import CasEV.agent.Person;
import CasEV.agent.Vehicle;
import CasEV.environment.Spawner;
import CasEV.environment.roads.ParkingSpace;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import utils.Tools;
import CasEV.environment.roads.BusStop;

public class Building extends ElectricEntity{

	private Double loadPrice;
	private List<Person> occupants;
	private List<ParkingSpace> parkingSpaces;

	
	/** TimeCycle
	 * 	1 Tick = 10 sec
	 * 	24h = 8640 ticks
	 * 	1 hour = 360 ticks
	 */
	
	//Time of day translation from ticks and schedule
	private static final int[] NIGHT = {0, 2160}; 				//00:00 - 06:00
	private static final int[] MORNING = {2160, 4320}; 			//06:00 - 12:00
	private static final int[] AFTERNOON = {4320, 6480}; 		//12:00 - 18:00
	private static final int[] EVENING = {6480, 8640}; 			//18:00 - 00:00
	private static final int[] MORNING_RUSH = {2520, 3060}; 	//07:00 - 08:30
	private static final int[] AFTERNOON_RUSH = {5580, 6120}; 	//15:30 - 17:00
	
	private static final int[] AM0 = {0, 360}; 				//00:00 - 06:00
	private static final int[] AM1 = {360, 720}; 			//06:00 - 12:00
	private static final int[] AM2 = {720, 1080}; 		//12:00 - 18:00
	private static final int[] AM3 = {1080, 1440}; 			//18:00 - 00:00
	private static final int[] AM4 = {1440, 1800}; 	//07:00 - 08:30
	private static final int[] AM5 = {1800, 2160}; 	//15:30 - 17:00
	private static final int[] AM6 = {2160, 2520}; 				//00:00 - 06:00
	private static final int[] AM7 = {2520, 2880}; 			//06:00 - 12:00
	private static final int[] AM8 = {2880, 3240}; 		//12:00 - 18:00
	private static final int[] AM9 = {3240, 3600}; 			//18:00 - 00:00
	private static final int[] AM10 = {3600, 3960}; 	//07:00 - 08:30
	private static final int[] AM11 = {3960, 4320}; 	//15:30 - 17:00
	private static final int[] PM0 = {4320, 4680}; 				//00:00 - 06:00
	private static final int[] PM1 = {4680, 5040}; 			//06:00 - 12:00
	private static final int[] PM2 = {5040, 5400}; 		//12:00 - 18:00
	private static final int[] PM3 = {5400, 5760}; 			//18:00 - 00:00
	private static final int[] PM4 = {5760, 6120}; 	//07:00 - 08:30
	private static final int[] PM5 = {6120, 6480}; 	//15:30 - 17:00
	private static final int[] PM6 = {6480, 6840}; 				//00:00 - 06:00
	private static final int[] PM7 = {6840, 7200}; 			//06:00 - 12:00
	private static final int[] PM8 = {7200, 7560}; 		//12:00 - 18:00
	private static final int[] PM9 = {6480, 7920}; 			//18:00 - 00:00
	private static final int[] PM10 = {7920, 8280}; 	//07:00 - 08:30
	private static final int[] PM11 = {8280, 8640}; 	//15:30 - 17:00
	
	private String timeOfDay;
	public Spawner spawner;
	
	public Building(ContinuousSpace<Object> space, Grid<Object> grid, Spawner spawner, List<ParkingSpace> parkingSpaces) {
		super(space, grid);
		this.totalLoad = 0.2d;
		this.parent = null;
		this.occupants = new ArrayList<Person>();
		this.grid = grid;
		this.space = space;
		this.parkingSpaces = parkingSpaces;
		this.loadPrice = 0d;
		this.timeOfDay = "";
		this.spawner = spawner;
		
	}
	
	/**
	 * Runs every step
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		//setLoad3();
		changeLoad();
		//setLoadPrice();
		spawner.getMarket().getPriceLevel();
		System.out.println("Demand: " + spawner.getMarket().getDemand());
	
	}
	
	public void changeLoad() {
		int time = Tools.getTime();
		if (time % 360 == 0) {
			setLoad3();
		}
	}

	
	public void addOccupants(Person p, Vehicle v, Boolean parkingDecision) {
		if (v instanceof Car && parkingDecision == true) {
			System.out.println("Vehicle: " + v  + " added");
			if (this.totalLoad >= 0.05 ) {
				update(-v.charge*0.025);
			}
		
			for (ParkingSpace ps: this.parkingSpaces) {
				if (!ps.isReserved()) {
					ps.reserve();
					v.isParkedInBuilding = true;
					v.buildingParkedIn = this;
					v.spaceParkedIn = ps;
					break;
				} else {

				}
			}
			occupants.add(p);
			int randomInt = ThreadLocalRandom.current().nextInt(1, 5);
			spawner.getReporter().addParkedCar(v.type);
			spawner.getMarket().setDemand();
		}
	}
	
	public void removeOccupants(Person p, Vehicle v) {
		
		if (v.buildingParkedIn == this) {
			v.isParkedInBuilding = false;
			v.buildingParkedIn = null;
			
			
			for (ParkingSpace ps: this.parkingSpaces) {
				if (v.spaceParkedIn == ps) {
					ps.vacate();
					v.isParkedInBuilding = false;
					v.buildingParkedIn = null;
					v.spaceParkedIn = null;
					//System.out.println("PS being vacated");
					break;
				} else {

				}
			}
			v.spaceParkedIn = null;
			spawner.getReporter().removeParkedCar(v.type);
			update(v.charge*0.025);
		} else {
			//System.out.println("Car is not aprked in this building");
		}


//		if(occupants.contains(p)) {
//			occupants.remove(p);
//			if (v.isParkedInBuilding) {
//				update(v.charge*0.25);
//				v.isParkedInBuilding = false;
//				spawner.getReporter().removeParkedCar(v.type);
//				
//			}
//			
//		} else {
//			//System.out.println("Occupant not in building: " + p);
//		}
	}

	
	public void setLoad3() {
		int time = Tools.getTime();
		if(isInInterval(time, AM0)) {
			if (!this.timeOfDay.equals("AM0")) {
				this.timeOfDay = "AM0";
				setLoad2(0d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(15);
			}
			
		} else if(isInInterval(time, AM1)) {
			if (!this.timeOfDay.equals("AM1")) {
				this.timeOfDay = "AM1";
				setLoad2(-0.020d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(14);
			}
			
		} else if(isInInterval(time, AM2)) {
			if (!this.timeOfDay.equals("AM2")) {
				this.timeOfDay = "AM2";
				setLoad2(-0.020d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(13);
			}
			
		} else if(isInInterval(time, AM3)) {
			if (!this.timeOfDay.equals("AM3")) {
				this.timeOfDay = "AM3";
				setLoad2(-0.020d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(10);
			}
		} 
		else if(isInInterval(time, AM4)) {
			if (!this.timeOfDay.equals("AM4")) {
				this.timeOfDay = "AM4";
				setLoad2(-0.020d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(11);
			}
		} 
		else if(isInInterval(time, AM5)) {
			if (!this.timeOfDay.equals("AM5")) {
				this.timeOfDay = "AM5";
				setLoad2(0.03d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(13);
			}
		} 
		else if(isInInterval(time, AM6)) {
			if (!this.timeOfDay.equals("AM6")) {
				this.timeOfDay = "AM6";
				setLoad2(0.05d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(15);
			}
		} 
		else if(isInInterval(time, AM7)) {
			if (!this.timeOfDay.equals("AM7")) {
				this.timeOfDay = "AM7";
				setLoad2(0.08d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(16);
			}
		} 
		else if(isInInterval(time, AM8)) {
			if (!this.timeOfDay.equals("AM8")) {
				this.timeOfDay = "AM8";
				setLoad2(0.05d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(18);
			}
		}
		
		else if(isInInterval(time, AM9)) {
			if (!this.timeOfDay.equals("AM9")) {
				this.timeOfDay = "AM9";
				setLoad2(0.05d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(20);
			}
		} 
		else if(isInInterval(time, AM10)) {
			if (!this.timeOfDay.equals("AM10")) {
				this.timeOfDay = "AM10";
				setLoad2(-0.06d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(23);
			}
		} 
		else if(isInInterval(time, AM11)) {
			if (!this.timeOfDay.equals("AM11")) {
				this.timeOfDay = "AM11";
				setLoad2(-0.06d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(25);
			}
		}
		else if(isInInterval(time, PM0)) {
			if (!this.timeOfDay.equals("PM0")) {
				this.timeOfDay = "PM0";
				setLoad2(-0.08d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(28);
			}
		} 
		else if(isInInterval(time, PM1)) {
			if (!this.timeOfDay.equals("PM1")) {
				this.timeOfDay = "PM1";
				setLoad2(-0.025d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(27);
			}
		} 
		else if(isInInterval(time, PM2)) {
			if (!this.timeOfDay.equals("PM2")) {
				this.timeOfDay = "PM2";
				setLoad2(-0.025d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(26);
			}
		} 
		else if(isInInterval(time, PM3)) {
			if (!this.timeOfDay.equals("PM3")) {
				this.timeOfDay = "PM3";
				setLoad2(-0.025d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(25);
			}
		} 
		else if(isInInterval(time, PM4)) {
			if (!this.timeOfDay.equals("PM4")) {
				this.timeOfDay = "PM4";
				setLoad2(-0.01d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(21);
			}
		} 
		else if(isInInterval(time, PM5)) {
			if (!this.timeOfDay.equals("PM5")) {
				this.timeOfDay = "PM5";
				setLoad2(-0.01d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(20);
			}
		} 
		else if(isInInterval(time, PM6)) {
			if (!this.timeOfDay.equals("PM6")) {
				this.timeOfDay = "PM6";
				setLoad2(-0.01d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(19);
			}
		} 
		else if(isInInterval(time, PM7)) {
			if (!this.timeOfDay.equals("PM7")) {
				this.timeOfDay = "PM7";
				setLoad2(-0.04d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(18);
			}
		} 
		else if(isInInterval(time, PM8)) {
			if (!this.timeOfDay.equals("PM8")) {
				this.timeOfDay = "PM8";
				setLoad2(-0.02d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(17);
			}
		} 
		else if(isInInterval(time, PM9)) {
			if (!this.timeOfDay.equals("PM9")) {
				this.timeOfDay = "PM9";
				setLoad2(-0.02d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(16);
			}
		} 
		else if(isInInterval(time, PM10)) {
			if (!this.timeOfDay.equals("PM10")) {
				this.timeOfDay = "PM10";
				setLoad2(-0.01d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(16);
			}
		} 
		else if(isInInterval(time, PM11)) {
			if (!this.timeOfDay.equals("PM11")) {
				this.timeOfDay = "PM11";
				setLoad2(-0.d);
				setLoadPrice();
				spawner.getReporter().setWeatherProfile(15);
			}
		}
		if((time % 8640) == 0 ) {
			//setLoad2(1d);
		}
	}
	
	
	public void setLoadPrice() {
		int time = Tools.getTime();
		if(isInInterval(time, NIGHT)) {
			this.loadPrice = 0d;
		} else if(isInInterval(time, MORNING)) {
			this.loadPrice = 0.5d;
		} else if(isInInterval(time, AFTERNOON)) {
			this.loadPrice = 0.4d;
		} else if(isInInterval(time, EVENING)) {
			this.loadPrice = 0.2d;
		} else if(isInInterval(time, MORNING_RUSH)) {
			this.loadPrice = 1d;
		} else if(isInInterval(time, AFTERNOON_RUSH)) {
			this.loadPrice = 0.4d;
		}
	}
	
	public Double getLoadPrice () {
		return this.loadPrice;
	}

	private boolean isInInterval(int n, int[] interval) {
		return n >= interval[0] && n < interval[1];
	}
	
	public BusStop getNearestBusStop() {
		Double minDist = Double.MAX_VALUE;
		Double dist = 0d;
		BusStop nearest = null;
		for(Object o: grid.getObjects()){
			if(o instanceof BusStop) {
				dist = Tools.manhattanDistance(grid.getLocation(o), grid.getLocation(this));
				if(dist < minDist) {
					minDist = dist;
					nearest = (BusStop) o;
				}
			}
		}
		return nearest;
	}
	
}