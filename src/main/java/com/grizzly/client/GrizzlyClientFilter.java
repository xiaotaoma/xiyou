package com.grizzly.client;

import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class GrizzlyClientFilter extends BaseFilter{
	@Override
	public NextAction handleWrite(FilterChainContext ctx) throws IOException {
		byte[] message = ctx.getMessage();
		int len = message.length;
		Buffer buffer = ctx.getMemoryManager().allocate(len+4);
		buffer.putInt(len);
		buffer.put(message);
		buffer.flip();
		ctx.setMessage(buffer);
		return ctx.getInvokeAction();
	}
	@Override
	public NextAction handleRead(FilterChainContext ctx) throws IOException {
		Buffer buffer = ctx.getMessage();
		if (buffer==null) {
			return null;
		}
		int remaining = buffer.remaining();
		System.out.println(buffer);
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
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream input = new DataInputStream(bis);
		int readInt = input.readInt();
		
		return super.handleRead(ctx);
	}
}
