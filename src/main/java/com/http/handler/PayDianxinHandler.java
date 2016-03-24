package com.http.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Account;
import com.model.Hero;
import com.model.backstage.Back_pay_dianxin;
import com.model.sys.ProductDianxin;
import com.model.sys.SysGloablMap;
import com.service.AccountManager;
import com.service.Back_pay_dianxinManager;
import com.service.HeroManager;
import com.socket.handler.PayHandler;
import com.util.MD5;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class PayDianxinHandler extends HttpHandler{
	private static Logger logger = LoggerFactory.getLogger(PayDianxinHandler.class);
	public static final String HANDLERNAME = "/Paydianxin";
	@Override
	public void service(Request request, Response response) throws Exception {
		try {
			String requestURI = request.getRequestURI();
			HashMap<String, String> parameters = SysUtil.getParameters(requestURI, HANDLERNAME+"/");
			logger.info("Pay91Handler parameters = "+parameters);
			System.err.println("Pay91Handler parameters = "+parameters);
			if (parameters.containsKey("account")&& parameters.containsKey("random")&&parameters.containsKey("key")
					&&parameters.containsKey("orderID")&&parameters.containsKey("time")&&parameters.containsKey("serialNo")
					&&parameters.containsKey("pid")&&parameters.containsKey("payType")) {
				int zoneid = Integer.parseInt(parameters.get("zoneid"));
				String account = parameters.get("account");
				String random = parameters.get("random");
				String key = parameters.get("key");
				int orderID = Integer.parseInt(parameters.get("orderID"));
				long time = Long.parseLong(parameters.get("time"));
				String serialNo = parameters.get("serialNo");
				int pid = Integer.parseInt(parameters.get("pid"));
				String payType = parameters.get("payType");
				String md5 = MD5.MD5(Globalconstants.key+random);
				if (!md5.equals(key)) {
					response.getNIOWriter().write("-2");
					logger.info("md5不正确,正确MD5是"+md5);
					return;
				}
				
				ProductDianxin product = SysGloablMap.getProductDianxinMap().get(pid);
				if (product==null) {
					response.getNIOWriter().write("-3");
					logger.info("md5不正确,正确MD5是"+product);
					return;
				}
				
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
				
				int money = 0;
				int copper = 0;
				if (product.getMoneyType() == 1) {
					money = product.getNum();
				}else if (product.getMoneyType()==2) {
					copper = product.getNum();
				}else {
					logger.info("wrong product   product="+product.toString());
					return;
				}
				boolean syncToDb = syncToDb(hid, account, copper, money, payType, serialNo, zoneid, orderID,pid);
				if (syncToDb) {
					Connection connection = GlobalMap.getConns().get(hid);
					if (connection!=null) {
						logger.info("pay message -- > send 6001");
						PayHandler.getPayHandler().login(hid, connection);
					}else {
						logger.info("pay message -- > connection = null");
					}
					response.getNIOWriter().write("1");
					return;
				}
			}else {
				logger.info("参数不正确");
				response.getNIOWriter().write("-1");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		response.getNIOWriter().write("-1");
	}
	
	private boolean syncToDb(int hid, String account, int copper, int money,String payType,String serialno,int zoneid,int orderId,int pid) {
		Back_pay_dianxinManager dianxinManager = (Back_pay_dianxinManager) BaseAction.getIntance().getBean("dianxinManager");
		Back_pay_dianxin pay_dianxin = new Back_pay_dianxin();
		pay_dianxin.setAccount(account);
		pay_dianxin.setCopper(copper);
		pay_dianxin.setHid(hid);
		pay_dianxin.setMoney(money);
		pay_dianxin.setPayType(payType);
		pay_dianxin.setSerialno(serialno);
		pay_dianxin.setTime(TimeUtil.currentTime());
		pay_dianxin.setZoneid(zoneid);
		pay_dianxin.setOrderId(orderId);
		pay_dianxin.setPid(pid);
		try {
			dianxinManager.insert(pay_dianxin);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return false;
		}
		return true;
	}
	
	
}
