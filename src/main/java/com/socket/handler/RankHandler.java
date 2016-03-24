package com.socket.handler;

import com.cache.GlobalMap;
import com.main.BaseAction;
import com.model.Arena;
import com.model.Hero;
import com.service.ArenaManager;
import com.service.HeroManager;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RankHandler {
	private static Logger logger = LoggerFactory.getLogger(RankHandler.class);
	
	private static RankHandler rankHandler;
	public static RankHandler getRankHandler() {
		if (rankHandler==null) {
			rankHandler = new RankHandler();
		}
		return rankHandler;
	}
	
	private static byte[] waveRankData = null;
	private static byte[] strengthRankData = null;
	private static byte[] robRankData = null;
	
	/**
	 * 每小时
	 * 荣誉排行榜刷新
	 */
	public void setRank() {
		setHonourRank();
		setWaveRank();//
		setStrengthRank();//
		setRobRank();//
	}
	
	public void setHonourRank() {
		try {
			CopyOnWriteArrayList<Hero> honourRank = GlobalMap.getHonourRank();
			CopyOnWriteArrayList<Integer> honourHidRank = GlobalMap.getHonourHidRank();
			HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
			List<Hero> list = heroManager.getHonourRank();
			if (list!=null) {
				honourHidRank.clear();
				honourRank.clear();
				int size = list.size();
				for (int i = 0; i < size; i++) {
					Hero hero = list.get(i);
					honourHidRank.add(hero.getId());
					honourRank.add(hero);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void setWaveRank() {
		try {
			ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
			List<Arena> waveRank = arenaManager.getWaveRank();
			if (waveRank!=null) {
				int size = waveRank.size();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				
				output.writeShort(size);
//				byte[] ranks = new byte[size];
				for (int i = 0; i < size; i++) {
					output.writeByte(i+1);//排名
//					ranks[i] = (byte) (i+1);
				}
				output.writeShort(size);
				
//				int[] hids = new int[size];
				for (int i = 0; i < size; i++) {
					int hid = waveRank.get(i).getHid();
					output.writeInt(hid);
//					hids[i] = hid; 
				}
				output.writeShort(size);
//				String[] names = new String[size];
				for (int i = 0; i < size; i++) {
					String name = waveRank.get(i).getName();
					output.writeUTF(name);
//					names[i] = name;
				}
				output.writeShort(size);
//				short[] waves = new short[size];
				for (int i = 0; i < size; i++) {
					int wave = waveRank.get(i).getWave();
					output.writeShort(wave);
//					waves[i] = (short) wave;
				}
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					int retinue = waveRank.get(i).getRetinue();
					output.writeByte(retinue);
				}
				
//				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) { 
					output.writeShort(size);
					int currentTime = TimeUtil.currentTime();
					HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
					for (int i = 0; i < size; i++) {
						int hid = waveRank.get(i).getHid();
						Hero hero = GlobalMap.getHeroMap().get(hid);
						if (hero==null) {
							hero = heroManager.getHeroById(hid);
						}
						int endCardTime = hero.getEndCardTime();
						int dayBetween = TimeUtil.getDayBetween(currentTime, endCardTime);
						System.out.println(hero.getName()+"   "+dayBetween);
						if (dayBetween>0) {
							output.writeByte(1);
						}else {
							output.writeByte(0);
						}
					}
//				}else if (Globalconstants.VERSION.equals(Globalconstants.VERSION_YIDONG)) {
//					output.writeShort(size);
//					int currentTime = TimeUtil.currentTime();
//					HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
//					for (int i = 0; i < size; i++) {
//						int hid = waveRank.get(i).getHid();
//						Hero hero = GlobalMap.getHeroMap().get(hid);
//						if (hero==null) {
//							hero = heroManager.getHeroById(hid);
//						}
//						int endCardTime = hero.getEndCardTime();
//						int dayBetween = TimeUtil.getDayBetween(currentTime, endCardTime);
//						System.out.println(hero.getName()+"   "+dayBetween);
//						if (dayBetween>0) {
//							output.writeByte(1);
//						}else {
//							output.writeByte(0);
//						}
//					}
//				}
				
				bos.close();
				output.close();
				waveRankData = bos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void setRobRank() {
		try {
			HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
			List<Hero> robRank = heroManager.getRobRank();
			if (robRank!=null && robRank.size()>0) {
				int size = robRank.size();
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					output.writeByte(i+1);
				}
				
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					int hid = robRank.get(i).getId();
					output.writeInt(hid);
				}
				
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					String name = robRank.get(i).getName();
					output.writeUTF(name);
				}
				
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					int copper = robRank.get(i).getRobedCopper();
					output.writeInt(copper);
				}
				
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					int tid = robRank.get(i).getTid();
					output.writeByte(tid);
				}
//				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
					output.writeShort(size);
					int currentTime = TimeUtil.currentTime();
					for (int i = 0; i < size; i++) {
						int cardDailyAward = robRank.get(i).getEndCardTime();
						int dayBetween = TimeUtil.getDayBetween(currentTime, cardDailyAward);
						System.out.println(robRank.get(i).getName()+"   "+dayBetween);
						if (dayBetween>0) {
							output.writeByte(1);
						}else {
							output.writeByte(0);
						}
					}
//				}
				
				robRankData = bos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	
	public void setStrengthRank() {
		HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
		try {
			List<Hero> strengthRank = heroManager.getStrengthRank();
			if (strengthRank!=null) {
				int size = strengthRank.size();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					output.writeByte(i+1);//排名
				}
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					int hid = strengthRank.get(i).getId();
					output.writeInt(hid);
				}
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					String name = strengthRank.get(i).getName();
					output.writeUTF(name);
				}
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					int strength = strengthRank.get(i).getStrength();
					output.writeShort(strength);
				}
				
				output.writeShort(size);
				for (int i = 0; i < size; i++) {
					int tid = strengthRank.get(i).getTid();
					output.writeByte(tid);
				}
				
//				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
					output.writeShort(size);
					int currentTime = TimeUtil.currentTime();
					for (int i = 0; i < size; i++) {
						int cardDailyAward = strengthRank.get(i).getEndCardTime();
						int dayBetween = TimeUtil.getDayBetween(currentTime, cardDailyAward);
						if (dayBetween>0) {
							output.writeByte(1);
						}else {
							output.writeByte(0);
						}
					}
//				}else if (Globalconstants.VERSION.equals(Globalconstants.VERSION_YIDONG)) {
//					output.writeShort(size);
//					int currentTime = TimeUtil.currentTime();
//					for (int i = 0; i < size; i++) {
//						int cardDailyAward = strengthRank.get(i).getEndCardTime();
//						int dayBetween = TimeUtil.getDayBetween(currentTime, cardDailyAward);
//						if (dayBetween>0) {
//							output.writeByte(1);
//						}else {
//							output.writeByte(0);
//						}
//					}
//				}
				bos.close();
				output.close();
				strengthRankData = bos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param obj1
	 * @param obj2
	 */
	public void honourRank(Object obj1,Object obj2) {
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
				Hero hero2 = GlobalMap.getHeroMap().get(hid);
				if (hero2==null) {
					return;
				}
				int myRank = 0;
				Integer indexOf = GlobalMap.getHonourHidRank().indexOf(hid);
				if (indexOf!=null) {
					myRank = indexOf;
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(2050);
				output.writeByte(myRank+1);
				CopyOnWriteArrayList<Hero> honourRank = GlobalMap.getHonourRank();
				int size = honourRank.size();
				output.writeShort(size);
				int currentTime = TimeUtil.currentTime();
				for (int i = 0; i < size; i++) {
					Hero hero = honourRank.get(i);
					output.writeByte(i+1);
					output.writeInt(hero.getId());
					output.writeUTF(hero.getName());
					output.writeByte(hero.getTid());
					output.writeByte(hero.getLevel());
					output.writeInt(hero.getStrength());
					output.writeInt(hero.getHonour());
//					if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
						int endCardTime = hero.getEndCardTime();
						int dayBetween = TimeUtil.getDayBetween(currentTime,endCardTime);
						System.out.println(hero.getName()+"   "+dayBetween);
						if (dayBetween > 0) {
							output.writeByte(1);
						} else {
							output.writeByte(0);
						}
//					}
//					else if (Globalconstants.VERSION.equals(Globalconstants.VERSION_YIDONG)) {
//						int endCardTime = hero.getEndCardTime();
//						int dayBetween = TimeUtil.getDayBetween(currentTime,endCardTime);
//						System.out.println(hero.getName()+"   "+dayBetween);
//						if (dayBetween > 0) {
//							output.writeByte(1);
//						} else {
//							output.writeByte(0);
//						}
//					}
				}
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void waveRank(Object obj1, Object obj2) {
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
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				if (waveRankData!=null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(7007);
					output.write(waveRankData);
					output.close();
					bos.close();
					connection.write(bos.toByteArray());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}

	private void test() throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(robRankData);
		DataInputStream input = new DataInputStream(bis);
		
		short size = input.readShort();
		byte[] ranks = new byte[size];
		for (int i = 0; i < size; i++) {
			ranks[i] = input.readByte();
		}
		input.readShort();
		int[] hids = new int[size];
		for (int i = 0; i < size; i++) {
			hids[i] = input.readInt();
		}
		input.readShort();
		String[] names = new String[size];
		for (int i = 0; i < size; i++) {
			names[i] = input.readUTF();
		}
		input.readShort();
		int[] waves = new int[size];
		for (int i = 0; i < size; i++) {
			waves[i] = input.readInt();
		}
		input.readShort();
		byte[] tids = new byte[size];
		for (int i = 0; i < size; i++) {
			tids[i] = input.readByte();
		}
		System.out.println(Arrays.toString(ranks));
		System.out.println(Arrays.toString(hids));
		System.out.println(Arrays.toString(names));
		System.out.println(Arrays.toString(waves));
		System.out.println(Arrays.toString(tids));
		
		bis.close();
		input.close();
	}
	
	public void strengthRank(Object obj1, Object obj2) {
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
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				if (strengthRankData!=null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(7009);
					output.write(strengthRankData);
					output.close();
					bos.close();
					connection.write(bos.toByteArray());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public void robCopperRank(Object obj1, Object obj2) {
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
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				if (robRankData!=null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(7011);
					output.write(robRankData);
					output.close();
					bos.close();
					connection.write(bos.toByteArray());
				}else {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(7011);
					output.writeShort(0);
					output.writeShort(0);
					output.writeShort(0);
					output.writeShort(0);
					output.writeShort(0);
					output.close();
					bos.close();
					connection.write(bos.toByteArray());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}
