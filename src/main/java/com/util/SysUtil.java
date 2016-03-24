package com.util;

import com.cache.GlobalMap;
import com.main.BaseAction;
import com.model.Login;
import com.model.backstage.Back_pay91;
import com.model.sys.Product91;
import com.service.Back_pay91Manager;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.Map.Entry;

public class SysUtil {
	private static Logger logger = LoggerFactory.getLogger(SysUtil.class);
	
	public static int[] ii ={2,3,5,7,8,10,15,20,30,40};
	
	
	public static void clearDeadCheck(Map<String, Login>  map) {
		Iterator<Entry<String, Login>> iterator = map.entrySet().iterator();
		int currentTime = TimeUtil.currentTime();
		while (iterator.hasNext()) {
			Entry<String, Login> next = iterator.next();
			Login value = next.getValue();
			int time = value.getTime();
			if (currentTime-time>600) {
				map.remove(next.getKey());
			}
		}
		System.out.println(map.size());
	}
	
	public static void main(String[] args) {
		test1();
	}
	public static void test1() {
		test2();
	}
	public static void test2() {
		System.out.println(getCaller(SysUtil.class));
	}
	
	/**
	 * 错误提示
	 */
	public static void warning(Connection connection,int errorNum) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(999);
			output.writeShort(errorNum);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public static List<String> split(String s,String delim) {
		List<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(s, delim);
		while (tokenizer.hasMoreTokens()) {
			String nextToken = tokenizer.nextToken();
			list.add(nextToken);
		}
		return list;
	}
	
	public static List<Integer> splitGetInt(String s,String delim) {
		List<Integer> list = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(s, delim);
		while (tokenizer.hasMoreTokens()) {
			String nextToken = tokenizer.nextToken();
			list.add(Integer.parseInt(nextToken.trim()));
		}
		return list;
	}
	
	/**
	 * 
	 */
	public static List<Integer> splitNum(int num,int times) {
		List<Integer> list = new ArrayList<Integer>();//拆分结果
		int length = ii.length;
		List<Integer> integers = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			if (ii[i]<num) {
				integers.add(ii[i]);
			}
		}
		
		boolean done = false;
		Random random = new Random();
		int size = integers.size();
		while (!done) {
			List<Integer> ll = new ArrayList<Integer>();
			for (int i = 0; i < times; i++) {
				ll.add(integers.get(random.nextInt(size)));
			}
			int sum = sum(ll);
			if (sum==num) {
				list.addAll(ll);
				done = true;
			}
		}
		return list;
	}
	
	public static int sum(List<Integer> list) {
		int n = 0;
//		int size = list.size();
//		for (int i = 0; i < size; i++) {
//			n+=list.get(i);
//		}
		int[] nn = new int[list.size()];
		Integer[] array = list.toArray(new Integer[list.size()]);
		return n;
	}
	
	/**
	 * 获取方法调用结构
	 * @return
	 */
	public static String getCaller() {
		try {
			StackTraceElement stack[] = (new Throwable()).getStackTrace();
			String re="";
			for (int i = 0; i < stack.length; i++) {
				StackTraceElement ste = stack[i];
				String className = ste.getClassName();
				if(className.indexOf("com.socket.handler")>=0 && className.indexOf("registerHandler")==-1){
					re=className+"."+ste.getMethodName()+","+re;
				}
			}
			return re;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public static byte[] getBytes(int cmdid,byte result){
		byte []a=new byte[5];
		a[0]=(byte)((cmdid >>> 24) & 0xFF);
		a[1]=(byte)((cmdid >>> 16) & 0xFF);
		a[2]=(byte)((cmdid >>>  8) & 0xFF);
		a[3]=(byte)((cmdid >>>  0) & 0xFF);
		
		a[4]= result;
		return a;
	}
	
	public static byte[] getBytes(int cmd,byte result,int toolId) {
		byte []a=new byte[9];
		a[0]=(byte)((cmd >>> 24) & 0xFF);
		a[1]=(byte)((cmd >>> 16) & 0xFF);
		a[2]=(byte)((cmd >>>  8) & 0xFF);
		a[3]=(byte)((cmd >>>  0) & 0xFF);
		
		a[4]= result;
		
		a[5]=(byte)((cmd >>> 24) & 0xFF);
		a[6]=(byte)((cmd >>> 16) & 0xFF);
		a[7]=(byte)((cmd >>>  8) & 0xFF);
		a[8]=(byte)((cmd >>>  0) & 0xFF);
		return a;
	}
	
	public static byte[] getBytes(int cmdid,int result){
		byte []a=new byte[8];
		a[0]=(byte)((cmdid >>> 24) & 0xFF);
		a[1]=(byte)((cmdid >>> 16) & 0xFF);
		a[2]=(byte)((cmdid >>>  8) & 0xFF);
		a[3]=(byte)((cmdid >>>  0) & 0xFF);
		
		a[4]=(byte)((result >>> 24) & 0xFF);
		a[5]=(byte)((result >>> 16) & 0xFF);
		a[6]=(byte)((result >>>  8) & 0xFF);
		a[7]=(byte)((result >>>  0) & 0xFF);
		return a;
	}
	
	public static byte[] getByte(int n) {
		byte []a=new byte[4];
		a[0]=(byte)((n >>> 24) & 0xFF);
		a[1]=(byte)((n >>> 16) & 0xFF);
		a[2]=(byte)((n >>>  8) & 0xFF);
		a[3]=(byte)((n >>>  0) & 0xFF);
		return a;
	}
	
	public static byte[] getByte(short n) {
		byte []a=new byte[2];
		a[0]=(byte)((n >>>  8) & 0xFF);
		a[1]=(byte)((n >>>  0) & 0xFF);
		return a;
	}
	
	/**
	 * 获取方法调用结构
	 * @return
	 */
	public static String getCaller(Class clasz) {
		try {
			String name = clasz.getName();
			StackTraceElement stack[] = (new Throwable()).getStackTrace();
			String re="";
			for (int i = 0; i < stack.length; i++) {
				StackTraceElement ste = stack[i];
				String className = ste.getClassName();
				if(className.indexOf(name)>=0 && className.indexOf("registerHandler")==-1){
					re=className+"."+ste.getMethodName()+","+re;
				}
			}
			return re;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public static Object getValue(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return s;
		}
	}
	
	@SuppressWarnings("unchecked")  
	public static Method getGetMethod(Class objectClass, String fieldName) {  
		StringBuffer sb = new StringBuffer();  
		sb.append("get");  
		sb.append(fieldName.substring(0, 1).toUpperCase());  
		sb.append(fieldName.substring(1));  
		try {  
			return objectClass.getMethod(sb.toString());  
		} catch (Exception e) {  
		}  
		return null;  
	}
	/** 
     * java反射bean的set方法 
     *  
     * @param objectClass 
     * @param fieldName 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static Method getSetMethod(Class objectClass, String fieldName) {  
        try {  
            Class[] parameterTypes = new Class[1];  
            Field field = objectClass.getDeclaredField(fieldName);  
            parameterTypes[0] = field.getType();  
            StringBuffer sb = new StringBuffer();  
            sb.append("set");  
            sb.append(fieldName.substring(0, 1).toUpperCase());  
            sb.append(fieldName.substring(1));  
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);  
            return method;  
        } catch (Exception e) {
            e.printStackTrace(); 
            logger.error("",e);
        }  
        return null;  
    }
    /**
     * 获取请求url中的数据
     * @param requestURI
     * @param handlerName
     * @return
     */
    public static HashMap<String, String> getParameters(String requestURI,String handlerName) {
		HashMap<String, String> map =new HashMap<String, String>();
		if (requestURI.indexOf(handlerName)!=-1) {
			String replace = requestURI.replace(handlerName, "");
			if (!replace.equals("")) {
				String[] split = replace.split("&");
				int length = split.length;
				for (int i = 0; i < length; i++) {
					String[] split2 = split[i].split("=");
					map.put(split2[0], split2[1]);
				}
			}
		}
		return map;
	}
    
	/**
	 * 获取连接ip
	 * @param connection
	 * @return
	 */
	public static String getIp(Connection connection) {
		String ip = "";
		InetSocketAddress peerAddress = (InetSocketAddress) connection.getPeerAddress();
		if (peerAddress!=null) {
			InetAddress address = peerAddress.getAddress();
			if (address!=null) {
				ip = address.getHostAddress();
			}
		}
		return ip;
	}
	/**
	 * 
	 * @param hid
	 * @param account
	 * @param consumeStreamId
	 * @param product91
	 * @param note
	 * @param orderId
	 * @param orderMoney
	 * @param originalMoney
	 */
	public static void syncToDb(int hid,String account,String consumeStreamId,
			Product91 product91,String note,int orderId,String orderMoney,
			String originalMoney,String terrace){
		try {
			Back_pay91 pay91 = new Back_pay91();
			pay91.setAccount(account);
			pay91.setConsumeStreamId(consumeStreamId);
			pay91.setHid(hid);
			int money = 0;
			int copper = 0;
			if (product91.getMoneyType()==1) {
				money = product91.getNum();
			}else if (product91.getMoneyType()==2) {
				copper = product91.getNum();
			}
			pay91.setMoney(money);
			pay91.setCopper(copper);
			pay91.setNote(note);
			pay91.setOrderId(orderId);
			if (orderMoney.equals("")) {
				orderMoney = product91.getPrice()+"";
			}
			pay91.setOrderMoney(orderMoney);
			pay91.setOriginalMoney(originalMoney);
			pay91.setPid(product91.getId());
			pay91.setRmb(product91.getPrice());
			pay91.setTime(TimeUtil.currentTime());
			pay91.setTerrace(terrace);
			Back_pay91Manager pay91Manager = (Back_pay91Manager) BaseAction.getIntance().getBean("pay91Manager");
			pay91Manager.insert(pay91);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 竞技场战斗广播
	 * 台湾版本
	 */
	public static void broadcastBattleResult(String winnerName,String loserName,int copper,int wave) {
//		if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
			try {
//				张三勇闯40关，战胜了李四，成功抢夺9999铜币
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(1022);
				output.writeUTF(winnerName);
				output.writeUTF(loserName);
				output.writeInt(copper);
				output.writeShort(wave);
				bos.close();
				output.close();
				byte[] byteArray = bos.toByteArray();
				Iterator<Entry<Integer, Connection>> iterator = GlobalMap
						.getConns().entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Integer, Connection> next = iterator.next();
					Connection connection = next.getValue();
					connection.write(byteArray);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("", e);
			}
		}
//	}
}
