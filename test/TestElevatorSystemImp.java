package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Observer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import elevator.Elevator;
import elevator.ElevatorImp;
import elevatorsystem.ElevatorPanel;
import elevatorsystem.ElevatorSystem;
import elevatorsystem.ElevatorSystemImp;

/**
 * JUnit tests for the Elevator class from the Assignment2.
 * @author Yi Jiang
 * @version 1.0.0  Mar. 16, 2018
 */

public class TestElevatorSystemImp {
	
	private ElevatorSystemImp system= new ElevatorSystemImp( 0, 20);
	private  ElevatorImp e = new ElevatorImp( 20, (ElevatorPanel) system, 4);
	
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
		system.addElevator(e);
		System.out.println("TestElevator Begin");
		
	}

	@After
	public void teardown() throws Exception{
		e = null;
		system=null;
		System.out.println("TestElevator End");
	}


	
	/**
	 * Test  when calling up it means the passenger intends to travel to a higher floor.
	 * @param floor - passengers current floor when calling for an {@link Elevator}
	 * @return an {@link ElevatorSystem} that has reach the requested floor
	 */
	@Test
	public void testCallUp() {
		
		system.addElevator(e);
		system.callUp(5);
		System.out.println("\tExecuting testCallUp");
		assertEquals(5, e.getFloor());
	}
	
	/**
	 * Test when calling down it means the passenger intends to travel to a lower floor.
	 * @param floor - passengers current floor when calling for an {@link Elevator}
	 * @return an {@link ElevatorSystem} that has reach the requested floor
	 */
	
	@Test
	public void testCallDown() {
	
		system.addElevator(e);
		system.callDown(2);
		System.out.println("\tExecuting testCallDown");
		System.out.println(e.getFloor());
		assertEquals(2, e.getFloor());
	
	}
	
	/**
	 * Test call this floor then check for elevator and floor
	 */
	@Test
	public void testCall() {
		system.addElevator(e);
		system.callDown(3);
		System.out.println("\tExecuting testCall");
		System.out.println(e.getFloor());
		assertEquals(3, e.getFloor());
	}
	
	/**
	 * Test check current floor
	 */
	@Test (expected=IllegalStateException.class)
	public void testFloorCheck() {
		ElevatorSystemImp sys= new ElevatorSystemImp( 0, 20);
		//sys.addElevator(new ElevatorImp(5, sys, 2));
		System.out.println("\tExecuting testFloorCheck");
		sys.checkForElevator();
		

	}
	
	
	/**
	 * Test return total power consumed by all {@link Elevator} in the {@link ElevatorSystem}
	 * @return total power consumed
	 */

	@Test
	public void testGetPowerConsumed() {
	
		system.addElevator(e);
		system.requestStop(e, 15);
		System.out.println("\tExecuting testGetPowerConsumed");

	    assertEquals(0.0, system.getPowerConsumed(),0.001);
	}
	
	/**
	 * Test return current floor of {@link Elevator} in {@link ElevatorSystem}.
	 *
	 * this method will likely change inn future designs.
	 * @return current floor of only {@link Elevator}
	 */
	
	@Test  (expected=UnsupportedOperationException.class)
	public void testGetCurrentFloor() {
		System.out.println("\tExecuting testGetCurrentFloor");
		system.getCurrentFloor();
		
		assertNotNull(system.getCurrentFloor());
		
	}
	
	/**
	 * Test get maximum floor for this {@link ElevatorSystem}
	 * @return maximum floor for this {@link ElevatorSystem}
	 */
	
	@Test
	public void testGetMaxFloor() {
		ElevatorSystemImp s= new ElevatorSystemImp( 0, 20);
	s.getMaxFloor();
		System.out.println("\tExecuting testGetMaxFloor");
		assertNotNull(s.getMaxFloor());
		
		assertEquals(20, s.getMaxFloor());
	}
	
	/**
	 * Test get minimum floor for this {@link ElevatorSystem}
	 * @return minimum floor for this {@link ElevatorSystem}
	 */
	
	@Test
	public void testGetMinFloor() {
		ElevatorSystemImp s= new ElevatorSystemImp( 0, 20);
		s.getMinFloor();
		System.out.println("\tExecuting testGetMinFloor");
		assertEquals(0, s.getMinFloor());
		assertNotNull(s.getMinFloor());
		
	
	}
	
	/**
	 * Test get total floors to which {@link ElevatorSystem} can send an {@link Elevator}.
	 * behavior and definition of this method will likely change when more elevators are introduced.
	 * @return total floors
	 */
	
	@Test
	public void testGetFloorCount() {
		ElevatorSystemImp s= new ElevatorSystemImp( 0, 20);
		System.out.println("\tExecuting testGetFloorCount");
		s.getFloorCount();
		assertNotNull(s.getFloorCount());
		assertEquals(21, s.getFloorCount());
	}
	
	/**
	 * Test add an {@link Elevator} to {@link ElevatorSystem}, if implemented multiple {@link Elevator} can be added
	 * @param elevator - {@link Elevator} object to be added to {@link ElevatorSystem}
	 */

	@Test
	public void testAddElevator() {
		system.addElevator(e);
		assertEquals(1, system.getElevatorCount());
		System.out.println("\tExecuting testAddElevator");
		
	}
	
	
	/**
	 * this method will request stop to the floor which is contral for the thread
	 * sleep time
	 * 
	 * @param elevator
	 * @param floors
	 * @throws InterruptedException 
	 */
	@Test
	public void testRequestStop() throws InterruptedException {
		system.start();
		system.addElevator(e);
		System.out.println("\tExecuting testRequestStop");
	    system.callDown(12).requestStop(12);
		Thread.sleep(2000);
		assertEquals(12, e.getFloor());
	}
	
	/**
	 * total number of elevators regardless of their states
	 * 
	 * @return total number of elevators
	 */
	@Test
	public void testGetElevatorCount() {
		system.addElevator(e);
		assertEquals(1, system.getElevatorCount());
		System.out.println("\tExecuting testGetElevatorCount");
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
