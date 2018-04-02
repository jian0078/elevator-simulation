package test;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import elevator.MovingState;
import elevator.QuickSortingFloors;


/**
 * JUnit tests for the Elevator class from the Assignment2.
 * 
 * @author Yi Jiang
 * @version 1.0.0 Mar. 16, 2018
 */

public class TestQuickSortFloor {
	QuickSortingFloors s = new QuickSortingFloors();
	private static final MovingState Up = MovingState.Up;
	private static final MovingState Down = MovingState.Down;;

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
	public void teardown() throws Exception {
		System.out.println("TestElevator End");
	}

	@Test
	public void testSort1() {
		int[] a = { 17 };
		int[] expected = { 17 };
		QuickSortingFloors.sort(a, 1, Up);
		System.out.println(Arrays.toString(a));
		Assert.assertArrayEquals(expected, a);
	}

	@Test
	public void testSort2() {
		int[] a = { 5, 17 };
		int[] expected = { 17, 5 };
		QuickSortingFloors.sort(a, 2, Down);
		System.out.println(Arrays.toString(a));
		Assert.assertArrayEquals(expected, a);
	}
	
	@Test
	public void testSort3() {
		int[] a = { 64, 18, 74, 89, 58, 17, 48, 44, 92, 88, 78, 80, 75, 25, 77, 18, 39, 95, 11, 2 };
		int[] expected = { 2, 11, 17, 18, 18, 25, 39, 44, 48, 58, 64, 74, 75, 77, 78, 80, 88, 89, 92, 95 };
		QuickSortingFloors.sort(a, 20, Up);
		System.out.println(Arrays.toString(a));
		Assert.assertArrayEquals(expected, a);
	}
	
	
	@Test
	public void testSort4() {
		int[] a = { 5, 17, 6 , 10, 15, 19, 4, 9, 2, 20 };
		int[] expected = { 20, 19, 17, 15, 10, 9, 6, 5, 4, 2 };
		QuickSortingFloors.sort(a, 10, Down);
		System.out.println(Arrays.toString(a));
		Assert.assertArrayEquals(expected, a);
	}

}
