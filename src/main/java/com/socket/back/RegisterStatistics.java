package com.socket.back;

import com.cache.GlobalMap;
import com.main.BaseAction;
import com.model.Hero;
import com.model.backstage.Back_register;
import com.model.backstage.Back_temporaryData;
import com.service.Back_registerManager;
import com.service.Back_temporaryDataManager;
import com.service.HeroManager;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class RegisterStatistics {
	private static Logger logger = LoggerFactory.getLogger(RegisterStatistics.class);
	
	private static RegisterStatistics registerStatistics;
	public static RegisterStatistics getRegisterStatistics() {
		if (registerStatistics==null) {
			registerStatistics = new RegisterStatistics();
		}
		return registerStatistics;
	}
	/**
	 * 次日留存人数
	 * @param hero
	 */
	public void oneDayOld(Hero hero) {
		try {
			int createTime = hero.getCreateTime();
			int currentTime = TimeUtil.currentTime();
			int dayBetween = TimeUtil.getDayBetween(createTime, currentTime);
			if (dayBetween==1) {
				GlobalMap.getOneDayOld().add(hero.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 注册统计
	 * @param currentTime
	 */
	public void registerStatistics(int currentTime) {
		try {
			Back_register register = new Back_register();
			
			register.setTime(currentTime);
			HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
			int allNum = heroManager.getAllNum();
			//总注册人数
			register.setRegister(allNum);
			
			int zeroTime = TimeUtil.getZeroTime(currentTime);
			int todayRegisterNum = heroManager.todayRegisterNum(zeroTime);
			register.setNewRehister(todayRegisterNum);//当天新注册角色
			
			register.setOneDayLogin(GlobalMap.getOneDayOld().size());
			GlobalMap.getOneDayOld().clear();
			
			Back_registerManager registerManager = (Back_registerManager) BaseAction.getIntance().getBean("registerManager");
			registerManager.insert(register);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 服务器关闭记录
	 * 前一天注册角色今天登陆角色id
	 */
	public void writeOneOld() {
		try {
			Back_temporaryData temporaryData = new Back_temporaryData();
			temporaryData.setFlag(Back_temporaryData.FLAG_ONEDAYOLD);
			String s = GlobalMap.getOneDayOld().toString();
			temporaryData.setData(s.substring(1, s.length()-1));
			Back_temporaryDataManager temporaryDataManager = (Back_temporaryDataManager) BaseAction.getIntance().getBean("temporaryDataManager");
			temporaryDataManager.insert(temporaryData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 服务器开启读取
	 * 前一天注册角色今天登陆角色id
	 */
	public void readOneOld() {
		try {
			Back_temporaryDataManager temporaryDataManager = (Back_temporaryDataManager) BaseAction.getIntance().getBean("temporaryDataManager");
			List<Back_temporaryData> list = temporaryDataManager.getByFlag(Back_temporaryData.FLAG_ONEDAYOLD);
			if (list!=null) {
				Iterator<Back_temporaryData> iterator = list.iterator();
				while (iterator.hasNext()) {
					Back_temporaryData temporaryData = iterator.next();
					String data = temporaryData.getData();
					if (data!=null && !"".equals(data)) {
						List<Integer> splitGetInt = SysUtil.splitGetInt(data, ",");
						GlobalMap.getOneDayOld().addAll(splitGetInt);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}
