package com.socket.thread;

import com.cache.GlobalMap;
import com.socket.battle.Call;
import com.socket.battle.Men;
import com.socket.battle.Room;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class CallMonster {
	private static CopyOnWriteArraySet<Integer> monsterRoomIds = new CopyOnWriteArraySet<Integer>();
	public static CopyOnWriteArraySet<Integer> getMonsterRoomIds() {
		return monsterRoomIds;
	}
	private static int currentTime = TimeUtil.currentTime();
	public CallMonster() {
		Timer timer = new Timer("callMonster");
		timer.schedule(new TimerTasker(), 0, 1000);
	}
	public void monsterDead(Room room, int callId) {
		try {
			ConcurrentHashMap<Integer, Men> members = room.getMembers();
			if (members==null) {
				return;
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(2045);
			output.writeInt(callId);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class TimerTasker extends TimerTask{
		@Override
		public void run() {
			currentTime = TimeUtil.currentTime();
			Iterator<Integer> iterator = monsterRoomIds.iterator();
			while (iterator.hasNext()) {
				Integer next = iterator.next();
				Room room = GlobalMap.getRoomMap().get(next);
				if (room!=null) {
					ConcurrentHashMap<Integer, Call> callMonsters = room.getCallMonsters();
					if (callMonsters!=null) {
						Iterator<Entry<Integer, Call>> ite = callMonsters.entrySet().iterator();
						while (ite.hasNext()) {
							Call call = ite.next().getValue();
							if (currentTime-call.getStartTime()>=call.getLastTime()) {//召唤物时间到
								monsterDead(room, call.getHid());
							}
						}
						if (callMonsters.size()==0) {
							monsterRoomIds.remove(room.getId());
						}
					}else {
						monsterRoomIds.remove(room.getId());
					}
				}
			}
		}
	}
}
