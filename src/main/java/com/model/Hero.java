package com.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Hero {
	public Hero() {
		// TODO Auto-generated constructor stub
	}
	private int id;
	private String name;//角色名称
	private int money;//充值元宝
	private int level;//徒弟id
	private int accId;//账号id
	private String account;//账号
	private int treeTime;//摇钱树时间
	private int createTime;//角色创建时间
	private int loginTime;//登陆时间
	private int logoutTime;//登出时间
	private int strength;//战斗力
	private int tid;//徒弟id
	private int rid;//roomid房间id
	private String wave;//竞技场波数,断线重连
	private int honour;//荣誉
	private int times;//徒弟大战次数
	private int honorTime;//荣誉恢复时间
	private int buyTimes;//购买战斗次数
	private int treeCopper;//摇钱树铜币
	private int treeMoney;//摇钱树元宝
	private String tids;//解锁过的徒弟
	
	private int firstPay;//第一次充值 ,  0 没有充值过，1已经完成首冲没有领取过奖励，2完成首冲并领取过奖励
	private int daily;//每日奖励，记录领取奖励时间
	
	private int days;//连续登陆天数
	private int months;//连续登陆的月份,不同月份的连续登陆天数不累计
	private HashMap<Integer, ConDays> daysAward;//连续登陆奖励
	private String conAward;//连续登陆奖励
	
	private int onlineTime;//当天累计在线时间(以分钟为单位)
	private String dailyOnline;//累计在线奖励
	
	private int charge;//累计每日充值数
	private String dailyRecharge;//每日累计充值，
	private int resetTime;//充值数据时间
	private int copper;//不在线时赠送铜币
	private int loginTimes;//2013.08.05 - 2013.09.05   期间签到次数
	private String firstDouble;//首次充值项目双倍奖励             1_2_3
	private int expOverTime;//经验累积到期时间
	private int exp;//累积经验
	private String award;//竞技场波数礼包
	private int maxWave;// 竞技场最大波数
	
	//-----------台湾------------//
	private String equipUpTimes;//每日提升装备次数 times_status  0 已达成，1未达成，2已领取
	private String challengeTimes;//每日竞技场挑战次数 times_status  0 已达成，1未达成，2已领取
	private String winTimes;//每日竞技场获胜次数 times_status  0 已达成，1未达成，2已领取
	private String buyToolsTimes;//每日光顾商城次数 times_status  0 已达成，1未达成，2已领取
	private String shakeTimes;//每日摇钱次数 times_status  0 已达成，1未达成，2已领取
	private String fishmenAward;//创建角色1-15天，每次登陆领取50+5*times  50_times_time  领取50 ，还未领取
	//-----------台湾------------//
	private int robedCopper;//累积抢夺铜币
	private String inviteFriend;//times,id_status-id_status
	private int invite;//
	private String terrace;//
	private int endCardTime;//月卡结束时间
	private int cardDailyAward;//月卡的每日奖励
	
	public String getEquipUpTimes() {
		return equipUpTimes;
	}
	public void setEquipUpTimes(String equipUpTimes) {
		this.equipUpTimes = equipUpTimes;
	}
	public String getChallengeTimes() {
		return challengeTimes;
	}
	public void setChallengeTimes(String challengeTimes) {
		this.challengeTimes = challengeTimes;
	}
	public String getWinTimes() {
		return winTimes;
	}
	public void setWinTimes(String winTimes) {
		this.winTimes = winTimes;
	}
	public String getBuyToolsTimes() {
		return buyToolsTimes;
	}
	public void setBuyToolsTimes(String buyToolsTimes) {
		this.buyToolsTimes = buyToolsTimes;
	}
	public String getShakeTimes() {
		return shakeTimes;
	}
	public void setShakeTimes(String shakeTimes) {
		this.shakeTimes = shakeTimes;
	}
	/**
	 * 50_times_time
	 * @return
	 */
	public String getFishmenAward() {
		return fishmenAward;
	}
	/**
	 * 50_times_time
	 * @param fishmenAward
	 */
	public void setFishmenAward(String fishmenAward) {
		this.fishmenAward = fishmenAward;
	}
	public int getDaily() {
		return daily;
	}
	public void setDaily(int daily) {
		this.daily = daily;
	}
	public String getDailyOnline() {
		return dailyOnline;
	}
	public void setDailyOnline(String dailyOnline) {
		this.dailyOnline = dailyOnline;
	}
	public String getDailyRecharge() {
		return dailyRecharge;
	}
	public void setDailyRecharge(String dailyRecharge) {
		this.dailyRecharge = dailyRecharge;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
	public void setAccId(int accId) {
		this.accId = accId;
	}
	public int getAccId() {
		return accId;
	}
	public void setTreeTime(int treeTime) {
		this.treeTime = treeTime;
	}
	public int getTreeTime() {
		return treeTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setLogoutTime(int logoutTime) {
		this.logoutTime = logoutTime;
	}
	public int getLogoutTime() {
		return logoutTime;
	}
	public void setLoginTime(int loginTime) {
		this.loginTime = loginTime;
	}
	public int getLoginTime() {
		return loginTime;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getStrength() {
		return strength;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getRid() {
		return rid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getTid() {
		return tid;
	}
	public void setWave(String wave) {
		this.wave = wave;
	}
	public String getWave() {
		return wave;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getTimes() {
		return times;
	}
	public void setHonorTime(int honorTime) {
		this.honorTime = honorTime;
	}
	public int getHonorTime() {
		return honorTime;
	}
	public void setHonour(int honour) {
		this.honour = honour;
	}
	public int getHonour() {
		return honour;
	}
	public void setBuyTimes(int buyTimes) {
		this.buyTimes = buyTimes;
	}
	public int getBuyTimes() {
		return buyTimes;
	}
	public void setTreeCopper(int treeCopper) {
		this.treeCopper = treeCopper;
	}
	public int getTreeCopper() {
		return treeCopper;
	}
	public void setTreeMoney(int treeMoney) {
		this.treeMoney = treeMoney;
	}
	public int getTreeMoney() {
		return treeMoney;
	}
	public void setTids(String tids) {
		this.tids = tids;
	}
	public String getTids() {
		return tids;
	}
	public void setFirstPay(int firstPay) {
		this.firstPay = firstPay;
	}
	public int getFirstPay() {
		return firstPay;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getDays() {
		return days;
	}
	public void setConAward(String conAward) {
		if (conAward!=null && !"".equals(conAward)) {
			String[] split = conAward.split(",");
			int length = split.length;
			HashMap<Integer, ConDays> map = new HashMap<Integer, ConDays>();
			for (int i = 0; i < length; i++) {
				ConDays conDays = new ConDays(split[i]);
				map.put(conDays.getId(), conDays);
			}
			this.daysAward = map;
		}
		this.conAward = conAward;
	}
	public String getConAward() {
		return conAward;
	}
	public void setDaysAward(HashMap<Integer, ConDays> daysAward) {
		StringBuffer sb = new StringBuffer();
		if (daysAward!=null) {
			Iterator<Entry<Integer, ConDays>> iterator = daysAward.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, ConDays> next = iterator.next();
				ConDays value = next.getValue();
				sb.append(value.toString()).append(",");
			}
		}
		this.conAward = sb.toString();
		this.daysAward = daysAward;
	}
	public HashMap<Integer, ConDays> getDaysAward() {
		return daysAward;
	}
	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}
	public int getOnlineTime() {
		return onlineTime;
	}
	public void setCharge(int charge) {
		this.charge = charge;
	}
	public int getCharge() {
		return charge;
	}
	public void setResetTime(int resetTime) {
		this.resetTime = resetTime;
	}
	public int getResetTime() {
		return resetTime;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	public int getCopper() {
		return copper;
	}
	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}
	public int getLoginTimes() {
		return loginTimes;
	}
	public void setFirstDouble(String firstDouble) {
		this.firstDouble = firstDouble;
	}
	public String getFirstDouble() {
		return firstDouble;
	}
	public void setMonths(int months) {
		this.months = months;
	}
	public int getMonths() {
		return months;
	}
	public void setExpOverTime(int expOverTime) {
		this.expOverTime = expOverTime;
	}
	public int getExpOverTime() {
		return expOverTime;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getExp() {
		return exp;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getAward() {
		return award;
	}
	public void setMaxWave(int maxWave) {
		this.maxWave = maxWave;
	}
	public int getMaxWave() {
		return maxWave;
	}
	public void setRobedCopper(int robedCopper) {
		this.robedCopper = robedCopper;
	}
	public int getRobedCopper() {
		return robedCopper;
	}
	public void setInviteFriend(String inviteFriend) {
		this.inviteFriend = inviteFriend;
	}
	public String getInviteFriend() {
		return inviteFriend;
	}
	public void setInvite(int invite) {
		this.invite = invite;
	}
	public int getInvite() {
		return invite;
	}
	public void setTerrace(String terrace) {
		this.terrace = terrace;
	}
	public String getTerrace() {
		return terrace;
	}
	public void setEndCardTime(int endCardTime) {
		this.endCardTime = endCardTime;
	}
	public int getEndCardTime() {
		return endCardTime;
	}
	public void setCardDailyAward(int cardDailyAward) {
		this.cardDailyAward = cardDailyAward;
	}
	public int getCardDailyAward() {
		return cardDailyAward;
	}
	
}
