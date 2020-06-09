package CasEV;

import repast.simphony.engine.schedule.ScheduledMethod;
import utils.Tools;


public class Market {
	
	/**
	 * Runs every step
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		
		setLoad();
		setPriceLevel();
//		System.out.println("V2G load available: " + this.v2gLoadAvailable);
//		System.out.println("Price level: " + this.priceLevel);
//		System.out.println("Num EV: " + this.numEV);
//		System.out.println("Num V2G: " + this.numV2G);
//		System.out.println("Total load market: " + this.totalLoad);
		
//		this.tester(this.totalLoad);
//		System.out.println("Output test: " + this.kWhPrice);

	
	}
	
	//Time of day translation from ticks and schedule
	
	private static final int[] AM0 = {0, 360}; 		//00:00 - 06:00
	private static final int[] AM1 = {360, 720}; 	//06:00 - 12:00
	private static final int[] AM2 = {720, 1080}; 	//12:00 - 18:00
	private static final int[] AM3 = {1080, 1440}; 	//18:00 - 00:00
	private static final int[] AM4 = {1440, 1800}; 	//07:00 - 08:30
	private static final int[] AM5 = {1800, 2160}; 	//15:30 - 17:00
	private static final int[] AM6 = {2160, 2520}; 	//00:00 - 06:00
	private static final int[] AM7 = {2520, 2880}; 	//06:00 - 12:00
	private static final int[] AM8 = {2880, 3240}; 	//12:00 - 18:00
	private static final int[] AM9 = {3240, 3600}; 	//18:00 - 00:00
	private static final int[] AM10 = {3600, 3960}; //07:00 - 08:30
	private static final int[] AM11 = {3960, 4320}; //15:30 - 17:00
	private static final int[] PM0 = {4320, 4680}; 	//00:00 - 06:00
	private static final int[] PM1 = {4680, 5040}; 	//06:00 - 12:00
	private static final int[] PM2 = {5040, 5400}; 	//12:00 - 18:00
	private static final int[] PM3 = {5400, 5760}; 	//18:00 - 00:00
	private static final int[] PM4 = {5760, 6120}; 	//07:00 - 08:30
	private static final int[] PM5 = {6120, 6480}; 	//15:30 - 17:00
	private static final int[] PM6 = {6480, 6840}; 	//00:00 - 06:00
	private static final int[] PM7 = {6840, 7200}; 	//06:00 - 12:00
	private static final int[] PM8 = {7200, 7560}; 	//12:00 - 18:00
	private static final int[] PM9 = {6480, 7920}; 	//18:00 - 00:00
	private static final int[] PM10 = {7920, 8280}; //07:00 - 08:30
	private static final int[] PM11 = {8280, 8640}; //15:30 - 17:00
	
	private static final int[] kWhPriceRange = {15, 45};
	private Double kWhPrice = 30d;

	private static final int[] loadInterval = {15, 45};
	
	
	private Double supply = 0d;
	private Double demand = 0d;
	private Double priceLevel = 0d;
	private Double loadPrice = 0d;
	private Double totalLoad = 0d;
	private int numCars = 0;
	private int numEV = 0;
	private int numV2G = 0;
	
	private Double v2gLoadAvailable = 0d;
	private Double v2gLoadWanted = 0d;
	
	private String timeOfDay = "";
	
//	Double input_start = 0d; // The lowest number of the range input.
//	Double input_end = 300d; // The lowest number of the range input.
//	Double output_start = 15d; // The lowest number of the range output.
//	Double output_end = 45d; // The largest number of the range output.
	
	
	private Double createDoubleInRange(Double inputVariable, Double input_start, Double input_end, Double output_start, Double output_end) {
		return output_start + ((output_end - output_start) / (input_end - input_start)) * (inputVariable - input_start);

		
	}
	
	public Double determineNeedFromAggregator(Double kWhOffered) {
		if (v2gLoadWanted > v2gLoadAvailable) {
			Double need = createDoubleInRange(kWhOffered, 0d, 25d, 0d, kWhOffered);
			System.out.println("Need: " + need);
			System.out.println("V2G avaialble: " + this.v2gLoadAvailable);
			System.out.println("V2G wanted: " + this.v2gLoadWanted);
			//Double need = kWhOffered * 0.25;<
			return need;
		} else {
			return 0d;
		}
		
	}
	
	
	public Double borrowFromAggregator (Double kWhOffered) {
		System.out.println("kWh offered: " + kWhOffered);
		if (kWhOffered > 0) {
			Double tempNeed = determineNeedFromAggregator(kWhOffered);
			this.v2gLoadAvailable += tempNeed;
			return tempNeed;
			
		} else {
			return 0d;
		}
		
	}
	
	
	public Double getSupply() {
		return supply;
	}
	public Double getDemand() {
		return demand;
	}
	public Double getPriceLevel() {
		return priceLevel;
	}
	public int getNumCars() {
		return numCars;
	}
	public int getNumEV() {
		return numEV;
	}
	public int getNumV2G() {
		return numV2G;
	}
	public Double getLoadPrice() {
		return loadPrice;
	}
	public Double getV2GLoadAvailable() {
		return v2gLoadAvailable;
	}
	public void setSupply() {
		supply ++;
	}
	public void setDemand() {
		demand ++;
	}
	public void setPriceLevel() {
		priceLevel = (numV2G * v2gLoadAvailable)/totalLoad;
	}
	public void addNumCars() {
		numCars++;
	}
	public void addNumEV() {
		numEV++;
	}
	public void addNumV2G() {
		numV2G++;
	}
	public void addV2GLoadAvailable(Double load) {
		this.v2gLoadAvailable += load;
	}
	public void removeNumCars() {
		numCars--;
	}
	public void removeNumEV() {
		numEV--;
	}
	public void removeNumV2G() {
		numV2G--;
	}
	public void removeV2GLoadAvailable(Double load) {
		this.v2gLoadAvailable -= load;
	}
	
	public void setLoad() {

		int time = Tools.getTime();
		if(isInInterval(time, AM0)) {
			if (!this.timeOfDay.equals("AM0")) {
				this.timeOfDay = "AM0";
				this.loadPrice = 10d;

			}
			
		} else if(isInInterval(time, AM1)) {
			if (!this.timeOfDay.equals("AM1")) {
				this.timeOfDay = "AM1";
				this.loadPrice = 12d;
			}
			
		} else if(isInInterval(time, AM2)) {
			if (!this.timeOfDay.equals("AM2")) {
				this.timeOfDay = "AM2";
				this.loadPrice = 13d;
			}
			
		} else if(isInInterval(time, AM3)) {
			if (!this.timeOfDay.equals("AM3")) {
				this.timeOfDay = "AM3";
				this.loadPrice = 13d;	
			}
		} 
		else if(isInInterval(time, AM4)) {
			if (!this.timeOfDay.equals("AM4")) {
				this.timeOfDay = "AM4";
				this.loadPrice = 15d;
			}
		} 
		else if(isInInterval(time, AM5)) {
			if (!this.timeOfDay.equals("AM5")) {
				this.timeOfDay = "AM5";
				this.loadPrice = 18d;
			}
		} 
		else if(isInInterval(time, AM6)) {
			if (!this.timeOfDay.equals("AM6")) {
				this.timeOfDay = "AM6";
				this.loadPrice = 8d;
			}
		} 
		else if(isInInterval(time, AM7)) {
			if (!this.timeOfDay.equals("AM7")) {
				this.timeOfDay = "AM7";
				this.loadPrice = 7d;
			}
		} 
		else if(isInInterval(time, AM8)) {
			if (!this.timeOfDay.equals("AM8")) {
				this.timeOfDay = "AM8";
				this.loadPrice = 12d;
			}
		}
		
		else if(isInInterval(time, AM9)) {
			if (!this.timeOfDay.equals("AM9")) {
				this.timeOfDay = "AM9";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, AM10)) {
			if (!this.timeOfDay.equals("AM10")) {
				this.timeOfDay = "AM10";
				this.loadPrice = 16d;
			}
		} 
		else if(isInInterval(time, AM11)) {
			if (!this.timeOfDay.equals("AM11")) {
				this.timeOfDay = "AM11";
				this.loadPrice = 14d;
			}
		}
		else if(isInInterval(time, PM0)) {
			if (!this.timeOfDay.equals("PM0")) {
				this.timeOfDay = "PM0";
				this.loadPrice = 20d;
			}
		} 
		else if(isInInterval(time, PM1)) {
			if (!this.timeOfDay.equals("PM1")) {
				this.timeOfDay = "PM1";
				this.loadPrice = 12d;
			}
		} 
		else if(isInInterval(time, PM2)) {
			if (!this.timeOfDay.equals("PM2")) {
				this.timeOfDay = "PM2";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, PM3)) {
			if (!this.timeOfDay.equals("PM3")) {
				this.timeOfDay = "PM3";
				this.loadPrice = 8d;
			}
		} 
		else if(isInInterval(time, PM4)) {
			if (!this.timeOfDay.equals("PM4")) {
				this.timeOfDay = "PM4";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, PM5)) {
			if (!this.timeOfDay.equals("PM5")) {
				this.timeOfDay = "PM5";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, PM6)) {
			if (!this.timeOfDay.equals("PM6")) {
				this.timeOfDay = "PM6";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, PM7)) {
			if (!this.timeOfDay.equals("PM7")) {
				this.timeOfDay = "PM7";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, PM8)) {
			if (!this.timeOfDay.equals("PM8")) {
				this.timeOfDay = "PM8";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, PM9)) {
			if (!this.timeOfDay.equals("PM9")) {
				this.timeOfDay = "PM9";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, PM10)) {
			if (!this.timeOfDay.equals("PM10")) {
				this.timeOfDay = "PM10";
				this.loadPrice = 13d;
			}
		} 
		else if(isInInterval(time, PM11)) {
			if (!this.timeOfDay.equals("PM11")) {
				this.timeOfDay = "PM11";
				this.loadPrice = 13d;
			}
		}
		if((time % 8640) == 0 ) {
			//setLoad2(1d);
		}
	}
	
	public void setkWhPrice() {
		
	}
	
	private boolean isInInterval(double outputValue, int[] interval) {
		return outputValue >= interval[0] && outputValue < interval[1];
	}
	public void setTotalLoad(Double totalLoad) {
		this.totalLoad = totalLoad;
		
	}
	
}

