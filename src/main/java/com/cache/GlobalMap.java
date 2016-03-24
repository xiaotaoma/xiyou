package com.cache;

import com.model.Account;
import com.model.Bag;
import com.model.Friend;
import com.model.Hero;
import com.model.backstage.Back_money;
import com.model.backstage.Back_record;
import com.model.backstage.Back_tool;
import com.socket.battle.Men;
import com.socket.battle.Room;
import org.glassfish.grizzly.Connection;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 
 * @author Administrator
 *
 */
public class GlobalMap {
	
	private static CopyOnWriteArraySet<Connection> allConnections = new CopyOnWriteArraySet<Connection>();
	/**
	 * 所有连接信息
	 * @return
	 */
	public static CopyOnWriteArraySet<Connection> getAllConnections() {
		return allConnections;
	}
	
//	/**
//	 * 登陆验证码
//	 */
//	private static Map<String, Login> checkLoginMap = new ConcurrentHashMap<String, Login>(100,0.75f);
//	public static Map<String, Login> getCheckLoginMap() {
//		return checkLoginMap;
//	}
	/**
	 * key=角色id，
	 */
	private static Map<Integer, Connection> conns = new ConcurrentHashMap<Integer, Connection>(4000,0.75f);
	/**
	 * 角色id对应连接
	 * @return
	 */
	public static Map<Integer, Connection> getConns() {
		return conns;
	}
	private static Map<String, Account> accountMap = new ConcurrentHashMap<String, Account>(4000,0.75f);
	/**
	 * 账号
	 */
	public static Map<String, Account> getAccountMap() {
		return accountMap;
	}
	private static Map<String, Connection> accConnections = new ConcurrentHashMap<String, Connection>(4000,0.75f);
	/**
	 * 账号对应连接
	 */
	public static Map<String, Connection> getAccConnections() {
		return accConnections;
	}
	
	/**
	 * 角色
	 */
	private static Map<Integer, Hero> heroMap = new ConcurrentHashMap<Integer, Hero>(4000,0.75f);
	public static Map<Integer, Hero> getHeroMap() {
		return heroMap;
	}
	
	private static Map<String, Integer> heroNameMap = new ConcurrentHashMap<String, Integer>();
	/**
	 * 角色名对应角色信息
	 * @return
	 */
	public static Map<String, Integer> getHeroNameMap() {
		return heroNameMap;
	}
	
	/**
	 * 好友
	 */
	private static Map<Integer, Friend> friendMap = new ConcurrentHashMap<Integer, Friend>(4000,0.75f);
	public static Map<Integer, Friend> getFriendMap() {
		return friendMap;
	}
	
	private static Map<Integer, Room> roomMap = new ConcurrentHashMap<Integer, Room>(500,0.75f);
	/**
	 * 战斗房间 
	 * rid房间id
	 */
	public static Map<Integer, Room> getRoomMap() {
		return roomMap;
	}
	
	private static CopyOnWriteArrayList<Integer> roomIds = new CopyOnWriteArrayList<Integer>();
	/**
	 * 战斗房间id
	 * @return
	 */
	public static CopyOnWriteArrayList<Integer> getRoomIds() {
		return roomIds;
	}
	public static CopyOnWriteArrayList<Integer> emptyRooms = new CopyOnWriteArrayList<Integer>();
	/**
	 * 空房间
	 * @return
	 */
	public static CopyOnWriteArrayList<Integer> getEmptyRooms() {
		return emptyRooms;
	}
	
	private static CopyOnWriteArrayList<Hero> honourRank = new CopyOnWriteArrayList<Hero>();
	/**
	 * 荣誉排行榜
	 * @return
	 */
	public static CopyOnWriteArrayList<Hero> getHonourRank() {
		return honourRank;
	}
	private static CopyOnWriteArrayList<Integer> honourHidRank = new CopyOnWriteArrayList<Integer>();
	/**
	 * 荣誉id排行榜
	 * @return
	 */
	public static CopyOnWriteArrayList<Integer> getHonourHidRank() {
		return honourHidRank;
	}
	
	/** 统计缓存 **/
	private static Map<Integer, CopyOnWriteArrayList<Back_record>> recordMap = new ConcurrentHashMap<Integer,  CopyOnWriteArrayList<Back_record>>();
	/**
	 * 元宝统计记录
	 * @return
	 */
	public static Map<Integer,  CopyOnWriteArrayList<Back_record>> getRecordMap() {
		return recordMap;
	}
	
	
	private static CopyOnWriteArraySet<Integer> oneDayOld = new CopyOnWriteArraySet<Integer>();
	/**
	 * 前一天注册角色今天登陆数
	 * @return
	 */
	public static CopyOnWriteArraySet<Integer> getOneDayOld() {
		return oneDayOld;
	}
	
	private static CopyOnWriteArrayList<Men> queue = new CopyOnWriteArrayList<Men>();
	/**
	 * 徒弟大战排队队列
	 * @return
	 */
	public static CopyOnWriteArrayList<Men> getQueue() {
		return queue;
	}
	
	private static Map<Integer, Map<Integer, Back_money>> moneyMap = new ConcurrentHashMap<Integer, Map<Integer,Back_money>>();
	/**
	 * 元宝统计
	 * @return
	 */
	public static Map<Integer, Map<Integer, Back_money>> getMoneyMap() {
		return moneyMap;
	}
	
	private static Map<Integer, Bag> bagMap = new ConcurrentHashMap<Integer, Bag>();
	/**
	 * 背包
	 * @return
	 */
	public static Map<Integer, Bag> getBagMap() {
		return bagMap;
	}
	
	private static Map<Integer, List<Back_tool>> backToolMap = new ConcurrentHashMap<Integer, List<Back_tool>>();
	/**
	 * 背包道具记录
	 * @return
	 */
	public static Map<Integer, List<Back_tool>> getBackToolMap() {
		return backToolMap;
	}
}
