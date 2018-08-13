package cameLot.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.cameLot.base.Base;
import com.cameLot.extentreport.ExtentTestManager;
import com.cameLot.utils.CommonFunctions;

public class HomePage extends Base {

	WebDriver driver;
	ExtentTest test;
	TestSoftAssert softAssert;
	CommonFunctions commFunc;

	public HomePage(WebDriver driver) {
		this.test = ExtentTestManager.getTest();
		this.driver = driver;
		PageFactory.initElements(driver, this);
		commFunc = new CommonFunctions(driver);
	}

	@FindBy(css = "[class*='navbar-right'] a[data-id='contact']")
	public WebElement contactUs;

	public HomePage clickContactUs() {
		try {
			commFunc.clickWhenReady(contactUs, 4);
			test.log(Status.PASS, "Click on Contact Us Link Passed.");
		} catch (Exception e) {
			test.log(Status.FAIL, "Click on Contact Us Link Failed.");
			throw e;
		}
		return this;
	}

}
