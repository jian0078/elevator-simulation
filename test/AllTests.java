package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * JUnit test class to execute all JUNIT tests for the "ShannonsTheorem" project
 * @author Yi Jiang
 * @version 1.0.0 Feb. 14, 2018
 */
@RunWith(Suite.class)
@SuiteClasses({ TestElevatorImp.class, TestElevatorSystemImp.class, TestQuickSortFloor.class })

public class AllTests {

}
