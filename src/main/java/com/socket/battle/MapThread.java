package com.socket.battle;

import com.socket.thread.Burn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class MapThread {
	private static Map<Integer, CopyOnWriteArraySet<Integer>> map = new ConcurrentHashMap<Integer, CopyOnWriteArraySet<Integer>>();
	public static Map<Integer, CopyOnWriteArraySet<Integer>> getMap() {
		return map;
	}
	
	
	
	public static void init() {
		new Burn();
	}
}
