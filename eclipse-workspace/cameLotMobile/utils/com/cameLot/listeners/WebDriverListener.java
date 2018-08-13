package com.cameLot.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.cameLot.base.LocalDriverFactory;
import com.cameLot.base.LocalDriverManager;
import com.cameLot.extentreport.ExtentManager;
import com.cameLot.extentreport.ExtentTestManager;

public class WebDriverListener implements IInvokedMethodListener, ITestListener {

	/**
	 * Prop object instance
	 */
	protected Properties prop;

	/**
	 * Invoked after the test class is instantiated and before any configuration
	 * method is called 1. Method call to set the suite name before execution
	 * starts and set the brand name for extent report name 2. Loading the
	 * properties file at once, that can be used anywhere on classes.
	 */
	@Override
	public void onStart(ITestContext context) {

		ExtentManager.getSuiteName(context);
		ExtentManager.getEnvName(context);
	}

	/**
	 * This method would be invoked before any beforeXX/AfterXX methods
	 * Condition: If method is beforeXX/AfterXX then create an instance of the
	 * driver and set the driver
	 * 
	 * @return
	 */
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult iTestResult) {
		try {
			if (iTestResult.getMethod().isBeforeClassConfiguration()
					|| iTestResult.getMethod().isBeforeMethodConfiguration()) {
				String browserName = method.getTestMethod().getTestClass().getXmlTest().getLocalParameters()
						.get("browserName");
				WebDriver driver = LocalDriverFactory.createInstance(browserName);
				LocalDriverManager.setWebDriver(driver);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method would be invoked after any beforeXX/AfterXX method Condition:
	 * If invoked method is test method then log the results in extent report
	 * and close the driver
	 */
	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult iTestresult) {
		try {
			if (iTestresult.getMethod().isTest()) {
				getresult(iTestresult);
				WebDriver driver = LocalDriverManager.getDriver();
				if (driver != null) {
					driver.close();
				}
			}
		} catch (Exception e) {
			try {
				throw new Exception(e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Invoked after all the tests have run and all their Configuration methods
	 * have been called.
	 */
	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.getInstance().flush();

		zipExtentReport();
	}

	/**
	 * This method will capture the screen and store the image in given path
	 * 
	 * @param imageName
	 * @param driver
	 * @return actualImagePath
	 * @throws Exception
	 */
	public String getScreenShot(String imageName, WebDriver driver) throws Exception {
		try {
			if (imageName.equals("")) {
				imageName = "blank";
			}
			File image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String imagelocation = System.getProperty("user.dir") + "\\extentreports\\screenshots\\";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			String actualImagePath = imagelocation + imageName + "_" + formater.format(calendar.getTime()) + ".png";
			File destFile = new File(actualImagePath);
			FileUtils.copyFile(image, destFile);
			return actualImagePath;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * Method to log test status for pass, fail, skipped, and info
	 * 
	 * @param iTestResult
	 * @throws Exception
	 */
	public void getresult(ITestResult iTestResult) throws Exception {
		try {
			if (iTestResult.getStatus() == ITestResult.SUCCESS) {
			} else if (iTestResult.getStatus() == ITestResult.SKIP) {
				ExtentTestManager.getTest().log(Status.SKIP, iTestResult.getMethod().getMethodName()
						+ " test is skipped and skip reason is:-" + iTestResult.getThrowable());
			} else if (iTestResult.getStatus() == ITestResult.FAILURE) {
				ExtentTestManager.getTest().log(Status.ERROR,
						iTestResult.getName() + " | Exception: " + iTestResult.getThrowable());
				ExtentTestManager.getTest().addScreenCaptureFromPath(
						getScreenShot(iTestResult.getMethod().getMethodName(), LocalDriverManager.getDriver()));
			} else if (iTestResult.getStatus() == ITestResult.STARTED) {
				ExtentTestManager.getTest().log(Status.INFO, iTestResult.getName() + " test is started");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * Invoked each time before a test will be invoked.
	 */
	@Override
	public void onTestStart(ITestResult iTestResult) {
	}

	/**
	 * Invoked each time a test succeeds.
	 */
	@Override
	public void onTestSuccess(ITestResult iTestResult) {
	}

	/**
	 * Invoked each time a test fails.
	 */
	@Override
	public void onTestFailure(ITestResult iTestResult) {
	}

	/**
	 * Invoked each time a test is skipped
	 */
	@Override
	public void onTestSkipped(ITestResult iTestResult) {
	}

	/**
	 * Invoked each time a method fails but has been annotated with
	 * successPercentage and this failure still keeps it within the success
	 * percentage requested.
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
	}

	public static void zipExtentReport() {
		try {

			String filePath = System.getProperty("user.dir") + "\\extentreports\\";
			File file = new File(filePath + ExtentManager.getTestSuiteName() + "ExecutionReport.html");

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			formater.format(calendar.getTime());
			String zipFileName = filePath + "archives\\" + ExtentManager.getTestSuiteName() + "AutoExecutionReport_"
					+ formater.format(calendar.getTime()) + ".zip";

			addToZipFile(file, zipFileName);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void addToZipFile(File file, String zipFileName) throws FileNotFoundException, IOException {

		FileOutputStream fos = new FileOutputStream(zipFileName);
		ZipOutputStream zos = new ZipOutputStream(fos);

		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);

		FileInputStream fis = new FileInputStream(file);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) > 0) {
			zos.write(bytes, 0, length);
		}
		zos.closeEntry();
		zos.close();
		fis.close();
		fos.close();
		System.out.println(file.getCanonicalPath() + " is zipped to " + zipFileName);
	}

}