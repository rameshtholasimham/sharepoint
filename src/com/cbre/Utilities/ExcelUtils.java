/**
 * 
 */
package com.cbre.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Fillo.Connection;
import Fillo.Fillo;
import Fillo.Recordset;

/**
 * @author Ramesh
 *
 */
public class ExcelUtils {
	static ExtentTest logger;
	static String path = System.getProperty("user.dir");
	public static String screenShotPath = path + "//Reports//Screenshots";
	public static DriverUtils driverUtils;
	public static String scorespath = System.getProperty("user.dir") + "\\ExternalLib\\TestData.xlsx";

	public ExcelUtils(ExtentTest logger) {
		ExcelUtils.logger = logger;
	}

	public ArrayList<HashMap<String, String>> getExcelData(String campaign, String pathway) {
		Connection connection = null;
		Recordset recordset = null;
		HashMap<String, String> resultValue = new HashMap<String, String>();
		ArrayList<HashMap<String, String>> resultValues = new ArrayList<HashMap<String, String>>();
		try {
			Fillo fillo = new Fillo();
			connection = fillo.getConnection(scorespath);
			System.out.println("Select * from `DeclineScores`");
			recordset = connection.executeQuery("Select * from `DeclineScores` ");
			ArrayList<String> data = recordset.getFieldNames();
			while (recordset.next()) {
				for (int i = 0; i < data.size(); i++) {
					resultValue.put(data.get(i), recordset.getField(data.get(i)));
					System.out
							.println("column name is " + data.get(i) + " value is " + recordset.getField(data.get(i)));
				}
				resultValues.add(resultValue);
				resultValue = new HashMap<String, String>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.ERROR, "Unable to read data from Excel Sheet ");
		} finally {
			recordset.close();
			connection.close();
		}
		return resultValues;
	}
}
