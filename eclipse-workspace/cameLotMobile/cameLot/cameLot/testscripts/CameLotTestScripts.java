package cameLot.testscripts;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.cameLot.base.Base;
import com.cameLot.extentreport.ExtentTestManager;
import com.cameLot.utils.CommonFunctions;

import cameLot.pageobjects.HomePage;

public class CameLotTestScripts extends Base {
	WebDriver driver;
	TestSoftAssert softAssert;
	HomePage homepage;
	ExtentTest test;
	CommonFunctions commFunc;

	@Parameters("env")
	@BeforeMethod
	public void beforeMethod(String env, Method method) {
		driver = invokeBrowser(getURL(env));
		ExtentTestManager.createTest(method.getName(), method.getAnnotation(Test.class).testName());
		ExtentTestManager.log("Test started on " + env);
		homepage = new HomePage(driver);
		softAssert = new TestSoftAssert();
		commFunc = new CommonFunctions(driver);
	}

	@Test(testName = "test1")
	public void test() {
		softAssert.softAssertEquals(5, 4, "Expected Not matched");
		homepage.clickContactUs();
		softAssert.softAssertTrue(commFunc.getTitle().equals("Camelot Global"), "Validation : Expected not matched");
		ExtentTestManager.getTest().log(Status.INFO, "Hi I am sid.");
		softAssert.assertAll();
	}
}
