package com.socket.battle;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.model.Hero;
import com.model.Report;
import com.model.backstage.Back_record;
import com.model.sys.SysCall;
import com.model.sys.SysGloablMap;
import com.model.sys.SysSkill;
import com.socket.handler.ArenaHandler;
import com.socket.thread.Burn;
import com.socket.thread.CallMonster;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SkillEffect {
	private static Logger logger = LoggerFactory.getLogger(SkillEffect.class);
	public static final String ATTUP = "攻击力提升";
	public static final String REDUCEARMOR = "护甲减低";
	public static final String BURN = "烧伤";
	private static SkillEffect skillEffect;
	public static SkillEffect getSkillEffect() {
		if (skillEffect == null) {
			skillEffect = new SkillEffect();
		}
		return skillEffect;
	}
	/**
	 * 
	 * @param skillId 技能id
	 * @param hid 技能使用着的id
	 * @param room 房间信息
	 */
	public boolean useSkill(int skillId, int hid, int thid, Room room, int x, int y, int flag) {
		if (thid!=0) {//对目标id普通攻击
			return attack(hid, thid, room, x, y, flag, skillId);
		}else {
			SysSkill sysSkill = SysGloablMap.getSkillMap().get(skillId);
			if (sysSkill==null) {
				return false;
			}
			return skill(sysSkill, hid, room, x, y,flag);
		}
	}
	/**
	 * 自身攻击的平方/（自身攻击+对方防御）/2
	 * 普通攻击
	 * @param hid
	 * @param thid
	 * @param room
	 */
	public boolean attack(int hid, int thid, Room room, int x, int y, int flag, int skillId) {
		try {
			Map<Integer, Men> members = room.getMembers();
			if (members==null) {
				return false;
			}
			Men member = members.get(hid);
			Men tMember = members.get(thid);
			if (member!=null && tMember!=null) {
				long timeMillis = TimeUtil.TimeMillis();
				int currentTime = (int) (timeMillis/1000);
				float att = member.getAtt();
				BigDecimal attSpeed = member.getAttSpeed();
				long attTime = member.getAttTime();
				//攻击间隔
				if (timeMillis!=0 && timeMillis-attTime<attSpeed.intValue()*1000) {
					return false;
				}
				//攻击范围
				int dis = (int) Math.hypot(member.getX()-tMember.getX(), member.getY()-tMember.getY());
				int intValue = member.getRange().multiply(new BigDecimal(80)).intValue();
				
				if (dis > intValue) {
					return false;
				}
//				System.err.println("基础攻击："+att);
				//攻击提升
				int upatt = count(member, currentTime, Buff.UPATT);
//				System.err.println("伤害提升："+upatt);
				att = att*upatt/100+att;
//				System.err.println("提升后攻击："+att);
				//免伤计算
				float armor = tMember.getArmor();
				float count = count(tMember, currentTime, Buff.REDUCEARMOR);
//				System.err.println("护甲减少："+count);
				armor = armor - armor*count/100;
				float acdef = BattleHandler.getTestHandler().getAcdef((int) armor);
//				System.err.println("减少伤害："+(1-acdef));
				att = att*acdef;
//				System.err.println("最终伤害："+att);
				float hp = tMember.getHp();
				if (att>=hp) {//被打死 战斗结束
					/**----------------------战斗结束---------------------**/
					tMember.setHp(0);
					members.put(tMember.getHid(), tMember);
					room.setBattle(false);
					battleEnd(room, hid, thid, skillId, x, y, tMember.getHid(), flag);
					return false;
				}else {
					tMember.setHp(hp-att);
				}
				//攻击成功，加入cd
				member.setAttTime(timeMillis);
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return false;
		}
	}
	/**
	 * 计算护甲
	 * @param tMember
	 * @param currentTime
	 * @param d
	 * @return
	 */
	private int count(Men tMember, int currentTime, int effect) {
		int a = 0;
		try {
			CopyOnWriteArrayList<Buff> tBuffs = tMember.getBuffs();
			if (tBuffs!=null) {
				for (Buff buff : tBuffs) {
					if (buff.getEffect()==effect) {
						int lastTime = buff.getLastTime();
						int startTime = buff.getStartTime();
						if (currentTime - startTime>=lastTime) {
							tBuffs.remove(buff);
						}else {
							if (a<buff.getEffectValue()) {
								a = buff.getEffectValue();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return a;
	}
	/** 
	 * 徒弟id2
	 * 分身   分身术
	 * 伤害 + 定身   		定身术
	 * 伤害 + debuff		火眼金睛
	 * 伤害 + buff     	法天象地
	 */
	public boolean skill(SysSkill sysSkill, int hid, Room room, int x, int y, int flag) {
		try {
			int skillId = sysSkill.getId();
			float cd = sysSkill.getCd();
			long timeMillis = TimeUtil.TimeMillis();
			Map<Integer, Men> members = room.getMembers();
			if (members==null) {
				return false;
			}
			Men tMember = getTmember(members, hid);
			Men member =  members.get(hid);
			if (tMember==null || member ==null) {
				return false;
			}
			float mp = member.getMp();
			int costMP = sysSkill.getCostMP();
			if (mp<costMP) {//不够魔法
				return false;
			}
			HashMap<Integer, Long> map = member.getCd();
			if (map != null && map.containsKey(skillId)) {
				Long lastTime = map.get(skillId);//上次释放技能时间
				if (timeMillis - lastTime<cd) {//技能cd没到
					return false;
				}
			}
			//扣除魔法
			member.setMp(mp-costMP);
			//设置cd
			if (map==null) {
				map = new HashMap<Integer, Long>();
			}
			
			map.put(skillId, timeMillis);
			member.setCd(map);
			
			skillEffect(sysSkill, hid, room, x, y, tMember, member,flag);
			System.out.println("hid:"+tMember.getHid()+"  buffs"+tMember.getBuffs());
			System.out.println("hid:"+member.getHid()+"  buffs"+member.getBuffs());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return false;
		}
	}
	/**
	 * 基础法术伤害+法术等级^2
	 * @param sysSkill
	 * @param hid
	 * @param room
	 * @param x
	 * @param y
	 * @param tMember
	 * @param member
	 */
	private void skillEffect(SysSkill sysSkill, int hid, Room room, int x, int y, Men tMember, Men member, int flag) {
		try {
			if (sysSkill==null) {
				return;
			}
			String zhaohuan = sysSkill.getZhaohuan();
			if (zhaohuan!=null) {
				call(room, member, zhaohuan, x, y,sysSkill.getId());
			}
			Float range = sysSkill.getRange();
			if (range==null) {
				buff(sysSkill, member, tMember,0,room.getId());
				float demage = sysSkill.getDemage();
				if (demage>0) {
					System.err.println("skillId:"+sysSkill.getId()+"skillName:"+sysSkill.getName()+" 最终伤害:"+demage);
					/**计算伤害*/
//					基础法术伤害+法术等级^2
					float hp = tMember.getHp();
					demage = skillDemage(member.getFali(), tMember.getFali(), demage);
					System.out.println("a:"+member.getFali()+" b:"+tMember.getFali()+" demage:"+demage);
					if (demage >= hp) {//被打死，战斗结束
						//**----------------------战斗结束---------------------**//
						tMember.setHp(0);
						room.getMembers().put(tMember.getHid(), tMember);
						room.setBattle(false);
						battleEnd(room, hid,0 , sysSkill.getId(), x, y, tMember.getHid(), flag);
						return;
					}
					tMember.setHp(hp - (int)demage);
				}
				return;
			}
			range = range * 80;//攻击范围，
			int hypot = (int) Math.hypot(tMember.getX() - x,tMember.getY() - y);
			if (hypot <= range) {//目标在技能范围内
				float demage = sysSkill.getDemage();
				buff(sysSkill, member, tMember,(int) demage,room.getId());
				/**计算buff*/
				if (demage>0) {
					/**计算伤害*/
//					人物技能伤害=基础法术伤害+法术等级^2
//					demage = demage/20*member.getFali()+demage;
					float hp = tMember.getHp();
					demage = skillDemage(member.getFali(), tMember.getFali(), demage);
					System.out.println("a:"+member.getFali()+" b:"+tMember.getFali()+" demage:"+demage);
					if (demage >= hp) {//被打死，战斗结束
						//**----------------------战斗结束---------------------**//
						tMember.setHp(0);
						room.getMembers().put(tMember.getHid(), tMember);
						room.setBattle(false);
						battleEnd(room, hid,0 , sysSkill.getId(), x, y, hid, flag);
						return;
					}
					tMember.setHp(hp - (int)demage);
				}
			}else {//
				String effect = sysSkill.getEffect();
				if (effect!=null && !effect.equals("")) {
					List<String> split = SysUtil.split(effect, "\n");
					for (String s : split) {
						List<String> list = SysUtil.split(s, ",");
						int target = Integer.parseInt(list.get(0));
						String eff = list.get(1);
						String string = list.get(2);//50%
						NumberFormat format = NumberFormat.getPercentInstance();
						Number parse = null;
						try {
							parse = format.parse(string);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (parse!=null) {
							int lastTime = Integer.parseInt(list.get(3));
							int effectValue = (int) (parse.floatValue()*100);
							if (target ==-1) {//目标是自己
								if (eff.equals(ATTUP)) {//增加攻击
									Buff buff = new Buff(Buff.UPATT, effectValue, lastTime);
									System.out.println("hid:"+member.getHid()+" "+buff.toString());
									CopyOnWriteArrayList<Buff> buffs = member.getBuffs();
									if (buffs == null) {
										buffs = new CopyOnWriteArrayList<Buff>();
									}
									buffs.add(buff);
									member.setBuffs(buffs);
								}
							}else if (target==2) {//目标是对方
								
							}
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
	 * 计算法术伤害
	 * @param fali 技能使用者法力
	 * @param fali2 被击中者法力
	 * @param demage 技能初始伤害
	 * @return
	 */
	public float skillDemage(int fali,int fali2,float demage) {
		//法力值相同，收到100%伤害
		if (fali == fali2) {
			return demage;
		}else if (fali>fali2) {
			int a = fali - fali2;
			float b = (float) (1/(1+0.02*a));
			demage = (1-b)*demage+demage;
			return demage;
		}else if (fali<fali2) {
			int a = fali2 - fali;
			float b = (float) (1/(1+0.02*a));
			demage = demage - demage*b;
			return demage;
		}else {
			return demage;
		}
	}
	/**
	 * 召唤物生成
	 * @param room 
	 * @param men 召唤物主人
	 */
	public void call(Room room, Men men, String zhaohuan, int x, int y, int skillId) {
		try {
			ConcurrentHashMap<Integer, Call> callMonsters = room.getCallMonsters();
			if (callMonsters==null) {
				callMonsters = new ConcurrentHashMap<Integer, Call>();
			}
			List<Integer> split = SysUtil.splitGetInt(zhaohuan, "\\|");
			Integer sysId = split.get(0);
			Integer num = split.get(1);
			SysCall sysCall = SysGloablMap.getCallMap().get(sysId);
			if (sysCall==null) {
				return;
			}
			for (int i = 0; i < num; i++) {
				float demage = sysCall.getDemage();
				Call call = new Call(sysId, men.getHid(), x, y, sysCall.getLastTime(),men.getAtt()*demage);
				callMonsters.put(call.getHid(), call);
				BattleHandler.getTestHandler().callMonster(room.getMembers(), call,2041,skillId);
				System.out.println("生成召唤物："+call);
			}
			room.setCallMonsters(callMonsters);
			CallMonster.getMonsterRoomIds().add(room.getId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 计算buff
	 * @param skill
	 * @param member
	 * @param tMember
	 */
	public void buff(SysSkill skill, Men member, Men tMember, int demage, int rid) {
		try {
			String effect = skill.getEffect();
			if (effect!=null && !effect.equals("")) {
				int currentTime = TimeUtil.currentTime();
				List<String> split = SysUtil.split(effect, "\n");
				for (String s : split) {
					List<String> list = SysUtil.split(s, ",");
					int target = Integer.parseInt(list.get(0));
					String eff = list.get(1);
					String string = list.get(2);//50%
					float effectValue = 0 ;
					if (!string.equals("0")) {
						NumberFormat format = NumberFormat.getPercentInstance();
						Number parse = format.parse(string);//0.5
						effectValue = parse.floatValue()*100;
					}
					int lastTime = Integer.parseInt(list.get(3));
					if (target ==-1) {//目标是自己
						if (eff.equals(ATTUP)) {//增加攻击
							Buff buff = new Buff(Buff.UPATT, (int) effectValue, lastTime);
//					 		System.out.println("hid:"+member.getHid()+" "+buff.toString());
							CopyOnWriteArrayList<Buff> buffs = member.getBuffs();
							if (buffs == null) {
								buffs = new CopyOnWriteArrayList<Buff>();
							}
							buffs.add(buff);
							member.setBuffs(buffs);
						}
					}else if (target == 2) {//
						if (eff.equals(REDUCEARMOR)) {//降低护甲
							Buff buff = new Buff(Buff.REDUCEARMOR, (int) effectValue, lastTime);
//							System.out.println("hid:"+member.getHid()+" "+buff.toString());
							CopyOnWriteArrayList<Buff> buffs = tMember.getBuffs();
							if (buffs == null) {
								buffs = new CopyOnWriteArrayList<Buff>();
							}
							buffs.add(buff);
							tMember.setBuffs(buffs);
							//降低护甲,计算护甲免伤
//							countAcDef(tMember, currentTime, lastTime, buff,buffs);
//							System.out.println("name："+member.getName()+" acdef:"+member.getAcdef());
						}else if(eff.equals(BURN)){//烧伤
							float n = effectValue/100*demage;
							CopyOnWriteArrayList<Float> burnDemage = new CopyOnWriteArrayList<Float>();
							for (int i = 0; i < lastTime; i++) {
								burnDemage.add(n);
							}
							Buff buff = new Buff(Buff.BURN, (int)effectValue, burnDemage);
							CopyOnWriteArrayList<Buff> buffs = tMember.getBuffs();
							if (buffs == null) {
								buffs = new CopyOnWriteArrayList<Buff>();
							}
							buffs.add(buff);
							tMember.setBuffs(buffs);
							Burn.getRoomIds().add(rid);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
//		System.out.println("hid:"+member.getHid()+"  buffs"+member.getBuffs());
	}
	/**
	 * 获取房间中的另外一个人
	 * @param members
	 * @param hid
	 * @return
	 */
	public Men getTmember(Map<Integer, Men> members, int hid) {
		if (members==null || hid==0) {
			return null;
		}
		try {
			Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Men> next = iterator.next();
				Men member = next.getValue();
				int tHid = member.getHid();
				if (tHid!=0 && tHid!=hid) {
					return member;
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 有人被打死，战斗结束
	 * @param room 
	 * @param hid 获胜者角色id
	 * @param thid 失败者
	 */
	public void battleEnd(Room room, int hid, int thid, int skillId, int x, int y, int winer, int flag) {
		try {
			//烧伤效果
			if (Burn.getRoomIds().contains(room.getId())) {
				Burn.getRoomIds().remove(room.getId());
			}
			
			//使用技能广播
			BattleHandler.getTestHandler().skill(thid, skillId, x, y, flag, hid, room);
			
			ConcurrentHashMap<Integer, Men> members = room.getMembers();
			if (members!=null) {
				Iterator<Entry<Integer, Men>> iterator = members.entrySet().iterator();
				while (iterator.hasNext()) {
					Men value = iterator.next().getValue();
					BattleHandler.getTestHandler().write2033(value, room);
				}
				int winCopper = 0;
				int winMoney = 3;
				int winShengWang = 0;
				Men winner = members.get(hid);
				Men loser = members.get(thid);
				if (loser==null) {
					loser = getTmember(members, hid);
				}
				thid = loser.getHid();
				
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
				Hero hero = GlobalMap.getHeroMap().get(hid);
				Hero tHero = GlobalMap.getHeroMap().get(thid);
				if (hero!=null && tHero!=null) {
					//获胜者增加荣誉，元宝
					logger.info(new StringBuffer().append("battle end winner:"+hero.getId()+"  loser:"+tHero.getId()).toString());
					hero.setHonour(hero.getHonour()+ Globalconstants.WINHONOUR);
					MoneyControl.moneyIncome(hero, Back_record.REASON_BATTLE, winMoney);
					connection.write(BattleHandler.getTestHandler().getByte(1017, hero.getMoney()));
					BattleHandler.getTestHandler().sendHonour(hero, connection);
					
					tHero.setHonour(tHero.getHonour()+ Globalconstants.LOSEHONOUR);
					BattleHandler.getTestHandler().sendHonour(tHero, connection);
					
					Connection conn = GlobalMap.getConns().get(tHero.getId());
					if (conn!=null) {
						ArenaHandler.getArenaHandler().setReport(hero, tHero.getTid(), thid, tHero.getName(),
								Report.TYPE_BATTLE,tHero.getStrength(),0);
					}
					BattleHandler.getTestHandler().quitRoom(hero,false);
					BattleHandler.getTestHandler().quitRoom(tHero,false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 房间信息
	 * @param room
	 * @param hid
	 * @throws IOException
	 */
	public void battleResult(Room room, int hid) throws IOException {
		try {
			Map<Integer, Men> members = room.getMembers();
			if (members!=null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(2037);
				output.writeInt(hid);
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
					Hero hero = GlobalMap.getHeroMap().get(key);
					if (hero!=null) {
						BattleHandler.getTestHandler().quitRoom(hero,false);
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
	}
	/**
	 * 持续技能后续伤害
	 * 中毒，烧伤   攻击(加攻击buff)*百分比
	 * @param skillId
	 * @param hid 角色id，一定不是0
	 * @param thid  技能目标角色id 
	 * @param room 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean lastSkill(int skillId, int hid, int thid, Room room, int x, int y, int flag) {
		try {
			SysSkill sysSkill = SysGloablMap.getSkillMap().get(skillId);
			if (sysSkill==null) {
				return false;
			}
			ConcurrentHashMap<Integer, Men> members = room.getMembers();
			if (members==null) {
				return false;
			}
			Men men = members.get(hid);
			Men tmember = getTmember(members, hid);
			if (men==null || tmember ==null) {
				return false;
			}
			int currentTime = TimeUtil.currentTime();
			String name = sysSkill.getName();
			if (name!=null && name.equals("火眼金睛")) {
				CopyOnWriteArrayList<Buff> buffs = men.getBuffs();
				if (buffs==null || buffs.size()==0) {
					return false;
				}
				float demage = 0;
				for (Buff buff : buffs) {
					int effect = buff.getEffect();
					if (effect == Buff.BURN) {
						int startTime = buff.getStartTime();
						int lastTime = buff.getLastTime();
						if (currentTime-startTime>lastTime) {
							buffs.remove(buff);
						}else {
							if (demage<buff.getEffectValue()) {
								demage = buff.getEffectValue();
							}
						}
					}
				}
				float att = 0;
				Iterator<Buff> iterator = tmember.getBuffs().iterator();
				while (iterator.hasNext()) {
					Buff buff = iterator.next();
					int effect = buff.getEffect();
					if (effect == Buff.UPATT) {
						int startTime = buff.getStartTime();
						int lastTime = buff.getLastTime();
						if (currentTime-startTime>lastTime) {
							buffs.remove(buff);
						}else {
							if (att<buff.getEffectValue()) {
								att = buff.getEffectValue();
							}
						}
					}
				}
				att = tmember.getAtt()*(1+att/100);
				demage = demage * att;
				System.out.println("烧伤伤害："+demage);
				float hp = men.getHp();
				if (hp<=demage) {
					tmember.setHp(0);
					//
					members.put(tmember.getHid(), tmember);
					room.setBattle(false);
					battleEnd(room, hid, thid, skillId, x, y, hid, flag);
					return false;
				}else {
					men.setHp(hp-demage);
				}
				return true;
			}else if(name!=null && name.equals("雷阵雨")){
				double hypot = Math.hypot(tmember.getX()-x, tmember.getY()-y);
				float range = sysSkill.getRange();
				if (hypot<=range*80) {//在技能范围内
//					基础法术伤害+法术等级^2
					int demage = sysSkill.getDemage();
					demage = (int) skillDemage(men.getFali(), tmember.getFali(), demage);
					System.out.println("a:"+men.getFali()+" b:"+tmember.getFali()+" demage:"+demage);
					float hp = tmember.getHp();
					if (demage >= hp) {//被打死，战斗结束
						tmember.setHp(0);
						members.put(tmember.getHid(), tmember);
						room.setBattle(false);
						battleEnd(room, hid, thid, skillId, x, y, hid, flag);
						return false;
					}
					tmember.setHp(hp - demage);
				}
				return true;
			}else if (name!=null && name.equals("沙漠送葬")) {
				System.out.println("沙漠送葬");
				double hypot = Math.hypot(tmember.getX()-x, tmember.getY()-y);
				float range = sysSkill.getRange();
				if (hypot<=range*80) {//在技能范围内
					int demage = sysSkill.getDemage();
					System.err.println("skillId:"+sysSkill.getId()+" demage:"+demage);
					demage = (int) skillDemage(men.getFali(), tmember.getFali(), demage);
					System.out.println("a:"+men.getFali()+" b:"+tmember.getFali()+" demage:"+demage);
					float hp = tmember.getHp();
					if (demage >= hp) {//被打死，战斗结束
						//**----------------------战斗结束---------------------**//
						tmember.setHp(0);
						members.put(tmember.getHid(), tmember);
						room.setBattle(false);
						battleEnd(room, hid, thid, skillId, x, y, hid, flag);
						return false;
					}
					tmember.setHp(hp - demage);
				}
				return true;
			}else if (name!=null && name.equals("天地异变")){
				System.out.println("天地异变");
				double hypot = Math.hypot(tmember.getX()-x, tmember.getY()-y);
				float range = sysSkill.getRange();
				if (hypot<=range*80) {//在技能范围内
					int demage = sysSkill.getDemage();
					/**计算伤害*/
					System.err.println("skillId:"+sysSkill.getId()+" demage:"+demage);
					demage = (int) skillDemage(men.getFali(), tmember.getFali(), demage);
					System.out.println("a:"+men.getFali()+" b:"+tmember.getFali()+" demage:"+demage);
					float hp = tmember.getHp();
					if (demage >= hp) {//被打死，战斗结束
						//**----------------------战斗结束---------------------**//
						tmember.setHp(0);
						members.put(tmember.getHid(), tmember);
						room.setBattle(false);
						battleEnd(room, hid, thid, skillId, x, y, hid, flag);
						return false;
					}
					tmember.setHp(hp - demage);
				}
				return true;
			}else if (name!=null && name.equals("法天象地")) {
				System.out.println("天地异变");
				double hypot = Math.hypot(tmember.getX()-x, tmember.getY()-y);
				float range = sysSkill.getRange();
				if (hypot<=range*80) {//在技能范围内
					int demage = sysSkill.getDemage();
					/**计算伤害*/
					System.err.println("skillId:"+sysSkill.getId()+" demage:"+demage);
					demage = (int) skillDemage(men.getFali(), tmember.getFali(), demage);
					System.out.println("a:"+men.getFali()+" b:"+tmember.getFali()+" demage:"+demage);
					float hp = tmember.getHp();
					if (demage >= hp) {//被打死，战斗结束
						//**----------------------战斗结束---------------------**//
						tmember.setHp(0);
						members.put(tmember.getHid(), tmember);
						room.setBattle(false);
						battleEnd(room, hid, thid, skillId, x, y, hid, flag);
						return false;
					}
					tmember.setHp(hp - demage);
				}
				return true;
			}
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 自身攻击的平方/（自身攻击+对方防御）/2
	 * @param hid
	 * @param thid
	 * @param room
	 */
	public void callAttack(Men tMen, Call call, Room room) {
		try {
			int sysId = call.getSysId();
			SysCall sysCall = SysGloablMap.getCallMap().get(sysId);
			if (sysCall==null) {
				return;
			}
			float attSpeed = sysCall.getAttSpeed();
			attSpeed = attSpeed*100;
			long currentTime = TimeUtil.TimeMillis();
			long attTime = call.getAttTime();
			if (attTime==0 || currentTime-attSpeed>=attTime) {//可以攻击
				int skillId = sysCall.getSkillId();
//					float demage = sysSkill.getDemage();
					float att = call.getAtt();
//					基础法术伤害+法术等级^2
//					demage = demage+sysSkill.getLevel()*2;
//					System.out.println("hid:"+tMen.getHid()+" hp:"+tMen.getHp()+" time:"+currentTime/1000);
					
					int d = count(tMen, TimeUtil.currentTime(), Buff.REDUCEARMOR);
					float armor = tMen.getArmor();
					float a = d/100;//减护甲%
					armor = armor*(1-a);//最终护甲
//					自身攻击的平方/（自身攻击+对方防御）/2
					
					float demage = att*att/(att+armor)/2;
					
					float hp = tMen.getHp();
					if (demage>hp) {//打死了
						tMen.setHp(0);
						room.getMembers().put(tMen.getHid(), tMen);
						room.setBattle(false);
						battleEnd(room, call.getFather(), tMen.getHid(), skillId, tMen.getX(), tMen.getY(), call.getFather(), 1);
					}else {
						tMen.setHp(hp-demage);
					}
					call.setAttTime(currentTime);
					System.out.println("召唤物伤害："+demage);
					BattleHandler.getTestHandler().callAttackSend(room.getMembers(), tMen.getHid(), skillId, call.getHid());
			}
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
	}
	
//	public void buffTimer(Men men) {
//		CopyOnWriteArrayList<Buff> buffs = men.getBuffs();
//		if (buffs!=null && buffs.size()>0) {
//			int currentTime = TimeUtil.currentTime();
//			Iterator<Buff> iterator = buffs.iterator();
//			while (iterator.hasNext()) {
//				Buff buff = iterator.next();
//				int lastTime = buff.getLastTime();
//				int startTime = buff.getStartTime();
//				if (currentTime-startTime>=lastTime) {//到时间了
//					//降低护甲,计算护甲免伤
//					countAcDef(men, currentTime, lastTime, buff,buffs);
//				}
//			}
//		}
//	}
}

