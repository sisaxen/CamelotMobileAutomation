package com.cameLot.extentreport;

import java.io.File;

import org.openqa.selenium.Platform;
import org.testng.ITestContext;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * This class contains all the methods related to extent report configuration
 * @author Ssaxena2
 *
 */
public class ExtentManager {

	static ExtentReports extent;
	private  static String testContext = new String();
	private static String exeEnv = new String();
	
	/**
	 * Method to return extent report instance
	 * @return extent
	 */
	public static ExtentReports getInstance() {
		return extent;
	}
	
	/**
	 * Method to return test context as a suite name
	 * This suite name will be displayed on extent report header per brand(Catherines, dressbarn, maurices, justice, lanebryant etc.)
	 * @return testContext
	 */
	public synchronized static String getTestSuiteName() {
		return testContext;
	}
	
	public synchronized static String getTestEnvName() {
		return exeEnv.toUpperCase();
	}
	
	
	
	/**
	 * Method to get suite name  from test context and set to testContext
	 * @param ctx
	 */
	public synchronized static void getSuiteName(ITestContext ctx){
		testContext = ctx.getCurrentXmlTest().getSuite().getName();
	}
	
	public synchronized static void getEnvName(ITestContext ctx){
		exeEnv = ctx.getCurrentXmlTest().getLocalParameters().get("env");
	}
	
	/**
	 * ----------------------------Extent Report configuration---------------------------------
	 * This method will create an instance of extent report and set few properties for extent report
	 * @param fileName
	 * @return extent
	 */
	public static synchronized ExtentReports createInstance(String extentHtmlFileName) {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(extentHtmlFileName);
		htmlReporter.loadXMLConfig(new File(System.getProperty("user.dir") + "\\config\\xml\\extenthtmlreporter.xml"));
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		
		extent.setSystemInfo("OS", Platform.getCurrent().toString());
		extent.setSystemInfo("Host Name", "Harman");
		//extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("Environment", ExtentManager.getTestEnvName().toUpperCase());
		extent.setSystemInfo("User Name", System.getProperty("user.name"));

		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle(getTestSuiteName() + " Automation Status Report");
		htmlReporter.config().setReportName(getTestSuiteName() + " Automation Status Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);
		
		return extent;
	}

}
