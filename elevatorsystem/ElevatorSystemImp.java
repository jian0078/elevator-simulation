
package elevatorsystem;

import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import elevator.Elevator;
import elevator.MovingState;
import elevator.QuickSortingFloors;

/**
 * Assignment 2 Reference from Professor Shahriar (Shawn) Emami
 * 
 * @author Yi Jiang Class ElevatorSystemImp
 * @version March.11, 2018
 */

public class ElevatorSystemImp implements ElevatorSystem, ElevatorPanel {
	private final Object REQUEST_LOCK = new Object();
	private final int MAX_FLOOR;
	private final int MIN_FLOOR;
	private Map<Elevator, List<Integer>> stops;
	private ExecutorService service;
	
	AtomicBoolean alive = new AtomicBoolean(true);
	// private QuickSortingFloors sortFloor=new QuickSortingFloors();

	/**
	 * Constructor of class ElevatorSystemImp
	 * 
	 * @param MIN_FLOOR
	 *            minimun floor of the elevator
	 * @param MAX_FLOOR
	 *            maximun floor of the elevator
	 */
	public ElevatorSystemImp(int MIN_FLOOR, int MAX_FLOOR) {

		this.MIN_FLOOR = MIN_FLOOR;
		this.MAX_FLOOR = MAX_FLOOR;
		service = Executors.newCachedThreadPool();
		stops = new HashMap<>();
		// service = Executors.newFixedThreadPool(stops.size() + 1);
	}

	/**
	 * method run implements of Runnable class, which will synchronized for all
	 * elevator thread
	 */
	private Runnable run = () -> {
		// system thread
		AtomicInteger[] ai = new AtomicInteger[stops.size()];
		
		// initilize each atomic varibale
		// init ac to new AI(0);
		for(int i=0;i<ai.length;i++) {
			ai[i]=new AtomicInteger(0);	
		}
			while (alive.get()) {
				for (Elevator e : stops.keySet()) {
					// get list of stops
					List<Integer> l = stops.get(e);
					
					if (!(e.isIdle()) || stops.get(e).isEmpty() || (ai[e.id()].get()!=0)) {
						continue;
					}
				
					synchronized (REQUEST_LOCK) {
						int floor = l.remove(0);

						ai[e.id()].incrementAndGet();
						service.submit(() -> {
							e.moveTo(floor);
							ai[e.id()].decrementAndGet();
						});
					}
				}
			}
	};

	/**
	 * method start, which start the main thread controlling {@link ElevatorSystem}
	 */
	public void start() {
		alive.set(true);
		service.submit(run);// system Thread
		// if runnable inherited
		// service.submit(this);
		// can refference from network code
	}

	/**
	 * this method will request stop to the floor which is contral for the thread
	 * sleep time
	 * 
	 * @param elevator
	 * @param floors
	 */
	public void requestStop(Elevator elevator, int floor) {
		requestStops(elevator, floor);
	}

	/**
	 * this method will show the request stop through syschronized sleep time
	 * 
	 * @param elevator, object of different elevator,
	 *           
	 * @param floors-mutipul floors which choosed by the user in the elevator
	 *            
	 */
	public void requestStops(Elevator elevator, int... floors) {
		// USE THIS SORT BEFORE IS BETTER , NO NEED CHANGE A LOT OF CODE
		// four thread run same time, add information to your map
		// IF SORT AFTER WILL BE DIFFERENT
		// sort floors first
		if (floors==null || elevator == null) {
			throw new NullPointerException();
		}

		QuickSortingFloors.sort(floors, floors.length);

		synchronized (REQUEST_LOCK) {
			List<Integer> list = stops.get(elevator);
			for (int f : floors) {
				list.add(f);
			}
			// maybe you can use this method
			// IntStream.Of(floors).foreach(f->l.add(floors));
		}
	}

	/**
	 * when calling up it means the passenger intends to travel to a higher floor.
	 * 
	 * @param floor  - passengers current floor when calling for an {@link Elevator}
	 *          
	 * @return an {@link ElevatorSystem} that has reach the requested floor
	 */
	@Override
	public Elevator callUp(int floor) {
		return call(floor, MovingState.Up);

	}

	/**
	 * when calling down it means the passenger intends to travel to a lower floor.
	 * 
	 * @param floor - passengers current floor when calling for an {@link Elevator}
	 *           
	 * @return an {@link ElevatorSystem} that has reach the requested floor
	 */
	@Override
	public Elevator callDown(int floor) {
		return call(floor, MovingState.Down);
	}

	/**
	 * 
	 * @param floor -target floor
	 *           
	 * @param direction -the elevatory will go up or go dowm
	 *           
	 * @return elevator which will comes
	 */
	private Elevator call(int floor, MovingState direction) {
		checkForElevator();
		floorCheck(floor);
		Elevator e = getAvailableElevatorIndex(floor);
		e.moveTo(floor);
		return e;
	}

	/**
	 * this method will index all elevator current floor and return the closet
	 * elevator to the user stayed
	 * 
	 * @param floor -current floor
	 *           
	 * @return -closestElevator
	 */
	private synchronized Elevator getAvailableElevatorIndex(int floor) {
		// check for each elevator
		// if elevator isIdle, e.isIdle();
		// if list of stops for elevator isEmpty() ,stops.get(e).isEmpty();
		// loop on all elvetaors
		int closest = Integer.MAX_VALUE;
		Elevator closestElevator = null;
		for (Elevator e : stops.keySet()) {

			// check for each elevator
			if (e.isIdle() && stops.get(e).isEmpty() && (Math.abs(e.getFloor() - floor) + 1)<closest) {
				closestElevator = e;
				closest = Math.abs(e.getFloor() - floor) + 1;
			}
		}
		return closestElevator;
	}

	/**
	 * check for floors whcih is coming
	 * 
	 * @param floor -  current floor
	 *          
	 */
	private void floorCheck(int floor) {
		if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
			throw new IllegalArgumentException("The floor is not valid, please re-enter");
		}
	}

	/**
	 * check for which elevaor will come use the elevator ID
	 */
	private void checkForElevator() {
		if (stops.isEmpty()) {
			throw new IllegalStateException("The stop floor is not valid, please re-enter");
		}
	}

	/**
	 * return total power consumed by all {@link Elevator} in the
	 * {@link ElevatorSystem}
	 * 
	 * @return total power consumed
	 */
	@Override
	public double getPowerConsumed() {
		// loop throu all elevators and sum up the power used
		int sum = 0;
		for (Elevator e : stops.keySet()) {
			sum += e.getPowerConsumed();

		}

		return sum;
	}

	/**
	 * return current floor of {@link Elevator} in {@link ElevatorSystem} when only
	 * have one elevator but should not be used anymore since there are more than
	 * one elevator, because different elevator will have the different the current
	 * floor
	 */
	@Override
	public int getCurrentFloor() {
		throw new UnsupportedOperationException();
	}

	/**
	 * get maximum floor for this {@link ElevatorSystem}
	 * 
	 * @return maximum floor for this {@link ElevatorSystem}
	 */
	@Override
	public int getMaxFloor() {

		return MAX_FLOOR;
	}

	/**
	 * get minimum floor for this {@link ElevatorSystem}
	 * 
	 * @return minimum floor for this {@link ElevatorSystem}
	 */
	@Override
	public int getMinFloor() {

		return MIN_FLOOR;
	}

	/**
	 * get total floors to which {@link ElevatorSystem} can send an
	 * {@link Elevator}. behavior and definition of this method will likely change
	 * when more elevators are introduced.
	 * 
	 * @return total floors
	 */
	@Override
	public int getFloorCount() {
		return Math.abs(MIN_FLOOR - MAX_FLOOR) + 1;
	}

	/**
	 * add an {@link Elevator} to {@link ElevatorSystem}, if implemented multiple
	 * {@link Elevator} can be added
	 * 
	 * @param elevator- {@link Elevator} object to be added to {@link ElevatorSystem}
	 *            
	 */
	@Override
	public void addElevator(Elevator elevator) {
		if (elevator == null) {
			throw new NullPointerException("The number is not valid, please re-enter");

		}
		stops.put(elevator, new LinkedList<>());
	}

	/**
	 * add an {@link Observer} to be attached to all {@link Elevator} objects
	 * 
	 * @param observer
	 *            - to be added to all {@link Elevator}, cannot be null
	 */
	@Override
	public void addObserver(Observer observer) {
		if (observer == null) {
			throw new NullPointerException("The number is not valid, please re-enter");

		}

		// loop throu all elevators and add observer one at a time

		for (Elevator e : stops.keySet()) {
			e.addObserver(observer);
		}

	}

	/**
	 * total number of elevators regardless of their states
	 * 
	 * @return total number of elevators
	 */
	@Override
	public int getElevatorCount() {
		return stops.size();
	}

	/**
	 * shutdown {@link ExecutorService} which handles are threads
	 */
	public void shutdown() {
		alive.set(false);
		service.shutdown();
	}

}
