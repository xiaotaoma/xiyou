package com.socket.back;

import com.main.BaseAction;
import com.model.Hero;
import com.model.backstage.Back_login;
import com.service.Back_loginManager;
import com.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginStatistics {
	private static Logger logger = LoggerFactory.getLogger(LoginStatistics.class);
	private static LoginStatistics loginStatistics;
	public static LoginStatistics getLoginStatistics() {
		if (loginStatistics==null) {
			loginStatistics = new LoginStatistics();
		}
		return loginStatistics;
	}
	/**
	 * 登陆调用
	 * @param hero
	 */
	public void login(Hero hero) {
		try {
			int hid = hero.getId();
			int currentTime = TimeUtil.currentTime();
			Back_loginManager loginManager = (Back_loginManager) BaseAction.getIntance().getBean("loginManager");
			int zeroTime = TimeUtil.getZeroTime(currentTime);
			Back_login login = loginManager.getByHid(hid, zeroTime);
			if (login==null) {
				login = new Back_login(zeroTime,hid);
			}
			login.setTimes(login.getTimes()+1);
			if (login.getId()==0) {
				loginManager.insert(login);
			}else {
				loginManager.updateTimes(login);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 下线调通
	 * @param hero
	 */
	public void logout(Hero hero) {
		try {
			int hid = hero.getId();
			int currentTime = TimeUtil.currentTime();
			int zeroTime = TimeUtil.getZeroTime(currentTime);
			int loginTime = hero.getLoginTime();
			int logoutTime = hero.getLogoutTime();
			int onlineTime = logoutTime - loginTime;
			if (onlineTime<0) {
				onlineTime = currentTime - loginTime;
				if (onlineTime<=0) {
					return;
				}
			}
			Back_loginManager loginManager = (Back_loginManager) BaseAction.getIntance().getBean("loginManager");
			Back_login login = loginManager.getByHid(hid, zeroTime);
			
			if (login==null) {
				login = new Back_login(zeroTime,hid);
			}
			login.setTotalTime(login.getTotalTime()+onlineTime);
			
			if (login.getId()==0) {
				loginManager.insert(login);
			}else {
				loginManager.updateTatolTime(login);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}
