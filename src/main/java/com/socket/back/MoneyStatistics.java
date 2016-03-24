package com.socket.back;

import com.alibaba.fastjson.JSONArray;
import com.cache.GlobalMap;
import com.main.BaseAction;
import com.model.backstage.Back_money;
import com.model.backstage.Back_temporaryData;
import com.service.Back_moneyManager;
import com.service.Back_temporaryDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class MoneyStatistics {
	private static MoneyStatistics moneyStatistics;
	public static MoneyStatistics getMoneyStatistics() {
		if (moneyStatistics==null) {
			moneyStatistics = new MoneyStatistics();
		}
		return moneyStatistics;
	}
	private static Logger logger = LoggerFactory.getLogger(MoneyStatistics.class);
	
	public void statistics(Back_money money) {
		int type = money.getType();
		int reason = money.getReason();
		int num = money.getNum();
		try {
			Map<Integer, Map<Integer, Back_money>> moneyMap = GlobalMap.getMoneyMap();
			if (moneyMap.containsKey(type)) {
				Map<Integer, Back_money> map = moneyMap.get(type);
				if (map.containsKey(reason)) {
					Back_money back_money = map.get(reason);
					back_money.setNum(back_money.getNum()+num);
				}else {
					map.put(reason, money);
				}
			}else {
				Map<Integer, Back_money> map = new ConcurrentHashMap<Integer, Back_money>();
				map.put(reason, money);
				moneyMap.put(type, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void sync(int currentTime) {
		try {
			Iterator<Entry<Integer, Map<Integer, Back_money>>> iterator = GlobalMap.getMoneyMap().entrySet().iterator();
			Back_moneyManager moneyManager = (Back_moneyManager) BaseAction.getIntance().getBean("moneyManager");
			while (iterator.hasNext()) {
				Entry<Integer, Map<Integer, Back_money>> next = iterator.next();
				Map<Integer, Back_money> value = next.getValue();
				Iterator<Entry<Integer, Back_money>> ite = value.entrySet().iterator();
				while (ite.hasNext()) {
					Entry<Integer, Back_money> entry = ite.next();
					Back_money back_money = entry.getValue();
					back_money.setTime(currentTime);
					moneyManager.insert(back_money);
				}
			}
			GlobalMap.getMoneyMap().clear();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	
	public void serverClose() {
		try {
			Iterator<Entry<Integer, Map<Integer, Back_money>>> iterator = GlobalMap.getMoneyMap().entrySet().iterator();
			JSONArray jsonArray = new JSONArray();
			while (iterator.hasNext()) {
				Entry<Integer, Map<Integer, Back_money>> next = iterator.next();
				Iterator<Entry<Integer, Back_money>> ite = next.getValue().entrySet().iterator();
				while (ite.hasNext()) {
					Entry<Integer, Back_money> entry = ite.next();
					Back_money value = entry.getValue();
					jsonArray.add(value.toString());
				}
			}
			
			Back_temporaryDataManager temporaryDataManager = (Back_temporaryDataManager) BaseAction.getIntance().getBean("temporaryDataManager");
			Back_temporaryData temporaryData = new Back_temporaryData();
			temporaryData.setData(jsonArray.toString());
			temporaryData.setFlag(Back_temporaryData.FLAG_MONEYMAP);
			temporaryDataManager.insert(temporaryData);
			GlobalMap.getMoneyMap().clear();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void serverStart() {
		try {
			Back_temporaryDataManager temporaryDataManager = (Back_temporaryDataManager) BaseAction.getIntance().getBean("temporaryDataManager");
			List<Back_temporaryData> list = temporaryDataManager.getByFlag(Back_temporaryData.FLAG_MONEYMAP);
			if (list!=null) {
				int size = list.size();
				for (int i = 0; i < size; i++) {
					Back_temporaryData temporaryData = list.get(i);
					String data = temporaryData.getData();
					JSONArray fromObject = JSONArray.parseArray(data);
					Iterator iterator = fromObject.iterator();
					while (iterator.hasNext()) {
						String s = (String) iterator.next();
						Back_money back_money = new Back_money(s);
						statistics(back_money);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}
