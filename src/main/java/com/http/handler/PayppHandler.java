package com.http.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Account;
import com.model.Hero;
import com.model.backstage.Back_record;
import com.model.sys.Product91;
import com.model.sys.SysGloablMap;
import com.service.AccountManager;
import com.service.HeroManager;
import com.socket.handler.HeroHandler;
import com.util.MD5;
import com.util.MoneyControl;
import com.util.SysUtil;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayppHandler extends HttpHandler{
	public static String HANDLERNAME = "/Paypp";
	private static Logger logger = LoggerFactory.getLogger(PayppHandler.class);
	@Override
	public void service(Request request, Response response) throws Exception {
		String requestURI = request.getRequestURI();
		System.out.println(requestURI);
		HashMap<String, String> parameters = SysUtil.getParameters(requestURI, HANDLERNAME+"/");
		if (parameters.containsKey("key")&&parameters.containsKey("account")&&
				parameters.containsKey("pid")&&parameters.containsKey("random")&&parameters.containsKey("orderID")) {
			String key = parameters.get("key");
			String account = parameters.get("account");
			Integer pid = Integer.parseInt(parameters.get("pid"));
			String random = parameters.get("random");
			int orderId = Integer.parseInt(parameters.get("orderID"));
			String amount = parameters.get("amount");
			String payId = parameters.get("payId");
			String md5 = MD5.MD5(Globalconstants.key+random);
			if (!md5.equals(key)) {
				response.getNIOWriter().write("-2");
				logger.info("md5不正确,正确MD5是"+md5);
				return;
			}
			
			Map<Integer, Product91> product91Map = SysGloablMap.getProduct91Map();
			if (!product91Map.containsKey(pid)) {
				response.getNIOWriter().write("-1");
				logger.info("pid不正确,pid="+pid);
				return;
			} 
			
			Product91 product = product91Map.get(pid);
			int moneyType = product.getMoneyType();
			int num = product.getNum();
			
			AccountManager accountManager = (AccountManager) BaseAction.getIntance().getBean("accountManager");
			List<Account> byAccount = accountManager.getByAccount(account);
			if (byAccount==null||byAccount.size()==0) {
				response.getNIOWriter().write("-1");
				logger.info("byAccount==null||byAccount.size()==0");
				return;
			}
			
			Account accou = byAccount.get(0);
			HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
			List<Hero> heros = heroManager.getHeros(accou.getId());
			if (heros==null || heros.size()==0) {
				response.getNIOWriter().write("-1");
				logger.info("heros==null || heros.size()==0");
				return;
			}
			Hero hero = heros.get(0);
			int hid = hero.getId();
			boolean online = false;
			if (GlobalMap.getHeroMap().containsKey(hid)) {
				hero = GlobalMap.getHeroMap().get(hid);
				online = true;
			}
			
			//首次购买双倍奖励
			String firstDouble = hero.getFirstDouble();
			List<Integer> splitGetInt = SysUtil.splitGetInt(firstDouble, "_");
			boolean first = false;
			if (!splitGetInt.contains(product.getId())) {//有首次双倍奖励，
				num = num*2;
				hero.setFirstDouble(hero.getFirstDouble()+"_"+product.getId());
				first = true;
			}
			
			SysUtil.syncToDb(hid, account, payId, product, "", orderId, amount, "","pp");
			
			if (online) {//角色在线
				Connection connection = GlobalMap.getConns().get(hid);
				switch (moneyType) {
				case 1:
					MoneyControl.moneyIncome(hero, Back_record.REASON_RECHARGE, num);
					HeroHandler.getHeroHandler().send1011(1, hero.getMoney(),0, connection);
					break;
				case 2:
					HeroHandler.getHeroHandler().giveCopper(num, connection);
					break;
				default:
					break;
				}
				HeroHandler.getHeroHandler().recharge(hero, connection, product.getPrice());
				if (connection!=null) {
					connection.write(SysUtil.getBytes(1018, (byte) 1));//充值成功
					if (first) {
						HeroHandler.getHeroHandler().firstDouble(hero, connection);
					}
				}
				logger.info("hid = "+hid +"角色在线充值成功product="+product);
				response.getNIOWriter().write("1");
				return;
			}else {
				int result = -1;
				switch (moneyType) {
				case 1:
					MoneyControl.moneyIncome(hero, Back_record.REASON_RECHARGE, num);
					heroManager.update(hero);
					result = 1;
					break;
				case 2:
					hero.setCopper(num+hero.getCopper());
					heroManager.update(hero);
					result = 1;
					break;
				default:
					break;
				}
				logger.info("hid = "+hid +"角色不在线充值成功product="+product);
				response.getNIOWriter().write(result+"");
				return;
			}
		}
	}
}
