package com.model.sys;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SysGloablMap {
	
	
	private static Map<Integer, String> waveMap = new ConcurrentHashMap<Integer, String>();
	/**
	 * 竞技场没波出怪
	 * @return
	 */
	public static Map<Integer, String> getWaveMap() {
		return waveMap;
	}
	/**
	 * 竞技场出怪配置
	 */
	private static Map<Integer, ArenaMonster> monsterMap = new ConcurrentHashMap<Integer, ArenaMonster>();
	public static Map<Integer, ArenaMonster> getMonsterMap() {
		return monsterMap;
	}
	/**
	 * 出怪分配表
	 * key1=出怪总数  key2=并行数.
	 * 拆分
	 */
	private static Map<Integer, Map<Integer, List<List<Integer>>>> map = new ConcurrentHashMap<Integer, Map<Integer,List<List<Integer>>>>();
	public static Map<Integer, Map<Integer, List<List<Integer>>>> getMap() {
		return map;
	}
	
	private static Map<Integer, Map<Integer, Money>> moneyMap = new ConcurrentHashMap<Integer, Map<Integer,Money>>();
	/**
	 * 
	 * @return
	 */
	public static Map<Integer, Map<Integer, Money>> getMoneyMap() {
		return moneyMap;
	}
	
	
	private static Map<Integer, SysSkill> skillMap = new ConcurrentHashMap<Integer, SysSkill>();
	/**
	 * 技能
	 */
	public static Map<Integer, SysSkill> getSkillMap() {
		return skillMap;
	}
	
	private static Map<Integer, SysCall> callMap = new ConcurrentHashMap<Integer, SysCall>();
	/**
	 * 召唤物
	 * @return
	 */
	public static Map<Integer, SysCall> getCallMap() {
		return callMap;
	}
	
	private static Map<String, Product> productMap = new ConcurrentHashMap<String, Product>();
	/**
	 * 产品
	 * @return
	 */
	public static Map<String, Product> getProductMap() {
		return productMap;
	}
	
	private static Map<Integer, Product91> product91Map = new ConcurrentHashMap<Integer, Product91>();
	/**
	 * 91产品
	 * @return
	 */
	public static Map<Integer, Product91> getProduct91Map() {
		return product91Map;
	}
	
	private static Map<Integer, Tool> toolMap = new ConcurrentHashMap<Integer, Tool>();
	/**
	 * 道具信息
	 * @return
	 */
	public static Map<Integer, Tool> getToolMap() {
		return toolMap;
	}
	
	private static Map<Integer, Shop> shopMap = new ConcurrentHashMap<Integer, Shop>();
	/**
	 * 商店
	 * @return
	 */
	public static Map<Integer, Shop> getShopMap() {
		return shopMap;
	}
	
	private static Map<Integer, Integer> awardMap = new ConcurrentHashMap<Integer, Integer>();
	static{
		awardMap.put(8, 16001);
		awardMap.put(15, 16002);
		awardMap.put(30, 16003);
		awardMap.put(50, 16004);
		awardMap.put(70, 16005);
		awardMap.put(80, 16006);
		awardMap.put(90, 16007);
		awardMap.put(100, 16008);
	}
	/**
	 * 到达波数赠送后礼包
	 * @return
	 */
	public static Map<Integer, Integer> getAwardMap() {
		return awardMap;
	}
	
	
	private static Map<Integer, ProductDianxin> productDianxinMap = new ConcurrentHashMap<Integer, ProductDianxin>();
	/**
	 * 电信支付商品
	 * @return
	 */
	public static Map<Integer, ProductDianxin> getProductDianxinMap() {
		return productDianxinMap;
	}
	
	private static Map<Integer, Task> taskMap = new ConcurrentHashMap<Integer, Task>();
	/**
	 * 任务
	 * @return
	 */
	public static Map<Integer, Task> getTaskMap() {
		return taskMap;
	}
	
	private static Map<Integer, MonsterLoot> lootMap = new ConcurrentHashMap<Integer, MonsterLoot>();
	/**
	 * 掉落列表
	 * @return
	 */
	public static Map<Integer, MonsterLoot> getLootMap() {
		return lootMap;
	}
	
	private static Map<Integer, CopperShop> copperShopMap = new ConcurrentHashMap<Integer, CopperShop>();
	/**
	 * 铜币商店
	 * @return
	 */
	public static Map<Integer, CopperShop> getCopperShopMap() {
		return copperShopMap;
	}
	private static Map<Integer, ProductYiDong> yidongProductMap = new ConcurrentHashMap<Integer, ProductYiDong>();
	/**
	 * 移动产品
	 * @return
	 */
	public static Map<Integer, ProductYiDong> getYidongProductMap() {
		return yidongProductMap;
	}
}
