package com.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Friend {
	private int id;
	private int hid;
	private String data;//
	private int head;//头像
//	private List<FriendUnit> list = new ArrayList<FriendUnit>();
	private Map<Integer, FriendUnit> map;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		setList(data);
		this.data = data;
	}
	
	public void setData() {
		StringBuffer sb = new StringBuffer();
		if (getMap()!=null && getMap().size()>0) {
			Iterator<Entry<Integer, FriendUnit>> iterator = getMap().entrySet().iterator();
			String s = null;
			while (iterator.hasNext()) {
				Entry<Integer, FriendUnit> next = iterator.next();
				s = next.getValue().toString();
				sb.append(s).append(",");
			}
		}
		this.data = sb.toString();
	}
	public void setList(String data) {
		Map<Integer, FriendUnit> map = new TreeMap<Integer, FriendUnit>();
		String[] split = data.split(",");
		int length = split.length;
		for (int i = 0; i < length; i++) {
			String[] split2 = split[i].split("_");
			if (split2.length!=3) {
				continue;
			}
			FriendUnit unit = new FriendUnit();
			unit.setHid(Integer.parseInt(split2[0]));
			unit.setName(split2[1]);
			unit.setHead(Integer.parseInt(split2[2]));
			map.put(unit.getHid(), unit);
		}
		this.setMap(map);
	}
	public void setHead(int head) {
		this.head = head;
	}
	public int getHead() {
		return head;
	}
	public void setMap(Map<Integer, FriendUnit> map) {
		this.map = map;
	}
	public Map<Integer, FriendUnit> getMap() {
		return map;
	}
}
