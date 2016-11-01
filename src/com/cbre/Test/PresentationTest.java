/**
 * 
 */
package com.cbre.Test;

import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cbre.PageObjects.CreatePresentationPageObjects;
import com.cbre.PageObjects.DeleteThisSitePageObjects;
import com.cbre.PageObjects.DeleteWebPageObjects;
import com.cbre.PageObjects.HomePageObjects;
import com.cbre.PageObjects.LoginPageObjects;
import com.cbre.PageObjects.PresentationAreasPageObjects;
import com.cbre.PageObjects.PresentationPageObjects;
import com.cbre.Utilities.DriverUtils;
import com.cbre.Utilities.Objects;
import com.cbre.Utilities.PreRequsite;
import com.cbre.Utilities.Utils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @author Ramesh
 *
 */
public class PresentationTest {
	WebDriver driver;
	public ExtentReports reporter;
	public ExtentTest logger;
	public static Utils utils;
	public static DriverUtils driverUtils;
	String browser;
	HashMap<String, Properties> prop = new HashMap<String, Properties>();
	HashMap<String, String> resultValues = new HashMap<String, String>();

	@Parameters("browser")
	@BeforeMethod
	public void setUp(String browser) {
		try {
			this.browser = browser;
			utils = new Utils(logger);
			reporter = PreRequsite.reporter;
			prop = Objects.loadPropertyFile(browser);
		} catch (Exception e) {
			logger = reporter.startTest("Pre Config");
			logger.log(LogStatus.FAIL, "Unable To open Properties File..</br><pre>" + e.getMessage() + "</pre>");
		}
	}

	/*
	 * Description: Create Presentation Test
	 * @author : Ramesh
	 * @Since : Oct 9, 2016
	 */
	@Test
	public void createPresentationTest() {
		try{
			logger = reporter.startTest("TestCase-001 :: Create Presentation Test");
			intializeObjects();
			driver = driverUtils.getDriver(browser);
			
			// Launch the Application
			driverUtils.launchApplication(prop.get("URLProperty").getProperty("sharepointURL"));
			
			// Login Page
			LoginPageObjects loginPageObject = new LoginPageObjects(driver, logger);
			loginPageObject.logon(prop);
			
			// Home Page
			HomePageObjects homePageObject = new HomePageObjects(driver, logger);
			homePageObject.selectAreaFromHorizontalMenu(prop.get("GenericInfo").getProperty("AreaSelection"));
			
			// Presentation Page
			PresentationPageObjects presentationPageObject = new PresentationPageObjects(driver, logger);
			presentationPageObject.clickCreatePresentation();
			
			// Create Presentation Pop Up
			CreatePresentationPageObjects createPresentationPageObject = new CreatePresentationPageObjects(driver, logger);
			createPresentationPageObject.createPresentation(prop);
			
			// Presentation Areas Page
			PresentationAreasPageObjects presentationAreasPageObject = new PresentationAreasPageObjects(driver, logger);
			presentationAreasPageObject.verifyPresentationDetails(prop);
			presentationAreasPageObject.clickRemovePresentation();
			
			// Delete This Site Page
			DeleteThisSitePageObjects deleteThisSitePageObject = new DeleteThisSitePageObjects(driver, logger);
			deleteThisSitePageObject.DeletePresentation();
			
			// Delete Web Page
			DeleteWebPageObjects deleteWebPageObject = new DeleteWebPageObjects(driver, logger);
			deleteWebPageObject.goBackToSite();
			
			// Home Page
			HomePageObjects homePageObjectAfterDeletingPresentation = new HomePageObjects(driver, logger);
			homePageObjectAfterDeletingPresentation.homePageVerificationAfterDeletingPresentation();
			
			
			
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Exception occured in PresentationTest > createPresentationTest"
					+ e.getMessage() + logger.addScreenCapture(driverUtils.takeScreenShot("createPresentationTest")));
		}
		
		
	}
	
	@AfterMethod
	public void tearDown() {
		reporter.endTest(logger);
		reporter.flush();
		/*if (driver != null) {
				driver.quit();
		}*/
	}

	private void intializeObjects() {
		driverUtils = new DriverUtils(driver, logger);
		utils = new Utils(logger);

	}
}
