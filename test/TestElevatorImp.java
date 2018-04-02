package test;

import static org.junit.Assert.*;


import java.util.Observer;

import org.junit.After;
import org.junit.AfterClass;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import elevator.Elevator;
import elevator.ElevatorImp;
import elevator.MovingState;

import elevatorsystem.ElevatorPanel;
import elevatorsystem.ElevatorSystem;
import elevatorsystem.ElevatorSystemImp;

/**
 * JUnit tests for the Elevator class from the Assignment1B.
 * @author Yi Jiang
 * @version 1.0.0  Feb. 14, 2018
 */

public class TestElevatorImp {
	
	ElevatorSystem system = new ElevatorSystemImp( 0, 20);
	ElevatorImp e = new ElevatorImp( 10, (ElevatorPanel) system, 0);

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("@BeforeClass - Runs one time, before any tests in this class.");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("@AfterClass - Runs one time, after all tests in this class.");
	}

	

	@Before
	public void setup() throws Exception {
		System.out.println("TestElevator Begin");
		
	}

	@After
	public void teardown() throws Exception{
		System.out.println("TestElevator End");
	}

	
	/**
	 * move {@link Elevator} to a specific floor
	 * @param floor - target floor
	 */
	@Test
	public void testMoveTo() {
		e.moveTo(2);
		System.out.println("\tExecuting testMoveTo");

		assertEquals(2, e.getFloor());
		
	}
	
	/**
	 * get current floor of {@link Elevator} at this point
	 * @return current floor
	 */
	
	@Test
	public void testGetFloor() {
		
		e.getFloor();
		System.out.println("\tExecuting testGetFloor");
	
		assertNotNull(e.getFloor());
	
	}

	
	/**
	 * Unique integer that identifies this {@link Elevator} object
	 * 
	 * @return unique identifier integer
	 */
	@Test
	public void  TestId() {
	     e.id();
	System.out.println("\tExecuting TestId");
		assertNotNull(e.id());
	
	}
	

	/**
	 * Test check if capacity has reached its maximum
	 * @return if {@link Elevator} is full
	 */
	@Test
	public void testIsFull() {
		
		System.out.println("\tExecuting testIsFull");
		assertEquals(false, e.isFull());
			
	}
	

	/**
	 * Test check if capacity is zero
	 * @return if {@link Elevator} is empty
	 */
	
	@Test
	public void testIsEmpty() {
		
		System.out.println("\tExecuting testIsEmpty");
		assertEquals(true, e.isEmpty());
		
	}
	
	/**
	 * Test return total amount of power consumed to this point
	 * @return power consumed
	 */
	@Test
	public void testGetPowerConsumed() {
		system.addElevator(e);
		System.out.println("\tExecuting testGetPowerConsumed");
		assertEquals(0.0, system.getPowerConsumed(),0.001);
	}
	
	
	/**
	 * Test get current capacity of the elevator not the maximum capacity.
	 * @return integer for total capacity currently in the {@link Elevator}
	 */
	@Test
	public void testGetCapacity() {
		e.getCapacity();
		assertNotNull(e.getCapacity());
		System.out.println("\tExecuting testGetCapacity");
    }
	
	/**
	 * add number of persons to {@link Elevator}
	 * @param persons - number of passengers getting on at current floor
	 */
	@Test (expected=IllegalArgumentException.class)
	public void testAddPersons() {
		e.addPersons(5);
		assertNotNull(e.getCapacity());
		assertEquals(5, e.getCapacity());
		e.addPersons(-4);
		System.out.println("\tExecuting testAddPersons");
		
		}
	

	/**
	 * represent the request made by one passenger inside of {@link Elevator}
	 * @param floor - target floor
	 * @throws InterruptedException 
	 */
	
	@Test
	public void testRequestStop() throws InterruptedException {
		system.start();
		system.addElevator(e);
	    system.callDown(12).requestStop(12);
		Thread.sleep(2000);
		assertEquals(12, e.getFloor());
		System.out.println("\tExecuting testRequestStop");
	}

	
	

	/**
	 * check if elevator is in {@link MovingState#Idle}
	 * 
	 * @return true if current elevator state is {@link MovingState#Idle}
	 */
	@Test
	public void TestIsIdle() {
		System.out.println("\tExecuting TestIsIdle");
		e.isIdle();
     assertTrue(e.isIdle());
		
	}
	
	
	/**
	 * boolean equal method, 
	 * @return true or false
	 */
	@Test
	public void TestEquals() {
		System.out.println("\tExecuting TestEquals");
     assertTrue(e.equals(e));
		
	}
	
	/**
	 * Test get current {@link MovingState} of the {@link Elevator}
	 * @return current {@link MovingState}
	 */
	
	@Test
	public void testGetState() {

		e.getState();
		System.out.println("\tExecuting testGetState");
		assertNotNull(e.getState());
		
	}
	
	
	/**
	 * add an {@link Observer} to be attached to all {@link Elevator} objects
	 * 
	 * @param observer
	 *            - to be added to all {@link Elevator}, cannot be null
	 */
	@Test (expected=NullPointerException.class	)
	public void addObserver() {
		Observer observer = null;
		e.addObserver(observer);
		System.out.println("\tExecuting addObserver");
	}
	
}
	
	

	
	



