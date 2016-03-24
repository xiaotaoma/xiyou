package com.socket.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Account;
import com.model.Bag;
import com.model.*;
import com.model.backstage.Back_record;
import com.model.sys.Money;
import com.model.sys.SysGloablMap;
import com.service.*;
import com.socket.back.BackstageHandler;
import com.socket.back.ToolStatistics;
import com.socket.battle.BattleHandler;
import com.socket.battle.Men;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeroHandler {
	private static Logger logger = LoggerFactory.getLogger(HeroHandler.class);
	private static HeroHandler heroHandler;
	public static HeroHandler getHeroHandler() {
		if (heroHandler==null) {
			heroHandler = new HeroHandler();
		}
		return heroHandler;
	}
	/**
	 * 心跳
	 * @param obj1
	 * @param obj2
	 */
	public void heartbeat(Object obj1,Object obj2) {
		try {
			Connection connection = null;
			byte[] bytes = null;
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes!=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(1009);
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
				
				Integer heartBeatTime = (Integer) connection.getAttributes().getAttribute("heartBeatTime");
				int currentTime = TimeUtil.currentTime();
				if (heartBeatTime==null) {
					connection.getAttributes().setAttribute("heartBeatTime", currentTime);
				}else {
//					System.out.println("heart beat,currentTime:"+TimeUtil.formatTime(TimeUtil.currentTime())+" lastTime:"+TimeUtil.formatTime(heartBeatTime));
					/*int n = currentTime-heartBeatTime;
					if (n>=32 || n<=28) {
						connection.close();
					}else {
						connection.getAttributes().setAttribute("heartBeatTime", currentTime);
					}*/
					connection.getAttributes().setAttribute("heartBeatTime", currentTime);
				}
				
				int onlineTime = hero.getOnlineTime();
				onlineTime++;
				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
					boolean b = false;
					if (onlineTime/2==10) {
//						20002	"每日線上10分鐘,獲得1000銅幣"
						hero.setDailyOnline("20002_2_1000_0");//id_type(copper or money)_num_status   type:1=money,2=copper  status : 0没有领取，1已领取
						b = true;
					}else if (onlineTime/2==30) {
//						20003	"每日線上30分鐘,	獲得3000銅幣"
						hero.setDailyOnline(hero.getDailyOnline()+",20003_2_3000_0");
						b = true;
					}else if (onlineTime/2==60) {
//						20004	"每日線上1小時	獲得5元寶"
						hero.setDailyOnline(hero.getDailyOnline()+",20004_1_5_0");
						b = true;
					}else if (onlineTime/2==120) {
//						20005	"每日線上2小時獲得15元寶"
						hero.setDailyOnline(hero.getDailyOnline()+",20005_1_15_0");
						b = true;
					}
					if (b) {
						ActivityHandler.getActivityHandler().openTaiWan(connection, hero);
					}
				}
				else {
					if (onlineTime/2==30) {
						hero.setDailyOnline("20007_2_2000_0");//id_type(copper or money)_num_status   type:1=money,2=copper  status : 0没有领取，1已领取
					}else if (onlineTime/2==120) {
						hero.setDailyOnline(hero.getDailyOnline()+",20008_1_8_0");
					}else if (onlineTime/2==300) {
						hero.setDailyOnline(hero.getDailyOnline()+",20009_1_18_0");
					}
				}
				
				hero.setOnlineTime(onlineTime);
				
				
				Integer syncTime = (Integer) connection.getAttributes().getAttribute("syncTime");
				if (syncTime==null) {
					connection.getAttributes().setAttribute("syncTime", currentTime);
				}else {
					if (currentTime-syncTime>=Globalconstants.SYNCTIME) {
						sync(hid);
						connection.getAttributes().setAttribute("syncTime", currentTime);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("",e);
		}
	}
	
	
	/**
	 * 下线调用
	 */
	public void logout(Connection connection) {
		try {
			Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
			if (hid!=null) {
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero!=null) {
					hero.setLogoutTime(TimeUtil.currentTime());
					Back_record.sync(hid);
					//角色
					GlobalMap.getHeroMap().remove(hid);
					GlobalMap.getHeroNameMap().remove(hero.getName());
					GlobalMap.getConns().remove(hid);
					//战斗房间
					if (GlobalMap.getQueue().size()>0) {
						Men men = GlobalMap.getQueue().get(0);
						if (men.getHid()==hid) {
							GlobalMap.getQueue().clear();
						}
					}
					BattleHandler.getTestHandler().quitRoom(hero,true);
					
					String account = hero.getAccount();
					Account a = GlobalMap.getAccountMap().get(account);
					if (a!=null) {
						AccountManager accountManager = (AccountManager) BaseAction.getIntance().getBean("accountManager");
						accountManager.update(a);
						GlobalMap.getAccountMap().remove(account);
					}
					
					BackstageHandler.getBackstageHandler().logout(hero);
					//好友信息保存
					FriendHandler.getFriendHandler().sync(hero.getId());
					//背包信息保存
					BagManager bagManager = (BagManager) BaseAction.getIntance().getBean("bagManager");
					Bag bag = GlobalMap.getBagMap().get(hid);
					if (bag!=null) {
						bagManager.update(bag);
					}
					GlobalMap.getBagMap().remove(hid);
					//
					ToolStatistics.getToolStatistics().sync(hid);
					//角色信息保存
					HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
					heroManager.update(hero);
				}
			}
			GlobalMap.getConns().remove(connection);
			
			String acc = (String) connection.getAttributes().getAttribute("account");
			if (acc!=null) {
				GlobalMap.getAccountMap().remove(acc);
//				logger.info("remove:"+GlobalMap.getCheckLoginMap().get(acc));
//				GlobalMap.getCheckLoginMap().remove(acc);
				GlobalMap.getAccConnections().remove(acc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("",e);
		}
	}
	/**
	 * 消费元宝
	 * @param obj1
	 * @param obj2
	 */
	public void spendMoney(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int type = input.readShort();
				int id = input.readShort();
				int level = input.readShort();
				bis.close();
				input.close();
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
			 	}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				Map<Integer, Money> map = SysGloablMap.getMoneyMap().get(type);
				if (map==null) {
					send1011(0, hero.getMoney(),type, connection);
					return;
				}
				Money money = map.get(id);
				if (money==null) {
					send1011(0, hero.getMoney(),type, connection);
					return;
				}
				int n = (level-1)/10;
				int spendMoney = money.getMoney();
				if (spendMoney==0 && money.getLevel()==0 && type==4) {
					int a=id%5+2;
					int b = 0;
					switch (a) {
					case 2:
						b= 500;
						break;
					case 3:
						b= 350;
						break;
					case 4:
						b= 150;
						break;
					case 5:
						b= 100;
						break;
					case 6:
						b= 80;
						break;
					default:
						break;
					}
					spendMoney =  (int) (b*(1+0.2*(n-1)));
				}
				if (spendMoney<=0) {
					return;
				}
				int hasMoney = hero.getMoney();
				if (spendMoney>hasMoney) {
					send1011(0, hero.getMoney(),type, connection);
					return;
				}
				int reason = 0;
				switch (type) {
				case 1:
					reason = Back_record.REASON_TUDI;
					break;
				case 2:
					reason = Back_record.REASON_SKILL;
					break;
				case 3:
					reason = Back_record.REASON_UPGUARD;
					break;
				case 4:
					reason = Back_record.REASON_EQUIPMENT;
					break;
				default:
					break;
				}
//				1解锁徒弟
//				2法术升级,修为提升
//				4装备进阶
				boolean moneyExpenses = MoneyControl.moneyExpenses(hero, reason, spendMoney);
				
				if (type == 1) {//解锁徒弟
					String tids = hero.getTids();
					if (!tids.equals("")) {
						Pattern pattern = Pattern.compile(String.valueOf(id));
						Matcher matcher = pattern.matcher(tids);
						if (!matcher.find()) {
							tids = tids + "_" + id;
							hero.setTids(tids);
						}
					}else {
						hero.setTids(String.valueOf(id));
					}
				}
				
				if (moneyExpenses) {
					send1011(1, hero.getMoney(),type, connection);
				}else {
					send1011(0, hero.getMoney(),type, connection);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 元宝消费结果
	 */
	public void send1011(int result,int money,int type ,Connection connection) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(1011);
			output.writeByte(result);
			output.writeByte(type);
			output.writeInt(money);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void shakeTree(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int flag=input.readByte();
				bis.close();
				input.close();
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
//				<1W        2000   5
//				3000  1W   3000   7
//				500  3000  4000   10
//				100  500   5500   15
//				10  100    8000   30
//				1  10      15000  50
//				1          30000  100
				int treeTime = hero.getTreeTime();
				int currentTime = TimeUtil.currentTime();
				int time = 0;
				if (treeTime ==0) {
					time = 0;
				}else {
					time = Globalconstants.TREECOLDDOWN - (currentTime - treeTime);
					if (time<0 || time>Globalconstants.TREECOLDDOWN) {
						time=0;
					}
				}
				
				ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
				int rank = arenaManager.getRankByHid(hid);
				int money = 0;
				if (rank==1) {
					money = 100;
				}else if (rank>1 && rank<=10) {
					money = 50;
				}else if (rank>10 && rank<=100) {
					money = 30;
				}else if (rank>100 && rank<=500) {
					money = 15;
				}else if (rank>500 && rank<=3000) {
					money = 10;
				}else if (rank>3000 && rank<=10000) {
					money = 7;
				}else {
					money = 5;
				}
				
				if (flag==1) {
					treeTime(connection,time,hero.getTreeCopper(),money);
				}else if (flag==2) {
					if (time<=0) {//时间已经了，可以领取
						Random random = new Random();
						int n = random.nextInt(100);
						if (n<10) {
							int treeMoney = money;
							MoneyControl.moneyIncome(hero, Back_record.REASON_SHAKETREE, treeMoney);
							//money更新
							send1011(1, hero.getMoney(),0, connection);
						}
						
						//送铜币
						giveCopper(hero.getTreeCopper(), connection);
						hero.setTreeTime(currentTime);
						treeTime(connection,Globalconstants.TREECOLDDOWN,hero.getTreeCopper(),money);
						
						setTreeCopper(hid, hero,rank);
						/**
						 * 碩果累累,每日完成搖錢樹2次
						 */
						String shakeTimes = hero.getShakeTimes();
						if (shakeTimes.equals("")) {
							hero.setShakeTimes("1_1");
						}else {
							List<Integer> splitGetInt = SysUtil.splitGetInt(shakeTimes, "_");
							Integer times = splitGetInt.get(0);
							Integer status = splitGetInt.get(1);
							times++;
							if (times==2) {
								status=0;
							}
							hero.setShakeTimes(times+"_"+status);
						}
						
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public void setTreeCopper(Integer hid, Hero hero, int rank) throws SQLException {
		int copper = 0;
		if (rank==1) {
			copper = 30000;
		}else if (rank>1 && rank<=10) {
			copper = 15000;
		}else if (rank>10 && rank<=100) {
			copper = 8000;
		}else if (rank>100 && rank<=500) {
			copper = 5500;
		}else if (rank>500 && rank<=3000) {
			copper = 4000;
		}else if (rank>3000 && rank<=10000) {
			copper = 3000;
		}else {
			copper = 2000;
		}
		hero.setTreeCopper(copper);
	}
	/**
	 * 一定几率获得元宝
	 * @param connection
	 * @param time
	 * @param copper
	 * @param money
	 * @throws IOException
	 */
	public void treeTime(Connection connection,int time,int copper,int money) throws IOException {
		//下次可以摇钱时间
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(bos);
		output.writeInt(1013);
		output.writeInt(time);
		output.writeInt(copper);
		output.writeInt(money);
		bos.close();
		output.close();
		connection.write(bos.toByteArray());
	}
	
	/**
	 * 送铜币
	 */
	public void giveCopper(int copper,Connection connection) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(1015);
			output.writeInt(copper);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
			logger.info("give copper hid = "+ connection.getAttributes().getAttribute("hid")+"  copper="+copper);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 定时保存缓存数据到数据库
	 */
	public void sync(int hid) {
		try {
			Hero hero = GlobalMap.getHeroMap().get(hid);
			if (hero!=null) {
				HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
				heroManager.update(hero);
				
				Friend friend = GlobalMap.getFriendMap().get(hid);
				if (friend!=null) {
					FriendManager friendManager = (FriendManager) BaseAction.getIntance().getBean("friendManager");
					friendManager.update(friend);
				}
				//元宝记录
				Back_record.sync(hid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 充值
	 * @param hero
	 * @param connection
	 */
	public void recharge(Hero hero, Connection connection, int price) {
		int firstPay = hero.getFirstPay();
		if (firstPay==0) {//首冲奖励
			HeroHandler.getHeroHandler().giveCopper(500000, connection);
			hero.setFirstPay(1);
		}
		int charge = hero.getCharge();
		charge+=price;
		hero.setCharge(charge);
		
		String dailyRecharge = hero.getDailyRecharge();
		//20010_50_2_18000_0,id_query_type_num_status,id_query_type_num_status,    type:2铜币，1元宝， status：0不能领取，1可领取未领取，2已领取
		StringBuffer sb = new StringBuffer();
		String[] split = dailyRecharge.split(",");
		int length = split.length;
		for (int i = 0; i < length; i++) {
			String[] split2 = split[i].split("_");
			int query = Integer.parseInt(split2[1]);
			int status = Integer.parseInt(split2[4]);
			if (charge>query && status==0) {
				sb.append(split2[0]).append("_").append(split2[1]).append("_").
				append(split2[2]).append("_").append(split2[3]).append("_1,");
			}else {
				sb.append(split[i]).append(",");
			}
		}
		hero.setDailyRecharge(sb.toString());
	}
	
	/**
	 * 每天24:00重置数据
	 */
	public void reset() {
		Iterator<Entry<Integer, Hero>> iterator = GlobalMap.getHeroMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Hero> next = iterator.next();
			Hero hero = next.getValue();
			reset(hero);
		}
	}
	/**
	 * 重置角色数据
	 * @param hero
	 */
	public void reset(Hero hero) {
		if (hero==null) {
			return;
		}
		int resetTime = hero.getResetTime();
		int currentTime = TimeUtil.currentTime();
		int dayBetween = TimeUtil.getDayBetween(resetTime, currentTime);
		//上次下线距离此次登陆一天
		if (dayBetween==0) {
			
		}else if (dayBetween>=1) {
			conDays(hero,dayBetween);
		}
		
		if (dayBetween>0) {
			hero.setOnlineTime(0);
			hero.setDailyOnline("");
			
			hero.setCharge(0);
			if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
				hero.setDailyRecharge("20006_50_2_180000_0,20007_150_2_580000_0,20008_350_1_1888_0,");//20010_50_2_18000_0,id_query_type_num_status,id_query_type_num_status
			}else {
				hero.setDailyRecharge("20010_50_2_180000_0,20011_150_2_580000_0,20012_350_1_1888_0,");//20010_50_2_18000_0,id_query_type_num_status,id_query_type_num_status
			}
			
			hero.setEquipUpTimes("");
			hero.setChallengeTimes("");
			hero.setWinTimes("");
			hero.setBuyToolsTimes("");
			hero.setShakeTimes("");
		}
		
		hero.setResetTime(currentTime);
	}
	/**
	 * 连续登陆天数增加
	 * @param hero
	 */
	public void conDays(Hero hero, int dayBetween) {
		int days = hero.getDays();
		int months = hero.getMonths();
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH)+1;
		if (dayBetween==1) {
			if (months==0) {//初始值
				hero.setMonths(month);
				days++;
			}else {//不是初始值
				if (month!=months) {
					days=1;
					hero.setMonths(month);
				}else {
					days++;
				}
			}
			hero.setDays(days);
//		System.out.println("name:"+hero.getName()+" month:"+hero.getMonths()+" days:"+hero.getDays());
			int n = 0;
			switch (days) {
			case 3: n=3; break;
			case 5: n=5; break;
			case 8: n=8; break;
			case 16: n=16; break;
			case 30: n=30; break;
			default:
				break;
			}
			if (n!=0) {
				ConDays conDays = new ConDays(n);
				HashMap<Integer, ConDays> daysAward = hero.getDaysAward();
				if (daysAward!=null) {
					if (!daysAward.containsKey(conDays.getId())) {
						daysAward.put(conDays.getId(), conDays);
					}
				}else {
					daysAward = new HashMap<Integer, ConDays>();
					daysAward.put(conDays.getId(), conDays);
				}
				hero.setDaysAward(daysAward);
			}
		}else if (dayBetween>1) {
			hero.setDays(1);
			hero.setMonths(month);
		}
	}
	/**
	 * 下线
	 * @param obj1
	 * @param obj2
	 */
	public void logout(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes!=null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	
	
	public void firstDouble(Hero hero, Connection connection) {
		try {
			String firstDouble = hero.getFirstDouble();//首次充值双倍奖励
			List<Integer> splitGetInt = new ArrayList<Integer>();
			if (firstDouble!=null&&!firstDouble.equals("")) {
				splitGetInt = SysUtil.splitGetInt(firstDouble, "_");
			}
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(1020);
			
			int size = splitGetInt.size();
			output.writeShort(size);
			for (int i = 0; i < size; i++) {
				output.writeInt(splitGetInt.get(i));
			}
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	public static void main(String[] args) {
		HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
		int id = 8;
		try {
			Hero hero = heroManager.getHeroById(id);
//			System.out.println(hero.getFirstDouble());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addExp(int exp,Connection connection) {
		connection.write(SysUtil.getBytes(1021, exp));
	}
	
	public int inviteFriend(String inviteAccount,String friendAccount) {
		int result = 0;
		AccountManager accountManager = (AccountManager) BaseAction.getIntance().getBean("accountManager");
		HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
		try {
			boolean invited = invited(friendAccount, accountManager, heroManager);
			if (invited) {
				List<Account> byAccount = accountManager.getByAccount(inviteAccount);
				if (byAccount!=null && byAccount.size()>0) {
					Account account = byAccount.get(0);
					int accId = account.getId();
					List<Hero> heros = heroManager.getHeros(accId);
					if (heros!=null && heros.size()>0) {
						Hero hero = heros.get(0);
						int hid = hero.getId();
						boolean online = false;
						if (GlobalMap.getHeroMap().containsKey(hid)) {
							online = true;
							hero = GlobalMap.getHeroMap().get(hid);
						}
						String inviteFriend = hero.getInviteFriend();
						if (inviteFriend.equals("")) {
							hero.setInviteFriend("1,20009_1_50_1");//id_type(copper or money)_num_status   type:1=money,2=copper  status : 0没有领取，1已领取
						}else {
							String[] split = inviteFriend.split(",");//times,id_status-id_status
							int times = Integer.parseInt(split[0])+1;
							String s = split[1];
							boolean b = false;
							if (times==3) {
								s = split[1]+"_20010_1_150_1";
								b = true;
							}else if (times==5) {
								s = split[1]+"_20011_1_250_1";
								b = true;
							}else if (times==10) {
								s = split[1]+"_20012_1_500_1";
								b = true;
							}
							hero.setInviteFriend(times+","+s);
							Connection connection = GlobalMap.getConns().get(hid);
							if (b && connection!=null) {
								ActivityHandler.getActivityHandler().openTaiWan(connection, hero);
							}
						}
						if (!online) {
							heroManager.update(hero);
						}
						result = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return result;
	}
	private boolean invited(String friendAccount, AccountManager accountManager,
			HeroManager heroManager) throws SQLException {
		boolean result = false;
		List<Account> byAccount2 = accountManager.getByAccount(friendAccount);
		if (byAccount2!=null && byAccount2.size()>0) {
			Account account = byAccount2.get(0);
			int id = account.getId();
			List<Hero> heros = heroManager.getHeros(id);
			if (heros!=null && heros.size()>0) {
				Hero hero = heros.get(0);
				boolean online = false;
				int hid = hero.getId();
				if (GlobalMap.getHeroMap().containsKey(hid)) {
					online = true;
					hero = GlobalMap.getHeroMap().get(hid);
				}
				if (hero.getInvite()==0) {
					result = true;
					hero.setInvite(1);
					MoneyControl.moneyIncome(hero, Back_record.REASON_INVITED, 100);
					if (!online) {
						heroManager.update(hero);
					}else {
						Connection connection = GlobalMap.getConns().get(hid);
						if (connection!=null) {
							HeroHandler.getHeroHandler().send1011(1, hero.getMoney(),0, connection);
						}
					}
				}
			}
		}
		return result;
	}
}
