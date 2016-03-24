package com.util;

import com.model.Hero;
import com.model.backstage.Back_money;
import com.model.backstage.Back_record;
import com.socket.back.MoneyStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoneyControl {
	private static final Logger logger = LoggerFactory.getLogger(MoneyControl.class);
	/**
	 * 元宝收入
	 */
	public static void moneyIncome(Hero hero,int reason,int money) {
		if (money<=0) {
			return;
		}
		int hasMoney = hero.getMoney();
		int a = hasMoney;
		if (hasMoney+money<=Integer.MAX_VALUE) {
			hasMoney+=money;
		}else {
			hasMoney = Integer.MAX_VALUE;
		}
		hero.setMoney(hasMoney);
		Back_record.setRecord(hero.getId(), money, Back_record.TYPE_ADDITION, reason);
		MoneyStatistics.getMoneyStatistics().statistics(new Back_money(money, Back_money.TYPE_ADD, reason));
		logger.info("moneyIncome,hid="+hero.getId()+",hasMoney="+a+" addMoney="+money+" money="+hero.getMoney()+" reason="+reason);
	}
	/**
	 * 元宝支出
	 */
	public static boolean moneyExpenses(Hero hero,int reason,int money) {
		if (money<=0) {
			return false;
		}
		int hasMoney = hero.getMoney();
		if (hasMoney<money) {
			return false;
		}
		hero.setMoney(hasMoney-money);
		Back_record.setRecord(hero.getId(), money, Back_record.TYPE_REDUCTION, reason);
		MoneyStatistics.getMoneyStatistics().statistics(new Back_money(money, Back_money.TYPE_REDUCE, reason));
		logger.info("moneyExpenses,hid="+hero.getId()+",hasMoney="+hasMoney+" spendMoney="+money+" money="+hero.getMoney()+" reason="+reason);
		return true;
	}
	
	
}
