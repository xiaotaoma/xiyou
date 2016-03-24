package com.socket.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Hero;
import com.model.LeaveMessage;
import com.model.backstage.Back_record;
import com.service.LeaveMessageManager;
import com.socket.battle.BattleHandler;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ChatHandler {
	private static Logger logger = LoggerFactory.getLogger(ChatHandler.class);
	private static ChatHandler chatHandler;
	public static ChatHandler getChatHandler() {
		if (chatHandler == null) {
			chatHandler = new ChatHandler();
		}
		return chatHandler;
	}
	/**
	 * 
	 */
	public void chat(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int thid = input.readInt();
				String message = input.readUTF();
				bis.close();
				input.close();
				//聊天信息太长
				if (message.length()>1000) {
					return;
				}
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid ==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				Connection tConnection = GlobalMap.getConns().get(thid);
				if (tConnection!=null) {
					//发送给对方
					chat(message, hid,hero.getTid(),hero.getName(), tConnection);
				}else {//留言
					leaveMessage(message, hid, hero.getName(), hero.getTid(), thid);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 聊天不在线留言
	 */
	public void leaveMessage(String message,int sendId,String sendName,int sendTid,int receiverId) {
		try {
			LeaveMessageManager leaveMessageManager = (LeaveMessageManager) BaseAction.getIntance().getBean("leaveMessageManager");
			LeaveMessage leaveMessage = new LeaveMessage();
			leaveMessage.setMessage(message);
			leaveMessage.setReceiverId(receiverId);
			leaveMessage.setSendTid(sendTid);
			leaveMessage.setSendId(sendId);
			leaveMessage.setSendName(sendName);
			leaveMessage.setTime(TimeUtil.currentTime());
			leaveMessageManager.insert(leaveMessage);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 发送留言
	 */
	public void sendLeaveMessage(int hid,Connection connection) {
		try {
			LeaveMessageManager leaveMessageManager = (LeaveMessageManager) BaseAction.getIntance().getBean("leaveMessageManager");
			List<LeaveMessage> list = leaveMessageManager.getByReceiverId(hid);
			if (list!=null) {
				StringBuffer sb = new StringBuffer();
				for (LeaveMessage leaveMessage : list) {
					chat(leaveMessage.getMessage(), leaveMessage.getSendId(), leaveMessage.getSendTid(), leaveMessage.getSendName(), connection);
					sb.append(leaveMessage.getId()).append(",");
				}
				if (sb.length()>0) {
					String s = sb.substring(0, sb.length()-1);
					leaveMessageManager.delete(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param message 信息内容
	 * @param hid 发送者角色id
	 * @param tid 发送者徒弟id
	 * @param name 发送者角色名字 
	 * @param connection    
	 */
	public void chat(String message,int hid,int tid,String name,Connection connection) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(5003);
			output.writeInt(hid);
			output.writeByte(tid);
			output.writeUTF(name);
			output.writeUTF(message);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 所有人聊天
	 * @param message
	 */
	public void broadcastChat(int hid,String name,String message) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(5005);
			output.writeInt(hid);
			output.writeUTF(name);
			output.writeUTF(message);
			bos.close();
			output.close();
			byte[] byteArray = bos.toByteArray();
			Iterator<Entry<Integer, Connection>> iterator = GlobalMap.getConns().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Connection> next = iterator.next();
				Connection connection = next.getValue();
				if (connection!=null) {
					connection.write(byteArray);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public void worldChat(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				String message = input.readUTF();
				bis.close();
				input.close();
				if (message.equals("")) {
					return;
				}
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				int currentTime = TimeUtil.currentTime();
				Integer chatTimes = (Integer) connection.getAttributes().getAttribute("chatTimes");
				if (chatTimes==null) {
					connection.getAttributes().setAttribute("chatTimes", currentTime);
				}else {
					if (currentTime - chatTimes >=5) {
						connection.getAttributes().setAttribute("chatTimes", currentTime);
					}else {
						SysUtil.warning(connection, Globalconstants.WORLDCHATCOLDDOWN);
						return;
					}
				}
				
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				int money = hero.getMoney();
				if (money<1) {
					SysUtil.warning(connection, Globalconstants.NOMONEY);
					return;
				}
				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
					
				}else {
					boolean moneyExpenses = MoneyControl.moneyExpenses(hero, Back_record.REASON_WORLDCHAT, 1);
					if (!moneyExpenses) {
						return;
					}
				}
				connection.write(BattleHandler.getTestHandler().getByte(1017, hero.getMoney()));
				broadcastChat(hid, hero.getName(), message);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
}
