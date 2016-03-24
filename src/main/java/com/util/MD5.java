package com.util;

import java.security.MessageDigest;

public class MD5 {
	private final static String[] HE_STRINGS={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

	public static String md5(String input) {
		return encodeByMD5(input);
	}
	
	public static String encodeByMD5(String s) {
		if (s!=null) {
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				byte[] result = digest.digest(s.getBytes());
				String sss = byteArrayToString(result);
				return sss;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static String byteArrayToString(byte[] b ) {
		int len = b.length;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(byteToString(b[i]));
		}
		return sb.toString();
	}
	private static String byteToString(byte b) {
		int n = b;
		if (n<0) {
			n=256+n;
		}
		int d1=n/16;
		int d2=n%16;
		return HE_STRINGS[d1]+HE_STRINGS[d2];
	}
	
	public static void main(String[] args) {
		String md5 = md5("2");//AAEB43CF39A306D6975D666F51377BCA
		System.out.println(md5);
	}
	public final static String MD5(String s){
	     char hexDigits[] = {
	         '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
	         'e', 'f'};
	     try {
	       byte[] strTemp = s.getBytes();
	       MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	       mdTemp.update(strTemp);
	       byte[] md = mdTemp.digest();
	       int j = md.length;
	       char str[] = new char[j * 2];
	       int k = 0;
	       for (int i = 0; i < j; i++) {
	         byte byte0 = md[i];
	         str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	         str[k++] = hexDigits[byte0 & 0xf];
	         }
	         return new String(str);
	       }
	       catch (Exception e){
	    	   return null;
	       }
	}
}
