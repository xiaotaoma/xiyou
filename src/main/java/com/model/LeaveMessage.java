package com.model;

public class LeaveMessage {
	private int id;
	private String message;
	private int time;//消息发送时间
	private int sendId;//发送者id
	private String sendName;//发送者名字
	private int sendTid;//发送者徒弟id
	private int receiverId;//接受者id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getSendId() {
		return sendId;
	}
	public void setSendId(int sendId) {
		this.sendId = sendId;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	public void setSendTid(int sendTid) {
		this.sendTid = sendTid;
	}
	public int getSendTid() {
		return sendTid;
	}
}
