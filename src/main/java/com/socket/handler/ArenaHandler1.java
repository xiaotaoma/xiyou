package com.socket.handler;

import com.cache.GlobalMap;
import com.main.BaseAction;
import com.model.Arena;
import com.model.Hero;
import com.model.Report;
import com.model.sys.ArenaMonster;
import com.model.sys.SysGloablMap;
import com.service.ArenaManager;
import com.service.HeroManager;
import com.service.ReportManager;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class ArenaHandler1 {
	private static ArenaHandler1 arenahandler1;
	public static ArenaHandler1 getArenahandler1() {
		if (arenahandler1 ==null) {
			arenahandler1 = new ArenaHandler1();
		}
		return arenahandler1;
	}
	private static Logger logger = LoggerFactory.getLogger(ArenaHandler1.class);
	
	
	/**
	 * 
	 */
	public void battleResult(Object obj1,Object obj2) {
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
				ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
				Arena arena2 = arenaManager.getByHid(list.get(2));
				int mapId = list.get(0);
				int otherHid = list.get(2);
				String otherName = arena2.getName();
				int otherTid = arena2.getRetinue();
				if (wave==0) {
					ArenaHandler.getArenaHandler().battleResultSend(otherName, otherTid, otherHid,0,0,0,0,connection,0);
					return;
				}
				ArenaMonster arenaMonster = SysGloablMap.getMonsterMap().get(wave);
				if (arenaMonster==null) {
					return;
				}
				int score = arenaMonster.getScore();
				//自己
				Arena arena = arenaManager.getByHid(hid);
				
				int rank = arena.getRank();//自己排名
				int rank2 = arena2.getRank();//对方排名
				
				String winnerName = arena.getName();
				String loserName = arena2.getName();
				
				int score1 = arena.getScore();
				int score2 = arena2.getScore();
				int prestige = 1;
				int winnerHid = otherHid;
				int robCopper = 0;
				logger.info("battleResult hid="+hid+" score="+score+"  last score :" + score1+" otherHid = "+arena2.getHid()+" otherScore="+score2);
				if (score>=score2) {//得分比挑战的人多,胜
					winnerHid = hid;
					if (rank>rank2) {//自己排名大于对方排名
						prestige = 1;
					}else {
						prestige = 3;
					}
					
					int hid2 = arena2.getHid();
					Hero otherHero = null;
					boolean online = false;
					HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
					if(GlobalMap.getHeroMap().containsKey(hid2)){
						otherHero = GlobalMap.getHeroMap().get(hid2);
						online = true;
					}else {
						otherHero = heroManager.getHeroById(hid2);
					}
					int treeCopper = otherHero.getTreeCopper();
					robCopper = (int) (treeCopper*0.1);//抢夺铜币
					hero.setRobedCopper(hero.getRobedCopper()+robCopper);
					otherHero.setTreeCopper(treeCopper-robCopper);
					if (online) {
						
					}else {
						heroManager.update(otherHero);
					}
					HeroHandler.getHeroHandler().giveCopper(robCopper, connection);
					SysUtil.broadcastBattleResult(winnerName, loserName, robCopper,wave);
					ArenaHandler.getArenaHandler().setReport(hero, arena2.getRetinue(), arena2.getHid(), 
							arena2.getName(), Report.TYPE_ARENA,arena2.getStrength(),robCopper);
					/**
					 * 百戰百勝,在競技場擊敗10名玩家
					 */
					String winTimes = hero.getWinTimes();
					if (winTimes.equals("")) {
						hero.setWinTimes("1_1");
					}else {
						List<Integer> splitGetInt = SysUtil.splitGetInt(winTimes, "_");
						Integer times = splitGetInt.get(0);
						Integer status = splitGetInt.get(1);
						times++;
						if (times==10) {
							status = 0;
						}
						hero.setWinTimes(times+"_"+status);
					}
					
				}else {//负
					if (rank>rank2) {//自己排名大于对方排名
						prestige = -3;
					}else {
						prestige = -1;
					}
				}
				
				if (score>score1) {
					arena.setScore(score);
				}
				
				//排名更新
				int p1 = arena.getPrestige();
				int p2 = p1 + prestige;
				arena.setPrestige(p2);
				
				
				int money = 0;
				int copper = arenaMonster.getCopper();
				
				ArenaHandler.getArenaHandler().battleResultSend(otherName, otherTid, winnerHid, copper , money , prestige,0,connection,robCopper);
				hero.setWave("");
				if (wave>arena.getWave()) {
					arena.setWave(wave);
				}
				
				setRank(arenaManager, arena, p1, p2);
				
				Arena byHid = arenaManager.getByHid(hid);
				if (byHid!=null) {
					ArenaHandler.getArenaHandler().sendRank(byHid.getRank(),connection);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param arenaManager
	 * @param arena
	 * @param prestige 小
	 * @param prestige2 大
	 * @throws SQLException
	 */
	public synchronized void setRank(ArenaManager arenaManager, Arena arena, int prestige,
			int prestige2) throws SQLException {
		try {
			long t1 = TimeUtil.TimeMillis();
			arenaManager.update(arena);
			List<Arena> list2 = arenaManager.getList(prestige, prestige2);//排名变化列表
			Collections.sort(list2, new SortByPrestige());
			Integer rank = arenaManager.getCountByPrestige(prestige2);
			if (rank==null || rank==0) {
				rank = 0;
			}
			
			for (Arena a : list2) {
				rank++;
				a.setRank(rank);
			}
			arenaManager.updateRank(list2);
			long t2 = TimeUtil.TimeMillis();
			logger.info("Rank   changelistsize:"+list2.size()+" cost time:"+(t2-t1));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void robMessage(Object obj1,Object obj2) {
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
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				sendRobMessage(connection, hid);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	private void sendRobMessage(Connection connection, Integer hid)
			throws SQLException, IOException {
		ReportManager reportManager = (ReportManager) BaseAction.getIntance().getBean("reportManager");
		List<Report> list = reportManager.getBobedReports(hid);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(bos);
		output.writeInt(8016);
		if (list==null || list.size()==0) {
			output.writeShort(0);
		}else {
			output.writeShort(list.size());
			Iterator<Report> iterator = list.iterator();
			while (iterator.hasNext()) {
				Report report = iterator.next();
				String s = report.getName()+"抢走铜币"+report.getRobCopper();
				output.writeUTF(s);
			}
		}
		bos.close();
		output.close();
		connection.write(bos.toByteArray());
	}
}

class SortByPrestige implements Comparator<Arena>{
	@Override
	public int compare(Arena a1, Arena a2) {
		if (a1==null) {
			return 1 ;
		}
		if (a2==null) {
			return -1;
		}
		if (a1!=null && a2!=null) {
			int a = a2.getPrestige()-a1.getPrestige();
			if (a!=0) {
				return a;
			}else {
				return a2.getStrength()-a1.getStrength();
			}
		}
		return 0;
	}
}
