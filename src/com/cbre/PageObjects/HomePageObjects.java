/**
 * 
 */
package com.cbre.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.cbre.Utilities.DriverUtils;
import com.cbre.Utilities.Utils;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @author Ramesh
 *
 */
public class HomePageObjects {
	WebDriver driver;
	ExtentTest logger;
	DriverUtils driverUtils;
	Utils utils;

	public HomePageObjects(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
		driverUtils = new DriverUtils(driver, logger);
		utils = new Utils(logger);
		driverUtils.waitForPage();
		if (!driver.getCurrentUrl().contains("default.aspx")) {
			logger.log(LogStatus.FAIL,
					"Not in the Home Page" + logger.addScreenCapture(driverUtils.takeScreenShot("HomePage")));
			throw new IllegalStateException("Not in the Home page");
		}
		PageFactory.initElements(driver, this);
	}
	
	public void selectAreaFromHorizontalMenu(String areaName){
		try{
			driver.findElement(By.xpath("//ul[contains(@id,'zz13_RootAspMenu')]//span[contains(text(),'"+areaName+"')]")).click();
		} catch(Exception e){
			logger.log(LogStatus.FAIL, "Unable to select Area From Horizontal Menu" + e.getStackTrace());
		}
	}
	
	public void selectAreaFromVerticalMenu(String areaName){
		try{
			driver.findElement(By.xpath("//ul[contains(@id,'zz15_RootAspMenu')]//span[contains(text(),'"+areaName+"')]")).click();
		} catch(Exception e){
			logger.log(LogStatus.FAIL, "Unable to select Area From Vertical Menu" + e.getStackTrace());
		}
	}
	
	public void homePageVerificationAfterDeletingPresentation(){
		logger.log(LogStatus.PASS, "Successfully Landed in Home Page after Deleting a Presentation");
	}
	
	
	
	
}
