/**
 * 
 */
package com.cbre.PageObjects;

import java.util.HashMap;
import java.util.Properties;

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
public class LoginPageObjects {
	WebDriver driver;
	ExtentTest logger;
	DriverUtils driverUtils;
	Utils utils;

	public LoginPageObjects(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
		driverUtils = new DriverUtils(driver, logger);
		utils = new Utils(logger);
		driverUtils.waitForPage();
		if (!driver.getCurrentUrl().contains("Login.asp")) {
			logger.log(LogStatus.FAIL,
					"Not in the Login Page" + logger.addScreenCapture(driverUtils.takeScreenShot("LoginPage")));
			throw new IllegalStateException("Not in the Login page");
		}
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "user_name")
	private WebElement userNameField;

	@FindBy(id = "password")
	private WebElement passwordField;

	@FindBy(id = "language")
	private WebElement languageDropDown;

	@FindBy(id = "submit_button")
	private WebElement logonButton;

	@FindBy(partialLinkText = "Clients")
	private WebElement forgotPasswordLinkClients;

	@FindBy(partialLinkText = "Employees")
	private WebElement forgotPasswordLinkEmployees;

	public void logon(HashMap<String, Properties> prop) {
		try {
			driverUtils.waitForPage();
			userNameField.sendKeys(prop.get("GenericInfo").getProperty("UserName"));
			passwordField.sendKeys(prop.get("GenericInfo").getProperty("Password"));
			logonButton.click();
			driverUtils.waitForPage();
			logger.log(LogStatus.INFO,
					"Entered username:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("UserName")
							+ "</b> and password:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("Password")
							+ "</b> and clicked on Logon Button");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.ERROR, "Exception Occured while logging in the Logon Page"
					+ logger.addScreenCapture(driverUtils.takeScreenShot("LogonPageLogging")));
		}

	}

}
