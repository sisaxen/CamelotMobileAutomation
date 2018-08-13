package com.cameLot.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.cameLot.extentreport.ExtentTestManager;

public class CommonFunctions {

	WebDriver driver;
	ExtentTest test;
	WebDriverWait wait = null;
	WebElement element;

	public CommonFunctions(WebDriver driver) {
		this.driver = driver;
		this.test = ExtentTestManager.getTest();
	}

	public synchronized void clickWhenReady(WebElement element, int timeout) {
		try {
			wait = new WebDriverWait(driver, timeout);
			wait.until(ExpectedConditions.visibilityOf(element));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();
		} catch (Exception e) {
			throw e;
		}
	}

	public synchronized String getTitle() {
		return driver.getTitle();
	}

}
