package com.socket.battle;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Arena;
import com.model.Hero;
import com.model.backstage.Back_record;
import com.service.ArenaManager;
import com.socket.handler.ArenaHandler;
import com.sysData.map.Loadmap;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class BattleHandler {
	private static Logger logger = LoggerFactory.getLogger(BattleHandler.class);
	private static BattleHandler battleHandler;
	public static BattleHandler getTestHandler() {
		if (battleHandler == null) {
			battleHandler = new BattleHandler();
		}
		return battleHandler;
	}
	
	public void heroList(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
		if (obj1!=null) {
			connection = (Connection) obj1;
		}
		if (obj2!=null) {
			bytes = (byte[]) obj2;
		}
		try {
			if (connection!=null && bytes!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int readInt = input.readInt();
				input.close();
				bis.close();
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				List<Integer> list = new ArrayList<Integer>();
				list.addAll(GlobalMap.getHeroMap().keySet());
				if (list.size()>50) {
					list = list.subList(0, 50);
				}
				ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
				List<Arena> onlineArena = arenaManager.onlineArena(list);
				Arena arena = arenaManager.getByHid(hid);
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(2002);
				output.writeInt(arena.getRank());
				output.writeByte(arena.getTimes());
				if (onlineArena!=null) {
					int size = onlineArena.size();
					output.writeShort(size);
					for (int i = 0; i < size; i++) {
						Arena a = onlineArena.get(i);
						output.writeInt(a.getRank());//排名
						output.writeInt(a.getHid());//角色id
						output.writeUTF(a.getName());//角色名
						output.writeInt(a.getPrestige());//声望
						output.writeInt(a.getStrength());//战斗力
						output.writeInt(a.getRetinue());//随从
					}
				}else {
					output.writeShort(0);
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
	 * 创建房间
	 * @param obj1
	 * @param obj2
	 */
	public void makeRoom(Object obj1,Object obj2) {
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
				int moveSpeedInt = input.readInt();//移动速度
				int tid = input.readInt();//徒弟id
				int rangeInt = input.readInt();//攻击范围
				int attSpeedInt = input.readInt();//攻击速度
				int hp = input.readInt();//当前生命值
				int att = input.readInt();//攻击
				int armor = input.readInt();//护甲
				int mp = input.readInt();//法力值
				int fali = input.readInt();//
				
				BigDecimal moveSpeed = new BigDecimal(moveSpeedInt);
				moveSpeed = moveSpeed.setScale(3, BigDecimal.ROUND_HALF_UP);
				moveSpeed = moveSpeed.divide(new BigDecimal(1000));
				
				BigDecimal range = new BigDecimal(rangeInt);
				range = range.setScale(2, BigDecimal.ROUND_HALF_UP);
				range = range.divide(new BigDecimal(100));
				
				BigDecimal attSpeed = new BigDecimal(attSpeedInt);
				range = range.setScale(2, BigDecimal.ROUND_HALF_UP);
				attSpeed = attSpeed.divide(new BigDecimal(100));
				
				int skillId1 = input.readInt();//普通攻击
				int skillId2 = input.readInt();//技能
				int skillId3 = input.readInt();//技能
				int skillId4 = input.readInt();//技能
				int skillId5 = input.readInt();//技能
				
				input.close();
				bis.close();
				
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				//没有挑战次数
				if (hero.getTimes()==0) {
					SysUtil.warning(connection, Globalconstants.NOBATTLETIMES);
					return;
				}
				if (hero.getRid()!=0) {//已经有房间了，不能创建房间
					return;
				}
				Room room = new Room();
				int rid = Room.getRoomId();
				room.setId(rid);
				hero.setRid(rid);
				room.setLeader(hid);
				room.setLeaderName(hero.getName());
				room.setLeaderStrength(hero.getStrength());
				room.setLeaderTid(hero.getTid());
				room.setLeaderTLevel(hero.getLevel());
				
				Men member = new Men();
				member.setTid(tid);
				member.setName(hero.getName());
				member.settLevel(hero.getLevel());
				member.setStrength(hero.getStrength());
				member.setArmor(armor);
				member.setAcdef(getAcdef(armor));
				member.setAtt(att);
				member.setAttSpeed(attSpeed);
				member.setHid(hid);
				member.setMoveSpeed(moveSpeed);
				member.setHp(hp);
				member.setHpMax(hp);
				member.setMp(Globalconstants.BATTLEMP);
				member.setMpMax(200);
				
				member.setRange(range);
				member.setFali(fali);
				member.setSkillId1(skillId1);
				member.setSkillId2(skillId2);
				member.setSkillId3(skillId3);
				member.setSkillId4(skillId4);
				member.setSkillId5(skillId5);
				ConcurrentHashMap<Integer, Men> members = new ConcurrentHashMap<Integer, Men>();
				members.put(member.getHid(), member);
				room.setMembers(members);
				
				GlobalMap.getRoomIds().add(room.getId());
				GlobalMap.getRoomMap().put(room.getId(), room);
				
				GlobalMap.getEmptyRooms().add(room.getId());
				
				sendRoomMessage(room);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 查看房间
	 */
	public void roomList(Object obj1,Object obj2) {
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
				int page = input.readByte();
				input.close();
				bis.close();
				int start = page*10;
				int end = start+10;
				
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				
				if (GlobalMap.getRoomIds().size()<end) {
					end = GlobalMap.getRoomIds().size();
				}
				
				List<Room> rooms = new ArrayList<Room>();
				for (int i = start; i < end; i++) {
					Integer id = GlobalMap.getRoomIds().get(i);
					Room room = GlobalMap.getRoomMap().get(id);
					if (room!=null) {
						rooms.add(room);
					}
				}
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(2019);
				output.writeByte(1);
				output.writeInt((int)(GlobalMap.getRoomIds().size()/10)+1);
				output.writeByte(page);
				output.writeShort(rooms.size());
				for (Room room : rooms) {
					output.writeInt(room.getId());
					output.writeInt(room.getLeader());
					output.writeInt(room.getLeaderTid());
					output.writeByte(room.getLeaderTLevel());
					output.writeInt(room.getLeaderStrength());
					output.writeUTF(room.getLeaderName());
					ConcurrentHashMap<Integer, Men> members = room.getMembers();
					if (members!=null && members.size()<2) {
						output.writeByte(1);
					}else {
						output.writeByte(0);
					}
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
	 * 进入房间
	 */
	public void inRoom(Object obj1,Object obj2) {
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
				int flag = input.readByte();//是否快读加入
				int rid = input.readInt();//房间id
				int tid = input.readInt();//徒弟id
				int moveSpeedInt = input.readInt();//移动速度
				int rangeInt = input.readInt();//攻击范围
				int attSpeedInt = input.readInt();//攻击速度
				int hp = input.readInt();//当前生命值
				int att = input.readInt();//攻击
				int armor = input.readInt();//护甲
				int mp = input.readInt();//法力值
				int fali = input.readInt();//法力
				BigDecimal moveSpeed = new BigDecimal(moveSpeedInt);
				moveSpeed = moveSpeed.setScale(3, BigDecimal.ROUND_HALF_UP);
				moveSpeed = moveSpeed.divide(new BigDecimal(1000));
				
				BigDecimal range = new BigDecimal(rangeInt);
				range = range.setScale(2, BigDecimal.ROUND_HALF_UP);
				range = range.divide(new BigDecimal(100));
				
				BigDecimal attSpeed = new BigDecimal(attSpeedInt);
				range = range.setScale(2, BigDecimal.ROUND_HALF_UP);
				attSpeed = attSpeed.divide(new BigDecimal(100));
				
				int skillId1 = input.readInt();//普通攻击
				int skillId2 = input.readInt();//移动速度
				int skillId3 = input.readInt();//移动速度
				int skillId4 = input.readInt();//移动速度
				int skillId5 = input.readInt();//移动速度
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
				//没有挑战次数
				if (hero.getTimes()==0) {
					SysUtil.warning(connection, Globalconstants.NOBATTLETIMES);
					return;
				}
				Room room = null;
				if (flag==1) {//快速加入
					room = quicklyIn();
				}else if(flag ==2){//不是快速加入
					room = GlobalMap.getRoomMap().get(rid);
				}else if(flag ==3){//排队通知加入
					room = GlobalMap.getRoomMap().get(rid);
					if (room == null || !room.getMembers().containsKey(hid)) {
						return;
					}
				}else {
					return;
				}
				
				if (room==null) {
					//角色名已经被注册
					SysUtil.warning(connection, Globalconstants.NOROOM);
					return;
				}
				
				Map<Integer, Men> members = room.getMembers();
				if (members.size()>=2 && flag!=3) {
					SysUtil.warning(connection, Globalconstants.HASNOSEAT);
					return;
				}
				Men member = new Men();
				member.setTid(tid);
				member.settLevel(hero.getLevel());
				member.setStrength(hero.getStrength());
				member.setArmor(armor);
				member.setAcdef(getAcdef(armor));
				member.setAtt(att);
				member.setAttSpeed(attSpeed);
				member.setHid(hid);
				member.setName(hero.getName());
				member.setMoveSpeed(moveSpeed);
				
				member.setHp(hp);
				member.setHpMax(hp);
				member.setMp(Globalconstants.BATTLEMP);
				member.setMpMax(200);
				
				member.setFali(fali);
				member.setRange(range);
				member.setSkillId1(skillId1);
				member.setSkillId2(skillId2);
				member.setSkillId3(skillId3);
				member.setSkillId4(skillId4);
				member.setSkillId5(skillId5);
				members.put(hid, member);
				hero.setRid(room.getId());
				
				GlobalMap.getEmptyRooms().remove(Integer.valueOf(room.getId()));
				sendRoomMessage(room);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 计算护甲免伤
	 * @param armor
	 * @return
	 */
	public float getAcdef(int armor) {
		float a = (float) (armor*0.015);
		return 1/(a+1);
	}
	/**
	 * 返回房间信息
	 * @param room
	 */
	public void sendRoomMessage(Room room) {
		try {
			Map<Integer, Men> members = room.getMembers();
			if (members!=null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(2017);
				output.writeByte(1);
				output.writeInt(room.getId());
				output.writeInt(room.getLeader());
				output.writeShort(members.size());
				Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Integer, Men> next = iterator.next();
					Men value = next.getValue();
					output.writeInt(value.getHid());
					output.writeUTF(value.getName());
					output.writeInt(value.getTid());
					output.writeByte(value.gettLevel());
					output.writeInt(value.getStrength());
					output.writeByte(value.getState());//1准备了，0没有准备
				}
				bos.close();
				output.close();
				byte[] byteArray = bos.toByteArray();
				iterator = members.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Integer, Men> next = iterator.next();
					Integer key = next.getKey();
					Connection connection = GlobalMap.getConns().get(key);
					if (connection!=null) {
						connection.write(byteArray);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 准备
	 */
	public void ready(Object obj1,Object obj2) {
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
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				//房主不用准备
				if (room.getLeader()==hid) {
					return;
				}
				Map<Integer, Men> members = room.getMembers();
				if (members!=null) {
					Men member = members.get(hid);
					member.setState(1);
				}
				sendRoomMessage(room);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void broadcastInRoom(Map<Integer, Men> members, byte[] bytes) {
		try {
			if (members==null) {
				return;
			}
			Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Men> next = iterator.next();
				Connection connection = GlobalMap.getConns().get(next.getKey());
				if (connection!=null) {
					connection.write(bytes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	/**
	 * 进入地图
	 */
	public void enterMap(Object obj1, Object obj2) {
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
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				//不是房主不能开始
				if (room.getLeader()!=hid) {
					return;
				}
				Map<Integer, Men> members = room.getMembers();
				int size = members.size();
				if (size==2) {
					Object[] array = Loadmap.getSysMap().keySet().toArray();
					Random random = new Random();
					int mapId = Integer.parseInt(array[random.nextInt(array.length)].toString());
					room.setMapId(mapId);
					com.sysData.map.Map map = Loadmap.getSysMap().get(mapId);
					List<Integer[]> list = new ArrayList<Integer[]>();
					
					list.addAll(map.getStartPoint());
					
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(2026);
					output.writeInt(mapId);
					output.writeShort(size);
					Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
					CopyOnWriteArraySet<Integer> set = MapThread.getMap().get(mapId);
					if (set==null) {
						set = new CopyOnWriteArraySet<Integer>();
					}else {
						set.add(hid);
					}
					while (iterator.hasNext()) {
						Men value = iterator.next().getValue();
						timesReduce(value.getHid());
						output.writeInt(value.getHid());
						BigDecimal moveSpeed = value.getMoveSpeed();
						moveSpeed = moveSpeed.multiply(new BigDecimal(1000));
						output.writeInt(moveSpeed.intValue());
						int nextInt = random.nextInt(list.size());
						Integer[] start = list.get(nextInt);
						list.remove(nextInt);
						output.writeInt(start[0]);//地图初始位置
						output.writeInt(start[1]);
						value.setX(start[0]);
						value.setY(start[1]);
						set.add(value.getHid());
					}
					MapThread.getMap().put(mapId, set);
					
					bos.close();
					output.close();
					byte[] byteArray = bos.toByteArray();
					//通知房主，其他人的位置
					broadcastInRoom(members, byteArray);
					room.setBattle(true);
					//房间内广播角色血量。。
					iterator = members.entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<Integer, Men> next = iterator.next();
						Men value = next.getValue();
						if (value instanceof Men) {
							write2033(value, room);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 扣除战斗次数
	 */
	public void timesReduce(int hid) {
		try {
			Hero hero = GlobalMap.getHeroMap().get(hid);
			if (hero!=null) {
				int times = hero.getTimes();
				if (times<=0) {
					return;
				}
				hero.setTimes(times-1);
				battleTimes(hero);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param room
	 */
	public void write2033(Men member, Room room) {
		try {
			if (member==null || room==null) {
				return;
			}
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2033);
			output.writeInt(member.getHid());
			output.writeInt((int) member.getHpMax());
			output.writeInt((int) member.getHp());
			output.writeInt((int) member.getMpMax());
			output.writeInt((int) member.getMp());
			System.err.println("hid:"+member.getHid()+" mp:"+member.getMp()+" hp:"+member.getHp());
			bos.close();
			output.close();
			Iterator<Entry<Integer, Men>> iterator = room.getMembers().entrySet().iterator();
			while (iterator.hasNext()) {
				Integer key = iterator.next().getKey();
				Connection connection = GlobalMap.getConns().get(key);
				if (connection!=null) {
					connection.write(bos.toByteArray());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 移动
	 * @param obj1
	 * @param obj2
	 */
	public void move(Object obj1,Object obj2) {
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
				int x = input.readInt();
				int y = input.readInt();
				input.close();
				bis.close();
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				Map<Integer, Men> members = room.getMembers();
				if (members!=null && members.containsKey(hid)) {
					Member member = members.get(hid);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(2029);
					output.writeInt(hid);
					output.writeInt(member.getX());
					output.writeInt(member.getY());
					output.writeInt(x);
					output.writeInt(y);
					bos.close();
					output.close();
					broadcastInRoom(members, bos.toByteArray());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 退出房间
	 * @param obj1
	 * @param obj2
	 */
	public void quitRoom(Object obj1,Object obj2) {
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
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				
				quitRoom(hero,true);
				hero.setRid(0);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(2031);
				output.writeByte(1);
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 退出房间
	 * @param hero
	 */
	public void quitRoom(Hero hero, boolean b) {
		try {
			int rid = hero.getRid();
			int hid = hero.getId();
			if (rid==0) {
				return;
			}
			Room room = GlobalMap.getRoomMap().get(rid);
			if (room==null) {
				hero.setRid(0);
				return;
			}
			
			Map<Integer, Men> members = room.getMembers();
			if (members==null) {
				return;
			}
			Member member = members.get(hid);
			//正在战斗中
			if (room.isBattle()) {
				room.setBattle(false);
				broadRoomMessage(room, members);
				int winCopper = 0;
				int winMoney = 0;
				int winShengWang = 0;
				int winHonour = 0;
				Men loser = members.get(hid);
				Men winner = SkillEffect.getSkillEffect().getTmember(members, hid);
				//战斗最终结果
				//获胜者
				Connection connection = GlobalMap.getConns().get(winner.getHid());
				if (connection!=null) {
					ArenaHandler.getArenaHandler().battleResultSend(loser.getName(), loser.getTid(),
							winner.getHid(), winCopper, winMoney, winShengWang, Globalconstants.WINHONOUR, connection,0);
				}
				int loserCopper = 0;
				int loserMoney = 0;
				int loserShengWang = 0;
				
				//失败者
				Connection tConnection = GlobalMap.getConns().get(loser.getHid());
				if (tConnection!=null) {
					ArenaHandler.getArenaHandler().battleResultSend(winner.getName(), winner.getTid(),
							winner.getHid(), loserCopper, loserMoney, loserShengWang, Globalconstants.LOSEHONOUR, tConnection,0);
				}
				Hero loserHero = GlobalMap.getHeroMap().get(hid);
				Hero winnerHero = GlobalMap.getHeroMap().get(winner.getHid());
				if (hero!=null && winnerHero!=null) {
					BattleHandler.getTestHandler().quitRoom(hero,true);
					BattleHandler.getTestHandler().quitRoom(winnerHero,true);
				}
				//战斗结束，主动退出者失败
				return;
			}
			
			hero.setRid(0);
			
			if (member!=null) {
				members.remove(hid);
				Set<Integer> load = room.getLoad();
				if (load!=null) {
					load.remove(hid);
				}
			}
			
			//剩下一个人当房主
 			if (members.size()>0) {
				Men last = (Men) members.values().toArray()[0];
				Men men = (Men) last;
				int thid = men.getHid();
				Hero hero2 = GlobalMap.getHeroMap().get(thid);
				if (hero2==null) {//另外一个人没有了
					GlobalMap.getRoomMap().remove(rid);
					GlobalMap.getRoomIds().remove(Integer.valueOf(rid));
					GlobalMap.getEmptyRooms().remove(Integer.valueOf(rid));
				}else {
					room.setLeader(thid);
					room.setLeaderName(hero2.getName());
					room.setLeaderStrength(hero2.getStrength());
					room.setLeaderTid(hero2.getTid());
					room.setLeaderTLevel(hero2.getTid());
					GlobalMap.getEmptyRooms().add(Integer.valueOf(rid));
					if (b) {
						sendRoomMessage(room);
					}
				}
			}else {//没人了
				GlobalMap.getRoomMap().remove(rid);
				GlobalMap.getRoomIds().remove(Integer.valueOf(rid));
				GlobalMap.getEmptyRooms().remove(Integer.valueOf(rid));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}

	public void broadRoomMessage(Room room, Map<Integer, Men> members) {
		try {
			Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
			while (iterator.hasNext()) {
				Men value = iterator.next().getValue();
				BattleHandler.getTestHandler().write2033(value, room);
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
	public void memberPosition(Object obj1,Object obj2) {
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
				int x = input.readInt();
				int y = input.readInt();
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
				
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				Map<Integer, Men> members = room.getMembers();
				if (members!=null && members.containsKey(hid)) {
					Men member = members.get(hid);
					member.setX(x);
					member.setY(y);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 使用技能
	 */
	public void useSkill(Object obj1,Object obj2) {
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
				int thid = input.readInt();//角色id=0 对地面放技能
				int skillId = input.readInt();
				int x = input.readInt();
				int y = input.readInt();
				int flag = input.readByte();//是否持续技能 1是0不是
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
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				boolean result = false;
				if (flag == 1) {//持续技能 计算每秒伤害 雷阵雨
					result = SkillEffect.getSkillEffect().lastSkill(skillId,hid, thid, room, x, y,flag);
				}else {
					result = SkillEffect.getSkillEffect().useSkill(skillId, hid, thid, room, x, y,flag);
				}
				if (result) {
					skill(thid, skillId, x, y, flag, hid, room);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param thid 目标角色id
	 * @param skillId
	 * @param x
	 * @param y
	 * @param flag
	 * @param hid
	 * @param room
	 * @throws IOException
	 */
	public void skill(int thid, int skillId, int x, int y, int flag,
			Integer hid, Room room) throws IOException {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2036);
			output.writeInt(hid);
			output.writeInt(thid);
			output.writeInt(skillId);
			output.writeInt(x);
			output.writeInt(y);
			output.writeByte(flag);
			byte[] byteArray = bos.toByteArray();
			broadcastInRoom(room.getMembers(), byteArray);
			menMessage(room);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}

	public void menMessage(Room room) {
		try {
			Map<Integer, Men> members = room.getMembers();
			if (members!=null) {
				Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Integer, Men> next = iterator.next();
					Men member = next.getValue();
					write2033(member, room);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 增加hp和mp
	 */
	public void addHpMp(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
		if (obj1!=null) {
			connection = (Connection) obj1;
		}
		if (obj2!=null) {
			bytes = (byte[]) obj2;
		}
		try {
			if (bytes!=null && connection!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int addHp = input.readInt();
				int addMp = input.readInt();
				bis.close();
				input.close();
				if (addHp < 0 || addMp < 0) {
					return;
				}
				Integer hid = (Integer) connection.getAttributes()
						.getAttribute("hid");
				if (hid == null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero == null) {
					return;
				}
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room == null) {
					return;
				}
				ConcurrentHashMap<Integer, Men> members = room.getMembers();
				if (members == null) {
					return;
				}
				Men member = (Men) members.get(hid);
				if (member == null) {
					return;
				}
				float hp = member.getHp();
				hp += addHp;
				if (hp > member.getHpMax()) {
					member.setHp(member.getHpMax());
				} else {
					member.setHp(hp);
				}
				float mp = member.getMp();
				mp += addMp;
				if (mp > member.getMpMax()) {
					member.setMp(member.getMpMax());
				} else {
					member.setMp(mp);
				}
				write2033(member, room);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 完成加载地图
	 * @param obj1
	 * @param obj2
	 */
	public void loadMap(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
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
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				ConcurrentHashMap<Integer, Men> members = room.getMembers();
				if (members==null) {
					return;
				}
				Set<Integer> load = room.getLoad();
				if (load==null) {
					load = new HashSet<Integer>();
				}
				load.add(hid);
				room.setLoad(load);
				if (load.size()==2) {
					if (load.size()==2) {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						DataOutputStream output = new DataOutputStream(bos);
						output.writeInt(2040);
						output.writeByte(1);
						bos.close();
						output.close();
						broadcastInRoom(members, bos.toByteArray());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 快速加入房间
	 * @param obj1
	 * @param obj2
	 */
	public Room quicklyIn() {
		try {
			int size = GlobalMap.getEmptyRooms().size();
			if (size>0) {
				Random random = new Random();
				int nextInt = random.nextInt(size);
				Integer rid = GlobalMap.getEmptyRooms().get(nextInt);
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room!=null) {
					return room;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return null;
	}
	/**
	 * 召唤物生成,通知客户端
	 */
	public void callMonster(ConcurrentHashMap<Integer, Men> members, Call call, int cmd, int skillId) {
		try {
			if (members!=null && call!=null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(cmd);
				if (cmd==2041) {
					output.writeInt(skillId);
				}
				output.writeInt(call.getHid());
				output.writeInt(call.getSysId());
				output.writeInt(call.getFather());
				output.writeInt(call.getX());
				output.writeInt(call.getY());
				bos.close();
				output.close();
				byte[] byteArray = bos.toByteArray();
				Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
				while (iterator.hasNext()) {
					Integer key = iterator.next().getKey();
					Connection connection = GlobalMap.getConns().get(key);
					if (connection!=null) {
						connection.write(byteArray);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 */
	public void monsterPosition(Object obj1,Object obj2) {
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
				int cid = input.readInt();
				int x = input.readInt();
				int y = input.readInt();
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
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				ConcurrentHashMap<Integer, Call> callMonsters = room.getCallMonsters();
				if (callMonsters==null) {
					return;
				}
				if (callMonsters.containsKey(cid)) {
					Call call = callMonsters.get(cid);
					if (call.getFather()==hid) {
						call.setX(x);
						call.setY(y);
						callMonster(room.getMembers(), call, 2043,0);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	/**
	 * 召唤物攻击
	 */
	public void callAttack(Object obj1,Object obj2) {
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
				int cid = input.readInt();//召唤物id
				int thid = input.readInt();//目标角色id
				bis.close();
				input.close();
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				if (hid==thid) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				ConcurrentHashMap<Integer, Call> callMonsters = room.getCallMonsters();
				if (callMonsters==null) {
					return;
				}
				ConcurrentHashMap<Integer, Men> members = room.getMembers();
				if (members==null) {
					return;
				}
				System.out.println("召唤物攻击：cid = "+cid);
				if (members.containsKey(hid)&&members.containsKey(thid)&&callMonsters.containsKey(cid)) {
					Call call = callMonsters.get(cid);
					if (call.getFather()==thid) {
						return;
					}
					SkillEffect.getSkillEffect().callAttack(members.get(thid), call, room);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 */
	public void callAttackSend(ConcurrentHashMap<Integer, Men> members, int thid, int skillId, int callId) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2046);
			output.writeInt(callId);
			output.writeInt(skillId);
			output.writeInt(thid);
			output.close();
			bos.close();
			byte[] byteArray = bos.toByteArray();
			Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
			while (iterator.hasNext()) {
				Integer key = iterator.next().getKey();
				Connection connection = GlobalMap.getConns().get(key);
				if (connection!=null) {
					connection.write(byteArray);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void callMove(Object obj1,Object obj2) {
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
				int cid = input.readInt();
				int x = input.readInt();
				int y = input.readInt();
				input.close();
				bis.close();
				
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int rid = hero.getRid();
				Room room = GlobalMap.getRoomMap().get(rid);
				if (room==null) {
					return;
				}
				ConcurrentHashMap<Integer, Call> callMonsters = room.getCallMonsters();
				if (callMonsters==null) {
					return;
				}
				
				ConcurrentHashMap<Integer, Men> members = room.getMembers();
				if (members!=null && callMonsters.get(cid)!=null) {
					Call call = callMonsters.get(cid);
					call.setX(x);
					call.setY(y);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(2048);
					output.writeInt(cid);
					output.writeInt(x);
					output.writeInt(y);
					output.close();
					bos.close();
					byte[] byteArray = bos.toByteArray();
					broadcastInRoom(members, byteArray);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 徒弟大战次数
	 * @param hero
	 */
	public void battleTimes(Hero hero) {
		try {
			setHonourTime(hero);
			int times = hero.getTimes();
			if (times>=8) {
				times = 8;
			}
			
			int currentTime = TimeUtil.currentTime();
			int time = hero.getHonorTime()+3600 - currentTime;
			if (time>3600 || time<0) {
				time = 0;
			}
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2052);
			output.writeByte(times);
			output.writeShort(time);
			bos.close();
			output.close();
			Connection connection = GlobalMap.getConns().get(hero.getId());
			if (connection!=null) {
				connection.write(bos.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 */
	public void setHonourTime(Hero hero) {
		try {
			int currentTime = TimeUtil.currentTime();
			int time = hero.getHonorTime();
			int times = hero.getTimes();
			if (times>= Globalconstants.BATTLETIMES) {
				hero.setTimes(Globalconstants.BATTLETIMES);
				hero.setHonorTime(currentTime);
			}else {
				int n = (currentTime - time)/3600;
				int m = (currentTime - time)%3600;
				if (n>0) {
					times+=n;
					if (times>= Globalconstants.BATTLETIMES) {
						hero.setHonorTime(currentTime);
						hero.setTimes(Globalconstants.BATTLETIMES);
					}else {
						hero.setTimes(times);
						hero.setHonorTime(currentTime-m);
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
	}
	/**
	 * 购买挑战次数
	 */
	public void buyBattleTimes(Object obj1,Object obj2) {
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
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int times = hero.getTimes();
				if (times!=0) {
					return;
				}
				int money = hero.getMoney();
				if (money<16) {
					return;
				}
				if (hero.getBuyTimes()==1) {
					return;
				}
				boolean moneyExpenses = MoneyControl.moneyExpenses(hero, Back_record.REASON_BATTLE, 16);
				if (!moneyExpenses) {
					return;
				}
				hero.setBuyTimes(1);
				hero.setTimes(8);
				hero.setHonorTime(TimeUtil.currentTime());
				
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(2052);
				output.writeByte(hero.getTimes());
				output.writeShort(0);
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
				
				connection.write(getByte(1017, hero.getMoney()));
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void sendHonour(Hero hero, Connection connection) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2054);
			output.writeInt(hero.getHonour());
			Integer rank = GlobalMap.getHonourHidRank().indexOf(hero.getId());
			if (rank==null) {
				output.writeByte(0);
			}else {
				output.writeByte(rank+1);
			}
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	
	public byte[] getByte(int cmd,int money) {
		byte []a=new byte[8];
		a[0]=(byte)((cmd >>> 24) & 0xFF);
		a[1]=(byte)((cmd >>> 16) & 0xFF);
		a[2]=(byte)((cmd >>>  8) & 0xFF);
		a[3]=(byte)((cmd >>>  0) & 0xFF); 
		
		a[4]=(byte)((money >>> 24) & 0xFF);
		a[5]=(byte)((money >>> 16) & 0xFF);
		a[6]=(byte)((money >>>  8) & 0xFF);
		a[7]=(byte)((money >>>  0) & 0xFF);
		return a;
	}
}