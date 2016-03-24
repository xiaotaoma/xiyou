package com.grizzly.client;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.GrizzlyFuture;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class GrizzlyClient {
	public static Set<Connection> set = new CopyOnWriteArraySet<Connection>();
	public static void main(String[] args) {
		FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();
		filterChainBuilder.add(new TransportFilter());
		
		filterChainBuilder.add(new GrizzlyClientFilter());
		
		TCPNIOTransport transport = TCPNIOTransportBuilder.newInstance().build();
		transport.setProcessor(filterChainBuilder.build());
		timer();
		try {
			transport.start();
//			for (int i = 0; i < 3000; i++) {
				try {
					GrizzlyFuture<Connection> future = transport.connect("192.168.1.115", 7778);
					Connection connection = future.get(10, TimeUnit.SECONDS);
					if (connection != null) {
						send(connection);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				Thread.sleep(100);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void send(Connection connection) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			Random random = new Random();
			int length = random.nextInt(30)+1;
			output.writeInt(1002);
			output.writeShort(1);
			for (int i = 0; i < 1; i++) {
				output.writeByte(1);
				output.writeShort(2);
				output.writeInt(3);
			}
			output.close();
			bos.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendBytes(Connection connection) {
		try {
			ByteArrayOutputStream bos =new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(1001);
			output.writeInt(11111);
			output.close();
			bos.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static int n=0;
	public static void timer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Iterator<Connection> iterator = set.iterator();
				n++;
				if (n==30) {
					System.out.println(""+set.size());
					n=0;
				}
				while (iterator.hasNext()) {
					Connection connection = iterator.next();
					send(connection);
				}
			}
		}, 1000, 500);
	}
	public static void timer(final Connection connection) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				sendBytes(connection);
			}
		}, 500, 500);
	}
}
