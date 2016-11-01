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
public class PresentationAreasPageObjects {
	WebDriver driver;
	ExtentTest logger;
	DriverUtils driverUtils;
	Utils utils;
	HashMap<String, String> presentationDetailsFromUI = new HashMap<String, String>();
	HashMap<String, String> presentationDetailsExpected = new HashMap<String, String>();
	
	public PresentationAreasPageObjects(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
		driverUtils = new DriverUtils(driver, logger);
		utils = new Utils(logger);
		driverUtils.waitForPage();
		if (!driver.getCurrentUrl().contains("AllItems.aspx")) {
			logger.log(LogStatus.FAIL,
					"Not in Presentation Areas Page" + logger.addScreenCapture(driverUtils.takeScreenShot("PresentationAreasPage")));
			throw new IllegalStateException("Not in the Presentation Areas page");
		}
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//ul[@id='zz15_RootAspMenu']//span[contains(text(),'Presentation:')]")
	private WebElement presentationName;
	
	@FindBy(xpath = "//ul[@id='zz15_RootAspMenu']//span[contains(text(),'Presentation Config')]")
	private WebElement presentationConfig;
	
	@FindBy(xpath = "//ul[@id='zz15_RootAspMenu']//span[contains(text(),'Presentation Areas')]")
	private WebElement presentationArea;

	@FindBy(xpath = "//ul[@id='zz15_RootAspMenu']//span[contains(text(),'Presentation Theme')]")
	private WebElement presentationTheme;
	
	@FindBy(xpath = "//ul[@id='zz15_RootAspMenu']//span[contains(text(),'Presentation Permissions')]")
	private WebElement presentationPermissions;
	
	@FindBy(xpath = "//ul[@id='zz15_RootAspMenu']//span[contains(text(),'Remove Presentation')]")
	private WebElement removePresentation;
	
	@FindBy(xpath = "//ul[@id='zz15_RootAspMenu']//span[contains(text(),'Site Contents')]")
	private WebElement siteContents;
	
	@FindBy(xpath = "//a[@class='ms-listlink ms-draggable']")
	private WebElement presentationTitle;
	
	public HashMap<String, String> getPresentationDetails(){
		presentationDetailsFromUI.put("PresentationName", presentationName.getText().split("Presentation: ")[1]);
		presentationDetailsFromUI.put("PresentationTitle",  presentationTitle.getText());
		return presentationDetailsFromUI;
	}
	
	public void verifyPresentationDetails(HashMap<String, Properties> prop){
		getPresentationDetails();
		presentationDetailsExpected.put("PresentationName", prop.get("GenericInfo").getProperty("PresentationName"));
		presentationDetailsExpected.put("PresentationTitle", prop.get("GenericInfo").getProperty("PresentationAreaTitle"));
		
		utils.compareHashmaps(presentationDetailsExpected, presentationDetailsFromUI);
	}
	
	public void clickRemovePresentation(){
		removePresentation.click();
	}
	
	
	
}
