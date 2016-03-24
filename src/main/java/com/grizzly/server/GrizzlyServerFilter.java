package com.grizzly.server;

import com.cache.GlobalMap;
import com.socket.handler.HeroHandler;
import org.apache.mina.adapter.Handler;
import org.apache.mina.dispatcherHandler.Dispatcher;
import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GrizzlyServerFilter extends BaseFilter{
	private static Logger logger = LoggerFactory.getLogger(GrizzlyServerFilter.class);
	
	private Dispatcher dispatcher;
	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	@Override
	public NextAction handleRead(FilterChainContext ctx) throws IOException {
		Buffer reminder=null;
		try {
			Buffer buffer = ctx.getMessage();
			if (buffer==null) {
				return null;
			}
			int remaining = buffer.remaining();
			if (remaining<4) {
				return ctx.getStopAction(buffer);
			}
			int length = buffer.getInt();
			if (remaining<length) {
				buffer.position(0);
				return ctx.getStopAction(buffer);
			}
			byte[] bytes = new byte[length];
			buffer.get(bytes);
			int cmd = getCmd(bytes);
			System.out.println("received:"+cmd);	
			logger.info("hid = "+ctx.getConnection().getAttributes().getAttribute("hid")+" received :" +cmd);
			cmd = cmd/1000*1000;
			Handler handler = dispatcher.get(cmd);
			if (handler == null) {
				return ctx.getStopAction();
			}else {
				handler.invoke(ctx.getConnection(), bytes);
			}
			reminder = null;
			if (buffer.hasRemaining()) {
				reminder = buffer.split(length);
			}
			buffer.tryDispose();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return ctx.getInvokeAction(reminder);
	}
	
	@Override
	public NextAction handleWrite(FilterChainContext ctx) throws IOException {
		try {
			byte[] message = ctx.getMessage();
			int cmd = getCmd(message);
//			logger.info("hid = "+ctx.getConnection().getAttributes().getAttribute("hid")+" send :" +cmd);
			System.out.println("send:"+cmd);
			int length = message.length;
			Buffer buffer = ctx.getMemoryManager().allocate(length+4);
			buffer.putInt(length);
			buffer.put(message);
			buffer.flip();
			ctx.setMessage(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return super.handleWrite(ctx);
	}
	@Override
	public NextAction handleAccept(FilterChainContext ctx) throws IOException {
		GlobalMap.getAllConnections().add(ctx.getConnection());
		return super.handleAccept(ctx);
	}
	@Override
	public NextAction handleConnect(FilterChainContext ctx) throws IOException {
		return super.handleConnect(ctx);
	}
	@Override
	public NextAction handleClose(FilterChainContext ctx) throws IOException {
		Connection connection = ctx.getConnection();
		GlobalMap.getAllConnections().remove(connection);
		HeroHandler.getHeroHandler().logout(connection);
		return super.handleClose(ctx);
	}
	//
	public static void sort(int[] nn , Connection connection,int time) {
		int length = nn.length;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length-1; j++) {
				if (nn[i]<nn[j]) {
					int a = nn[j];
					nn[j] = nn[i];
					nn[i] = a;
				}
			}
		}
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(length);
			for (int i = 0; i < length; i++) {
				output.writeInt(nn[i]);
			}
			output.close();
			bos.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public static int getCmd(byte[] b) {
		int cmd = 0;
		if (b.length<4) {
			return cmd;
		}
		cmd = (int) ( ((b[0] & 0xFF)<<24)|((b[0+1] & 0xFF)<<16)|((b[0+2] & 0xFF)<<8)|(b[0+3] & 0xFF));
		return cmd;
	}
	
}
