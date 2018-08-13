package com.cameLot.base;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.cameLot.extentreport.ExtentTestManager;

public class Base {

	public static WebDriver driver;
	public static Properties prop = null;

	public synchronized WebDriver invokeBrowser(String url) {

		System.out.println("Thread id = " + Thread.currentThread().getId());
		System.out.println("Hashcode of webDriver instance = " + LocalDriverManager.getDriver().hashCode());
		driver = LocalDriverManager.getDriver();
		driver.get(url);
		return driver;
	}

	@BeforeTest(alwaysRun = true)
	public synchronized void setFiles() {
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "\\config\\properties\\cameLot.properties");
			prop = new Properties();
			prop.load(fis);
			System.out.println("Property File Set : cameLot.properties");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public synchronized String getURL(String env) {
		return prop.getProperty(env.toLowerCase());
	}

	public static class TestSoftAssert extends SoftAssert {

		public List<String> messages = new ArrayList<>();

		@Override
		public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
			messages.add("onAssertFailure");
		}

		@Override
		public void assertAll() {
			try {
				messages.add("assertAll");
				super.assertAll();
			} catch (AssertionError e) {
				throw (e);
			}
		}

		public synchronized void softAssertEquals(int actual, int expected, String failureMessage) {

			this.assertEquals(actual, expected, failureMessage);

			if (this.messages.contains("onAssertFailure")) {
				ExtentTestManager.getTest().log(Status.FAIL, failureMessage);
			} else {
				ExtentTestManager.getTest().log(Status.PASS, failureMessage.replace("not", ""));
			}

			messages.clear();
		}

		public synchronized void softAssertTrue(Boolean condition, String failureMessage) {

			this.assertTrue(condition, failureMessage);

			if (this.messages.contains("onAssertFailure")) {
				ExtentTestManager.getTest().log(Status.FAIL, failureMessage);
			} else {
				ExtentTestManager.getTest().log(Status.PASS, failureMessage.replace("not", ""));
			}

			messages.clear();
		}

		public synchronized void softAssertFalse(Boolean condition, String failureMessage) {

			this.assertFalse(condition, failureMessage);

			if (this.messages.contains("onAssertFailure")) {
				ExtentTestManager.getTest().log(Status.FAIL, failureMessage);
			} else {
				ExtentTestManager.getTest().log(Status.PASS, failureMessage.replace("not", ""));
			}
			messages.clear();
		}



		public void WaitForLoaderToFinish(WebDriver driver) throws Exception {
			try {
				new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOf(
						driver.findElement(By.xpath("//div[@class='asc-global-indicator']/div[@class='loader']"))));
				Thread.sleep(2000);
				ExtentTestManager.getTest().log(Status.INFO, "Loader dissapeared.");
			} catch (Exception e) {
				ExtentTestManager.getTest().log(Status.INFO, "Loader not found.");
				throw new Exception(e);
			}
		}
	}
}
