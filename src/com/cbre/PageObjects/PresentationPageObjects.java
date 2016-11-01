/**
 * 
 */
package com.cbre.PageObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.cbre.Utilities.DriverUtils;
import com.cbre.Utilities.Utils;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @author Ramesh
 *
 */
public class PresentationPageObjects {
	WebDriver driver;
	ExtentTest logger;
	DriverUtils driverUtils;
	Utils utils;

	public PresentationPageObjects(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
		driverUtils = new DriverUtils(driver, logger);
		utils = new Utils(logger);
		driverUtils.waitForPage();
		Matcher matcher = Pattern.compile(".*com\\/.*\\/Pages\\/default.aspx").matcher(driver.getCurrentUrl());
		if (!matcher.find()) {
			logger.log(LogStatus.FAIL,
					"Not in Presentation Page" + logger.addScreenCapture(driverUtils.takeScreenShot("PresentationPage")));
			throw new IllegalStateException("Not on Presentation page");
		}
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//input[contains(@value,'Create New Presentation')]")
	private WebElement createNewPresentationButton;
	
	
	
	public void clickCreatePresentation(){
		createNewPresentationButton.click();
	}
	
}
