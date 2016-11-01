package com.cbre.Utilities;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Utils {
	static ExtentTest logger;
	static String path = System.getProperty("user.dir");
	public static String screenShotPath = path + "/Reports/Screenshots/";
	public static DriverUtils driverUtils;
	public static String scorespath = System.getProperty("user.dir") + "/ExternalLib/Scores.xlsx";
	WebDriver driver;
	public static SecureRandom random = new SecureRandom();
	HashMap<String, String> tempPresent = new HashMap<String, String>();
	LinkedList<String> Present = new LinkedList<String>();
	LinkedList<String> notPresent = new LinkedList<String>();
	LinkedList<String> extra = new LinkedList<String>();

	public Utils(ExtentTest logger) {
		Utils.logger = logger;
	}

	/*
	 * Description: Create ExtentReport handler
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public static ExtentReports configReporter(String logPath, String reportName) {
		ExtentReports Reporter = new ExtentReports(logPath, false, DisplayOrder.NEWEST_FIRST);
		Reporter.config().documentTitle("Test Report");
		Reporter.config().reportHeadline("Report Summary");
		Reporter.config().reportName(reportName);
		return Reporter;
	}

	/*
	 * Description: Method to Generate Random string of 32 characters
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public static String nextRandomString() {
		return new BigInteger(130, random).toString(32);
	}

	/*
	 * Description: Method to generate a randon number between minValue and
	 * maxvalue provided as parameters
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */

	public int generateRandomNumber(int minValue, int maxValue) {
		int randomNumber = 0;
		try {
			Random random = new Random();
			randomNumber = minValue + random.nextInt(maxValue);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Unable to generate Random Number" + e.getMessage());
		}
		return randomNumber;
	}

	/*
	 * Description: Method to auto generate email id
	 * 
	 * @author : Ramesh
	 * 
	 * @Since :Oct 7, 2016
	 */
	public String generateRandomMailId() {
		String emailID = null;
		try {
			String dateTimeString = new SimpleDateFormat("MMddhhmmss").format(new Date());
			emailID = "automation.cbre+" + dateTimeString + "@gmail.com";
		} catch (Exception e) {
			logger.log(LogStatus.FAIL,
					"Exception occured while converting generating random Emailid.</br>" + e.getMessage());
		}
		return emailID;
	}

	/*
	 * Description:Compares two arrayList and return a boolean
	 * 
	 * @author :Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public boolean compareArrayList(ArrayList<String> dbDocumentList, ArrayList<String> uiDocumentList,
			String message) {
		boolean elementPresence = false;
		try {
			for (int index = 0; index < dbDocumentList.size(); index++) {
				if (uiDocumentList.contains(dbDocumentList.get(index))) {
					elementPresence = true;
					logger.log(LogStatus.PASS, message);
				}
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Unable to compare two array list");
		}
		return elementPresence;
	}

	/*
	 * Description: Get the String in between 2 given strings
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */

	public static String substringBetween(String str, String leftPattern, String rightPattern) {
		if (str == null || leftPattern == null || rightPattern == null) {
			return null;
		}
		int start = str.indexOf(leftPattern);
		if (start != -1) {
			int end = str.indexOf(rightPattern, start + leftPattern.length());
			if (end != -1) {
				return str.substring(start + leftPattern.length(), end);
			}
		}
		return null;
	}
	
	/*
	 * Description: This method is used to compare the Hashmaps and print the
	 * present,notpresent and extra entries between the hashmaps
	 * 
	 * @author :Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public void compareHashmaps(HashMap<String, String> expected,
			HashMap<String, String> actual) {
		try {
			tempPresent = new HashMap<String, String>();
			Present = new LinkedList<String>();
			extra = new LinkedList<String>();
			HashMap<String, String> actualDecisionrules = actual;
			HashMap<String, String> expectedDecisionRules = expected;
			checkHashmaps(expectedDecisionRules, actualDecisionrules);
			checkExtra(actualDecisionrules, tempPresent);
			System.out.println("-------present-----------");
			for (int i = 0; i < Present.size(); i++) {
				System.out.println(Present.get(i));
			}
			System.out.println("-------notpresent-----------");
			for (int i = 0; i < notPresent.size(); i++) {
				System.out.println(notPresent.get(i));
			}
			System.out.println("-------extra-----------");
			for (int i = 0; i < extra.size(); i++) {
				System.out.println(extra.get(i));
			}
			logger.log(LogStatus.INFO, "Expected details : "
					+ expectedDecisionRules + "<br> Actual is "
					+ actualDecisionrules);
			if (!Present.isEmpty()) {
				logger.log(LogStatus.PASS,
						"<b style=color:green>Pass : Expected " + Present
								+ " Actual :" + Present + "</b>");
			}
			if (!notPresent.isEmpty()) {
				logger.log(LogStatus.FAIL,
						"<b style=color:red> Fail :  Actual :" + notPresent
								+ "</b>");
			}
			if (!extra.isEmpty()) {
				logger.log(LogStatus.ERROR,
						"<b style=color:red> Error : Actual :" + extra + "</b>");
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL,
					"Error Comparing Hashmaps" + e.getMessage());
		}
	}
	
	/*
	 * Description: The method is used to check two hashmaps
	 * 
	 * @author :Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public void checkHashmaps(HashMap<String, String> expected,
			HashMap<String, String> actual) {
		try {
			Iterator it = expected.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				boolean present = checkexists("" + pair.getKey(),
						"" + pair.getValue(), actual);
				System.out.println(present);
				if (present) {
					Present.add("" + pair.getKey() + "=" + pair.getValue());
					tempPresent.put("" + pair.getKey(), "" + pair.getValue());
				} else {
					notPresent.add("" + pair.getKey() + "=" + pair.getValue());
				}
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL,
					"Error comparing check maps" + e.getMessage());
		}
	}

	/*
	 * Description: This method is used to check the value in a hashmap exists
	 * on the another hashmap
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public boolean checkexists(String key, String value,
			HashMap<String, String> actual) {
		boolean present = false;
		try {
			Iterator it2 = actual.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry pair = (Map.Entry) it2.next();
				System.out
						.println((pair.getKey().toString().trim() + "= " + pair
								.getValue().toString().trim()));
				if (pair.getKey().toString().trim().contentEquals(key.trim())
						&& pair.getValue().toString().trim()
								.contentEquals(value.trim())) {
					present = true;
					break;
				}
			}
			it2 = null;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Error occured" + e.getMessage());
		}
		return present;
	}

	/*
	 * Description: This method is used to check for extra entries between the
	 * Hashmaps
	 * 
	 * @author : Ramesh
	 * 
	 * @Since : Oct 7, 2016
	 */
	public void checkExtra(HashMap<String, String> actual,
			HashMap<String, String> temppresent) {
		try {
			Iterator it2 = actual.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry pair = (Map.Entry) it2.next();
				if (temppresent.containsKey(pair.getKey().toString().trim())
						&& temppresent.containsValue(pair.getValue().toString()
								.trim())) {
				} else {
					extra.add("" + pair.getKey().toString().trim() + "-"
							+ pair.getValue().toString().trim());
				}
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Error Occured" + e.getMessage());
		}
	}
}
