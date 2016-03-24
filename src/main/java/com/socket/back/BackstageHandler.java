package com.socket.back;

import com.cache.GlobalMap;
import com.main.BaseAction;
import com.model.Hero;
import com.model.backstage.Back_onlineNum;
import com.service.Back_onlineNumManager;
import com.service.Back_temporaryDataManager;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map.Entry;

public class BackstageHandler {
	private static BackstageHandler backstageHandler;
	public static BackstageHandler getBackstageHandler() {
		if (backstageHandler==null) {
			backstageHandler = new BackstageHandler();
		}
		return backstageHandler;
	}
	private static Logger logger = LoggerFactory.getLogger(BackstageHandler.class);
	/**
	 * 在线人数
	 */
	public void onlineNum(int currentTime) {
		try {
			Iterator<Entry<Integer, Connection>> iterator = GlobalMap.getConns().entrySet().iterator();
			int n = 0;
			while (iterator.hasNext()) {
				Connection connection = iterator.next().getValue();
				if (connection!=null && connection.isOpen()) {
					Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
					if (hid!=null) {
						if (GlobalMap.getHeroMap().containsKey(hid)) {
							n++;
						}
					}
				}
			}
			Back_onlineNum onlineNum = new Back_onlineNum();
			onlineNum.setNum(n);
			onlineNum.setTime(currentTime);
			Back_onlineNumManager onlineNumManager = (Back_onlineNumManager) BaseAction.getIntance().getBean("onlineNumManager");
			onlineNumManager.insert(onlineNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 登陆统计
	 * @param hero
	 */
	public void login(Hero hero) {
		LoginStatistics.getLoginStatistics().login(hero);
		RegisterStatistics.getRegisterStatistics().oneDayOld(hero);
	}
	/**
	 * 下线统计
	 * @param hero
	 */
	public void logout(Hero hero) {
		LoginStatistics.getLoginStatistics().logout(hero);
	}
	/**
	 * 每天23:59:59调用
	 */
	public void day(int currentTime) {
		RegisterStatistics.getRegisterStatistics().registerStatistics(currentTime);
		MoneyStatistics.getMoneyStatistics().sync(currentTime);
	}
	/**
	 * 每小时59:59调用
	 */
	public void hour(int currentTime) {
		
	}
	/**
	 * 服务器关闭调用
	 */
	public void serverClose() {
		RegisterStatistics.getRegisterStatistics().writeOneOld();
	}
	/**
	 * 服务器开启
	 */
	public void serverStart() {
		try {
			RegisterStatistics.getRegisterStatistics().readOneOld();
			MoneyStatistics.getMoneyStatistics().serverStart();
			Back_temporaryDataManager temporaryDataManager = (Back_temporaryDataManager) BaseAction.getIntance().getBean("temporaryDataManager");
			temporaryDataManager.truncate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}
