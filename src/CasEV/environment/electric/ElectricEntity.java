package CasEV.environment.electric;


import CasEV.environment.Entity;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

/**
 * Entities are fixed geography and stuff
 * @author andrfo
 *
 */

public abstract class ElectricEntity extends Entity{
	
	
	protected ElectricEntity parent;
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	protected Double totalLoad;
	protected Integer totalOccupants;
	public ElectricEntity(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid);
		this.space = space;
		this.grid = grid;
		this.totalLoad = 0d;
		this.grid = grid;
		this.space = space;
		this.totalOccupants = 0;	
	}
	

	
	Double input_start = 0d; // The lowest number of the range input.
	Double input_end = 300d; // The lowest number of the range input.
	Double output_start = 15d; // The lowest number of the range output.
	Double output_end = 45d; // The largest number of the range output.
	

	

	
	
	public GridPoint getLocation() {
		return grid.getLocation(this);
	}
	
	public void setParent(ElectricEntity parent) {
		this.parent = parent;

	}
	
	public void update(Double delta) {
		
		this.totalLoad += delta;
		updateParent(delta);

	}
	
	
	public void updateParent(Double delta) {
		
		if(parent != null) {
			this.parent.update(delta);
		}
	}

	public void init() {

		updateParent(totalLoad);
	}
	
	public String getLoad() {

		return Double.toString(Math.round(totalLoad));
	}
	
	public void setLoad2(Double load) {
		
		this.totalLoad = load;
		updateParent(this.totalLoad);
		
	}
	
	
	public String getOccupants() {
		return totalOccupants.toString();
	}
}
