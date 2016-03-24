package com.http.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Hero;
import com.model.backstage.Back_paytaiwan;
import com.model.backstage.Back_record;
import com.service.Back_paytaiwanManager;
import com.service.HeroManager;
import com.socket.battle.BattleHandler;
import com.util.MD5;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class PaytaiwanHandler extends HttpHandler{
	private static Logger logger = LoggerFactory.getLogger(PaytaiwanHandler.class);
	public static final String HANDLERNAME = "/Paytaiwan";
	@Override
	public void service(Request request, Response response) throws Exception {
		int result = 0;
		try {
			String requestURI = request.getRequestURI();
			HashMap<String, String> map = SysUtil.getParameters(requestURI, HANDLERNAME+"/");
			if (map.containsKey("key")&&map.containsKey("amount")&&map.containsKey("offer")&&
					map.containsKey("system_account")&&map.containsKey("user_id")
					&&map.containsKey("hid")&&map.containsKey("random")) {
				int random = Integer.parseInt(map.get("random"));
				String key = map.get("key");
				int amount = Integer.parseInt(map.get("amount"));
				int offer = Integer.parseInt(map.get("offer"));
				String system_account = map.get("system_account");
				int user_id = Integer.parseInt(map.get("user_id"));
				int hid = Integer.parseInt(map.get("hid"));
				String md5 = MD5.MD5(Globalconstants.key+random);
				if (md5.equals(key)) {
					boolean online = false;
					Hero hero = GlobalMap.getHeroMap().get(hid);
					HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
					if (hero!=null) {
						online = true;
					}else {
						hero = heroManager.getHeroById(hid);
					}
					if (hero!=null) {
						int money = amount*Globalconstants.taiwanMoneyRate;
						if (offer>0) {
							money+=offer;
						}
						MoneyControl.moneyIncome(hero, Back_record.REASON_RECHARGE, money);
						
						Back_paytaiwan pay = new Back_paytaiwan();
						pay.setAccount(hero.getAccount());
						pay.setAmount(amount);
						pay.setHid(hid);
						pay.setMoney(money);
						pay.setOffer(offer);
						pay.setSystem_account(system_account);
						pay.setTime(TimeUtil.currentTime());
						pay.setUser_id(user_id);
						Back_paytaiwanManager paytaiwanManager = (Back_paytaiwanManager) BaseAction.getIntance().getBean("paytaiwanManager");
						try {
							paytaiwanManager.insert(pay);
						} catch (Exception e) {
							e.printStackTrace();
							logger.debug("", e);
						}
						
						if (online) {
							Connection connection = GlobalMap.getConns().get(hid);
							if (connection!=null) {
								connection.write(BattleHandler.getTestHandler().getByte(1017, hero.getMoney()));
							}
						}else {
							heroManager.update(hero);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("", e);
		}
		response.getNIOWriter().write(""+result);
	}
}
