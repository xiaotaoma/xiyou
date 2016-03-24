package com.main;

import com.cache.Globalconstants;
import com.grizzly.server.GrizzlyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {
	private static Logger logger = LoggerFactory.getLogger(LoadConfig.class);
	public static void loadConfig() {
		try {
			File file = new File("src/config.properties");
			Properties properties=new Properties();
			InputStream inputStream=new FileInputStream(file);
			properties.load(inputStream);
			GrizzlyServer.port = Integer.parseInt(properties.getProperty("port"));
			
			Globalconstants.zoneid = Integer.parseInt(properties.getProperty("zoneid"));
			
			Globalconstants.centralServerIp = properties.getProperty("CentralServerIp");
			Globalconstants.centralServerPort = Integer.parseInt(properties.getProperty("centralServerPort"));
			Globalconstants.payport = Integer.parseInt(properties.getProperty("payport"));
			Globalconstants.BATTLEMP = Integer.parseInt(properties.getProperty("battlemp"));
			
//			Globalconstants.VERSION_ANDROID = properties.getProperty("version_android");
//			Globalconstants.VERSION_APPLE = properties.getProperty("version_apple");
			
			Globalconstants.SERVERSTARTDATE = properties.getProperty("serverStartDate");
			Globalconstants.VERSION = properties.getProperty("version");
			
			Globalconstants.taiwanMoneyRate = Integer.parseInt(properties.getProperty("taiwan_money_rate"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
}
