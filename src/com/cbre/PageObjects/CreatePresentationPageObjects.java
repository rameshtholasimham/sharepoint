/**
 * 
 */
package com.cbre.PageObjects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cbre.Utilities.DriverUtils;
import com.cbre.Utilities.Utils;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @author Ramesh
 *
 */
public class CreatePresentationPageObjects {
	WebDriver driver;
	ExtentTest logger;
	DriverUtils driverUtils;
	Utils utils;
	final static String dateTimeStr = new SimpleDateFormat("MMddhhmmss")
			.format(new Date());
	public CreatePresentationPageObjects(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
		driverUtils = new DriverUtils(driver, logger);
		utils = new Utils(logger);
		driverUtils.waitForPage();
		if (driver.findElements(By.tagName("iframe")).size() == 0) {
			logger.log(LogStatus.FAIL,
					"Not in Create Presentation Pop Up" + logger.addScreenCapture(driverUtils.takeScreenShot("CreatePresentationPage")));
			throw new IllegalStateException("Not on Create Presentation Pop Up");
		}
		driver.switchTo().defaultContent();
		WebElement elementFrame = driver.findElement(By.xpath("//iframe[@class='ms-dlgFrame']"));
		driver.switchTo().frame(elementFrame);
		driverUtils.waitForPage();
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id = "Title")
	private WebElement presentationNameField;

	@FindBy(id = "ClientName")
	private WebElement clientNameField;

	@FindBy(id = "Brokers_TopSpan_EditorInput")
	private WebElement brokersNameOrEmailField;

	@FindBy(id = "Owner_TopSpan_EditorInput")
	private WebElement ownerNameOrEmailField;

	@FindBy(id = "SubMarket")
	private WebElement subMarketField;

	@FindBy(id = "FullScreenMode")
	private WebElement fullScreenModeCheckBox;

	@FindBy(xpath = "//input[contains(@aria-owns,'themeSelector_listbox')]")
	private WebElement titlePatternDropDown;

	@FindBy(id = "AreaType")
	private WebElement presentationAreaTypeButton;
	
	@FindBy(xpath = "//td[@class='dataCell title']/input")
	private WebElement areaTitleField;
	
	@FindBy(xpath = "//td[@class='dataCell areaType']/span/select")
	private WebElement areaTypeField;
	
	@FindBy(className = "delete")
	private WebElement areaDeleteCrossMarkButton;
	
	@FindBy(xpath = "//div[@class='k-button saveButton']")
	private WebElement saveAndContinueButton;
	
	public void selectPresentationServiceLineTag(String serviceLineTag){
		driver.findElement(By.xpath("//input[contains(@value,'"+serviceLineTag+"')]")).click();
	}
	
	public void createPresentation(HashMap<String, Properties> prop){
		prop.get("GenericInfo").put("PresentationName", prop.get("GenericInfo").getProperty("PresentationName")+dateTimeStr);
		presentationNameField.sendKeys(prop.get("GenericInfo").getProperty("PresentationName"));
		clientNameField.sendKeys(prop.get("GenericInfo").getProperty("ClientName"));
		clientNameField.sendKeys(Keys.TAB);
		brokersNameOrEmailField.sendKeys(prop.get("GenericInfo").getProperty("Broker"));
		brokersNameOrEmailField.sendKeys(Keys.SHIFT.TAB);
		brokersNameOrEmailField.sendKeys(Keys.TAB);
		brokersNameOrEmailField.sendKeys(Keys.TAB);
		ownerNameOrEmailField.sendKeys(prop.get("GenericInfo").getProperty("Owner"));
		ownerNameOrEmailField.sendKeys(Keys.SHIFT.TAB);
		ownerNameOrEmailField.sendKeys(Keys.TAB);
		ownerNameOrEmailField.sendKeys(Keys.TAB);
		subMarketField.sendKeys(prop.get("GenericInfo").getProperty("SubMarket"));
		titlePatternDropDown.click();
		titlePatternDropDown.sendKeys(prop.get("GenericInfo").getProperty("TitlePattern"));
		titlePatternDropDown.sendKeys(Keys.TAB);
		selectPresentationServiceLineTag(prop.get("GenericInfo").getProperty("PresentationServiceLineTag"));
		presentationAreaTypeButton.click();
		areaTitleField.sendKeys(prop.get("GenericInfo").getProperty("PresentationAreaTitle"));
		Select areaTypeSelection = new Select(areaTypeField);
		areaTypeSelection.selectByVisibleText(prop.get("GenericInfo").getProperty("PresentationAreaType"));
		try{
			Thread.sleep(10000);
		} catch(Exception e){
			
		}
		saveAndContinueButton.click();
		driver.switchTo().defaultContent();
		logger.log(LogStatus.INFO,
				"Entered PresentationName:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("PresentationName")
						+ "</b> ClientName:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("ClientName")
						+ "</b> Broker:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("Broker")
						+ "</b> Owner:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("Owner")
						+ "</b> SubMarket:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("SubMarket")
						+ "</b> TitlePattern:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("TitlePattern")
						+ "</b> PresentationServiceLineTag:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("PresentationServiceLineTag")
						+ "</b> PresentationAreaTitle:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("PresentationAreaTitle")
						+ "</b> PresentationAreaType:: <b style=color:blue>" + prop.get("GenericInfo").getProperty("PresentationAreaType")
						+ "</b> and clicked on Save and Continue Button");
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("processing-request")));
		
		wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing-request")));
	}
}
