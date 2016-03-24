package com.model;

public class FriendUnit {
	private int hid;
	private String name;
	private int head;//头像
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHead() {
		return head;
	}
	public void setHead(int head) {
		this.head = head;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(hid).append("_").append(name).append("_").append(head);
		return sb.toString();
	}
}
