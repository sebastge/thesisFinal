package CasEV;

public class Reporter {

	
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

	private Double parkedCars = 0d;
	
	private Double elPrice = 0d;
	
	private int parkedCars0 = 0;
	private int parkedCars1 = 0;
	private int parkedCars2 = 0;
	private int parkedCars3 = 0;
	
	
	
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
	public Double getCarsParked() {
		return parkedCars;
	}
	public int getCarsParked0() {
		return parkedCars0;
	}
	public int getCarsParked1() {
		return parkedCars1;
	}
	public int getCarsParked2() {
		return parkedCars2;
	}
	public int getCarsParked3() {
		return parkedCars3;
	}
	public Double getElPrice() {
		return elPrice;
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

		this.parkedCars++;
		
		if (type == 0) {
			this.parkedCars0++;
		} else if (type == 1) {
			this.parkedCars1++;
		} else if (type == 2) {
			this.parkedCars2++;
		} else {
			this.parkedCars3++;
		}	
	}
	
	public void removeParkedCar(int type) {

		this.parkedCars--;
		
		if (type == 0) {
			this.parkedCars0--;
		} else if (type == 1) {
			this.parkedCars1--;
		} else if (type == 2) {
			this.parkedCars2--;
		} else {
			this.parkedCars3--;
		}
	}
	
	public void setTotalLoad(Double totalLoad) {
		this.totalLoad = totalLoad;
	}
	
	public void setElPrice(Double elPrice) {
		this.elPrice = elPrice;
	}
	
	
	
	
	
	
	
	
	
	

}
