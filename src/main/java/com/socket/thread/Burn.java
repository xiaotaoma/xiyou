package com.socket.thread;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.model.Hero;
import com.model.Report;
import com.socket.battle.*;
import com.socket.handler.ArenaHandler;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


public class Burn {
	private static Logger logger = LoggerFactory.getLogger(Burn.class);
	
	/**
	 * 有烧伤buff房间id
	 */
	private static Set<Integer> roomIds = new CopyOnWriteArraySet<Integer>();
	public static Set<Integer> getRoomIds() {
		return roomIds;
	}
	
	/**
	 * 烧伤伤害
	 */
	public Burn() {
		Timer timer = new Timer("Burn");
		timer.schedule(new RemindTask(), 0, 1000);
	}
	class RemindTask extends TimerTask{
		@Override
		public void run() {
			try {
				Iterator<Integer> iterator = roomIds.iterator();
				while (iterator.hasNext()) {
					Integer next = iterator.next();
					Room room = GlobalMap.getRoomMap().get(next);
					boolean hasBurn = false;
					if (room!=null) {
						int rid = room.getId();
						ConcurrentHashMap<Integer, Men> members = room.getMembers();
						if (members!=null) {
							Iterator<Entry<Integer, Men>> ite = members.entrySet().iterator();
							while (ite.hasNext()) {
								Men men = ite.next().getValue();
								CopyOnWriteArrayList<Buff> buffs = men.getBuffs();
								if (buffs!=null) {
									for (Buff buff : buffs) {
										CopyOnWriteArrayList<Float> burnDemage = buff.getBurnDemage();
										if (burnDemage!=null && burnDemage.size()>0) {
											Float demage = burnDemage.get(0);
											float hp = men.getHp();
											if (demage>=hp) {
												men.setHp(0);
												room.setBattle(false);
												//广播房间内血量
												BattleHandler.getTestHandler().broadRoomMessage(room, members);
												
												Men winner = SkillEffect.getSkillEffect().getTmember(members, men.getHid());
												
												int winCopper = 0;
												int winMoney = 0;
												int winShengWang = 0;
												//获胜者
												Connection connection = GlobalMap.getConns().get(winner.getHid());
												
												if (connection!=null) {
													ArenaHandler.getArenaHandler().battleResultSend(men.getName(), men.getTid(),
															winner.getHid(), winCopper, winMoney, winShengWang,Globalconstants.WINHONOUR , connection,0);
												}
												
												int loserCopper = 0;
												int loserMoney = 0;
												int loserShengWang = 0;
												
												//失败者
												Connection tConnection = GlobalMap.getConns().get(men.getHid());
												if (tConnection!=null) {
													ArenaHandler.getArenaHandler().battleResultSend(winner.getName(), winner.getTid(), 
															winner.getHid(), loserCopper, loserMoney, loserShengWang,Globalconstants.LOSEHONOUR, tConnection,0);
												}
												
												Hero hero = GlobalMap.getHeroMap().get(winner.getHid());
												Hero tHero = GlobalMap.getHeroMap().get(men.getHid());
												if (hero!=null && tHero!=null) {
													Connection conn = GlobalMap.getConns().get(tHero.getId());
													if (conn!=null) {
														ArenaHandler.getArenaHandler().setReport(hero, tHero.getTid(), men.getHid(), tHero.getName(), Report.TYPE_BATTLE,tHero.getStrength(),0);
													}
													BattleHandler.getTestHandler().quitRoom(hero,false);
													BattleHandler.getTestHandler().quitRoom(tHero,false);
												}
											}else {
												men.setHp(hp-demage);
												//广播房间内血量
												BattleHandler.getTestHandler().broadRoomMessage(room, members);
											}
											burnDemage.remove(0);
											System.out.println("烧伤伤害："+demage+" 剩余血量："+men.getHp());
											if (burnDemage.size()>0) {
												hasBurn = true;
											}
										}
									}
								}
							}
						}
						if (!hasBurn) {
							roomIds.remove(rid);
						}
					}else {
						roomIds.remove(next);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("", e);
			}
		}
	}
}
