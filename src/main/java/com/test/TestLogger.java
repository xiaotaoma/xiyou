package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试slf4j
 * 需要以下jar文件
 * slf4j-api-1.6.1.jar
 * logback-access-0.9.29.jar
 * logback-classic-0.9.29.jar
 * logback-core-0.9.29.jar
 * 
 * 在工程src目录下建立logback.xml
 *  1.logback首先会试着查找logback.groovy文件;
	2.当没有找到时，继续试着查找logback-test.xml文件;
	3.当没有找到时，继续试着查找logback.xml文件;
	4.如果仍然没有找到，则使用默认配置（打印到控制台）。
 * @author Administrator
 *
 */
public class TestLogger {
	private static Logger logger = LoggerFactory.getLogger(TestLogger.class);
	public static void main(String[] args) {
		try {
			int parseInt = Integer.parseInt("sss");
			System.out.println(parseInt);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}
