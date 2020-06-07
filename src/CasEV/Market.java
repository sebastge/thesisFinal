package CasEV;


public class Market {
	
	private Double supply = 0d;
	private Double demand = 0d;
	private Double priceLevel = 0d;
	private int numCars = 0;
	private int numEV = 0;
	private int numV2G = 0;

	
	

	
	public Double getSupply() {
		return supply;
	}
	
	public Double getDemand() {
		return demand;
	}
	
	public Double getPriceLevel() {
		return priceLevel;
	}
	public void setSupply() {
		supply ++;
	}
	public void setDemand() {
		demand ++;
	}
	
	public void setPriceLevel() {
		priceLevel ++;
	}
		
		
}

