package CasEV;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import CasEV.physical.roads.Road;
import repast.simphony.engine.schedule.ScheduledMethod;
import utils.Tools;


public class Market {
	
	/**
	 * Runs every step
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){

		setPriceLevel();
		setAverageTravelTime(this.avgTravelTimeList);


	
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
	
	private static final int[] NIGHT = {0, 2160}; 				//00:00 - 06:00
	private static final int[] MORNING = {2160, 4320}; 			//06:00 - 12:00
	private static final int[] AFTERNOON = {4320, 6480}; 		//12:00 - 18:00
	private static final int[] EVENING = {6480, 8640}; 			//18:00 - 00:00
	private static final int[] MORNING_RUSH = {2520, 3060}; 	//07:00 - 08:30
	private static final int[] AFTERNOON_RUSH = {5580, 6120}; 	//15:30 - 17:00
	

	private static final Double maxkWhSellingPrice = 5d;
	private static final Double minkWhSellingPrice = 0d;
	private static final Double maxkWhBuyingPrice = 5d;
	private static final Double minkWhBuyingPrice = 0d;
	
	private int v2gChargingBack = 0;
	private int v2gChargingFrom = 0;
	
	
	
	private Double supply = 0d;
	private Double demand = 0d;
	private Double priceLevel = 45d;
	private Double loadPrice = 0d;
	private Double totalLoad = 0d;
	private Double kWhSellingPrice = 1d;
	private Double kWhBuyingPrice = 1d;
	private Double biggestLoad = 0d;
	private Double smallestLoad = 0d;
	private int numCars = 0;
	private int numEV = 0;
	private int numV2G = 0;

	
	private Double maxPriceLevel = 60d;
	private Double minPriceLevel = 35d;
	
	private Double v2gLoadAvailable = 1d;
	private Double v2gLoadWanted = 1d;
	
	private String timeOfDay = "";
	
	private Double avgTravelTime;
	private List<Double> avgTravelTimeList = new ArrayList<Double>();
	
	
	public void addToAvgTravelTimeList(Double time) {
		this.avgTravelTimeList.add(time);
	}

	
	
	private Double createDoubleInRange(Double inputVariable, Double input_start, Double input_end, Double output_start, Double output_end) {
		return output_start + ((output_end - output_start) / (input_end - input_start)) * (inputVariable - input_start);
	}
	
	public void setPriceLevel() {
		
		
		if (totalLoad/v2gLoadAvailable > this.minPriceLevel && totalLoad/v2gLoadAvailable < this.maxPriceLevel) {
			this.priceLevel = (totalLoad/v2gLoadAvailable);
		}
		
		
	}
	 //random to account for unknown variable in price mechanism
	public Double getPriceLevel() {
		return this.priceLevel + ThreadLocalRandom.current().nextDouble(-3, 8);
	}


	private double setAverageTravelTime(List <Double> marks) {
		  Double sum = 0d;
		  if(!marks.isEmpty()) {
		    for (Double mark : marks) {
		    	
		        sum += mark;
		    }
		    this.avgTravelTime = sum.doubleValue() / marks.size();
		    return sum.doubleValue() / marks.size();
		  }
		  this.avgTravelTime = sum;
		  return sum;
	}
	
	public Double getAverageTravelTime() {
		return this.avgTravelTime;
	}
	
	private boolean isInInterval(int n, int[] interval) {
		return n >= interval[0] && n < interval[1];
	}
	
	
	public void setPrices() {
		if (v2gLoadAvailable > v2gLoadWanted) {
			this.kWhSellingPrice --;
			this.kWhBuyingPrice ++;
		} else {
			this.kWhSellingPrice ++;
			this.kWhBuyingPrice --;			
		}
	}
	
	public Double determineDemand(Double kWhOffered) {
		if (v2gLoadWanted > v2gLoadAvailable) {
			Double need = createDoubleInRange(kWhOffered, 0d, 25d, 0d, kWhOffered);

			return need;
		} else {
			Double need = createDoubleInRange(kWhOffered, -25d, 0d, kWhOffered, 0d);
			return need;
		}	
	}
	
	public double multiplyWithPeriodValue(double load) {
		int time = Tools.getTime();
		if (this.isInInterval(time, NIGHT)) {
			return load*0.8;
		} else if (this.isInInterval(time, MORNING)) {
			return load*1.2;
		} else if (this.isInInterval(time, AFTERNOON)) {
			return load*0.5;
		} else if (this.isInInterval(time, EVENING)) {
			return load*2.0;
		}
		else {
			return 0d;
		}
	}
	
	
	public Double borrowFromAggregator (Double kWhOffered) {

		
		if (v2gLoadWanted > v2gLoadAvailable) {
			Double need = this.multiplyWithPeriodValue(kWhOffered);
			if(kWhOffered > 0) {
				v2gLoadAvailable += need;
				v2gLoadWanted -= need;
				return need;
			} else {
				return 0d;
			}
		} else {
			Double need = this.multiplyWithPeriodValue(kWhOffered);
			if (kWhOffered < 0) {
				v2gLoadAvailable += need;
				v2gLoadWanted -= need;
				return need;
			} else {
				return 0d;
			}
		}	
	}
	public void setkWhSellingPrice() {
		double priceRatio = Math.abs(this.v2gLoadWanted/this.v2gLoadAvailable);
		if (this.minkWhSellingPrice <= priceRatio && priceRatio <= this.maxkWhSellingPrice) {
			this.kWhSellingPrice = priceRatio;
		} 
		
	}
	public void setkWhBuyingPrice() {
		double priceRatio = Math.abs(this.v2gLoadAvailable/this.v2gLoadWanted);
		if (this.minkWhBuyingPrice <= priceRatio && priceRatio <= this.maxkWhBuyingPrice) {
			this.kWhBuyingPrice = priceRatio;
		} 
	
	}

	public Double getkWhSellingPrice() {
		return this.kWhSellingPrice;
	}
	public Double getkWhBuyingPrice() {
		return this.kWhBuyingPrice;
	}
	public Double getTotalLoad() {

		return this.totalLoad;
	}
	public int getV2GChargingBack () {
		return this.v2gChargingBack;
		
	}
	public int getV2GChargingFrom() {
		return this.v2gChargingFrom;
	}
	public void addV2GChargingBack() {
	
		this.v2gChargingBack ++;
	}
	
	public void removeNumV2G() {

		this.numV2G--;

	}
	public void addV2GChargingFrom() {
		this.v2gChargingFrom ++;
	}
	public void removeV2GChargingBack() {

			this.v2gChargingBack --;

	}
	public void removeV2GChargingFrom() {
			this.v2gChargingFrom --;

	}
	public Double getSupply() {
		return supply;
	}
	public Double getDemand() {
		return demand;
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
	public Double getV2GLoadWanted() {
		return v2gLoadWanted;
	}
	public void setSupply() {
		supply ++;
	}
	public void setDemand() {
		demand ++;
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
		this.v2gLoadWanted -= load;
	}
	public void removeNumCars() {
		numCars--;
	}
	public void removeNumEV() {
		numEV--;
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
		
		
		
		if (totalLoad < 1300) {
			this.totalLoad = totalLoad + 87;
		} else {
			this.totalLoad = totalLoad;
		}
		
		//this.totalLoad = totalLoad;
		if (this.totalLoad > this.biggestLoad && this.totalLoad != 1000) {
			this.biggestLoad = this.totalLoad;
			//System.out.println("Biggest load: " + this.biggestLoad);
		}
		if (this.smallestLoad == 0) {
			this.smallestLoad = this.totalLoad;
		}
		if (this.totalLoad < this.smallestLoad && this.totalLoad != 0) {
			this.smallestLoad = this.totalLoad;
			//System.out.println("Smallest load: " + this.smallestLoad);
		}
		
	}
	
}

