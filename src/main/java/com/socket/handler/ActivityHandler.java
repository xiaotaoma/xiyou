package com.socket.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.ConDays;
import com.model.Hero;
import com.model.backstage.Back_record;
import com.service.HeroManager;
import com.socket.battle.BattleHandler;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityHandler {
	private static Logger logger = LoggerFactory.getLogger(AccountHandler.class);
	private static ActivityHandler activityHandler;
	public static ActivityHandler getActivityHandler() {
		if (activityHandler==null) {
			activityHandler = new ActivityHandler();
		}
		return activityHandler;
	}
	
	/**
	 * 领取奖励
	 * @param obj1
	 * @param obj2
	 */
	public void getAward(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
		if (obj1!=null) {
			connection = (Connection) obj1;
		}
		if (obj2!=null) {
			bytes = (byte[]) obj2;
		}
		if (connection!=null && bytes !=null) {
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int id = input.readShort();
				bis.close();
				input.close();
				
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid == null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int status = 2;//返回结果
				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
					status = getAwardTaiWan(connection, id, hero, status);
				}else {
					status = getAward(connection, id, hero, status);
				}
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(7005);
				output.writeShort(id);
				output.writeByte(status);
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("", e);
			}
		}
	}

	private int getAward(Connection connection, int id, Hero hero, int status) {
		if (id==20000) {//首冲奖励
			int firstPay = hero.getFirstPay();
			if (firstPay==1) {
				HeroHandler.getHeroHandler().giveCopper(500000, connection);
				hero.setFirstPay(2);
				status =1;
			}
		}else if (id == 20001) {//每日签到
			status = getDaily(hero, connection);
		}else if (id>=20002 && id<=20006) {//连续登陆奖励
			status = getContinuous(hero, id, connection);
		}else if (id>=20007 && id<=20009) {//累计在线奖励
			status = getOnlineTime(hero, connection, id);
		}else if (id>=20010 && id<=20012) {//每日累计充值奖励
			status = getChargeAward(hero, connection, id);
		}
		return status;
	}
	
	private int getAwardTaiWan(Connection connection, int id, Hero hero, int status) {
		if (id==20000) {//首冲奖励
			int firstPay = hero.getFirstPay();
			if (firstPay==1) {
				HeroHandler.getHeroHandler().giveCopper(500000, connection);
				hero.setFirstPay(2);
				status =1;
			}
		}else if (id == 20001) {//每日签到
			status = getDaily(hero, connection);
		}else if (id>=20002 && id<=20005) {//累计在线奖励
			status = getOnlineTime(hero, connection, id);
		}else if (id>=20006 && id<=20008) {//每日累计充值奖励
			status = getChargeAward(hero, connection, id);
		}else if (id>=20009 && id<=20012) {//
			status = getFriendAward(hero, id, connection);
		}
		return status;
	}
	/**
	 * 领取好友邀请奖励哦
	 * @return
	 */
	private int getFriendAward(Hero hero, int id, Connection connection) {
		String inviteFriend = hero.getInviteFriend();
//		hero.setInviteFriend("1,20009_1_50_1");//id_type(copper or money)_num_status   type:1=money,2=copper  status : 0没有领取，1已领取
		if (inviteFriend==null || inviteFriend.equals("")) {
			return 0;
		}
		Pattern pattern = Pattern.compile(id+"_\\d*_\\d*_\\d");
		Matcher matcher = pattern.matcher(inviteFriend);
		int result = 0;
		if (matcher.find()) {
			String group = matcher.group();
			List<Integer> list = SysUtil.splitGetInt(group, "_");
			int status = list.get(3);
			if (status!=0) {
				int type = list.get(1);
				int num = list.get(2);
				if (type==1) {
					MoneyControl.moneyIncome(hero, Back_record.REASON_ACTIVITY, num);
					HeroHandler.getHeroHandler().send1011(1, hero.getMoney(),0, connection);
				}else if (type==2) {
					HeroHandler.getHeroHandler().giveCopper(num, connection);
				}
				inviteFriend = inviteFriend.replace(group, id+"_"+list.get(1)+"_"+list.get(2)+"_2");
				hero.setInviteFriend(inviteFriend);
				result = 1;
			}
		}
		return result;
	}
	/**
	 * 打开面板获取数据
	 * @param obj1
	 * @param obj2
	 */
	public void open(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
		if (obj1!=null) {
			connection = (Connection) obj1;
		}
		if (obj2!=null) {
			bytes = (byte[]) obj2;
		}
		try {
			if (connection!=null && bytes !=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid == null) {
					return;
				}
				
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
					openTaiWan(connection, hero);
				}else {
					open(connection, hero);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param connection
	 * @param hero
	 */
	public void openTaiWan(Connection connection, Hero hero) {
		try {
			int currentTime = TimeUtil.currentTime();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(7002);
			output.writeShort(13);
			//首冲奖励
			output.writeShort(20000);
			int firstPayStatus = 0;
			if (hero.getFirstPay()==1) {//已经完成首冲没有领取过奖励
				firstPayStatus = 1;
			}
			output.writeByte(firstPayStatus);
			
			//每日登陆奖励
			output.writeShort(20001);
			int daily = hero.getDaily();
			int dailyStatus = 0;
			if (daily==0) {
				dailyStatus = 1;
			}else {
				int dayBetween = TimeUtil.getDayBetween(daily, currentTime);
				if (dayBetween>0) {
					dailyStatus = 1;
				}else if (dayBetween==0) {//当天领取过了
					dailyStatus = 2;
				}
			}
			output.writeByte(dailyStatus);
			
			//累计在线时长奖励
			//id_type(copper or money)_num_status   type:1=money,2=copper  status : 0没有领取，1已领取
			String dailyOnline = hero.getDailyOnline();//
			Pattern pattern = null;
			Matcher matcher = null;
			for (int i = 20002; i <= 20005; i++) {
				pattern = Pattern.compile(i+"_\\d*_\\d*_\\d");
				matcher = pattern.matcher(dailyOnline);
				output.writeShort(i);
				if (matcher.find()) {
					String group = matcher.group();
					String[] split = group.split("_");
					int parseInt = Integer.parseInt(split[3]);
					if (parseInt==2) {//已领取，不能再领取
						output.writeByte(2);
					}else {//未领取，可以领取
						output.writeByte(1);
					}
				}else {
					output.writeByte(0);
				}
			}
			//当日充值礼包20006-20008
			String dailyRecharge = hero.getDailyRecharge();
			String[] split = dailyRecharge.split(",");
			int length = split.length;
			for (int i = 0; i < length; i++) {
				String[] split2 = split[i].split("_");
				int id = Integer.parseInt(split2[0]);
				int status = Integer.parseInt(split2[4]);
				output.writeShort(id);
				if (status==1) {
					output.writeByte(1);
				}else {
					output.writeByte(0);
				}
			}
			//邀请好友 20009-20012
			String inviteFriend = hero.getInviteFriend();
			int inviteNums = 0;
			if (inviteFriend==null || inviteFriend.equals("")) {
				for (int i = 20009; i <=20012; i++) {
					output.writeShort(i);
					output.writeByte(0);
				}
			}else {
				String[] split2 = inviteFriend.split(",");//	hero.setInviteFriend("1,20009_1_50_1");//
				inviteNums = Integer.parseInt(split2[0]);
				for (int i = 20009; i <=20012; i++) {//
					pattern = Pattern.compile(i+"_\\d*_\\d*_\\d");
					matcher = pattern.matcher(split2[1]);
					output.writeShort(i);
					if (matcher.find()) {
						String group = matcher.group();
						List<Integer> list = SysUtil.splitGetInt(group, "_");
						output.writeByte(list.get(1));
					}else {
						output.writeByte(0);
					}
				}
			}
			
			output.writeShort(hero.getOnlineTime()/2);
			output.writeInt(hero.getCharge());
			output.writeShort(inviteNums);//好友邀请个数
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}

	/**
	 * 
	 * @param connection
	 * @param hero
	 * @throws IOException
	 */
	public void open(Connection connection, Hero hero) {
		try {
			int currentTime = TimeUtil.currentTime();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(7002);
			output.writeShort(13);
			//首冲奖励
			output.writeShort(20000);
			int firstPayStatus = 0;
			if (hero.getFirstPay()==1) {//已经完成首冲没有领取过奖励
				firstPayStatus = 1;
			}
			output.writeByte(firstPayStatus);
			
			//每日登陆奖励
			output.writeShort(20001);
			int daily = hero.getDaily();
			int dailyStatus = 0;
			if (daily==0) {
				dailyStatus = 1;
			}else {
				int dayBetween = TimeUtil.getDayBetween(daily, currentTime);
				if (dayBetween>0) {//当天领取过了
					dailyStatus = 1;
				}
			}
			output.writeByte(dailyStatus);
			//连续登陆奖励
			HashMap<Integer, ConDays> map = hero.getDaysAward();
			for (int i = 20002; i <= 20006; i++) {
				output.writeShort(i);
				if (map==null) {
					output.writeByte(0);
				}else {
					ConDays conDays = map.get(i);
					int conDaysStatus = 0;//20002活动奖励领取状态
					if (conDays != null) {//已经达成
						int time = conDays.getTime();
						int dayBetween = TimeUtil.getDayBetween(time,currentTime);
						if (dayBetween == 0) {//今天达成
							int get = conDays.getGet();
							if (get != 1){
								conDaysStatus = 1;
							}
						}
					}
					output.writeByte(conDaysStatus);
				}
			}
			
			//累计在线时长奖励
			String dailyOnline = hero.getDailyOnline();//
			if (dailyOnline==null || dailyOnline.equals("")) {
				output.writeShort(20007);
				output.writeByte(0);
				output.writeShort(20008);
				output.writeByte(0);
				output.writeShort(20009);
				output.writeByte(0);
			}else {
				Pattern pattern = null;
				Matcher matcher = null;
				for (int i = 20007; i <= 20009; i++) {
					pattern = Pattern.compile(i+"_\\d*_\\d*_\\d");
					matcher = pattern.matcher(dailyOnline);
					output.writeShort(i);
					if (matcher.find()) {
//						1可以领取，0不能领取 ,2已经领取
						String group = matcher.group();
						String[] split = group.split("_");
						int parseInt = Integer.parseInt(split[3]);//0 可以领    2 领取过
						if (parseInt==2) {//已领取，不能再领取
							output.writeByte(0);
						}else {//未领取，可以领取
							output.writeByte(1);
						}
					}else {
						output.writeByte(0);
					}
				}
			}
			//累计每日充值奖励
			//20010_50_2_18000_0,id_query_type_num_status,id_query_type_num_status,    
//			type:2铜币，1元宝， status：0不能领取，1可领取未领取，2已领取
			String dailyRecharge = hero.getDailyRecharge();
			String[] split = dailyRecharge.split(",");
			int length = split.length;
			for (int i = 0; i < length; i++) {
				String[] split2 = split[i].split("_");
				int id = Integer.parseInt(split2[0]);
				int status = Integer.parseInt(split2[4]);
				output.writeShort(id);
				if (status==1) {
					output.writeByte(1);
				}else {
					output.writeByte(0);
				}
			}
			output.writeShort(hero.getOnlineTime()/2);
			output.writeInt(hero.getCharge());
			output.writeShort(hero.getDays());
			connection.write(bos.toByteArray());
			bos.close();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 领取连续登陆奖励
	 * @param hero
	 */
	public int getContinuous(Hero hero, int id, Connection connection) {
		HashMap<Integer, ConDays> daysAward = hero.getDaysAward();
		if (daysAward!=null && daysAward.containsKey(id)) {
			int currentTime = TimeUtil.currentTime();
			ConDays conDays = daysAward.get(id);
			int get = conDays.getGet();//是否领取
			int time = conDays.getTime();//达成时间
			int dayBetween = TimeUtil.getDayBetween(time, currentTime);
			if (dayBetween==0 && get == 0) {//今天达成，并且未领取奖励
				int copper = conDays.getCopper();
				int money = conDays.getMoney();
				if (copper>0) {
					HeroHandler.getHeroHandler().giveCopper(copper, connection);
				}
				if (money>0) {
					MoneyControl.moneyIncome(hero, Back_record.REASON_ACTIVITY, money);
					HeroHandler.getHeroHandler().send1011(1, hero.getMoney(),0, connection);
				}
				conDays.setGet(1);
				daysAward.put(conDays.getId(), conDays);
				hero.setDaysAward(daysAward);
				
				return 1;
			}
		}
		return 2;
	}
	
	/**
	 * 领取每日登陆奖励
	 * @return 1 领取成功，2 领取失败
	 * @param hero
	 */
	public int getDaily(Hero hero, Connection connection){
		try {
			int currentTime = TimeUtil.currentTime();
			boolean canGet = false;//是否能够领取
			int daily = hero.getDaily();
			if (daily==0) {
				canGet = true;
			}else {
				int dayBetween = TimeUtil.getDayBetween(daily, currentTime);
				if (dayBetween>0) {
					canGet=true;
				}
			}
			if (canGet) {
				HeroHandler.getHeroHandler().giveCopper(5000, connection);
				hero.setDaily(currentTime);
				hero.setLoginTimes(hero.getLoginTimes()+1);
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return 2;
	}
	/**
	 * 领取每日累计在线奖励
	 * @return
	 */
	public int getOnlineTime(Hero hero, Connection connection, int id) {
		try {
			String dailyOnline = hero.getDailyOnline();
			Pattern pattern = Pattern.compile(id+"_\\d_\\d*_0");//20007_2_2000_0
			Matcher matcher = pattern.matcher(dailyOnline);
			if (matcher.find()) {
				String group = matcher.group();
				String[] split = group.split("_");//20007_2_2000_0
				int type = Integer.parseInt(split[1]);
				int num = Integer.parseInt(split[2]);
				if (type==1) {
					MoneyControl.moneyIncome(hero, Back_record.REASON_ACTIVITY, num);
					HeroHandler.getHeroHandler().send1011(1, hero.getMoney(),0, connection);
				}else if (type==2) {
					HeroHandler.getHeroHandler().giveCopper(num, connection);
				}else {
					return 2;
				}
				StringBuffer sb = new StringBuffer();
				sb.append(split[0]).append("_");
				sb.append(split[1]).append("_");
				sb.append(split[2]).append("_2");
				String replace = dailyOnline.replace(group, sb.toString());
				hero.setDailyOnline(replace);
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
		return 2;
	}
	/**
	 * 领取累计充值奖励
	 * @param hero
	 * @param connection
	 * @param id
	 * @return 
	 */
	public int getChargeAward(Hero hero, Connection connection, int id) {
		try {
			//20010_50_2_18000_0,id_query_type_num_status,id_query_type_num_status,    type:2铜币，1元宝， status：0不能领取，1可领取未领取，2已领取
			String dailyRecharge = hero.getDailyRecharge();
			Pattern pattern = Pattern.compile(id+"_\\d*_\\d_\\d*_1");//
			Matcher matcher = pattern.matcher(dailyRecharge);
			if (matcher.find()) {
				String group = matcher.group();
				String[] split = group.split("_");//20007_2_2000_0
				int type = Integer.parseInt(split[2]);
				int num = Integer.parseInt(split[3]);
				if (type==1) {
					MoneyControl.moneyIncome(hero, Back_record.REASON_ACTIVITY, num);
					HeroHandler.getHeroHandler().send1011(1, hero.getMoney(),0, connection);
				}else if (type==2) {
					HeroHandler.getHeroHandler().giveCopper(num, connection);
				}else {
					return 2;
				}
				StringBuffer sb = new StringBuffer();
				sb.append(split[0]).append("_");
				sb.append(split[1]).append("_");
				sb.append(split[2]).append("_");
				sb.append(split[3]).append("_2");
				String replace = dailyRecharge.replace(group, sb.toString());
				hero.setDailyRecharge(replace);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return 2;
	}
	/**
	 * 2013.08.05 - 2013.09.05   期间签到活动奖励发放，
	 * 
	 */
	public void activity() {
		String currentDate = TimeUtil.currentDate("yyyy-MM-dd");
		if (currentDate.equals("2013-09-17")) {
			try {
				HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
				List<Hero> list = heroManager.getByLoginTimes();
				if (list!=null) {
					Iterator<Hero> iterator = list.iterator();
					while (iterator.hasNext()) {
						Hero hero = iterator.next();
						int hid = hero.getId();
						int loginTimes = hero.getLoginTimes();
//						签到7次：奖励188元宝
//						签到15次：奖励488元宝
//						签到25次：奖励888元宝
//						签到30次：奖励1588元宝
						int money = 0;
						if (loginTimes>=7 && loginTimes<15) {
							money = 188;
						}else if (loginTimes>=15 && loginTimes<25) {
							money = 488;
						}else if (loginTimes>=25 && loginTimes<30) {
							money = 888;
						}else if (loginTimes>30) {
							money = 1588;
						}
						
						if (money>0) {
							boolean containsKey = GlobalMap.getHeroMap().containsKey(hid);
							if (containsKey) {//在线
								hero = GlobalMap.getHeroMap().get(hid);
								MoneyControl.moneyIncome(hero, Back_record.REASON_ACTIVITY, money);
								Connection connection = GlobalMap.getConns().get(hid);
								if (connection!=null) {
									connection.write(SysUtil.getBytes(1018, (byte) 1));//充值成功
								}
							}else {//不在线
								MoneyControl.moneyIncome(hero, Back_record.REASON_ACTIVITY, money);
								heroManager.update(hero);
							}
						}
						logger.info("activity: give loginTimes award"+hid);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("", e);
			}
		}else {
			logger.info("activity: give loginTimes award ，wait currentDate = "+ currentDate);
		}
	}
	
	
	public void getVip(Object arg0,Object arg1) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (arg0!=null) {
				connection = (Connection) arg0;
			}
			if (arg1!=null) {
				bytes = (byte[]) arg1;
			}
			if (connection!=null && bytes !=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				
				vipMessage(connection, hero);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}

	public void vipMessage(Connection connection, Hero hero) {
		try {
			int endCardTime = hero.getEndCardTime();
			int currentTime = TimeUtil.currentTime();
			int dayBetween = TimeUtil.getDayBetween(currentTime, endCardTime);
			if (endCardTime>=0&&dayBetween>0) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(7013);
				output.writeByte(dayBetween);
				int cardDailyAward = hero.getCardDailyAward();
				int day = TimeUtil.getDayBetween(cardDailyAward, currentTime);
				if (day>0) {
					output.writeByte(1);
				}else {
					output.writeByte(0);
				}
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
			}else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(7013);
				output.writeByte(0);
				output.writeByte(0);
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 领取vip每日元宝
	 * @param arg0
	 * @param arg1
	 */
	public void getVipDailyAward(Object arg0,Object arg1) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (arg0!=null) {
				connection = (Connection) arg0;
			}
			if (arg1!=null) {
				bytes = (byte[]) arg1;
			}
			if (connection!=null && bytes !=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				
				int endCardTime = hero.getEndCardTime();
				if (endCardTime>=0) {
					int currentTime = TimeUtil.currentTime();
					int dayBetween = TimeUtil.getDayBetween(currentTime, endCardTime);
					int cardDailyAward = hero.getCardDailyAward();
					int day = TimeUtil.getDayBetween(cardDailyAward, currentTime);
					if (dayBetween>0 && day>0) {
						MoneyControl.moneyIncome(hero, Back_record.REASON_VIP, 50);
						hero.setCardDailyAward(currentTime);
						connection.write(BattleHandler.getTestHandler().getByte(1017, hero.getMoney()));
						connection.write(SysUtil.getBytes(7015, (byte)1));
						vipMessage(connection, hero);
					}else {
						connection.write(SysUtil.getBytes(7015, (byte)0));
					}
				}else {
					connection.write(SysUtil.getBytes(7015, (byte)0));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}
