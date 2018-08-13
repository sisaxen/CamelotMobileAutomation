package com.cameLot.extentreport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * This class contains methods to create/get instance of extent report
 * @author Ssaxena2
 *
 */
public class ExtentTestManager {

	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	private static ExtentReports extent = ExtentManager.getInstance();
	
	/**
	 * Method to get extent test instance and return
	 * @return extent
	 */
	public synchronized static ExtentTest getTest() {
		return (ExtentTest) extentTest.get();
	}
	
	/**
	 * Method to create extent test instance
	 * @param name
	 * @param description
	 * @return extent
	 */
	public synchronized static ExtentTest createTest(String name, String description) {
		ExtentTest test = extent.createTest(name, description);
		extentTest.set(test);
		return getTest();
	}
	
	/**
	 * Method to log info message on reporter
	 * @param message
	 */
	public synchronized static void log(String message) {
		getTest().info(message);
	}

}
