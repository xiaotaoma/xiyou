package com.http.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Account;
import com.model.Hero;
import com.model.backstage.Back_payyidong;
import com.model.backstage.Back_record;
import com.model.sys.ProductYiDong;
import com.model.sys.SysGloablMap;
import com.service.AccountManager;
import com.service.Back_payyidongManager;
import com.service.HeroManager;
import com.socket.handler.HeroHandler;
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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class PayYiDongHandler extends HttpHandler{
	private static Logger logger = LoggerFactory.getLogger(PayYiDongHandler.class);
	public static final String HANDLERNAME = "/PayYiDong";
	@Override
	public void service(Request request, Response response) throws Exception {
		try {
			String requestURI = request.getRequestURI();
			HashMap<String, String> parameters = SysUtil.getParameters(requestURI, HANDLERNAME+"/");
			if (parameters.containsKey("account")&&parameters.containsKey("pid")&&parameters.containsKey("consumeCode")
					&&parameters.containsKey("contentId") && parameters.containsKey("cpid")&&parameters.containsKey("versionId")
					&&parameters.containsKey("random")&&parameters.containsKey("key")) {
//				String md5 = MD5.MD5(account+pid+consumeCode+contentId+cpid+versionId+nextInt+Globalconstants.key);
				String key = parameters.get("key");
				
				int pid = Integer.parseInt(parameters.get("pid"));
				String consumeCode = parameters.get("consumeCode");
				String contentId = parameters.get("contentId");
				String cpid = parameters.get("cpid");
				String versionId = parameters.get("versionId");
				String random = parameters.get("random");//道具计费代码
				String acc = parameters.get("account");
				String md5 = MD5.MD5(acc+pid+consumeCode+contentId+cpid+versionId+random+Globalconstants.key);
				
				if (md5.equals(key)) {
					AccountManager accountManager = (AccountManager) BaseAction.getIntance().getBean("accountManager");
					List<Account> byAccount = accountManager.getByAccount(acc);
					if (byAccount!=null && byAccount.size()>0) {
						Account account = byAccount.get(0);
						int accId = account.getId();
						HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
						List<Hero> heros = heroManager.getHeros(accId);
						if (heros!=null && heros.size()>0) {
							Hero hero = heros.get(0);
							int hid = hero.getId();
							boolean online = false;
							if (GlobalMap.getHeroMap().containsKey(hid)) {
								hero = GlobalMap.getHeroMap().get(hid);
								online = true;
							}
//							http://127.0.0.1:8081/PayYiDong/account=asv&consumeCode=006010913001&transIDO=12345678901234567&cpServiceId=120123002000&random=353447653&key=9f776f8eff62226d9b8c768253cf80a8
							ProductYiDong product = SysGloablMap.getYidongProductMap().get(pid);
							if (product==null) {
								return;
							}
							
							int moneyType = product.getMoneyType();
							int num = product.getMoneyNum();
							//首次购买双倍奖励
							String firstDouble = hero.getFirstDouble();
							List<Integer> splitGetInt = SysUtil.splitGetInt(firstDouble, "_");
							boolean first = false;
							if (!splitGetInt.contains(product.getId())) {//有首次双倍奖励，
//								num = num*2;
//								hero.setFirstDouble(hero.getFirstDouble()+"_"+product.getId());
								first = true;
							}
							int money = 0;
							int copper = 0;
							if (moneyType == 1) {//， 1 元宝
								money = num;
							}else if (moneyType ==2) {//2 铜币  
								copper = num;
							}
							/**
							 * 保存数据库
							 */
							syncToDb(Integer.parseInt(product.getPrice()), money, hid, acc, consumeCode, copper, contentId, cpid, versionId);
							
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
								HeroHandler.getHeroHandler().recharge(hero, connection, Integer.parseInt(product.getPrice()));
								if (connection!=null) {
									connection.write(SysUtil.getBytes(1018, (byte) 1));//充值成功
//									if (first) {
//										HeroHandler.getHeroHandler().firstDouble(hero, connection);
//									}
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}

	private void syncToDb(int price,int money,int hid,
			String account,String consumeCode,int copper,String contentId,String cpid,String versionId) throws SQLException {
		Back_payyidong payyidong = new Back_payyidong();
		payyidong.setAccount(account);
		payyidong.setCopper(copper);
		payyidong.setHid(hid);
		payyidong.setMoney(money);
		payyidong.setPrice(price);
		payyidong.setTime(TimeUtil.currentTime());
		payyidong.setConsumeCode(consumeCode);
		payyidong.setContentId(contentId);
		payyidong.setCpid(cpid);
		payyidong.setVersionId(versionId);
		Back_payyidongManager payyidongManager = (Back_payyidongManager) BaseAction.getIntance().getBean("payyidongManager");
		payyidongManager.insert(payyidong);
	}
}
