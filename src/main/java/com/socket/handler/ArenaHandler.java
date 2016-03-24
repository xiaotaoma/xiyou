package com.socket.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Arena;
import com.model.Hero;
import com.model.Report;
import com.model.backstage.Back_tool;
import com.model.sys.ArenaMonster;
import com.model.sys.SysGloablMap;
import com.service.ArenaManager;
import com.service.HeroManager;
import com.service.ReportManager;
import com.sysData.map.Loadmap;
import com.util.InitStaticData;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArenaHandler {
	private static Logger logger = LoggerFactory.getLogger(ArenaHandler.class);
	
	private static ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
	private static ArenaHandler arenaHandler;
	public static ArenaHandler getArenaHandler() {
		if (arenaHandler ==null) {
			arenaHandler = new ArenaHandler();
		}
		return arenaHandler;
	}
	/**
	 * 竞技场地图id
	 */
	public Arena initArena(int hid, Hero hero) {
		try {
			Integer maxRank = arenaManager.getMaxRank();
			if (maxRank==null) {
				maxRank=0;
			}
			Arena arena = new Arena();
			arena.setHid(hid);
			arena.setRank(maxRank+1);
			arena.setPrestige(0);
			arena.setRetinue(6);
			arena.setStrength(hero.getStrength());
			arena.setTime(TimeUtil.currentTime());
			arena.setTimes(5);
			arena.setName(hero.getName());
			arena.setAchievement("");
			arenaManager.insert(arena);
			HeroHandler.getHeroHandler().setTreeCopper(hid, hero,arena.getRank());
			return arena;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return null;
		}
	}
	
	/**
	 * A挑战B
	 */
	public void battle(Object obj1, Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (bytes!=null && connection!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				bis.close();
				input.close();
				int otherHid = input.readInt();
				Object hidObj = connection.getAttributes().getAttribute("hid");
				if (hidObj!=null) {
					int hid = (Integer) hidObj;
					if (otherHid == hid) {
						return;
					}
					battle(hid, otherHid, connection);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param hid
	 * @param otherHid
	 * @param connection
	 */
	public void battle(int hid,int otherHid,Connection connection) {
		try {
			Hero hero = GlobalMap.getHeroMap().get(hid);
			if (hero==null) {
				return;
			}
			ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
			Arena arena = arenaManager.getByHid(hid);
			Arena otherArena = arenaManager.getByHid(otherHid);
			if (arena==null || otherArena==null) {
				return;
			}
			/**---------------------------挑战次数---------------------------**/
			setChanllgeTimes(arena);
			int times = arena.getTimes();
			if (times<=0) {//没有挑战次数
				SysUtil.warning(connection, Globalconstants.NOARENATIMES);
				return;
			}
			/***-----------------------扣次数-----------------------***/
			arena.setTimes(arena.getTimes()-1);
			if (arena.getTimes()==4) {
				arena.setTime(TimeUtil.currentTime());
			}
			
			sendArenaTime(connection, arena, TimeUtil.currentTime());
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2010);
			Object[] array = Loadmap.getSysMap().keySet().toArray();
			Random random = new Random();
			int mapId = Integer.parseInt(array[random.nextInt(array.length)].toString());
			output.writeShort(mapId);
			
			hero.setWave(new StringBuffer().append(mapId).append(",").append(0).append(",").append(otherHid).toString());
			/**
			 * 每日任务 任意挑战一次竞技场
			 */
			String challengeTimes = hero.getChallengeTimes();
			if (challengeTimes.equals("")) {
				hero.setChallengeTimes("1_0");
			}else {
				String[] split = challengeTimes.split("_");
				hero.setChallengeTimes(Integer.parseInt(split[0])+1+"_"+split[1]);
			}
			
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
			arenaManager.update(arena);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 匹配玩家
	 */
	public void match(Object obj1, Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		if (obj2!=null) {
			bytes = (byte[]) obj2;
		}
		if (obj1!=null) {
			connection = (Connection) obj1;
		}
		try {
			if (connection!=null && bytes!=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int strength = hero.getStrength();
				
				List<Arena> match = arenaManager.getMatch(strength);
				if (match == null || match.size()==1) {//没有匹配到
					SysUtil.warning(connection, Globalconstants.NOMATCHED);
				}else {
					Random random = new Random();
					int nextInt = random.nextInt(match.size());
					Arena arena = match.get(nextInt);
					if (arena.getHid() == hid) {
						match.remove(arena);
						nextInt = random.nextInt(match.size());
						arena = match.get(nextInt);
					}
					int rankByHid = arenaManager.getRankByHid(arena.getHid());
					
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(2005);
					output.writeInt(rankByHid);
					output.writeInt(arena.getHid());
					output.writeUTF(arena.getName());
					output.writeInt(arena.getPrestige());
					output.writeInt(arena.getStrength());
					output.writeInt(arena.getRetinue());
					bos.close();
					output.close();
					connection.write(bos.toByteArray());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public boolean setChanllgeTimes(Arena arena) {
		boolean change = false;
		int currentTime = TimeUtil.currentTime();
		int time = arena.getTime();
		int times = arena.getTimes();
		if (times>=Globalconstants.ARENATIMES) {
			arena.setTime(currentTime);
			change = true;
		}else {
			int n = (currentTime - time)/3600;
			int m = (currentTime - time)%3600;
//			System.out.println("currentTime - time="+(currentTime - time));
//			System.out.println("n = "+n+"   m = "+m);
			if (n>0) {
				times+=n;
				if (times>=Globalconstants.ARENATIMES) {
					arena.setTime(currentTime);
					arena.setTimes(Globalconstants.ARENATIMES);
				}else {
					arena.setTimes(times);
					arena.setTime(currentTime-m);
				}
				change = true;
			}
		}
		logger.info("setChanllgeTimes  name:"+arena.getName()+"  arena Time:"+ TimeUtil.formatTime(arena.getTime())+"  times:"+arena.getTimes());
//		System.out.println("setChanllgeTimes  name:"+arena.getName()+"  arena Time:"+TimeUtil.formatTime(arena.getTime())+"  times:"+arena.getTimes());
		return change;
	}
	
	public void sendChanglleTimes(Arena arena, Connection connection) {
		try {
			boolean setChanllgeTimes = setChanllgeTimes(arena);
			if (setChanllgeTimes) {
				arenaManager.update(arena);
			}
			sendArenaTime(connection, arena, TimeUtil.currentTime());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void sendRank(int rank,Connection connection) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2007);
			output.writeInt(rank);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void sendPre(int prestige,Connection connection) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2008);
			output.writeInt(prestige);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public static List<Integer> boss = new ArrayList<Integer>();
	public static List<Integer> list = new ArrayList<Integer>();
	public static List<Integer> monsters = new ArrayList<Integer>();
	public static int waveAdd = 300;
	public static int maxMonsterConfi = 0;
	public static final String [] MONSTER_STRINGS = {"10000","10001","10003","10004",
		"10005","10006","10008","10009","10011","10012","10014","10015","10017","10019",
		"10020","10022","10024","10026","10027","10029","10030","10032","10033","10036",
		"10038","10039","10041","10042","10044","10046","10048","10049","10052","10054",
		"10055","10057","10058","10062","10064","10068","10069","10070","10072","10074",
		"10075"};
	
	/**
	 * 客户端
	 */
	public void waveData(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (connection!=null && bytes!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int wave = input.readInt();//第几波怪
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
				String waveData = hero.getWave();
				if (waveData!=null && !waveData.equals("")) {//已经在打了，返回下一波信息
					List<Integer> list = SysUtil.splitGetInt(waveData, ",");
					Integer integer = list.get(1);
					if (integer==wave) {
						if (wave+1>100) {
							String monsters = getMonsters(wave+1);
							if (monsters!=null && !monsters.equals("")) {
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								DataOutputStream output = new DataOutputStream(bos);
								output.writeInt(2012);
								output.writeInt(wave+1);
								System.out.println((wave+1)+"\t"+monsters);
								output.writeUTF(monsters);
								bos.close();
								output.close();
								connection.write(bos.toByteArray());
								hero.setWave(new StringBuffer().append(list.get(0)).append(",").append(wave+1).append(",").append(list.get(2)).toString());
								if (wave>hero.getMaxWave()) {
									hero.setMaxWave(wave);
								}
								giveWaveAward(wave, hero);
							}
						}else {
							String data = SysGloablMap.getWaveMap().get(wave+1);
							if (data!=null && !data.equals("")) {
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								DataOutputStream output = new DataOutputStream(bos);
								output.writeInt(2012);
								output.writeInt(wave+1);
								output.writeUTF(data);
								bos.close();
								output.close();
								connection.write(bos.toByteArray());
								hero.setWave(new StringBuffer().append(list.get(0)).append(",").append(wave+1).append(",").append(list.get(2)).toString());
								if (wave>hero.getMaxWave()) {
									hero.setMaxWave(wave);
								}
								giveWaveAward(wave, hero);
							}else {
								logger.info("data == null");
							}
						}
					}else {
//						System.err.println(2+"波数不对："+hid);
					}
				}else {
//					System.err.println(1+"没有选择地图："+hid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 赠送波数礼包
	 * 91版本
	 */
	private void giveWaveAward(int wave,Hero hero) {
		if (SysGloablMap.getAwardMap().containsKey(wave) &&
				(Globalconstants.VERSION.equals(Globalconstants.VERSION_91)||Globalconstants.VERSION.equals(Globalconstants.VERSION_APPLESTORE))) {
			int toolId = SysGloablMap.getAwardMap().get(wave);
			String award = hero.getAward();
			Pattern pattern = Pattern.compile("_"+toolId);
			Matcher matcher = pattern.matcher(award);
			if (!matcher.find()) {
				BagHandler.getBagHandler().giveTools(toolId, 1, hero.getId(), Back_tool.REASON_GIFT);
				award = award+"_"+toolId;
			}
			hero.setAward(award);
		}
	}
	
	public String getMonsters(int wave) {
		int n = 5;//100波以后并行数5
		ArenaMonster monster = new ArenaMonster();
		monster.setWave(wave);
		monster.setBoss(boss);
		monster.setHide(2);
		
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		monster.setList(list);
		monster.setMonsterNum(40);
		monster.setMonsters(monsters);
		monster.setRelife(2);
		monster.setCoefficient((wave-100)*waveAdd+maxMonsterConfi);
		monster.setBossCoeffi((int) (monster.getCoefficient()*10));
		ArenaMonster arenaMonster = SysGloablMap.getMonsterMap().get(wave-1);
		if (arenaMonster==null) {
			return null;
		}
		int copper = arenaMonster.getCopper();
		monster.setCopper((int) (copper+monster.getCoefficient()*5));
		String s = InitStaticData.getMonster(monster);
		SysGloablMap.getWaveMap().put(wave, s);
		SysGloablMap.getMonsterMap().put(wave, monster);
		return s;
	}
	
	
	public static void main(String[] args) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.SECOND, 0);
//		calendar.set(Calendar.MINUTE, 0);calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//		System.out.println(calendar.getTimeInMillis()/1000);
		
		HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
		try {
			List<Integer> splitGetInt = SysUtil.splitGetInt("17,24,30,10,22,16,9,15,29,25,13,14,8,11,18,19,12,20,21,23,26,28,27,31", ",");
			HashMap<Integer, Integer> treeCopper = heroManager.getTreeCopper(splitGetInt);
			System.out.println(treeCopper);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	private int getMid(Random random, List<Integer> monsters) {
		return monsters.get(random.nextInt(monsters.size()));
	}
	/**
	 * 
	 */
	public void battleResult(Object obj1,Object obj2) {/*
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (connection!=null && bytes!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int wave = input.readInt();
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
				
				String waveData = hero.getWave();
				if (waveData==null || waveData.equals("")) {
					return;
				}
				List<Integer> list = SysUtil.splitGetInt(waveData,",");
				int mapId = list.get(0);
				int otherHid = list.get(2);
				
				//自己
				Arena arena = arenaManager.getByHid(hid);
				String a1 = arena.getAchievement();
				int retinue = arena.getRetinue();
				
				//对方
				Arena arena2 = arenaManager.getByHid(list.get(2));
				int tRetinue = arena2.getRetinue();
				String a2 = arena2.getAchievement();
				int thid = arena2.getHid();
				String s = new StringBuffer().append("\\[").append(mapId).append("\\,\\d*]").toString();
				
				boolean rankChange = false;
				//胜负
				int win = 0;
				//和对方比
				Pattern pattern =Pattern.compile(s);
				Matcher matcher = pattern.matcher(a2);
				if (matcher.find()) {
					String group = matcher.group();
					String[] split = group.split(",");
					int wa = Integer.parseInt(split[1].substring(0, split[1].length()-1));
					if (wa>wave) {//输
						win = 1;
					}else if (wa == wave) {//平
						win = 3;
					}else if (wa < wave){//赢
						win = 3;
					}
				}else {
					win = 3;
				}
				System.out.println("对方成绩："+a2+"  自己本次成绩："+wave+" win:"+win);
				//和自己比
				matcher = pattern.matcher(a1);
				if (matcher.find()) {
					String group = matcher.group();
					String[] split = group.split(",");
					int wa = Integer.parseInt(split[1].substring(0, split[1].length()-1));
					if (wave>wa) {//好于上次的成绩
						String ss = new StringBuffer().append("[").append(mapId).append(",").append(wave).append("]").toString();
						a1 = a1.replace(group, ss);
						arena.setAchievement(a1);
					}
				}else {
					String ss = new StringBuffer().append("[").append(mapId).append(",").append(wave).append("]").toString();
					arena.setAchievement(a1+ss);
				}
				int p1 = arena.getPrestige();
				int p2 = p1;
				int shengwang = p2;
				int winnerHid = 0;
				String otherName = arena2.getName();
				int otherTid = arena2.getRetinue();
				int copper = 0;
				switch (win) {
				case 1://输
					shengwang = 1;
					p2 = p1+1; 
					winnerHid = arena2.getHid();
					arena.setPrestige(p2);
					copper = wave*100;
					break;
				case 2://平
					shengwang = 2;
					p2 = p1+2; 
					arena.setPrestige(p2);
					break;
				case 3://赢
					shengwang = 3;
					p2 = p1+3; 
					winnerHid = hero.getId();
					arena.setPrestige(p2);
					copper = wave*500;
					break;
				default:
					break;
				}
				//100波以后每波1000
				if (wave>100) {
					copper+=(wave - 100)*1000;
				}
				
				ArenaHandler.getArenaHandler().sendPre(arena.getPrestige(),connection);
				//排名更新
				HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
				String name = heroManager.getName(otherHid);
				if (win==3 && arena.getRank()>arena2.getRank()) {
					rankChange = true;
				}
				
				if (rankChange) {
					updateRank(arena, arena2);
					setReport(hero, tRetinue, thid, name,Report.TYPE_ARENA);
				}else {
					arenaManager.update(arena);
				}
				battleResultSend(otherName, otherTid, winnerHid, copper , 0 , shengwang, 0,connection);
				
				hero.setWave("");
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	*/}
	
	public void battleResultSend(String otherName,int otherTid,int winnerHid,int copper,
			int money,int shengwang,int honour,Connection connection,int robCopper) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2014);
			output.writeUTF(otherName);
			output.writeInt(otherTid);
			output.writeInt(winnerHid);
			output.writeInt(copper);
			output.writeInt(money);
			output.writeShort(shengwang);
			output.writeInt(honour);
			output.writeInt(robCopper);
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
	 * @param winner 获胜者角色信息
	 * @param loserTid 失败者徒弟id
	 * @param loserHid 失败者角色id
	 * @param loserName 失败者角色名字
	 * @param type 竞技场或徒弟pk
	 * @param connection 失败者链接
	 */
	public void setReport(Hero winner, int loserTid,
						  int loserHid, String loserName , int type, int loserStrength, int robCopper) {
		try {
			Report report = new Report();
			report.setHid(winner.getId());
			report.setName(winner.getName());
			report.setThid(loserHid);
			report.settName(loserName);
			report.setTime(TimeUtil.currentTime());
			report.setRetinue(winner.getTid());
			report.settRetinue(loserTid);
			report.setType(type);
			report.setStrength1(winner.getStrength());//获胜者战斗力
			report.setStrength2(loserStrength);
			report.setRobCopper(robCopper);
			Random random = new Random();
			int nextInt = random.nextInt(5);
			report.setDescId(nextInt+1);
			ReportManager reportManager = (ReportManager) BaseAction.getIntance().getBean("reportManager");
			reportManager.insert(report);
			
			Connection connection = GlobalMap.getConns().get(loserHid);
			if (connection!=null) {
				AccountHandler.getAccountHandler().report(connection, loserHid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	/**
	 * 
	 */
	public void open(Object obj1, Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (bytes!=null && connection!=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid!=null) {
					Hero hero = GlobalMap.getHeroMap().get(hid);
					if (hero == null) {
						return ;
					}
					Arena arena = arenaManager.getByHid(hid);
					int rank = arena.getRank();
					
				
					Integer device = (Integer) connection.getAttributes().getAttribute("device");
					if (device == null) {
						device = 0;
					}
					
					int num = 50;
					if (device == 0) {// ios

					}else if (device == 1) {//android
						num = 30;
					}else {
						
					}
					
					logger.info("num = " + num);
					List<Arena> list = null;
					if (rank<num) {
						list = arenaManager.get100(num);
					}else {
						int end = rank+10;
						int start = end-num;
						logger.info(" start="+start +"  end="+end);
						list = arenaManager.getRankList(start, end);
					}
					
					
					sendChanglleTimes(arena, connection);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(2002);
					output.writeInt(rank);
					output.writeByte(arena.getTimes());
					if (list!=null) {
						int size = list.size();
						output.writeShort(size);
						List<Integer> hids = new ArrayList<Integer>();
						for (int i = size-1; i >= 0; i--) {
							Arena a = list.get(i);
							hids.add(a.getHid());
							output.writeInt(a.getRank());//排名
							output.writeInt(a.getHid());//角色id
							output.writeUTF(a.getName());//角色名
							output.writeInt(a.getPrestige());//声望
							output.writeInt(a.getStrength());//战斗力
							output.writeInt(a.getRetinue());//随从
						}
						HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
						HashMap<Integer, Integer> treeCopper = heroManager.getTreeCopper(hids);
						if (treeCopper!=null) {
							output.writeShort(size);
							for (int i = size-1; i >= 0; i--) {
								int hid2 = list.get(i).getHid();
								Integer integer = treeCopper.get(hid2);
								if (integer!=null) {
									output.writeInt(integer);
								}else {
									output.writeInt(0);
								}
							}
						}else {
							output.writeShort(0);
						}
						
//						if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
							HashMap<Integer, Integer> endCardTime = heroManager.getEndCardTime(hids);
							int currentTime = TimeUtil.currentTime();
							if (endCardTime!=null) {
								output.writeShort(size);
								for (int i = size-1; i >= 0; i--) {
									int hid2 = list.get(i).getHid();
									Integer time = endCardTime.get(hid2);
									int dayBetween = TimeUtil.getDayBetween(currentTime, time);
									if (dayBetween>0) {
										output.writeByte(1);
									}else {
										output.writeByte(0);
									}
								}
							}else {
								output.writeShort(0);
							}
//						}
						
					}else {
						output.writeShort(0);
						output.writeShort(0);
//						if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
							output.writeShort(0);
//						}
					}
					bos.close();
					output.close();
					connection.write(bos.toByteArray());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 更新排名
	 * arena 排名上升
	 * arena2排名下降
	 */
	public synchronized void updateRank(Arena arena , Arena arena2) {
		try {
			//更新排名
			String achievement = arena.getAchievement();
			int hid = arena.getHid();
			String name = arena.getName();
			int prestige = arena.getPrestige();
			int retinue = arena.getRetinue();
			int strength = arena.getStrength();
			int time = arena.getTime();
			int times = arena.getTimes();
			
			arena.setAchievement(arena2.getAchievement());
			arena.setHid(arena2.getHid());
			arena.setName(arena2.getName());
			arena.setPrestige(arena2.getPrestige());
			arena.setRetinue(arena2.getRetinue());
			arena.setStrength(arena2.getStrength());
			arena.setTime(arena2.getTime());
			arena.setTimes(arena2.getTimes());
			
			arena2.setAchievement(achievement);
			arena2.setHid(hid);
			arena2.setName(name);
			arena2.setPrestige(prestige);
			arena2.setRetinue(retinue);
			arena2.setStrength(strength);
			arena2.setTime(time);
			arena2.setTimes(times);
			
			arenaManager.update(arena, arena2);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 战报
	 * @param obj1
	 * @param obj2
	 */
	public void sendReport(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (bytes!=null && connection!=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				AccountHandler.getAccountHandler().report(connection, hid);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 竞技场挑战次数，时间刷新，
	 * @param connection
	 * @param arena
	 * @param currentTime
	 * @throws IOException
	 */
	private void sendArenaTime(Connection connection, Arena arena,
			int currentTime) throws IOException {
		int times = arena.getTimes();
		int time = 0;
		if (times==5) {
			
		}else {
			time = arena.getTime()+3600 - currentTime;
			if (time>3600 || time<0) {
				time = 0;
			}
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(bos);
		output.writeInt(2006);
		output.writeByte(times);
		output.writeShort(time);
		bos.close();
		output.close();
		connection.write(bos.toByteArray());
	}
	
}
