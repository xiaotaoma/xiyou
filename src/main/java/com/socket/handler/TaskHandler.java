package com.socket.handler;

import com.cache.GlobalMap;
import com.model.Hero;
import com.model.backstage.Back_record;
import com.model.backstage.Back_tool;
import com.model.sys.SysGloablMap;
import com.model.sys.Task;
import com.socket.battle.BattleHandler;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

public class TaskHandler {
	private static TaskHandler taskHandler;
	public static TaskHandler getTaskHandler() {
		if (taskHandler == null) {
			taskHandler = new TaskHandler();
		}
		return taskHandler;
	}
	
	private static Logger logger = LoggerFactory.getLogger(TaskHandler.class);
	
	
	public void taskMessage(Object obj1,Object obj2) {
		try {
			Connection connection = null;
			byte[] bytes = null;
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
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
				taskMessage(connection, hero);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void taskMessage(Connection connection,Hero hero) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(8010);
			
			String fishmenAward = hero.getFishmenAward();
			if (fishmenAward.equals("")) {
				output.writeShort(6);
			}else {
				List<Integer> list = SysUtil.splitGetInt(fishmenAward, "_");
				Integer times = list.get(1);
				if (times>=15) {
					output.writeShort(5);
				}else {
					output.writeShort(6);
				}
			}
			
			//每日强化装备
			output.writeInt(15000);
			String equipUpTimes = hero.getEquipUpTimes();
			output.writeByte(5);
			if (equipUpTimes.equals("")) {
				output.writeByte(0);
				output.writeByte(1);
			}else {
				String[] split = equipUpTimes.split("_");
				output.writeByte(Integer.parseInt(split[0]));
				output.writeByte(Integer.parseInt(split[1]));
			}
			//每日竞技场挑战
			output.writeInt(15001);
			String challengeTimes = hero.getChallengeTimes();
			output.writeByte(1);
			if (challengeTimes.equals("")) {
				output.writeByte(0);
				output.writeByte(1);
			}else {
				String[] split = challengeTimes.split("_");
				output.writeByte(Integer.parseInt(split[0]));
				output.writeByte(Integer.parseInt(split[1]));
			}
			//竞技场获胜10次
			output.writeInt(15002);
			String winTimes = hero.getWinTimes();
			output.writeByte(10);
			if (winTimes.equals("")) {
				output.writeByte(0);
				output.writeByte(1);
			}else {
				String[] split = winTimes.split("_");
				output.writeByte(Integer.parseInt(split[0]));
				output.writeByte(Integer.parseInt(split[1]));
			}
			//商城购买
			output.writeInt(15003);
			String buyToolsTimes = hero.getBuyToolsTimes();
			output.writeByte(1);
			if (buyToolsTimes.equals("")) {
				output.writeByte(0);
				output.writeByte(1);
			}else {
				String[] split = buyToolsTimes.split("_");
				output.writeByte(Integer.parseInt(split[0]));
				output.writeByte(Integer.parseInt(split[1]));
			}
			//摇钱两次
			output.writeInt(15004);
			String shakeTimes = hero.getShakeTimes();
			output.writeByte(2);
			if (shakeTimes.equals("")) {
				output.writeByte(0);
				output.writeByte(1);
			}else {
				String[] split = shakeTimes.split("_");
				output.writeByte(Integer.parseInt(split[0]));
				output.writeByte(Integer.parseInt(split[1]));
			}
			//開服越送越多，新手
			if (fishmenAward.equals("")) {
				output.writeInt(15005);
//				<type name="query">int8u</type><!-- 达成条件次数 -->
//				<type name="times">int8u</type><!-- 达成次数-->
//				<type name="status">int8u</type><!-- 能否领取, 1未达条件 ，0可以领取，2 已经领取 -->
				output.writeByte(15);
				output.writeByte(0);
				output.writeByte(0);
			}else {
				List<Integer> list = SysUtil.splitGetInt(fishmenAward, "_");
				Integer times = list.get(1);
				if (times<15) {
					output.writeInt(15005);
					output.writeByte(15);
					output.writeByte(times);
					Integer time = list.get(2);
					int day = TimeUtil.getDayBetween(time, TimeUtil.currentTime());
					if (day>0) {
						output.writeByte(0);
					}else {
						output.writeByte(2);
					}
				}
			}
			
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 强化装备客户端通知
	 */
	public void equipUp(Object obj1,Object obj2) {
		try {
			Connection connection = null;
			byte[] bytes = null;
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
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
				String equipUpTimes = hero.getEquipUpTimes();
				if (equipUpTimes.equals("")) {
					hero.setEquipUpTimes("1_1");
				}else {
					String[] split = equipUpTimes.split("_");
					int times = Integer.parseInt(split[0])+1;
					int status = Integer.parseInt(split[1]);
					if (times==5) {
						status = 0;
					}
					if (times>=5) {
	                                        times = 5;
                                        }
					hero.setEquipUpTimes(times+"_"+status);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 领取任务奖励
	 * @param obj1
	 * @param obj2
	 */
	public void getTaskAward(Object obj1,Object obj2) {
		try {
			Connection connection = null;
			byte[] bytes = null;
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes !=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int taskId = input.readInt();
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
				Task task = SysGloablMap.getTaskMap().get(taskId);
				if (task==null) {
					return;
				}
				//
				if (getTaskStatus(hero, taskId)) {
					String award = task.getAward();
					if (award!=null&&!award.equals("")) {
						String[] split = award.split(",");
						int length = split.length;
						for (int i = 0; i <length ; i++) {
							List<Integer> list = SysUtil.splitGetInt(split[i], "_");
							Integer type = list.get(0);
							Integer id = list.get(1);
							Integer num = list.get(2);
							if (type==0) {
								HeroHandler.getHeroHandler().giveCopper(num, connection);
							}else if (type==1) {
								MoneyControl.moneyIncome(hero, Back_record.REASON_ACTIVITY, num);
								connection.write(BattleHandler.getTestHandler().getByte(1017, hero.getMoney()));
							}else if (type==2) {
								BagHandler.getBagHandler().giveTools(id, num, hid, Back_tool.REASON_GIFT);
							}else {
								continue;
							}
						}
					}else {
						if (taskId==15005) {
							String fishmenAward = hero.getFishmenAward();
							int num = 0;
							if (fishmenAward.equals("")) {
								num = 10;
							}else {
								List<Integer> list = SysUtil.splitGetInt(fishmenAward, "_");
								num = list.get(0)+list.get(1)*5;
							}
							MoneyControl.moneyIncome(hero, Back_record.REASON_ACTIVITY, num);
							connection.write(BattleHandler.getTestHandler().getByte(1017, hero.getMoney()));
						}
					}
					
					switch (taskId) {
					case 15000://强化装备
						String equipUpTimes = hero.getEquipUpTimes();
						if (!equipUpTimes.equals("")) {
							hero.setEquipUpTimes(equipUpTimes.split("_")[0]+"_2");
						}
						break;
					case 15001://小試牛刀,任意挑战一次竞技场
						String challengeTimes = hero.getChallengeTimes();
						if (!challengeTimes.equals("")) {
							hero.setChallengeTimes(challengeTimes.split("_")[0]+"_2");
						}
						break;
					case 15002://百戰百勝,在競技場擊敗10名玩家
						String winTimes = hero.getWinTimes();
						if (!winTimes.equals("")) {
							hero.setWinTimes(winTimes.split("_")[0]+"_2");
						}
						break;
					case 15003://光顧商城,任意成功購買一次道具
						String buyToolsTimes = hero.getBuyToolsTimes();
						if (!buyToolsTimes.equals("")) {
							hero.setBuyToolsTimes(buyToolsTimes.split("_")[0]+"_2");
						}
						break;
					case 15004://碩果累累,每日完成搖錢樹2次
						String shakeTimes = hero.getShakeTimes();
						if (!shakeTimes.equals("")) {
							hero.setShakeTimes(shakeTimes.split("_")[0]+"_2");
						}
						break;
					case 15005://
						String fishmenAward = hero.getFishmenAward();
						if (fishmenAward.equals("")) {
							hero.setFishmenAward("10_1_"+TimeUtil.currentTime());
						}else {
							List<Integer> list = SysUtil.splitGetInt(fishmenAward, "_");
							Integer times = list.get(1);
							hero.setFishmenAward("10_"+(times+1)+"_"+TimeUtil.currentTime());
						}
						break;
					default:
						break;
					}
					taskMessage(connection, hero);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	/**
	 * 返回任务奖励能否领取
	 * @param hero
	 * @param taskId
	 * @return
	 */
	private boolean getTaskStatus(Hero hero,int taskId) {
		boolean result = false;
		switch (taskId) {
		case 15000://强化装备
			String equipUpTimes = hero.getEquipUpTimes();
			if (!equipUpTimes.equals("")) {
				String[] split = equipUpTimes.split("_");
				int status = Integer.parseInt(split[1]);
				if (status == 0) {
					result = true;
				}
			}
			break;
		case 15001://小試牛刀,任意挑战一次竞技场
			String challengeTimes = hero.getChallengeTimes();
			if (!challengeTimes.equals("")) {
				String[] split = challengeTimes.split("_");
				int status = Integer.parseInt(split[1]);
				if (status == 0) {
					result = true;
				}
			}
			break;
		case 15002://百戰百勝,在競技場擊敗10名玩家
			String winTimes = hero.getWinTimes();
			if (!winTimes.equals("")) {
				String[] split = winTimes.split("_");
				int status = Integer.parseInt(split[1]);
				if (status == 0) {
					result = true;
				}
			}
			break;
		case 15003://光顧商城,任意成功購買一次道具
			String buyToolsTimes = hero.getBuyToolsTimes();
			if (!buyToolsTimes.equals("")) {
				String[] split = buyToolsTimes.split("_");
				int status = Integer.parseInt(split[1]);
				if (status == 0) {
					result = true;
				}
			}
			break;
		case 15004://碩果累累,每日完成搖錢樹2次
			String shakeTimes = hero.getShakeTimes();
			if (!shakeTimes.equals("")) {
				String[] split = shakeTimes.split("_");
				int status = Integer.parseInt(split[1]);
				if (status == 0) {
					result = true;
				}
			}
			break;
		case 15005://
			String fishmenAward = hero.getFishmenAward();
			if (fishmenAward.equals("")) {
				result = true;
			}else {
				List<Integer> list = SysUtil.splitGetInt(fishmenAward, "_");
				Integer num = list.get(0);
				Integer times = list.get(1);
				Integer time = list.get(2);
				if (times>=15) {
					result = false;
				}else {
					int dayBetween = TimeUtil.getDayBetween(time, TimeUtil.currentTime());
					if (dayBetween>0) {
						result = true;
					}
				}
			}
			break;
		default:
			break;
		}
		return result;
	}
}
