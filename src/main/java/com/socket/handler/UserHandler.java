package com.socket.handler;

import org.apache.mina.core.session.IoSession;

/**
 * 
 * @author Administrator
 *
 */
public class UserHandler {
	private static UserHandler userHandler;
	public static UserHandler getUserHandler() {
		if (userHandler==null) {
			userHandler=new UserHandler();
		}
		return userHandler;
	}
	/**
	 * 
	 * @param message
	 * @param session
	 * @param cmd
	 */
	public void login(byte[] message,IoSession session,int cmd) {
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
