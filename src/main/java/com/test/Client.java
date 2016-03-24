package com.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), 8081);
			/*InputStream input = socket.getInputStream();
			OutputStream write = socket.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output=new DataOutputStream(bos);
			output.writeInt(12);
			output.writeInt(11111);
			output.writeInt(1111111);
			output.close();
			bos.close();
			byte[] byteArray = bos.toByteArray();
			System.out.println(".............................");
			for (int i = 0; i < byteArray.length; i++) {
				System.out.print(byteArray[i]+",");
			}
			System.out.println(".............................");
			write.write(bos.toByteArray());
			write.flush();*/
			while (true) {
				OutputStream output = socket.getOutputStream();
				output.write("111111".getBytes());
				output.flush();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
