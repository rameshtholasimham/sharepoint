package com.cbre.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Objects {

	public static HashMap<String, Properties> loadPropertyFile(String browser) {
		Properties genericInfo, urlProperty;
		FileInputStream propertyFile = null;
		HashMap<String, Properties> prop = new HashMap<String, Properties>();
		try {
			String path = System.getProperty("user.dir");
			genericInfo = new Properties();
			propertyFile = new FileInputStream(path + "/Properties/GenericInfo.properties");
			genericInfo.load(propertyFile);
			genericInfo.put("Browser", browser);

			urlProperty = new Properties();
			propertyFile = new FileInputStream(path + "/Properties/URL.properties");
			urlProperty.load(propertyFile);

			prop.put("GenericInfo", genericInfo);
			prop.put("URLProperty", urlProperty);

		} catch (Exception e) {
		} finally {
			try {
				propertyFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

}
