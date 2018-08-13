package com.cameLot.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
	public int retryCount = 0;
	private int maxRetryCount = 0; //Run the failed test 2 times

	/**
	 * Below method returns 'true' if the test method has to be retried else 'false'
	 * and it takes the 'Result' as parameter of the test method that just ran
	 */
	public boolean retry(ITestResult iTestResult) {
		//if (!iTestResult.isSuccess()) { // Check if test not succeed
			if (retryCount < maxRetryCount) {
				System.out.println("Retrying test " + iTestResult.getName() + " with status "
						+ getResultStatusName(iTestResult.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
				retryCount++;
				return true;
			}
		//} else {
			//iTestResult.setStatus(ITestResult.SUCCESS); // If test passes, TestNG marks it as passed
		//}
		return false;
	}
	
	public String getResultStatusName(int status) {
		String resultName = null;
		if (status == 1)
			resultName = "SUCCESS";
		if (status == 2)
			resultName = "FAILURE";
		if (status == 3)
			resultName = "SKIP";
		return resultName;
	}
}