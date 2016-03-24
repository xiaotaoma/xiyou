package com.socket.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.http.message.MessageSend;
import com.main.BaseAction;
import com.model.Account;
import com.model.Announcement;
import com.model.Arena;
import com.model.Friend;
import com.model.Hero;
import com.model.Report;
import com.service.*;
import com.socket.back.BackstageHandler;
import com.socket.battle.BattleHandler;
import com.test.TestLogger;
import com.util.MD5;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.*;

public class AccountHandler {
	private static Logger logger = LoggerFactory.getLogger(TestLogger.class);
	private static AccountHandler accountHandler;
	public static AccountHandler getAccountHandler() {
		if (accountHandler == null) {
			accountHandler = new AccountHandler();
		}
		return accountHandler;
	}
	/**
	 * 请求登陆，第一步
	 * 是否存在账号，是否需要注册账号角色
	 */
	public void requestLogin(Object obj1,Object obj2) {
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
				registerAccount(connection, bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	private void registerAccount(Connection connection, byte[] bytes) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			DataInputStream input = new DataInputStream(bis);
			input.readInt();//cmd
			String acc = input.readUTF();//
			String check = input.readUTF();
			String terrace = input.readUTF();
			/**
			 *  type 
			 *  0 ios登陆
			 *  1 android登陆
			 *  2  其他
			 */
			int type = input.readByte();
			input.close();
			bis.close();
			acc = acc.trim();
			if (GlobalMap.getAccConnections().containsKey(acc)) {
				Connection connection2 = GlobalMap.getAccConnections().get(acc);
				if (connection2!=null && connection2.isOpen()) {
					System.out.println("重新登陆"+ TimeUtil.currentDate()+" account :"+acc+"  conn+"+connection2);
					send1016(connection2);
					connection2.close();
				}
			}
			int currentTime = TimeUtil.currentTime();
//			boolean canLogin = false;
//			if (GlobalMap.getCheckLoginMap().containsKey(acc)) {
//				Login login = GlobalMap.getCheckLoginMap().get(acc);
//				if (login.getTime()<currentTime-120) {//10分钟
//					canLogin = true;
//				}
//				GlobalMap.getCheckLoginMap().remove(acc);
//			}else {
//				logger.info(GlobalMap.getCheckLoginMap().toString());
//				System.out.println("GlobalMap.getCheckLoginMap().containsKey(acc)");
//				logger.info("GlobalMap.getCheckLoginMap().containsKey(acc)");
//				return;
//			}
//			if (canLogin) {
//				logger.info("不能登录：account:"+acc+"\tcheck:"+check);
//				return;
//			}
			
			AccountManager accountManager = (AccountManager) BaseAction.getIntance().getBean("accountManager");
			List<Account> byAccount = accountManager.getByAccount(acc);
			String ip = SysUtil.getIp(connection);
//			logger.info("account Ip:"+ip);
			Account account = null;
			if (byAccount == null || byAccount.size()==0) {//账号不存在，创建账号
				//注册账号
				account = new Account();
				account.setAccount(acc);
				account.setCreateIp(ip);
				account.setRegisterTime(currentTime);
				account.setTerrace(terrace);
				logger.info("insert account");
				accountManager.insert(account);
			}else {
				account = byAccount.get(0);
				logger.info("account :"+account.getId());
			}
			//缓存账号信息
			GlobalMap.getAccountMap().put(acc, account);
			//返回结果
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(1002);
			output.writeByte(1);
			output.close();
			bos.close();
			bos.toByteArray();
			connection.write(bos.toByteArray());
			connection.getAttributes().setAttribute("account", acc);
			
			GlobalMap.getAccConnections().put(acc, connection);
			
			connection.getAttributes().setAttribute("device",type);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void send1016(Connection connection) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(1016);
			output.writeByte(1);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
//			System.out.println("send:"+1016);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getHero(Object obj1,Object obj2) {
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
				String acc = (String) connection.getAttributes().getAttribute("account");
				if (acc == null) {
					return;
				}
				Account account = GlobalMap.getAccountMap().get(acc);
				if (account == null) {
					return;
				}
				int accId = account.getId();
				
				HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
				List<Hero> heros = heroManager.getHeros(accId);
				if (heros==null || heros.size()==0) {//没有角色，需要创建角色
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(1004);
					output.writeByte(2);
					output.writeShort(0);
					output.close();
					bos.close();
					connection.write(bos.toByteArray());
				}else {//有角色
					Hero hero = heros.get(0);
					String name = hero.getName();
					heroMessage(connection, name, acc, account, hero,false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	
	/**
	 * 创建角色
	 */
	public void createHero(Object obj1,Object obj2) {
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
				String name = input.readUTF();
				input.close();
				bis.close();
				name = name.trim();
//				logger.info("createhero ,   hero name = " + name);
				if (!formatHeroName(name)) {
					//角色名不合法
					SysUtil.warning(connection, Globalconstants.LEGALNAME);
					return;
				}
				
				if (name.length()>10) {
					return;
				}
				
				int currentTime = TimeUtil.currentTime();
				String acc = (String) connection.getAttributes().getAttribute("account");
				Account account = GlobalMap.getAccountMap().get(acc);
				if (account==null) {
					return;
				}
				
				HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
				Hero heroByName = heroManager.getHeroByName(name);
				if (heroByName!=null) {
					//角色名已经被注册
					SysUtil.warning(connection, Globalconstants.UNAVAILABLENAME);
					return;
				}
				
				Hero hero = new Hero();
				hero.setName(name);
				hero.setLevel(0);
				hero.setCreateTime(currentTime);
				hero.setLoginTime(currentTime);
				hero.setTreeTime(0);
				hero.setAccount(acc);
				hero.setAccId(account.getId());
				hero.setWave("");
				hero.setTimes(8);
				hero.setTid(6);
				hero.setTids("6");
				hero.setFirstDouble("");
				hero.setAward("");
				hero.setEquipUpTimes("");
				hero.setChallengeTimes("");
				hero.setWinTimes("");
				hero.setBuyToolsTimes("");
				hero.setShakeTimes("");
				hero.setFishmenAward("");
				hero.setInviteFriend("");
				hero.setTerrace(account.getTerrace());
				heroManager.insert(hero);
				
				heroMessage(connection, name, acc, account, hero,true);
				syncHttpServer(acc, account.getTerrace(), name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void syncHttpServer(String acc,String terrace,String name) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("http://").append(Globalconstants.centralServerIp).append(":").append(Globalconstants.centralServerPort).append("/");
			Random random = new Random();
			int nextInt = random.nextInt(Integer.MAX_VALUE);
			
			String encodeByMD5 = MD5.encodeByMD5("tiantucg173"+nextInt+acc);
			sb.append("Register/").append("account=").append(acc).append("&random=").
			append(nextInt).append("&key=").append(encodeByMD5).append("&terrace=").append(terrace).
			append("&zoneid=").append(Globalconstants.zoneid).append("&name=").append(name);
			MessageSend messageSend = new MessageSend("UTF-8", "UTF-8");
			String s = sb.toString();
			messageSend.connect(null, s);
			logger.error("syncHttpServer:"+s);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("",e);
		}
	}
	public void heroMessage(Connection connection, String name, String acc,
							Account account, Hero hero, boolean sync) throws SQLException, IOException {
		//重复请求角色
		Connection connection2 = GlobalMap.getConns().get(hero.getId());
		if (connection2!=null && !connection.equals(connection2)) {
			System.out.println("重复登录："+hero.getId()+" ");
			connection2.close();
			return;
		}
		int hid = hero.getId();
		//角色缓存
		hero.setLoginTime(TimeUtil.currentTime());
		GlobalMap.getHeroMap().put(hero.getId(), hero);
		connection.getAttributes().setAttribute("hid", hero.getId());
		//角色名对应角色id
		GlobalMap.getHeroNameMap().put(name, hero.getId());
		//连接信息
		GlobalMap.getConns().put(hero.getId(), connection);
		//好友信息
		FriendManager friendManager = (FriendManager) BaseAction.getIntance().getBean("friendManager");
		Friend friend = friendManager.getByHid(hero.getId());
		GlobalMap.getFriendMap().put(hero.getId(), friend);
		
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(bos);
		output.writeInt(1004);
		output.writeByte(1);
		output.writeShort(1);//长度,角色个数
		output.writeInt(hero.getId());
		output.writeUTF(hero.getName());
		output.writeInt(hero.getMoney());
		output.writeByte(hero.getInvite());
		bos.close();
		output.close();
		connection.write(bos.toByteArray());
		//背包信息
		BagHandler.getBagHandler().init(hid,account.getTerrace());
		if (sync) {
//			syncHttpServer(acc, account.getTerrace(), hero.getName());
			//竞技场数据插入
			Arena arena = ArenaHandler.getArenaHandler().initArena(hero.getId(), hero);
			if (arena!=null) {
				ArenaHandler.getArenaHandler().sendChanglleTimes(arena,connection);
				ArenaHandler.getArenaHandler().sendRank(arena.getRank(),connection);
				ArenaHandler.getArenaHandler().sendPre(arena.getPrestige(),connection);
			}
		}else {
			ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
			Arena arena = arenaManager.getByHid(hero.getId());
			if (arena!=null) {
				ArenaHandler.getArenaHandler().sendChanglleTimes(arena,	connection);
				ArenaHandler.getArenaHandler().sendRank(arena.getRank(), connection);
				ArenaHandler.getArenaHandler().sendPre(arena.getPrestige(), connection);
			}
			arenaManager.update(arena);
		}
		BattleHandler.getTestHandler().sendHonour(hero, connection);
		BattleHandler.getTestHandler().battleTimes(hero);
		HeroHandler.getHeroHandler().firstDouble(hero, connection);
		
		announcement(connection);
		report(connection, hero.getId());
		PayHandler.getPayHandler().login(hero.getId(), connection);
		
		//留言
		ChatHandler.getChatHandler().sendLeaveMessage(hero.getId(), connection);
		
		HeroHandler.getHeroHandler().reset(hero);
		BackstageHandler.getBackstageHandler().login(hero);
		if (hero.getCopper()>0) {
			HeroHandler.getHeroHandler().giveCopper(hero.getCopper(), connection);
			hero.setCopper(0);
		}
	}
	/**
	 * 角色名称是符合法
	 * @param name
	 * @return
	 */
	public boolean formatHeroName(String name) {
		if (name == null || name.equals("")) {
			return false;
		}
		return true;
	}
	
	public void syncStrength(Object obj1,Object obj2) {
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
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int strength = input.readInt();
				int retinue = input.readInt();
				int level = input.readByte();
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
				/*String tids = hero.getTids();
				Pattern pattern = Pattern.compile(String.valueOf(retinue));
				Matcher matcher = pattern.matcher(tids);
				if (!matcher.find()) {
					SysUtil.warning(connection, Globalconstants.NOTID);
					return;
				}*/
				hero.setStrength(strength);
				hero.setLevel(level);
				hero.setTid(retinue);
				
				ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("hid", hid);
				map.put("strength", strength);
				arenaManager.updateStrength(map);
				map.clear();
				
				map.put("hid", hid);
				map.put("retinue", retinue);
				arenaManager.updateRetinue(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public static List<Announcement> announcements = new ArrayList<Announcement>();
	public void initAnnouncements() {
		try {
			AnnouncementManager announcementManager = (AnnouncementManager) BaseAction.getIntance().getBean("announcementManager");
			List<Announcement> announcements = announcementManager.getAnnouncements(10);
			if (announcements!=null) {
				this.announcements.clear();
				this.announcements.addAll(announcements);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 公告
	 * @param connection
	 */
	public void announcement(Connection connection) {
		try {
			int size = announcements.size();
			if (size>0) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(4001);
				output.writeShort(size);
				for (Announcement a : announcements) {
					output.writeInt(a.getTime());
					output.writeUTF(a.getContent());
				}
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
	 * 战报
	 */
	public void report(Connection connection,int hid) {
		try {
			ReportManager reportManager = (ReportManager) BaseAction.getIntance().getBean("reportManager");
			List<Report> reports = reportManager.getReports(hid);
			if (reports!=null && reports.size()>0) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(4003);
				int size = reports.size();
				output.writeShort(size);
//				System.out.println("send report :" + size);
				ByteBuffer buffer = ByteBuffer.allocate(size*4);
				for (int i = 0; i < size; i++) {
					Report report = reports.get(i);
					output.writeInt(report.getId());
					output.writeByte(report.getType());
					output.writeInt(report.getHid());
					output.writeUTF(report.getName());
					output.writeByte(report.getDescId());
					output.writeByte(report.getRetinue());
					output.writeInt(report.getStrength1());
					buffer.put(SysUtil.getByte(report.getRobCopper()));
				}
				
				buffer.flip();
				byte[] b = buffer.array();
				output.writeShort(size);
				output.write(b);
				
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
