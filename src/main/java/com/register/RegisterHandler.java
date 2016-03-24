package com.register;


import com.grizzly.server.GrizzlyServerFilter;
import com.socket.battle.BattleHandler;
import com.socket.battle.BattleQueueHandler;
import com.socket.handler.*;
import org.apache.mina.adapter.MinaHandlerAdapter;
import org.apache.mina.dispatcherHandler.Dispatcher;
import org.apache.mina.dispatcherHandler.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterHandler extends MinaHandlerAdapter{
	private static Logger logger = LoggerFactory.getLogger(RegisterHandler.class);
	
	private Dispatcher dispatcher;

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}
	
	private GrizzlyServerFilter grizzlyServerFilter;
	public void setGrizzlyServerFilter(GrizzlyServerFilter grizzlyServerFilter) {
		this.grizzlyServerFilter = grizzlyServerFilter;
	}
	//登陆协议
	public static final int login=1000;
	public static final int arena = 2000;
	public static final int friend = 3000;
	public static final int report = 4000;
	public static final int chat = 5000;
	public static final int pay = 6000;
	public static final int activity = 7000;
	public static final int bag = 8000; 
	@Override
	public void initialize() {
		registerLoginHandler();
		registerArenaHandler();
		registerFriendHandler();
		registerChatHandler();
		registerPayHandler();
		registerActivityHandler();
		registerBagHandler();
	}
	
	public void registerLoginHandler() {
		dispatcher.put(login, this);
		//请求登陆
		putInvoker(login+1, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				AccountHandler.getAccountHandler().requestLogin(arg0, arg1);
			}
		});
		//请求角色
		putInvoker(login+3, new Invoker() {
			@Override 
			public void invoke(Object arg0, Object arg1) {
				AccountHandler.getAccountHandler().getHero(arg0, arg1);
			}
		});
		//创建角色
		putInvoker(login+6, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				AccountHandler.getAccountHandler().createHero(arg0, arg1);
			}
		});
		//同步战斗力
		putInvoker(login+7, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				AccountHandler.getAccountHandler().syncStrength(arg0, arg1);
			}
		});
		putInvoker(login+8, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				HeroHandler.getHeroHandler().heartbeat(arg0, arg1);
			}
		});
		//花费元宝
		putInvoker(login+10, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				HeroHandler.getHeroHandler().spendMoney(arg0, arg1);
			}
		});
		//摇钱
		putInvoker(login+14, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				HeroHandler.getHeroHandler().shakeTree(arg0, arg1);
			}
		});
		//下线
		putInvoker(login+19, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				HeroHandler.getHeroHandler().logout(arg0, arg1);
			}
		});
	}
	
	/**
	 * 竞技场
	 */
	public void registerArenaHandler() {
		dispatcher.put(arena, this);
		//
		putInvoker(arena+1, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ArenaHandler.getArenaHandler().open(arg0, arg1);
			}
		});
		//自动匹配
		putInvoker(arena+4, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ArenaHandler.getArenaHandler().match(arg0, arg1);
			}
		});
		//挑战
		putInvoker(arena+9, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ArenaHandler.getArenaHandler().battle(arg0, arg1);
			}
		});
		//下一波怪信息
		putInvoker(arena+11, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ArenaHandler.getArenaHandler().waveData(arg0, arg1);
			}
		});
		putInvoker(arena+13, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ArenaHandler1.getArenahandler1().battleResult(arg0, arg1);
			}
		});
		putInvoker(arena+15, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().heroList(arg0, arg1);
			}
		});
		
		//创建房间
		putInvoker(arena+16, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().makeRoom(arg0, arg1);
			}
		});
		//房间信息
		putInvoker(arena+18, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().roomList(arg0, arg1);
			}
		});
		//进入房间
		putInvoker(arena+21, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().inRoom(arg0, arg1);
			}
		});
		//准备
		putInvoker(arena+23, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().ready(arg0, arg1);
			}
		});
		//开始游戏
		putInvoker(arena+25, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().enterMap(arg0, arg1);
			}
		});
		
		putInvoker(arena+28, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().move(arg0, arg1);
			}
		});
		
		//退出房间
		putInvoker(arena+30, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().quitRoom(arg0, arg1);
			}
		});
		
		//更新坐标
		putInvoker(arena+34, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().memberPosition(arg0, arg1);
			}
		});
		//使用技能
		putInvoker(arena+35, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().useSkill(arg0, arg1);
			}
		});
		//加血加魔
		putInvoker(arena+38, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().addHpMp(arg0, arg1);
			}
		});
		//加载地图完成
		putInvoker(arena+39, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().loadMap(arg0, arg1);
			}
		});
		//召唤物移动
		putInvoker(arena+42, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().monsterPosition(arg0, arg1);
			}
		});
		//召唤物攻击
		putInvoker(arena+44, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().callAttack(arg0, arg1);
			}
		});
		//召唤物移动
		putInvoker(arena+47, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().callMove(arg0, arg1);
			}
		});
		//荣誉排行榜
		putInvoker(arena+49, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				RankHandler.getRankHandler().honourRank(arg0, arg1);
			}
		});
		//购买竞技场次数
		putInvoker(arena+53, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleHandler.getTestHandler().buyBattleTimes(arg0, arg1);
			}
		});
		
		//开始排队
		putInvoker(arena+55, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleQueueHandler.getBattleQueueHandler().enterQueue(arg0, arg1);
			}
		});
		//离开排队
		putInvoker(arena+57, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				BattleQueueHandler.getBattleQueueHandler().quitQueue(arg0, arg1);
			}
		});
	}
	
	public void registerFriendHandler() {
		dispatcher.put(friend, this);
		//登陆查看好友列表
		putInvoker(friend+1, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				FriendHandler.getFriendHandler().friendList(arg0, arg1);
			}
		});
		putInvoker(friend+4, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				FriendHandler.getFriendHandler().addFriend(arg0, arg1);
			}
		});
		putInvoker(friend+6, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				FriendHandler.getFriendHandler().delFriend(arg0, arg1);
			}
		});
	}
	/**
	 * 聊天
	 */
	public void registerChatHandler() {
		dispatcher.put(chat, this);
		putInvoker(chat+1, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ChatHandler.getChatHandler().chat(arg0, arg1);
			}
		});
		
		putInvoker(chat+4, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ChatHandler.getChatHandler().worldChat(arg0, arg1);
			}
		});
		
	}
	/**
	 * 
	 */
	public void registerReportHandler() {
		dispatcher.put(report, this);
		putInvoker(report+5, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ArenaHandler.getArenaHandler().sendReport(arg0, arg1);
			}
		});
	}
	public void registerPayHandler() {
		dispatcher.put(pay, this);
		//请求发货
		putInvoker(pay+3, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				PayHandler.getPayHandler().give(arg0, arg1);
			}
		});
	}
	
	public void registerActivityHandler() {
		dispatcher.put(activity, this);
		putInvoker(activity+1, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ActivityHandler.getActivityHandler().open(arg0, arg1);
			}
		});
		putInvoker(activity+4, new Invoker() {
			@Override
			public void invoke(Object arg0, Object arg1) {
				ActivityHandler.getActivityHandler().getAward(arg0, arg1);
			}
		});
		
		putInvoker(activity+6, new Invoker() {//波数排行
			@Override
			public void invoke(Object arg0, Object arg1) {
				RankHandler.getRankHandler().waveRank(arg0, arg1);
			}
		});
		
		putInvoker(activity+8, new Invoker() {//战斗力排行
			@Override
			public void invoke(Object arg0, Object arg1) {
				RankHandler.getRankHandler().strengthRank(arg0, arg1);
			}
		});
		
		putInvoker(activity+10, new Invoker() {//抢夺排行
			@Override
			public void invoke(Object arg0, Object arg1) {
				RankHandler.getRankHandler().robCopperRank(arg0, arg1);
			}
		});
		
		putInvoker(activity+12, new Invoker() {//获取vip时间
			@Override
			public void invoke(Object arg0, Object arg1) {
				ActivityHandler.getActivityHandler().getVip(arg0, arg1);
			}
		});
		
		putInvoker(activity+14, new Invoker() {//领取vip每日元宝
			@Override
			public void invoke(Object arg0, Object arg1) {
				ActivityHandler.getActivityHandler().getVipDailyAward(arg0, arg1);
			}
		});
	}
	private void registerBagHandler() {
		dispatcher.put(bag, this);
		putInvoker(bag+2, new Invoker() {//战斗力排行
			@Override
			public void invoke(Object arg0, Object arg1) {
				BagHandler.getBagHandler().buyTools(arg0, arg1);	
			}
		});
		putInvoker(bag+4, new Invoker() {//
			@Override
			public void invoke(Object arg0, Object arg1) {
				BagHandler.getBagHandler().useTools(arg0, arg1);
			}
		});
		putInvoker(bag+6, new Invoker() {//
			@Override
			public void invoke(Object arg0, Object arg1) {
				BagHandler.getBagHandler().getPracticeTime(arg0, arg1);
			}
		});
		putInvoker(bag+8, new Invoker() {//
			@Override
			public void invoke(Object arg0, Object arg1) {
				BagHandler.getBagHandler().getPracticeExp(arg0, arg1);
			}
		});
		
		putInvoker(bag+14, new Invoker() {//每日任务信息
			@Override
			public void invoke(Object arg0, Object arg1) {
				TaskHandler.getTaskHandler().taskMessage(arg0, arg1);
			}
		});
		
		putInvoker(bag+12, new Invoker() {//
			@Override
			public void invoke(Object arg0, Object arg1) {
				TaskHandler.getTaskHandler().equipUp(arg0, arg1);
			}
		});
		
		putInvoker(bag+13, new Invoker() {//
			@Override
			public void invoke(Object arg0, Object arg1) {
				TaskHandler.getTaskHandler().getTaskAward(arg0, arg1);
			}
		});
		
		putInvoker(bag+15, new Invoker() {//摇钱树信息
			@Override
			public void invoke(Object arg0, Object arg1) {
				ArenaHandler1.getArenahandler1().robMessage(arg0, arg1);
			}
		});
		
		putInvoker(bag+18, new Invoker() {//物品掉落
			@Override
			public void invoke(Object arg0, Object arg1) {
				LootHandler.getLootHandler().loot(arg0, arg1);
			}
		});
		
		putInvoker(bag+19, new Invoker() {//购买铜币
			@Override
			public void invoke(Object arg0, Object arg1) {
				BagHandler.getBagHandler().buyCopper(arg0, arg1);
			}
		});		
		
	}
}
