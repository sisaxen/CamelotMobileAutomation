package com.cameLot.base;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cameLot.extentreport.ExtentManager;
import com.cameLot.extentreport.ExtentTestManager;
import com.cameLot.listeners.WebDriverListener;

public class LocalDriverFactory {
	
	static WebDriverListener utilWebDriver;

	public static WebDriver createInstance(String browserName) throws Exception {
		RemoteWebDriver driver = null;
		DesiredCapabilities capabilities = new DesiredCapabilities();

		try {
			/*************** Extent Report *****************/

			if (ExtentManager.getInstance() == null)
				ExtentManager.createInstance(System.getProperty("user.dir") + "\\extentreports\\"
						+ ExtentManager.getTestSuiteName() + "ExecutionReport" + ".html");

			/********************** Driver Initialization *******************/

			System.out.printf("Opening %s browser.\n", browserName);

			switch (browserName.toLowerCase()) {
			
			case "androidemulator": {
				capabilities.setCapability("device", "Android Emulator");
				capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
				capabilities.setCapability(CapabilityType.VERSION, "8.1.0");
				capabilities.setCapability(CapabilityType.PLATFORM, "Android");
				capabilities.setCapability("platformName", "Android");
				capabilities.setCapability("deviceName", "Nexus_5X_API_27:5554");
				driver = new RemoteWebDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
				break;
			}

			case "android": {
				capabilities.setCapability("device", "Android");
				capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
				capabilities.setCapability(CapabilityType.VERSION, "4.2.1");
				capabilities.setCapability(CapabilityType.PLATFORM, "Android");
				capabilities.setCapability("platformName", "Android");
				capabilities.setCapability("deviceName", "d1aaa82c9804");
				capabilities.setCapability("automationName", "Appium");
				driver = new RemoteWebDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
				break;
			}
			
			case "chrome": { 
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-infobars");
				driver = new ChromeDriver(options);
				break;
			}
				
			default: {
				ExtentTestManager.log(browserName + " Incorrect/No browsers defined");
				break;
			}
			}

			if (browserName.equalsIgnoreCase("firefox") || browserName.equalsIgnoreCase("chrome")
					|| browserName.equalsIgnoreCase("ie")) {
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				driver.manage().window().maximize();
			} else {
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
			}

		} catch (Exception e) {
			throw e;
		}
		return driver;
	}

}