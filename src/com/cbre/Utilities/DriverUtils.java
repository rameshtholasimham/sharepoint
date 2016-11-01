package com.cbre.Utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.cbre.Utilities.Utils;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

public class DriverUtils {
	WebDriver driver;
	public ExtentTest logger;
	String screenShotPath;
	Utils utils;

	public static final int DEFAULT_WAIT_TIME = 40;
	public static String reportPath;

	public DriverUtils(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
	}

	/*
	 * Description: Launch webDriver based on browserType and return the driver
	 * instance.
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public WebDriver getDriver(String browser) {
		try {
			if (browser.equalsIgnoreCase("FF") || browser.equalsIgnoreCase("Firefox")) {
				driver = new FirefoxDriver(downloadFile());
			} else if (browser.equalsIgnoreCase("Chrome")) {
				ChromeDriverManager.getInstance().setup();
				driver = new ChromeDriver(downloadFileChrome());
			} else if (browser.equalsIgnoreCase("IE") || browser.equalsIgnoreCase("Internet Explorer")) {
				InternetExplorerDriverManager.getInstance().setup();
				DesiredCapabilities ieCap = new DesiredCapabilities();
				ieCap.setCapability("ignoreZoomSetting", true);
				ieCap.setCapability("requireWindowFocus", true);
				ieCap.setCapability("enablePersistentHover", false);
				driver = new InternetExplorerDriver(ieCap);
			}
			driver.manage().window().maximize();
			logger.log(LogStatus.INFO,
					"Local Execution started in <b style=color:blue>" + browser + "</b> browser");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception occured while invoking Local driver</br>" + e.getMessage());
		}
		return driver;
	}

	/*
	 * Description: Create download profile for firefox browser
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public static FirefoxProfile downloadFile() {
		String workingDirectory = System.getProperty("user.dir") + "/ExternalLib/Downloads";

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.dir", workingDirectory);
		profile.setPreference("browser.helperApps.neverAsk.openFile",
				"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/jar,application/x-mspowerpoint,application/powerpoint,application/x-powerpoint,application/vnd.ms-powerpoint,application/pdf");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/jar,application/x-mspowerpoint,application/powerpoint,application/x-powerpoint,application/vnd.ms-powerpoint,application/pdf");
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.closeWhenDone", false);
		profile.setPreference("reader.parse-on-load.enabled", false);
		return profile;
	}

	/*
	 * Description: Create download profile for chrome browser
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public static ChromeOptions downloadFileChrome() {

		String workingDirectory1 = System.getProperty("user.dir") + "/ExternalLib/Downloads";
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", workingDirectory1);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("chrome.switches", "--disable-extensions");
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);

		return options;
	}

	/*
	 * Description: Launch the URL
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public void launchApplication(String url) {
		try {
			System.out.println(url);
			driver.get(url);
			waitForPage();
			logger.log(LogStatus.INFO, "Launched URL - <b style=color:blue>" + url + "</b>");
		} catch (WebDriverException e) {
			logger.log(LogStatus.FAIL, "Unable to Launch the Application <b style=color:blue>" + url + "</b>");
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Unable to Launch the Application <b style=color:blue>" + url + "</b>");
		}
	}

	/*
	 * Description: Wait for the Page util it reaches Ready State
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public void waitForPage() {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIME);
			final JavascriptExecutor executor = (JavascriptExecutor) driver;
			ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver arg0) {
					return executor.executeScript("return document.readyState").equals("complete");
				}
			};

			wait.until(condition);
		} catch (TimeoutException e) {
			logger.log(LogStatus.FAIL,
					"Page not loaded within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
		} catch (WebDriverException e) {
			logger.log(LogStatus.FAIL,
					"Element not found within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
		} catch (Exception e) {
			logger.log(LogStatus.FAIL,
					"Page not loaded within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
		}
	}
	
	public List<String> getDropdownValues(WebElement element) {
		List<String> dropdownValues = new ArrayList<String>();
		List<WebElement> option = getElements(element, By.tagName("option"));
		for (int index = 1; index < option.size(); index++) {
			dropdownValues.add(option.get(index).getText().trim());
		}
		return dropdownValues;
	}
	
	public List<WebElement> getElements(WebElement element, By locator) {
		List<WebElement> elements = null;
		try {
			elements = element.findElements(locator);
		} catch (NoSuchElementException e) {
			logger.log(LogStatus.FAIL, "Element - " + locator
					+ " Not Found</br><pre>" + e.getMessage());
		} catch (WebDriverException e) {
			logger.log(LogStatus.FAIL, "Element - " + locator
					+ " Not Found</br><pre>" + e.getMessage());
		}
		return elements;
	}
	
	/*
	 * Description: Take a Screenshot
	 * 
	 * @param : Full path of the image. Eg: D://Projects//Screenshots//error.jpg
	 * 
	 * @return : Full path of the image.
	 * 
	 * @author : Ramesh
	 * 
	 * @Modified : Oct 7, 2016
	 */

	public String takeScreenShot(String screenShotName) {
		String dateTimeString = new SimpleDateFormat("MMddhhmmss")
				.format(new Date());
		try {
			String screenShotPath = reportPath + "/screenshot/" + screenShotName
					+ dateTimeString + ".jpg";
			new File(reportPath + "/screenshot").mkdir();
			FileUtils
					.copyFile(((TakesScreenshot) driver)
							.getScreenshotAs(OutputType.FILE), new File(
							screenShotPath));
			final BufferedImage image = ImageIO.read(new URL("file:///"
					+ screenShotPath));

			Graphics g = image.getGraphics();
			// g.setFont(g.getFont().deriveFont(30f));
			g.setFont(new Font("default", Font.BOLD, 30));
			g.setColor(Color.red);
			g.drawString(driver.getTitle() + " :: " + driver.getCurrentUrl(),
					50, 50);
			g.dispose();
			ImageIO.write(image, "png", new File(screenShotPath));

			/*
			 * Robot robot = new Robot(); BufferedImage screenShot =
			 * robot.createScreenCapture(new
			 * Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			 * ImageIO.write(screenShot, "JPG", new File(screenShotPath));
			 */
		} catch (IOException e) {
			logger.log(LogStatus.FAIL,
					"Unable to Take Screenshot!!</br>" + e.getMessage());
		} catch (Exception e) {
			logger.log(LogStatus.FAIL,
					"Unable to Take Screenshot!!</br>" + e.getMessage());
		}
		return "screenshot\\" + screenShotName + dateTimeString + ".jpg";
	}
	
	public <T> void verifyAndProceed(T expected, T actual, String message) {
		try {
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertEquals(actual, expected);
			softAssert.assertAll();
			logger.log(LogStatus.PASS, "<pre><b style=color:green>" + message
					+ "</br>" + actual + "</b></pre>");
		} catch (AssertionError e) {
			String exceptionMessage = "";
			if (e.getMessage() == null) {
				exceptionMessage = "<b style=color:red>" + e.getMessage()
						+ "</b></br>";
			}
			logger.log(
					LogStatus.FAIL,
					"<pre>"
							+ exceptionMessage
							+ "<b style=color:red>"
							+ message
							+ "</br>Expected - "
							+ expected
							+ "</br>Actual   - "
							+ actual
							+ "</b></pre>"
							+ logger.addScreenCapture(takeScreenShot("verifyAndProceed")));
		}
	}
	
	public <T> void verifyAndProceedApi(T expected, T actual, String message) {
		try {
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertEquals(actual, expected);
			softAssert.assertAll();
			logger.log(LogStatus.PASS, "<pre><b style=color:green>" + message
					+ "</br>" + actual + "</b></pre>");
		} catch (AssertionError e) {
			String exceptionMessage = "";
			if (e.getMessage() == null) {
				exceptionMessage = "<b style=color:red>" + e.getMessage()
						+ "</b></br>";
			}
			logger.log(
					LogStatus.FAIL,
					"<pre>"
							+ exceptionMessage
							+ "<b style=color:red>"
							+ message
							+ "</br>Expected - "
							+ expected
							+ "</br>Actual   - "
							+ actual
							+ "</b></pre>"
							);
		}
	}

	public <T> void verifyAndProceedNotEqualTo(T expected, T actual,
			String message) {
		try {
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertNotEquals(actual, expected);
			softAssert.assertAll();
			logger.log(LogStatus.PASS, "<pre><b style=color:green>" + message
					+ "</br>" + actual + "</b></pre>");
		} catch (AssertionError e) {
			String exceptionMessage = "";
			if (e.getMessage() == null) {
				exceptionMessage = "<b style=color:red>" + e.getMessage()
						+ "</b></br>";
			}
			logger.log(
					LogStatus.FAIL,
					"<pre>"
							+ exceptionMessage
							+ "<b style=color:red>"
							+ message
							+ "</br>Expected - "
							+ expected
							+ "</br>Actual   - "
							+ actual
							+ "</b></pre>"
							+ logger.addScreenCapture(takeScreenShot("verifyAndProceedNotEqualTo")));
		}
	}

	public <T> void verifyAndStop(T expected, T actual, String message) {
		try {
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertEquals(actual, expected);
			softAssert.assertAll();
			logger.log(LogStatus.PASS, "<pre><b style=color:green>" + message
					+ "</br>" + actual + "</b></pre>");
		} catch (AssertionError e) {
			String exceptionMessage = "";
			if (e.getMessage() == null) {
				exceptionMessage = "<b style=color:red>" + e.getMessage()
						+ "</b></br>";
			}
			logger.log(LogStatus.FAIL, "<pre>" + exceptionMessage
					+ "<b style=color:red>" + message + "</br>Expected - "
					+ expected + "</br>Actual   - " + actual + "</b></pre>"
					+ logger.addScreenCapture(takeScreenShot("verifyAndStop")));
			throw e;
		}
	}

	public <T> void verifyAndProceedpdf(T expected, T actual, String message) {
		try {
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertEquals(actual, expected);
			softAssert.assertAll();
			logger.log(LogStatus.PASS, message + "</br>Expected - "
					+ "<b style=color:blue>" + expected + "</b>" + "Actual - "
					+ "<b style=color:blue>" + actual + "</b>");
		} catch (AssertionError e) {
			logger.log(LogStatus.FAIL, "<pre>" + e.getMessage() + "</br>"
					+ message + "</br>Expected - " + expected
					+ "</br> Actual - " + actual + "</pre>");
		}
	}

	// @Ramesh - Modified Logger info message
	public void verifySafely(
			ArrayList<HashMap<String, String>> expectedArrayList,
			ArrayList<HashMap<String, String>> actualArrayList, String message) {
		String missingHashKey = null;
		try {
			if (actualArrayList.equals(expectedArrayList)
					&& expectedArrayList.equals(actualArrayList)) {
				logger.log(LogStatus.PASS, "PASS : " + message);
				logger.log(LogStatus.INFO,
						message + " : " + expectedArrayList.toString());
			} else {
				for (int index = 0; index < expectedArrayList.size(); index++) {
					boolean hashFound = expectedArrayList.get(index).equals(
							actualArrayList.get(index));

					Set<String> HashKeySet = expectedArrayList.get(index)
							.keySet();
					boolean found = true;
					for (String hashKey : HashKeySet) {
						missingHashKey = hashKey;
						if (expectedArrayList
								.get(index)
								.get(hashKey)
								.equals(actualArrayList.get(index).get(hashKey)))
							found = found && true;
						else {
							logger.log(LogStatus.FAIL, hashKey + " : "
									+ expectedArrayList.get(index).get(hashKey)
									+ "||" + hashKey + " : "
									+ actualArrayList.get(index).get(hashKey));
							found = false;
						}
						if (hashFound != true && found != true) {
							logger.log(LogStatus.FAIL, message
									+ "</br> Expected : " + expectedArrayList
									+ "</br> Actual : " + actualArrayList);
						}
					}
				}
			}
		} catch (NullPointerException e) {
			logger.log(LogStatus.FAIL, missingHashKey + " is not found in the "
					+ actualArrayList);
		}
	}
	
	public void killSession() {
		try {
			Runtime.getRuntime().exec(
					System.getProperty("user.dir")
							+ "/ExternalLib/KillProcess.bat");
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Unable to kill the current session");
		}
	}
}
