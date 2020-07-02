package CasEV;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import CasEV.agent.Car;
import CasEV.agent.EV;
import CasEV.agent.Bus;
import CasEV.agent.Prosumer;
import CasEV.physical.electric.Aggregator;
import CasEV.physical.electric.ElectricEntity;
import CasEV.physical.electric.RegionalGridNode;
import CasEV.physical.roads.BusStop;
import CasEV.physical.roads.Road;
import CasEV.physical.roads.Spawn;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import utils.Tools;

/**
 * Handles the spawning of agents in the simulation and delegates to Spawns
 * @author andrfo
 *
 */
public class Spawner {

	/**
	 * Spawns agents periodically
	 * TODO: Figure out class stuff (Generalize)
	 */
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private Road[] spawnPoints;
	private List<Road> parkingNexi;
	private List<Aggregator> aggregators;
	private List<Aggregator> aggreagators2;
	private Network<Object> net;
	private List<BusStop> busStops;

		
	//private static final int[] NIGHT = {0, 8640};
	
	//Time of day translation from ticks and schedule
	private static final int[] NIGHT = {0, 1080}; 				//00:00 - 06:00
	private static final int[] MORNING = {1080, 2160}; 			//06:00 - 12:00
	private static final int[] AFTERNOON = {2160, 3240}; 		//12:00 - 18:00
	private static final int[] EVENING = {3240, 4320}; 			//18:00 - 00:00
	
	private static final int[] MORNING_RUSH = {0, 3060}; 	//07:00 - 08:30
	private static final int[] AFTERNOON_RUSH = {5580, 6120}; 	//15:30 - 17:00
	
	private static final int[] BUS =  {2160, 7920};
	
	private static final int[] TEST1 = {0, 2160};

	

	
	
	
	
	/**
	 * The distribution between the spawn points
	 * where the lists index + 1 corresponds to the spawn point number noted in the simulation by sidewalks in the shape of numbers
	 * 
	 * Does not need to sum to 1, you can have relative probabilities
	 */
	private Double[] loadDistribution;
	private Double[] csum; //Cumulative load distribution
	
	private Double nightFrequency;
	private int populationStartCount;
	

	private Double morningFrequency;
	private Double afternoonFrequency;
	private Double eveningFrequency;
	private Double rushFrequency;
	private int personsPerCar;

	
	private List<Prosumer> population;
	
	private static final int DAYS_TO_RUN = 7;
	
	private double frequency; //Spawns per tick
	private ArrayList<Prosumer> idleWorkers;
	private ArrayList<Prosumer> idleShoppers;
	private Reporter reporter;
	private Market market;
	
	private int experimentNum;
	private int CENTRE_CHARGING;
	private int OUTSIDE_CHARGING; 

	
	@SuppressWarnings("unchecked")
	public Spawner(
			ContinuousSpace<Object> space, 
			Grid<Object> grid, 
			Context<Object> context, 
			Road[] spawnPoints, 
			List<Road> despawnPoints, 
			List<Road> parkingSpaces, 
			List<Aggregator> aggregators,
			List<Aggregator> aggreagators2,
			List<BusStop> busStops, 
			List<Road> parkingNexiRoads,
			int experimentNum,
			int CENTRE_CHARGING,
			int OUTSIDE_CHARGING) {
		super();
		this.space = space;
		this.grid = grid;
		this.spawnPoints = spawnPoints;
		this.aggregators = aggregators;
		this.aggreagators2 = aggreagators2;
		this.busStops = busStops;
		this.parkingNexi = parkingNexiRoads;
		this.experimentNum = experimentNum;
		this.reporter = new Reporter(this.experimentNum);
		this.market = new Market();
		if(spawnPoints.length == 0 || despawnPoints.size() == 0) {
			throw new IllegalArgumentException("no spawn or goal");
		}
		this.experimentNum = experimentNum;
		this.CENTRE_CHARGING = CENTRE_CHARGING;
		this.OUTSIDE_CHARGING = OUTSIDE_CHARGING;
		
		net = (Network<Object>)context.getProjection("road network");
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		
		//Sets up the parameters to be determined in the GUI
		this.nightFrequency = params.getDouble("Car_frequency_at_Night");
		this.morningFrequency = params.getDouble("Car_frequency_in_the_Morning");
		this.afternoonFrequency = params.getDouble("Car_frequency_in_the_Afternoon");
		this.eveningFrequency = params.getDouble("Car_frequency_in_the_Evening");
		this.rushFrequency = params.getDouble("Car_frequency_in_Rushhour");
		this.populationStartCount = params.getInteger("population_start_count");
		
		this.loadDistribution = new Double[4];
		loadDistribution[0] = params.getDouble("load_on_entry_1");
		loadDistribution[1] = params.getDouble("load_on_entry_2");
		loadDistribution[2] = params.getDouble("load_on_entry_3");
		loadDistribution[3] = params.getDouble("load_on_entry_4");
		
		this.population = new ArrayList<Prosumer>(populationStartCount);
		this.idleWorkers = new ArrayList<Prosumer>();
		this.idleShoppers = new ArrayList<Prosumer>();
		generatePopulation();
		
		//Create a cumulative probabilities list
		csum = new Double[spawnPoints.length];
		Double sum = 0d;
		for(int i = 0; i < loadDistribution.length; i++) {
			csum[i] = sum + loadDistribution[i];
			sum = Double.valueOf(csum[i]);
		}
	}
	
	/**
	 * Is called each step of the simulation
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		//Are we there yet?
		isRunEnd();
		
		//Spawn frequencies
		setFrequency();
		
		//Spawns agents into the model
		spawn();
	}
	
	/**
	 * Checks whether the simulation has run the determined amount of days. if so it ends the run.
	 */
	private void isRunEnd() {
		double currentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if(currentTick >= Tools.TICKS_PER_DAY * DAYS_TO_RUN) {
			RunEnvironment.getInstance().endRun();
		}
	}
	
		
	/**
	 * Generates the population and splits it into workers and shoppers
	 */
	private void generatePopulation() {
		System.out.println("Generatepop called: " + populationStartCount);
		for(int i = 0; i < populationStartCount; i++) {
			Prosumer p = new Prosumer(space, grid, this);
			System.out.println("Person added: " + p);
			
			//In case of experiment 1. Spawn agents with goals at both parts of town at 50/50 ratio. TODO: add to official parameters
			
			if (this.experimentNum == 1) {
				
				if (Math.random() > 0.5) {
					//System.out.println("math random 1");
					p.setWorkPlace(aggregators.get(RandomHelper.nextIntFromTo(0, aggregators.size() - 1)));
				} else {
					System.out.println("math random 2");
					System.out.println(aggreagators2.size());
					p.setWorkPlace(aggreagators2.get(RandomHelper.nextIntFromTo(0, aggreagators2.size() - 1)));
				}
				
				
				//If not experiment 1, normal goals are added
			} else {
				p.setWorkPlace(aggregators.get(RandomHelper.nextIntFromTo(0, aggregators.size() - 1)));
			}
			

			
			population.add(p);
			idleWorkers.add(p);
			System.out.println(p + " has workpkace: " + p.workPlace);
		}
	}
	
	/**
	 * Sets up and spawns the agents of the simulation, and ads them to the queue of a(random) spawn point
	 */
	
	private void spawn() {
		//TODO: implement car pooling
		int spawnCount;
		int time = Tools.getTime();
		if(time % 30 == 0 /*&& isInInterval(time, BUS)*/) { //Spawn bus every 5 minutes from a random spawn
			//System.out.println("Bus spawned");
			Road r = getSpawnPoint();
				Spawn s = (Spawn) r;
				Bus bus = new Bus(space, grid, 50, parkingNexi, this);
				for(int i = 0; i < busStops.size(); i++) {
					Road currentBussStop = r;
					List<Road> used = new ArrayList<Road>();
					double dist = 0;
					double maxDist = Double.MAX_VALUE;
					Road bestBusStop = null;
					for(Road b : busStops) {
						if(used.contains(b)) {
							continue;
						}
						dist = Tools.gridDistance(currentBussStop.getLocation(), b.getLocation());
						if(dist < maxDist) {
							bestBusStop = b;
						}
					}
					bus.addGoal(bestBusStop);
					used.add(bestBusStop);
					currentBussStop = bestBusStop;
				}
				s.addToVehicleQueue(bus);
		}
		if(isInInterval(time, NIGHT)) { //Spawn worker
			//98% of the workers are going to work over an hour and a half(2% are sick)
			Double workers = ((double) idleWorkers.size())*0.2d*(1d/540d);
			BigDecimal[] valRem = BigDecimal.valueOf(workers).divideAndRemainder(BigDecimal.ONE);
			spawnCount = valRem[0].intValue();
			if(Tools.isTrigger(valRem[1].doubleValue())) { //Uses the remainder as a probability for an extra spawn
				spawnCount++;
			}
			spawnAgent(true, spawnCount);
		} else if(isInInterval(time, MORNING)) { //Spawn worker
			//98% of the workers are going to work over an hour and a half(2% are sick)
			Double workers = ((double) idleWorkers.size())*0.4d*(1d/540d);
			BigDecimal[] valRem = BigDecimal.valueOf(workers).divideAndRemainder(BigDecimal.ONE);
			spawnCount = valRem[0].intValue();
			if(Tools.isTrigger(valRem[1].doubleValue())) { //Uses the remainder as a probability for an extra spawn
				spawnCount++;
			}
			spawnAgent(true, spawnCount);

		} else if(isInInterval(time, AFTERNOON)) { //Spawn worker
			//98% of the workers are going to work over an hour and a half(2% are sick)
			Double workers = ((double) idleWorkers.size())*0.2d*(1d/540d);
			BigDecimal[] valRem = BigDecimal.valueOf(workers).divideAndRemainder(BigDecimal.ONE);
			spawnCount = valRem[0].intValue();
			if(Tools.isTrigger(valRem[1].doubleValue())) { //Uses the remainder as a probability for an extra spawn
				spawnCount++;
			}
			spawnAgent(true, spawnCount);

		} else if(isInInterval(time, EVENING)) { //Spawn worker
			//98% of the workers are going to work over an hour and a half(2% are sick)
			Double workers = ((double) idleWorkers.size())*0.2d*(1d/540d);
			BigDecimal[] valRem = BigDecimal.valueOf(workers).divideAndRemainder(BigDecimal.ONE);
			spawnCount = valRem[0].intValue();
			if(Tools.isTrigger(valRem[1].doubleValue())) { //Uses the remainder as a probability for an extra spawn
				spawnCount++;
			}
			spawnAgent(true, spawnCount);

		} 
		else {
			BigDecimal[] valRem = BigDecimal.valueOf(frequency).divideAndRemainder(BigDecimal.ONE);
			spawnCount = valRem[0].intValue();
			if(Tools.isTrigger(valRem[1].doubleValue())) { //Uses the remainder as a probability for an extra spawn
				spawnCount++;
			}
			spawnAgent(false, spawnCount);
		}
		
	}


	/**
	 * Sets up and spawns a person into the simulation in either a car, or as waiting for a bus as a spawn point.
	 * @param isWorker is it a worker? if not, its a shopper
	 * @param spawnCount The number of agents to spawn
	 */
	
	private void spawnAgent(boolean isWorker, int spawnCount) {
		if(isWorker) {
			
			for (int i = 0; i < spawnCount; i++) {
				//System.out.println("in wrorker for");
				if(idleWorkers.size() == 0) {
					return;
				}
				Prosumer p = idleWorkers.remove(0);
				
				//Start and goal
				Spawn start = getSpawnPoint();
				if(p.getTravelChoice().equals("bus")) {//Bus
					start.addToBusQueue(p);
				}
				else {//Car
					
					//Percentage EV-adjust accordingly. Currently at 0.2 to 0.8 normal cars. TODO: add to official parameters
					
					if (Math.random() > 0.8) {
						//System.out.println("A EV is spawneds");

						EV ev = new EV(space, grid, 5, parkingNexi, this);
						ev.addOccupant(p);
						
						//Setup
						  
						ev.addGoal(p.getWorkPlace());
						ev.setStart(start);
						ev.setNet(net);
						
						start.addToVehicleQueue(ev);
					} else {
						//System.out.println("A car is spawneds");
						Car car = new Car(space, grid, 5, parkingNexi, this);
						car.addOccupant(p);
						
						//Setup
						
						car.addGoal(p.getWorkPlace());
						car.setStart(start);
						car.setNet(net);
						
						start.addToVehicleQueue(car);

					}
					

				}
			}
		}
		else {//Shopper
			
			for (int i = 0; i < spawnCount; i++) {

				if(idleShoppers.size() == 0) {
					continue;
				}

				//Start and goal
				Spawn start = getSpawnPoint();
				Prosumer p = idleShoppers.remove(0);
				
				//Random shopping place each trip
				p.setShoppingPlace(aggregators.get(RandomHelper.nextIntFromTo(0, aggregators.size() - 1)));
				
				if(p.getTravelChoice().equals("bus1")) {//Bus
					start.addToBusQueue(p);
				}
				
				//Percentage EV-adjust accordingly. Currently at 0.2 to 0.8 normal cars. TODO: add to official parameters
				else {//car
					
					if (Math.random() > 0.8) {
						//Add the agent to the context
						EV ev = new EV(space, grid, 5, parkingNexi, this);
						
						ev.addOccupant(p);
						
						//Setup
						
//						car.addGoal(parkingSpaces.get(RandomHelper.nextIntFromTo(0, parkingSpaces.size() - 1)));//Random parking space as a goal
						ev.addGoal(p.getShoppingPlace());
						ev.setStart(start);
						ev.setNet(net);
						
						start.addToVehicleQueue(ev);
					} else {
						//Add the agent to the context
						Car car = new Car(space, grid, 5, parkingNexi, this);
						
						car.addOccupant(p);
						
						//Setup
						
//						car.addGoal(parkingSpaces.get(RandomHelper.nextIntFromTo(0, parkingSpaces.size() - 1)));//Random parking space as a goal
						car.addGoal(p.getShoppingPlace());
						car.setStart(start);
						car.setNet(net);
						
						start.addToVehicleQueue(car);

					}

				

				}
			}
		}
	}
	
	/**
	 * Returns a random spawn point from the defined probability distribution
	 * @return Random spawn from distribution
	 */
	private Spawn getSpawnPoint() {
		Spawn start = null;
		
		double r = new Random().nextDouble() * csum[csum.length-1];
		
		for(int i = 0; i < spawnPoints.length; i++) {
		    if (csum[i] > r) {
		        start = (Spawn) spawnPoints[i];
		    	break;
		    }
		}
		return start;
	}
	
	/**
	 * spawn rate times 100 to make it show up on the graph in the GUI
	 * @return double, spawn rate*100
	 */
	public double getSpawnRate() {
		return frequency*100d;
	}
	
	public Reporter getReporter() {
		return reporter;
	}
	
	public Market getMarket() {
		return this.market;
	}
	
	/**
	 * Sets the spawn rate based on the time of day
	 */
	private void setFrequency() {
		int time = Tools.getTime();
		frequency = 0;
		
		if(isInInterval(time, NIGHT)) {
			frequency += nightFrequency;
		}
		else if(isInInterval(time, MORNING)) {
			frequency += morningFrequency;
		}
		else if(isInInterval(time, AFTERNOON)) {
			frequency += afternoonFrequency;		
		}
		else if(isInInterval(time, EVENING)) {
			frequency += eveningFrequency;
		}

		
		
	}
	
	/**
	 * Helper function to determine of a tick tick count mod(day) is within an interval
	 * @param n
	 * @param interval
	 * @return True if within interval, false otherwise
	 */
	private boolean isInInterval(int n, int[] interval) {
		return n >= interval[0] && n < interval[1];
	}
	
	/**
	 * Returns a shopper to the pool of shopper
	 * @param p, Person
	 */
	public void returnShopper(Prosumer p) {
		idleShoppers.add(p);
	}
	
	/**
	 * Returns are worker to the pool of workers
	 * @param p, Person
	 */
	public void returnWorker(Prosumer p) {
		idleWorkers.add(p);
	}
}