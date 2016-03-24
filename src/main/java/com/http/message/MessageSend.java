package com.http.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageSend {
	
	private static Logger logger = LoggerFactory.getLogger(MessageSend.class);
	private URL url;
	private HttpURLConnection urlconn;
	 
	String inencoding;
	String outencoding;
	public MessageSend() {
		inencoding = "UTF-8";
		outencoding = "UTF-8";
	}
	
	
	public MessageSend(String inencoding,String outencoding) {
		this.inencoding = inencoding;
		this.outencoding= outencoding;
	}
	public String connect(String params, String postUrl) {
		BufferedReader br = null;
		String response = "", brLine = "";
		try {
			url = new URL(postUrl);
			urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setRequestProperty("user-agent","mozilla/4.7 [en] (win98; i)");    //set request header 
			urlconn.setRequestProperty("X-Forwarded-For", "127.0.0.1");
			urlconn.setConnectTimeout(3000);
			urlconn.setReadTimeout(3000);
			//urlconn.setRequestMethod("POST");     // request method, default GET
			if (params==null) {
				urlconn.setRequestMethod("GET");
			}else {
				urlconn.setRequestMethod("POST");
			}
			urlconn.setUseCaches(false);    //Post can not user cache
			urlconn.setDoOutput(true);    //set output from urlconn
			urlconn.setDoInput(true);    //set input from urlconn
			urlconn.setReadTimeout(3000);//��ʱ1����
			if (params!=null) {
				OutputStream out = urlconn.getOutputStream();
				out.write(params.getBytes("UTF-8"));
				out.flush();
				out.close();    // output stream close,That's means need not to post data to this outputstream
			}
			
			br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), inencoding));
			while((brLine = br.readLine())!=null)
				response =(new StringBuilder(String.valueOf(response))).append(brLine).toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		} finally {
			urlconn.disconnect();
		}
		return response;
	}
	
}
