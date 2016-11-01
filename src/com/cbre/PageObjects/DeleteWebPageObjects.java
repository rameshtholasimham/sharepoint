/**
 * 
 */
package com.cbre.PageObjects;

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
public class DeleteWebPageObjects {
	WebDriver driver;
	ExtentTest logger;
	DriverUtils driverUtils;
	Utils utils;

	public DeleteWebPageObjects(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
		driverUtils = new DriverUtils(driver, logger);
		utils = new Utils(logger);
		driverUtils.waitForPage();
		if (!driver.getCurrentUrl().contains("webdeleted.aspx")) {
			logger.log(LogStatus.FAIL,
					"Not in Delete Web Page" + logger.addScreenCapture(driverUtils.takeScreenShot("DeleteWebPage")));
			throw new IllegalStateException("Not in the Delete Web page");
		}
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//div[@id='ms-error-gobackcont']/a")
	private WebElement goBackToSiteButton;
	
	public void goBackToSite(){
		logger.log(LogStatus.INFO, "Click on Go Back To Site Button.");
		goBackToSiteButton.click();
	}
	
}
