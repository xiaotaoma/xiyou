package com.socket.thread;

import com.cache.GlobalMap;
import com.socket.battle.BattleHandler;
import com.socket.battle.Men;
import com.socket.battle.Room;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class GiveMp {
	
	public GiveMp() {
		Timer timer = new Timer();
		timer.schedule(new DesertSchedul(), 0, 2000);
	}
	
	
	private class DesertSchedul extends TimerTask{
		@Override
		public void run() {
			Iterator<Entry<Integer, Room>> iterator = GlobalMap.getRoomMap().entrySet().iterator();
			while (iterator.hasNext()) {
				Room room = iterator.next().getValue();
				if (room.isBattle()) {
					ConcurrentHashMap<Integer, Men> members = room.getMembers();
					if (members!=null) {
						Iterator<Entry<Integer, Men>> ite = members.entrySet().iterator();
						while (ite.hasNext()) {
							Men men = ite.next().getValue();
							float mp = men.getMp();
							float mpMax = men.getMpMax();
							mp+=1;
							if (mp>=mpMax) {
								mp=mpMax;
							}
							men.setMp(mp);
							BattleHandler.getTestHandler().write2033(men, room);
						}
					}
				}
			}
		}
	}
}
