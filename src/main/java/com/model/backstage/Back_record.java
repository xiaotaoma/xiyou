package com.model.backstage;

import com.cache.GlobalMap;
import com.main.BaseAction;
import com.service.Back_recordManager;
import com.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Back_record {
	private static Logger logger = LoggerFactory.getLogger(Back_record.class);
	/*-----------------------------------*/
	public static final int TYPE_ADDITION = 1;
	public static final int TYPE_REDUCTION = 2;
	/*-----------------------------------*/
	public static final int REASON_RECHARGE = 1;//充值
	public static final int REASON_GM = 2;//后门
	public static final int REASON_GUARDLEVELUP = 3;//护卫升级
	public static final int REASON_EQUIPSTRONG = 4;//装备强化
	public static final int REASON_SHAKETREE = 5;//摇钱
	public static final int REASON_BATTLE = 6;//徒弟大战获得
	public static final int REASON_BATTLETIMES = 7;//购买土地大战次数
	public static final int REASON_WORLDCHAT = 8;//世界聊天
	public static final int REASON_TUDI = 9;//解锁徒弟
	public static final int REASON_SKILL = 10;//法术升级,修为提升
	public static final int REASON_EQUIPMENT = 11;//装备进阶
	public static final int REASON_ACTIVITY = 12;//活动获取
	public static final int REASON_BUYFROMSHOP = 13;//从商店购买道具
	public static final int REASON_USELIBAO = 14;//使用礼包
	public static final int REASON_UPGUARD = 15;//升级护卫
	public static final int REASON_INVITED = 16;//被邀请奖励
	public static final int REASON_LOOT = 17;//掉落怪物
	public static final int REASON_VIP = 18;//vip每日奖励
	public static final int REASON_BUYCOPPER = 19;//元宝兑换铜币
//	1解锁徒弟
//	2法术升级,修为提升
//	4装备进阶
	public Back_record(int num,int type,int reason,int hid) {
		this.num = num;
		this.type = type;
		this.reason = reason;
		this.hid = hid;
		this.time = TimeUtil.currentTime();
	}
	/**
	 * 添加记录
	 * @param num
	 * @param type
	 * @param reason
	 */
	public static void setRecord(int hid,int num,int type,int reason) {
		try {
			Back_record record = new Back_record(num, type, reason,hid);
			CopyOnWriteArrayList<Back_record> list = GlobalMap.getRecordMap().get(hid);
			if (list==null) {
				list = new CopyOnWriteArrayList<Back_record>();
			}
			list.add(record);
			GlobalMap.getRecordMap().put(hid, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 保存数据
	 */
	public static void sync(int hid) {
		try {
			if (GlobalMap.getRecordMap().containsKey(hid)) {
				Back_recordManager recordManager = (Back_recordManager) BaseAction.getIntance().getBean("recordManager");
				CopyOnWriteArrayList<Back_record> list = GlobalMap.getRecordMap().get(hid);
				Iterator<Back_record> iterator = list.iterator();
				while (iterator.hasNext()) {
					Back_record record = iterator.next();
					recordManager.insert(record);
				}
				GlobalMap.getRecordMap().remove(hid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	
	private int id;
	private int num;//元宝数量
	private int type;//增加还是减少1增加2减少
	private int reason;//原因
	private int hid;//角色id
	private int time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getHid() {
		return hid;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getTime() {
		return time;
	}
	
	
	
}
