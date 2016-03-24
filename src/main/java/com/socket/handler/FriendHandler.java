package com.socket.handler;

import com.cache.GlobalMap;
import com.main.BaseAction;
import com.model.Friend;
import com.model.FriendUnit;
import com.model.Hero;
import com.service.FriendManager;
import com.service.HeroManager;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FriendHandler {
	private static FriendManager friendManager = (FriendManager) BaseAction.getIntance().getBean("friendManager");
	private static Logger logger = LoggerFactory.getLogger(FriendHandler.class);
	private static FriendHandler friendHandler;
	public static FriendHandler getFriendHandler() {
		if (friendHandler==null) {
			friendHandler = new FriendHandler();
		}
		return friendHandler;
	}
	
	public void friendList(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes!=null) {
				Object hidObj = connection.getAttributes().getAttribute("hid");
				if (hidObj!=null) {
					int hid = (Integer) hidObj;
					Hero hero = GlobalMap.getHeroMap().get(hid);
					if (hero==null) {
						return;
					}
					
					Friend friend = GlobalMap.getFriendMap().get(hid);
					if (friend==null) {
						return;
					}
					Map<Integer, FriendUnit> map = friend.getMap();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(3002);
					if (map==null) {
						output.writeShort(0);
					}else {
						output.writeShort(map.size());
						Iterator<Entry<Integer, FriendUnit>> iterator = map.entrySet().iterator();
						HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
						while (iterator.hasNext()) {
							FriendUnit friendUnit = iterator.next().getValue();
							int hid2 = friendUnit.getHid();
							output.writeInt(friendUnit.getHid());
							output.writeUTF(friendUnit.getName());
							if (GlobalMap.getHeroMap().containsKey(hid2)) {
								output.writeByte(1);
							}else {
								output.writeByte(0);
							}
//							if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
								Hero hero2 = GlobalMap.getHeroMap().get(hid2);
								if (hero2 == null) {
									hero2 = heroManager.getHeroById(hid2);
								}
								int endCardTime = hero2.getEndCardTime();
								int dayBetween = TimeUtil.getDayBetween(TimeUtil.currentTime(), endCardTime);
								if (dayBetween > 0) {
									output.writeByte(1);
								} else {
									output.writeByte(0);
								}
//							}
						}
					}
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
	/**
	 * 添加好友
	 * @param obj1
	 * @param obj2
	 */
	public void addFriend(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
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
				int fhid = input.readInt();
				bis.close();
				input.close();
				
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				//不能是自己
				if (fhid == hid) {
					addFriend(connection, 2);
					return;
				}
				Hero fhero = GlobalMap.getHeroMap().get(fhid);
				if (fhero==null) {
					HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
					fhero = heroManager.getHeroById(fhid);
					if (fhero == null) {//没有这个人
						return;
					}
				}
				
				Friend friend = GlobalMap.getFriendMap().get(hid);
				//已经是好友了
				if (friend==null) {
					return;
				}
				Map<Integer, FriendUnit> map = friend.getMap();
				if (map==null) {
					return;
				}
				if (map.containsKey(fhid)) {
					addFriend(connection, 2);
					return;
				}
				
				FriendUnit unit = new FriendUnit();
				unit.setName(fhero.getName());
				unit.setHid(fhid);
				unit.setHead(0);
				friend.getMap().put(unit.getHid(), unit);
				friend.setData();
				addFriend(connection, 1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通知添加好友成功
	 * @param connection
	 * @param result 结果成功失败。。
	 * @param hid 添加角色id
	 * @param name 添加角色名
	 * @param head 头像
	 */
	public void addFriend(Connection connection,int result) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(3005);
			output.writeByte(result);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 删除好友
	 */
	public void delFriend(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
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
				int fhid = input.readInt();//删除的好友的角色id
				bis.close();
				input.close();
				Object hidObj = connection.getAttributes().getAttribute("hid");
				if (hidObj!=null) {
					int hid = (Integer) hidObj;
					Hero hero = GlobalMap.getHeroMap().get(hid);
					if (hero==null) {
						return;
					}
					delHero(connection, hero, hid, fhid);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param connection
	 * @param hero
	 * @param otherHid
	 */
	public void delHero(Connection connection, Hero hero, int hid, int otherHid) {
		try {
			/**-----------------自己-----------------**/
			Friend friend = GlobalMap.getFriendMap().get(hid);
			if (friend==null) {
				/*ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(3007);
				output.writeByte(1);
				bos.close();
				output.close();
				connection.write(bos.toByteArray());*/
				connection.write(SysUtil.getBytes(3007, (byte) 1));
				return;
			}
			Map<Integer, FriendUnit> map = friend.getMap();
			if (map==null) {
				return;
			}
			map.remove(otherHid);
			/**-----------------自己-----------------**/
			connection.write(SysUtil.getBytes(3007, (byte) 1));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void sync(int hid) {
		Friend friend = GlobalMap.getFriendMap().get(hid);
		if (friend!=null) {
			try {
				friend.setData();
				friendManager.update(friend);
				GlobalMap.getFriendMap().remove(friend);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("", e);
			}
		}
	}
}
