/**
 * 
 */
package com.cbre.PageObjects;

import org.openqa.selenium.Alert;
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
public class DeleteThisSitePageObjects {
	WebDriver driver;
	ExtentTest logger;
	DriverUtils driverUtils;
	Utils utils;

	public DeleteThisSitePageObjects(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
		driverUtils = new DriverUtils(driver, logger);
		utils = new Utils(logger);
		driverUtils.waitForPage();
		if (!driver.getCurrentUrl().contains("deleteweb.aspx")) {
			logger.log(LogStatus.FAIL,
					"Not in Delete This Site Page" + logger.addScreenCapture(driverUtils.takeScreenShot("DeleteThisSitePage")));
			throw new IllegalStateException("Not in the Delete This Site page");
		}
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//input[@value='Delete']")
	private WebElement deleteButton;
	
	@FindBy(xpath="//input[@value='Cancel']")
	private WebElement cancelButton;
	
	public void DeletePresentation(){
		logger.log(LogStatus.INFO, "Click on Delete Button to Remove the Presentation");
		deleteButton.click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		driverUtils.waitForPage();
	}
	
}
