package elevator;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import elevatorsystem.ElevatorPanel;

/**
 * Assignment 2 Reference from Professor Shahriar (Shawn) Emami
 * 
 * @author Yi Jiang Class ElevatorImp
 * @version March.11, 2018
 */
public class ElevatorImp extends Observable implements Elevator {
	private static final int POWER_START_STOP = 2;
	private static final int POWER_CONTINUOUS = 1;
	private static final long SLEEP_START_STOP = 500;
	private static final long SLEEP_CONTINUOUS = 250;
	private final int MIN_FLOOR = 0;
	private final int MAX_FLOOR = 21;
	private final double MAX_CAPACITY_PERSONS;
	private final boolean delay;
	private final int ID;
	private double powerUsed;
	private int currentFloor = 0;
	private int capacity;
	private final ElevatorPanel panel;
	private volatile MovingState state;
	private int step = 0;

	/**
	 * Constructor with 4 parameter
	 * 
	 * @param CAPACITY_PERSON-
	 *            maximun person can fit in elevator
	 * @param panel
	 *            - the object of elevatorPanel
	 * @param ID
	 *            -the elevator number
	 * @param delay
	 *            -boolean elevator thread run postpone,
	 * 
	 */
	public ElevatorImp(double CAPACITY_PERSON, ElevatorPanel panel, int ID, boolean delay) {
		this.MAX_CAPACITY_PERSONS = CAPACITY_PERSON;
		this.panel = panel;
		this.ID = ID;
		this.delay = delay;
		this.state = MovingState.Idle;
	}

	/**
	 * Constructor overloaded with 3 parameter
	 * 
	 * @param CAPACITY_PERSON
	 *            - maximun person can fit in elevator
	 * @param panel
	 *            - the object of elevatorPanel
	 * @param ID
	 *            -the elevator number
	 */
	public ElevatorImp(double CAPACITY_PERSON, ElevatorPanel panel, int ID) {
		this(CAPACITY_PERSON, panel, ID, true);
	}

	/**
	 * move {@link Elevator} to a specific floor
	 * 
	 * @param floor
	 *            - target floor
	 * @throws InterruptedException
	 */
	@Override
	public void moveTo(int floor) {
		if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
			throw new IllegalArgumentException("The floor is not valid, please re-enter");
		}

		while (floor != currentFloor) {
			switch (getState()) {
			case Idle:
				step = floor - currentFloor < 0 ? -1 : 1;
				state = step == 1 ? MovingState.SlowUp : MovingState.SlowDown;
				break;
			case SlowUp:
				currentFloor += 1;
				powerUsed += 2;
				step = floor - currentFloor;
				if (step == 1)
					state = MovingState.SlowUp;
				else if (step > 1)
					state = MovingState.Up;
				else
					state = MovingState.Idle;
				break;
			case SlowDown:
				currentFloor -= 1;
				powerUsed += 2;
				step = currentFloor - floor;
				if (step == 1)
					state = MovingState.SlowDown;
				else if (step > 1)
					state = MovingState.Down;
				else
					state = MovingState.Idle;
				break;
			case Up:
				currentFloor += 1;
				powerUsed += 1;
				step = floor - currentFloor;
				state = step > 1 ? MovingState.Up : MovingState.SlowUp;
				break;
			case Down:
				currentFloor -= 1;
				powerUsed += 1;
				step = currentFloor - floor;
				state = step > 1 ? MovingState.Down : MovingState.SlowDown;
				break;
			case Off:
				break;
			case Stops:
				break;
			default:
				break;
			}

			setChanged();
			notifyObservers(Arrays.asList(ID, currentFloor,floor, powerUsed));
			try {
				Thread.sleep(SLEEP_CONTINUOUS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * method process moving state will move to target floor
	 * 
	 * @param floor
	 *            -target floor
	 */
	private void processMovingState(int floor) {
		if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
			throw new IllegalArgumentException("The floor is not valid, please re-enter");
		}
	}

	/**
	 * get current floor of {@link Elevator} at this point
	 * 
	 * @return current floor
	 */
	@Override
	public int getFloor() {

		return currentFloor;
	}

	/**
	 * Unique integer that identifies this {@link Elevator} object
	 * 
	 * @return unique identifier integer
	 */
	@Override
	public int id() {

		return ID;
	}

	/**
	 * check if elevator is in {@link MovingState#Idle}
	 * 
	 * @return true if current elevator state is {@link MovingState#Idle}
	 */
	@Override
	public boolean isFull() {

		return MAX_CAPACITY_PERSONS - capacity <= 0 ? true : false;

	}

	/**
	 * check if capacity is zero
	 * 
	 * @return if {@link Elevator} is empty
	 */
	@Override
	public boolean isEmpty() {

		return capacity == 0 ? true : false;
	}

	/**
	 * return total amount of power consumed to this point
	 * 
	 * @return power consumed
	 */
	@Override
	public double getPowerConsumed() {
		int gap = Math.abs(getFloor() - currentFloor);
		if (gap < 3) {
			powerUsed = POWER_START_STOP * gap;
		} else if (gap > 3) {
			powerUsed = POWER_START_STOP * 2 + POWER_CONTINUOUS * (gap - 2);
		} else
			powerUsed = 0;
		return powerUsed;
	}

	/**
	 * get current capacity of the elevator not the maximum capacity.
	 * 
	 * @return integer for total capacity currently in the {@link Elevator}
	 */
	@Override
	public int getCapacity() {

		return capacity;
	}

	/**
	 * add number of persons to {@link Elevator}
	 * 
	 * @param persons
	 *            - number of passengers getting on at current floor
	 */
	@Override
	public void addPersons(int persons) {
		if (persons < 0 || persons > MAX_CAPACITY_PERSONS) {
			throw new IllegalArgumentException("The persons is not valid, please re-enter");
		}

		capacity += persons;
	}

	/**
	 * represent the request made by one passenger inside of an {@link Elevator}
	 * object
	 * 
	 * @param floor
	 *            - target floor
	 */
	@Override
	public void requestStop(int floor) {
		if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
			throw new IllegalArgumentException("The floor is not valid, please re-enter");
		}
		panel.requestStop(this, floor);

	}

	/**
	 * represent the request made by multiple passenger inside of an
	 * {@link Elevator} object
	 * 
	 * @param floors
	 *            - target floors
	 */
	@Override
	public void requestStops(int... floors) {
		if (floors == null) {
			throw new NullPointerException("The floors is not valid, please re-enter");
		}
		panel.requestStops(this, floors);
	}

	/**
	 * check if elevator is in {@link MovingState#Idle}
	 * 
	 * @return true if current elevator state is {@link MovingState#Idle}
	 */
	@Override
	public boolean isIdle() {

		return state.isIdle();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElevatorImp other = (ElevatorImp) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	/**
	 * get current {@link MovingState} of the {@link Elevator}
	 * 
	 * @return current {@link MovingState}
	 */
	@Override
	public MovingState getState() {
		return state;
	}

	/**
	 * add an {@link Observer} to this {@link Elevator}
	 * 
	 * @param observer
	 *            - add to this {@link Elevator}, cannot be null
	 */
	@Override
	public void addObserver(Observer observer) {
		if (observer == null) {
			throw new NullPointerException("The observer is not valid, please re-enter");
		}
		super.addObserver(observer);
	}
}