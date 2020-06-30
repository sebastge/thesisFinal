package CasEV;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import CasEV.physical.electric.Aggregator;
import CasEV.physical.electric.RegionalGridNode;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Reporter {
	
	private int experimentNum;
	
	private ArrayList<RegionalGridNode> rgList = new ArrayList<RegionalGridNode>();
	
	private RegionalGridNode rgnCentre;
	private RegionalGridNode rgnOutside;
	
	
	public Reporter(int experimentNum) {

		this.experimentNum = experimentNum;

	}
	
	private int peakCars0Parked = 0;
	private int peakCars1Parked = 0;
	private int peakCars2Parked = 0;
	private int peakCars3Parked = 0;
	
	private Double biggestLoadCentre = 0d;
	private Double biggestLoadOutside = 0d;
	
	private Double smallestLoadCentre = 0d;
	private Double smallestLoadOutside = 0d;

	
	//General
	//===============================================================
	private int nGeneralTime = 0; //Number of addages, may be better to use a list and use .size() later
	private int nGeneralCost = 0;
	private int nGeneralDistance = 0;
	private Double averageTravelTime = 0d;
	private Double averageTravelCost = 0d;
	private Double averageTravelDistance = 0d; //Just cars for now
	
	//Bus
	//===============================================================
	private int nBusTime = 0; //Number of addages, may be better to use a list and use .size() later
	private int nBusCost = 0;
	private int nBusDistance = 0;
	private Double averageBusCost = 0d;
	private Double averageBusTravelTime = 0d;
	private Double averageBusTravelDistance = 0d; //From bus stop to destination("walking")
	
	
	//Car
	//===============================================================
	private int nCarTime = 0; //Number of addages, may be better to use a list and use .size() later
	private int nCarCost = 0;
	private int nCarDistance = 0;
	private Double AverageCarCost = 0d;
	private Double AverageCarTravelTime = 0d;
	private Double averageCarTravelDistance = 0d;
	
	//SEB
	
	
	private Double totalLoad = 0d;

	private int parkedCars = 0;
	
	private Double elPrice = 0d;
	
	private int parkedCars0 = 0;
	private int parkedCars1 = 0;
	private int parkedCars2 = 0;
	private int parkedCars3 = 0;
	private int parkedCars4 = 0;
	
	private int biggestParkedCars0 = 0;
	private int biggestParkedCars1 = 0;
	private int biggestParkedCars2 = 0;
	private int biggestParkedCars3 = 0;
	private int biggestParkedCars4 = 0;
	private int biggestTotalCars = 0;
	
	private int weatherProfile = 0;
	
	private int randomInt = ThreadLocalRandom.current().nextInt(1, 5);
	private Double totalLoadCentre = 0d;
	private Double totalLoadOutside = 0d;
	
	
	
	
	
	//===============================================================
	//Getters
	//===============================================================
	public Double getAverageTravelTime() {
		return averageTravelTime;
	}
	public Double getAverageTravelCost() {
		return averageTravelCost;
	}
	public Double getAverageTravelDistance() {
		return averageTravelDistance;
	}
	public Double getAverageBusCost() {
		return averageBusCost;
	}
	public Double getAverageBusTravelTime() {
		return averageBusTravelTime;
	}
	public Double getAverageBusTravelDistance() {
		return averageBusTravelDistance;
	}
	public Double getAverageCarCost() {
		return AverageCarCost;
	}
	public Double getAverageCarTravelTime() {
		return AverageCarTravelTime;
	}
	public Double getAverageCarTravelDistance() {
		return averageCarTravelDistance;
	}
	public Double getTotalLoad() {
		return totalLoad;
	}
	public Double getTotalLoadCentre() {
		return totalLoadCentre;
	}
	public Double getTotalLoadOutside() {
		return totalLoadOutside;
	}
	public int getCarsParked() {
		return parkedCars;
	}
	public int getCarsParked0() {
		if (this.parkedCars0 < 0) {
			return 0;
		}
		return parkedCars0;
	}
	public int getCarsParked1() {
		if (this.parkedCars1 < 0) {
			return 0;
		}
		return parkedCars1;
	}
	public int getCarsParked2() {
		
		if (this.parkedCars2 < 0) {
			return 0;
		}
		return parkedCars2;
	}
	public int getCarsParked3() {
		
	if (this.parkedCars3 < 0) {
		return 0;
	}

	return parkedCars3;
	

		
	}
	public int getCarsParked4() {
		return parkedCars4;
	}
	public Double getElPrice() {
		return elPrice;
	}
	public int getWeatherProfile() {
		return weatherProfile;
	}
	
	
	public void setRgn(RegionalGridNode rgnCentre, RegionalGridNode rgnOutside) {
		this.rgnCentre =rgnCentre;
		this.rgnOutside = rgnOutside;
	}
	
	
	//===============================================================
	//Adders
	//===============================================================
	
	//General
	//===============================================================
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageTravelTime(Double val) {
		nGeneralTime++;
		Double oldVal = Double.valueOf(averageTravelTime);
		Double newVal = oldVal + (val - oldVal) / nGeneralTime;
		
		averageTravelTime = newVal;
		
	}
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageTravelCost(Double val) {
		nGeneralCost++;
		Double oldVal = Double.valueOf(averageTravelCost);
		Double newVal = oldVal + (val - oldVal) / nGeneralCost;
		
		averageTravelCost = newVal;
	}
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageTravelDistance(Double val) {
		nGeneralDistance++;
		Double oldVal = Double.valueOf(averageTravelDistance);
		Double newVal = oldVal + (val - oldVal) / nGeneralDistance;
		
		averageTravelDistance = newVal;
	}
	
	
	//Bus
	//===============================================================	
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageBusCost(Double val) {
		addToAverageTravelCost(val);
		nBusCost++;
		Double oldVal = Double.valueOf(averageBusCost);
		Double newVal = oldVal + (val - oldVal) / nBusCost;
		
		averageBusCost = newVal;
		
	}
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageBusTravelTime(Double val) {
		addToAverageTravelTime(val);
		nBusTime++;
		Double oldVal = Double.valueOf(averageBusTravelTime);
		Double newVal = oldVal + (val - oldVal) / nBusTime;
		
		averageBusTravelTime = newVal;
	}
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageBusTravelDistance(Double val) {
		addToAverageTravelDistance(val);
		nBusDistance++;
		Double oldVal = Double.valueOf(averageBusTravelDistance);
		Double newVal = oldVal + (val - oldVal) / nBusDistance;
		
		averageBusTravelDistance = newVal;
	}
	
	
	//Car
	//===============================================================
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageCarCost(Double val) {
		addToAverageTravelCost(val);
		nCarCost++;
		Double oldVal = Double.valueOf(AverageCarCost);
		Double newVal = oldVal + (val - oldVal) / nCarCost;
		
		AverageCarCost = newVal;
	}
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageCarTravelTime(Double val) {
		addToAverageTravelTime(val);
		nCarTime++;
		Double oldVal = Double.valueOf(AverageCarTravelTime);
		Double newVal = oldVal + (val - oldVal) / nCarTime;
		
		AverageCarTravelTime = newVal;
	}
	/**
	 * @param val the value to add to the average
	 */
	public void addToAverageCarTravelDistance(Double val) {
		addToAverageTravelDistance(val);
		nCarDistance++;
		Double oldVal = Double.valueOf(averageCarTravelDistance);
		Double newVal = oldVal + (val - oldVal) / nCarDistance;
		
		averageCarTravelDistance = newVal;
	}
	
	public void addParkedCar(int type) {
		
		//int adder = ThreadLocalRandom.current().nextInt(1, 3);

		this.parkedCars ++;

		if (type == 0) {
			this.parkedCars0 += ThreadLocalRandom.current().nextInt(2, 3);;
			if (this.biggestParkedCars0 < this.parkedCars0) {
				this.biggestParkedCars0 = this.parkedCars0;
				//System.out.println("new biggestcar0: " + this.biggestParkedCars0);
			}
		} else if (type == 1) {
			this.parkedCars1 += ThreadLocalRandom.current().nextInt(2, 6);
			if (this.biggestParkedCars1 < this.parkedCars1) {
				this.biggestParkedCars1 = this.parkedCars1;
				//System.out.println("new biggestcar1: " + this.biggestParkedCars1);
			}
		} else if (type == 2) {
			this.parkedCars2 += ThreadLocalRandom.current().nextInt(2, 5);
			if (this.biggestParkedCars2 < this.parkedCars2) {
				this.biggestParkedCars2 = this.parkedCars2;
				//System.out.println("new biggestcar2: " + this.biggestParkedCars2);
			}
		} else if (type == 3) {
			
			this.parkedCars3 += ThreadLocalRandom.current().nextInt(2, 4);
			if (this.biggestParkedCars3 < this.parkedCars3) {
				this.biggestParkedCars3 = this.parkedCars3;
				//System.out.println("new biggestcar3: " + this.biggestParkedCars3);
			}
		} else {
			this.parkedCars --;
			this.parkedCars4++;
			if (this.biggestParkedCars4 < this.parkedCars4) {
				this.biggestParkedCars4 = this.parkedCars4;
				//System.out.println("new biggestcar4: " + this.biggestParkedCars4);
			}
		}
		
		if (this.biggestTotalCars < this.parkedCars) {
			this.biggestTotalCars = this.parkedCars;
			//System.out.println("New biggest total cars: " + this.biggestTotalCars);

		}
			
		
	}
	
	public void removeParkedCar(int type) {

		this.parkedCars--;
		
		if (type == 0) {
			this.parkedCars0 -= ThreadLocalRandom.current().nextInt(2, 3);
		} else if (type == 1) {
			this.parkedCars1 -= ThreadLocalRandom.current().nextInt(2, 6);
		} else if (type == 2) {
			this.parkedCars2 -= ThreadLocalRandom.current().nextInt(2, 5);
		} else if (type == 3) {
			this.parkedCars3 -= ThreadLocalRandom.current().nextInt(2, 4);
		} else {
			this.parkedCars++;
			this.parkedCars4--;
		}
	}
	
	public void setTotalLoad(Double totalLoad, RegionalGridNode rgn) {
		
		if (this.experimentNum == 1) {
			if (rgn == this.rgnCentre) {
				if (totalLoad > this.biggestLoadCentre) {
					this.biggestLoadCentre = totalLoad;
					System.out.println("New biggest load centre: " + this.biggestLoadCentre);
				}
				if (totalLoad < this.smallestLoadCentre || this.smallestLoadCentre == 0) {
					this.smallestLoadCentre = totalLoad;
					System.out.println("New smallest load centre: " + this.smallestLoadCentre);
				}
				setTotalLoadCentre(totalLoad);
			} else if (rgn == this.rgnOutside) {
				if (totalLoad > this.biggestLoadOutside) {
					this.biggestLoadOutside = totalLoad;
					System.out.println("New biggest load outside: " + this.biggestLoadOutside);
				}
				if (totalLoad < this.smallestLoadOutside || this.smallestLoadOutside == 0) {
					this.smallestLoadOutside = totalLoad;
					System.out.println("New smallest load outside: " + this.smallestLoadOutside);
				}
				setTotalLoadOutside(totalLoad);

			}
			
		} else {
			this.totalLoad = totalLoad;
		}


	}
	public void setTotalLoadCentre(Double totalLoad) {

		this.totalLoadCentre = totalLoad;
	}
	public void setTotalLoadOutside(Double totalLoad) {

		this.totalLoadOutside = totalLoad;
	}
	
	public void setElPrice(Double elPrice) {
		this.elPrice = elPrice;
	}
	
	public void setWeatherProfile(int weatherProfile) {
		this.weatherProfile = weatherProfile;
	}
}
