package com.socket.battle;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.model.Hero;
import com.util.SysUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class BattleQueueHandler {
	private static BattleQueueHandler battleQueueHandler = null;
	public static BattleQueueHandler getBattleQueueHandler() {
		if (battleQueueHandler==null) {
			battleQueueHandler = new BattleQueueHandler();
		}
		return battleQueueHandler;
	}
	
	private static Logger logger = LoggerFactory.getLogger(BattleQueueHandler.class);
	/**
	 * 进入排队状态
	 */
	public void enterQueue(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
		if (obj1!=null) {
			connection = (Connection) obj1;
		}
		if (obj2!=null) {
			bytes = (byte[]) obj2;
		}
		if (connection!=null && bytes!=null) {
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int moveSpeedInt = input.readInt();//移动速度
				if (moveSpeedInt<=0) {
					return;
				}
				int tid = input.readInt();//徒弟id
				if (tid<=0) {
					return;
				}
				int rangeInt = input.readInt();//攻击范围
				if (rangeInt<=0) {
					return;
				}
				int attSpeedInt = input.readInt();//攻击速度
				if (attSpeedInt<=0) {
					return;
				}
				int hp = input.readInt();//当前生命值
				if (hp<=0) {
					return;
				}
				int att = input.readInt();//攻击
				if (att<=0) {
					return;
				}
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
				
				//已经有房间
				int rid = hero.getRid();
				if (rid!=0) {
					return;
				}
				
				//没有挑战次数
				if (hero.getTimes()==0) {
					SysUtil.warning(connection, Globalconstants.NOBATTLETIMES);
					return;
				}
				
				Men men = new Men();
				men.setTid(tid);
				men.setName(hero.getName());
				men.settLevel(hero.getLevel());
				men.setStrength(hero.getStrength());
				men.setArmor(armor);
				men.setAcdef(getAcdef(armor));
				men.setAtt(att);
				men.setAttSpeed(attSpeed);
				men.setHid(hid);
				men.setMoveSpeed(moveSpeed);
				men.setHp(hp);
				men.setHpMax(hp);
				men.setMp(Globalconstants.BATTLEMP);
				men.setMpMax(200);
				
				men.setRange(range);
				men.setFali(fali);
				men.setSkillId1(skillId1);
				men.setSkillId2(skillId2);
				men.setSkillId3(skillId3);
				men.setSkillId4(skillId4);
				men.setSkillId5(skillId5);
				
				if(GlobalMap.getQueue().size()>0){
					Men earlyMen = GlobalMap.getQueue().get(0);//另外在排队的人
					if (earlyMen.getHid()==hid) {
						return;
					}
					Hero earlyHero = GlobalMap.getHeroMap().get(earlyMen.getHid());//先来的人
					if (earlyHero!=null) {//
						Room room = makeEmptyRoom(hero, earlyHero,men,earlyMen);
						GlobalMap.getQueue().clear();
						BattleHandler.getTestHandler().sendRoomMessage(room);
					}else {
						GlobalMap.getHeroMap().clear();
						GlobalMap.getQueue().add(men);
					}
				}else {
					GlobalMap.getQueue().add(men);//加入队列
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("", e);
			}
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
	 * 先在排队的人是房主
	 * 
	 * 
	 */
	public Room makeEmptyRoom(Hero hero, Hero earlyHero, Men men, Men earlyMen) {
		Room room = new Room();
		int rid = Room.getRoomId();
		hero.setRid(rid);
		earlyHero.setRid(rid);
		room.setId(rid);
		room.setLeader(earlyHero.getId());
		room.setLeaderName(earlyHero.getName());
		room.setLeaderStrength(earlyHero.getStrength());
		room.setLeaderTid(earlyHero.getTid());
		room.setLeaderTLevel(earlyHero.getLevel());
		ConcurrentHashMap<Integer, Men> members = new ConcurrentHashMap<Integer, Men>();
		members.put(men.getHid(), men);
		members.put(earlyMen.getHid(), earlyMen);
		room.setMembers(members);
		GlobalMap.getRoomIds().add(room.getId());
		GlobalMap.getRoomMap().put(room.getId(), room);
		return room;
	}
	/**
	 * 离开排队
	 * @param obj1
	 * @param obj2
	 */
	public void quitQueue(Object obj1,Object obj2) {
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
			
			if (GlobalMap.getQueue().size()>0) {
				Men men = GlobalMap.getQueue().get(0);
				if (men.getHid()==hid) {
					GlobalMap.getQueue().remove(0);
					connection.write(SysUtil.getBytes(2058, (byte) 1));
				}
			}
		}
	}
}
