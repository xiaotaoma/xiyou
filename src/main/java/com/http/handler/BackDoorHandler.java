package com.http.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Account;
import com.model.Hero;
import com.model.backstage.Back_record;
import com.model.backstage.Back_tool;
import com.service.AccountManager;
import com.service.HeroManager;
import com.socket.back.BackstageHandler;
import com.socket.battle.BattleHandler;
import com.socket.handler.*;
import com.util.MD5;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BackDoorHandler extends HttpHandler{
	public static final String HANDLERNAME = "/backdoor";
	private static Logger logger = LoggerFactory.getLogger(BackDoorHandler.class);
	@Override
	public void service(Request request, Response response) throws Exception {
		try {
			Method method = request.getMethod();
			if (method.toString().equals("GET")) {
				String requestURI = request.getRequestURI();
				logger.info(requestURI);
//				System.out.println(requestURI);
				HashMap<String, String> parameters = SysUtil.getParameters(requestURI, BackDoorHandler.HANDLERNAME+"/");
//				System.out.println(parameters);
				String s = trigger(parameters);
				response.getNIOWriter().write(s);
			}else {
				response.getNIOWriter().write("-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public String trigger(Map<String, String> map) {
		String n = "-1";
		if (map.containsKey("cmd")&& map.containsKey("random")&&map.containsKey("key")) {
			int cmd = Integer.parseInt(map.get("cmd"));
			String random = map.get("random");
			String code = map.get("key");
			String encodeByMD5 = MD5.encodeByMD5(Globalconstants.key+random+cmd);
//			logger.info("trigger :"+map.toString()+"  encodeByMD5:"+encodeByMD5);
			if (code.equals(encodeByMD5)) {
				//http:127.0.0.1:8001/key=67B62E91687512920A120B3963704FDA&cmd=1001&id=1&serverName=11111&currentNum=111&random=1
				if (cmd==1005) {
//					String account = map.get("account");
//					if (account!=null) {
//						n = map.get("check");
//						Login login = new Login(n, account);
//						GlobalMap.getCheckLoginMap().put(account, login);
//					}
				}else if (cmd==1006) {//每小时触发,
//					SysUtil.clearDeadCheck(GlobalMap.getCheckLoginMap());
					//排行榜刷新
					RankHandler.getRankHandler().setRank();
				}else if (cmd==1007) {//后门加元宝
					//http://127.0.0.1:8081/backdoor/key=FBB177FEB5BE65C80BEE51C79F416ED7&cmd=1007&hid=88&money=100000&random=1
					try {
						if ((map.containsKey("hid") || map.containsKey("name") || map.containsKey("account"))&& map.containsKey("money")) {
							int money = Integer.parseInt(map.get("money"));
							String account = map.get("account");
							String string = map.get("hid");
							Integer hid =null;
							if (string!=null) {
								hid = Integer.parseInt(string);
							}
							String name = map.get("name");
							boolean online = true;
							Hero hero = null;
							if (account!=null) {
								AccountManager accountManager = (AccountManager) BaseAction.getIntance().getBean("accountManager");
								Account a = GlobalMap.getAccountMap().get(account);
								if (a==null) {
									List<Account> byAccount = accountManager.getByAccount(account);
									if (byAccount!=null) {
										a = byAccount.get(0);
										HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
										List<Hero> heros = heroManager.getHeros(a.getId());
										if (heros!=null) {
											hero = heros.get(0);
											online = false;
										}
									}
								}
							}else if (name!=null) {
								hid = GlobalMap.getHeroNameMap().get(name);
								hero = GlobalMap.getHeroMap().get(hid);
								if (hero==null) {
									HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
									hero = heroManager.getHeroByName(name);
									online = false;
								}
							}else if (hid !=null) {
								hero = GlobalMap.getHeroMap().get(hid);
								if (hero==null) {
									HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
									hero = heroManager.getHeroById(hid);
									online = false;
								}
							}
							
							if (hero!=null) {
								MoneyControl.moneyIncome(hero,Back_record.REASON_GM, money);
								if (!online) {
									HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
									heroManager.update(hero);
								}else {
									Connection connection = GlobalMap.getConns().get(hid);
									if (connection!=null) {
										connection.write(BattleHandler.getTestHandler().getByte(1017, hero.getMoney()));
									}
								}
								n = "1";
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("", e);
					}
				}else if (cmd==1008) {//广播
//					http://192.168.1.199:8001/key=ECB7962D6A6F52E8C688F978EC3290EB&cmd=1007&hid=1&money=11111&random=1
					n = broadcast(map);
				}else if (cmd==1009) {//在线人数统计
					int currentTime = TimeUtil.currentTime();
					BackstageHandler.getBackstageHandler().onlineNum(currentTime);
				}else if (cmd==1010) {//每天23:59:59
					int currentTime = TimeUtil.currentTime();
					BackstageHandler.getBackstageHandler().day(currentTime);
				}else if (cmd==1011) {//每小时59:59
					int currentTime = TimeUtil.currentTime();
					BackstageHandler.getBackstageHandler().hour(currentTime);
				}else if (cmd == 1012) {//送铜币
					//http://127.0.0.1:8081/backdoor/key=E432BB16640B595DACAE9014B6D33EFF&cmd=1012&hid=69&copper=2000000000&random=1
					int hid = Integer.parseInt(map.get("hid"));
					int copper = Integer.parseInt(map.get("copper"));
					Connection connection = GlobalMap.getConns().get(hid);
					if (connection!=null) {
						HeroHandler.getHeroHandler().giveCopper(copper, connection);
						n = "1";
					}else {
						try {
							HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
							HashMap<String, Integer> parameter = new HashMap<String, Integer>();
							parameter.put("hid", hid);
							parameter.put("copper", copper);
							heroManager.giveCopper(parameter);
							n = "1";
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("", e);
						}
					}
				}else if (cmd == 1013) {//实时在线人数
					//http://127.0.0.1:8081/backdoor/cmd=1013&random=1&key=0E781F1BDA00BB48B1A7E8823E73F88E
					Iterator<Entry<Integer, Connection>> iterator = GlobalMap.getConns().entrySet().iterator();
					int num = 0;
					while (iterator.hasNext()) {
						Entry<Integer, Connection> next = iterator.next();
						Connection value = next.getValue();
						if (value!=null && value.isOpen()) {
							num++;
						}
					}
					n = num+"";
				}else if (cmd==1014) {//24:00触发
					HeroHandler.getHeroHandler().reset();
					ActivityHandler.getActivityHandler().activity();
				}else if (cmd==1015) {//reload
					//http://127.0.0.1:8081/backdoor/cmd=1015&random=1&key=7FEF16734705AEC0E51966B77DF07CD7
					AccountHandler.getAccountHandler().initAnnouncements();
					n = "1";
				}else if (cmd==1016) {
					//http://127.0.0.1:8081/backdoor/cmd=1016&random=1&key=FA50E134B2CBEAE713DFC02E2C6361F5&hid=65&toolId=16001&num=1
					int hid = Integer.parseInt(map.get("hid"));
					int toolId = Integer.parseInt(map.get("toolId"));
					int num = Integer.parseInt(map.get("num"));
					BagHandler.getBagHandler().giveTools(toolId, num, hid,Back_tool.REASON_BACKDOOR);
					n = ""+1;
				}else if (cmd==1017) {//好友邀请
					String inviteAccount = map.get("inviteAccount");
					String friendAccount = map.get("friendAccount");
					int inviteFriend = HeroHandler.getHeroHandler().inviteFriend(inviteAccount, friendAccount);
					n = ""+inviteFriend;
				}
			}
		}
		return n;
	}
	
	private String broadcast(Map<String, String> map){
		try {
			if (map.containsKey("red")&&map.containsKey("green")&&map.containsKey("black")&&map.containsKey("content")) {
				int red = Integer.parseInt(map.get("red"));
				int green = Integer.parseInt(map.get("green"));
				int black = Integer.parseInt(map.get("black"));
				String content = map.get("content");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(1012);
				output.writeByte(red);
				output.writeByte(green);
				output.writeByte(black);
				output.writeUTF(content);
				bos.close();
				output.close();
				byte[] byteArray = bos.toByteArray();
				Iterator<Entry<Integer, Connection>> iterator = GlobalMap.getConns().entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Integer, Connection> next = iterator.next();
					Connection connection = next.getValue();
					connection.write(byteArray);
				}
				return "1";
			}else {
				return "-1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return "-1";
		}
	}
}
