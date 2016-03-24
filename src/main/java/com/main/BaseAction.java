package com.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BaseAction {
	public static BaseAction baseAction=null;
	private static ApplicationContext applicationContext=null;
	public BaseAction() {
		applicationContext=new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
	}
	public static BaseAction getIntance() {
		if (baseAction==null) {
			baseAction = new BaseAction();
		}
		return baseAction;
	}
	public Object getBean(String bean) {
		return applicationContext.getBean(bean);
	}

	public static void main(String[] args) {
		BaseAction.getIntance();
	}
}
