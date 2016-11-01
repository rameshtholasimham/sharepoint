package com.cbre.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cbre.Utilities.DriverUtils;
import com.cbre.Utilities.Utils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class PreRequsite {
	public static ExtentReports reporter;
	static String testName, browserToRun, reportPath;
	public ExtentTest logger;

	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	final static String dateTimeString = new SimpleDateFormat("MMddhhmm")
			.format(new Date());

	@Parameters("browser")
	@BeforeMethod
	public void startTest(final ITestContext testContext,String browser) {
		testName = testContext.getSuite().getName().toString();
		browserToRun = browser;
	}
	@Test
	public void start() {
		String path = System.getProperty("user.dir");
		reportPath = path + "/Reports/" + dateTimeString +"-"+testName + " Logs";
		DriverUtils.reportPath = reportPath;
		reporter = Utils.configReporter(reportPath + "/log.html",
				"Batch Execution");
	}
	
}
