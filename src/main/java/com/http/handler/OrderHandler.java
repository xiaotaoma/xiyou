package com.http.handler;

import com.alibaba.fastjson.JSONObject;
import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Account;
import com.model.Hero;
import com.model.backstage.Back_pay_message;
import com.model.sys.Product;
import com.model.sys.SysGloablMap;
import com.service.AccountManager;
import com.service.Back_pay_messageManager;
import com.service.HeroManager;
import com.socket.handler.PayHandler;
import com.util.MD5;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OrderHandler extends HttpHandler{
	public static final String HANDLERNAME = "/order";
	private static Logger logger = LoggerFactory.getLogger(OrderHandler.class);
	
	@Override
	public void service(Request request, Response response) throws Exception {
		try {
			Method method = request.getMethod();
			if (method.toString().equals("POST")) {
				Map<String, String[]> parameterMap = request.getParameterMap();
				Map<String, String> map = new HashMap<String, String>();
				Iterator<Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String[]> next = iterator.next();
					System.out.println("key:"+next.getKey()+" \n value:"+next.getValue()[0]);
					logger.info("key:"+next.getKey()+"  value:"+next.getValue()[0]);
					map.put(next.getKey(), next.getValue()[0]);
				}
				int trigger = trigger(map);
				logger.info("pay_message--> result "+ trigger);
				response.getNIOWriter().write(""+trigger);
				return;
			}else {
				response.getNIOWriter().write("-1");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
		response.getNIOWriter().write("-1");
	}
	/**
	 * 
	 *  2013-07-09 09:48:01 -key:key  value:97b7e0f03a7b187eb1b64d37792d1e73
		2013-07-09 09:48:01 -key:cmd  value:888888
		2013-07-09 09:48:01 -key:random  value:1281766650
		2013-07-09 09:48:01 -key:account  value:10:1C:0C:3A:E6:B8
		2013-07-09 09:48:01 -key:terrace  value:Device
		2013-07-09 09:48:01 -key:zoneid  value:1
		2013-07-09 09:48:01 -key:orderId  value:1000000079857121
		2013-07-09 09:48:01 -key:order  value:ewoJInNpZ25hdHVyZSIgPSAiQWdPczlLcUJXSTBoVDVreEIxMmFCdFIzdDM5ZkM2OUlQc2xUemt0em1ZSUdtaHo3ZGFtd0Z1QjhjYWFoem1adkJ3LzVUTU1wb08zRXhHNHBWZVRpNEJMOUtvYkRSakUyZlBOZ1daK2NuS0tSd3MySndCTGQvQWpSRkhHblE0djZZRWFNUEF0UVRpeHM2Q2lxS3pMM2liVC9ORTBNOEhWalhXTU80aHliQnhPRUFBQURWekNDQTFNd2dnSTdvQU1DQVFJQ0NHVVVrVTNaV0FTMU1BMEdDU3FHU0liM0RRRUJCUVVBTUg4eEN6QUpCZ05WQkFZVEFsVlRNUk13RVFZRFZRUUtEQXBCY0hCc1pTQkpibU11TVNZd0pBWURWUVFMREIxQmNIQnNaU0JEWlhKMGFXWnBZMkYwYVc5dUlFRjFkR2h2Y21sMGVURXpNREVHQTFVRUF3d3FRWEJ3YkdVZ2FWUjFibVZ6SUZOMGIzSmxJRU5sY25ScFptbGpZWFJwYjI0Z1FYVjBhRzl5YVhSNU1CNFhEVEE1TURZeE5USXlNRFUxTmxvWERURTBNRFl4TkRJeU1EVTFObG93WkRFak1DRUdBMVVFQXd3YVVIVnlZMmhoYzJWU1pXTmxhWEIwUTJWeWRHbG1hV05oZEdVeEd6QVpCZ05WQkFzTUVrRndjR3hsSUdsVWRXNWxjeUJUZEc5eVpURVRNQkVHQTFVRUNnd0tRWEJ3YkdVZ1NXNWpMakVMTUFrR0ExVUVCaE1DVlZNd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFNclJqRjJjdDRJclNkaVRDaGFJMGc4cHd2L2NtSHM4cC9Sd1YvcnQvOTFYS1ZoTmw0WElCaW1LalFRTmZnSHNEczZ5anUrK0RyS0pFN3VLc3BoTWRkS1lmRkU1ckdYc0FkQkVqQndSSXhleFRldngzSExFRkdBdDFtb0t4NTA5ZGh4dGlJZERnSnYyWWFWczQ5QjB1SnZOZHk2U01xTk5MSHNETHpEUzlvWkhBZ01CQUFHamNqQndNQXdHQTFVZEV3RUIvd1FDTUFBd0h3WURWUjBqQkJnd0ZvQVVOaDNvNHAyQzBnRVl0VEpyRHRkREM1RllRem93RGdZRFZSMFBBUUgvQkFRREFnZUFNQjBHQTFVZERnUVdCQlNwZzRQeUdVakZQaEpYQ0JUTXphTittVjhrOVRBUUJnb3Foa2lHOTJOa0JnVUJCQUlGQURBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQUVhU2JQanRtTjRDL0lCM1FFcEszMlJ4YWNDRFhkVlhBZVZSZVM1RmFaeGMrdDg4cFFQOTNCaUF4dmRXLzNlVFNNR1k1RmJlQVlMM2V0cVA1Z204d3JGb2pYMGlreVZSU3RRKy9BUTBLRWp0cUIwN2tMczlRVWU4Y3pSOFVHZmRNMUV1bVYvVWd2RGQ0TndOWXhMUU1nNFdUUWZna1FRVnk4R1had1ZIZ2JFL1VDNlk3MDUzcEdYQms1MU5QTTN3b3hoZDNnU1JMdlhqK2xvSHNTdGNURXFlOXBCRHBtRzUrc2s0dHcrR0szR01lRU41LytlMVFUOW5wL0tsMW5qK2FCdzdDMHhzeTBiRm5hQWQxY1NTNnhkb3J5L0NVdk02Z3RLc21uT09kcVRlc2JwMGJzOHNuNldxczBDOWRnY3hSSHVPTVoydG04bnBMVW03YXJnT1N6UT09IjsKCSJwdXJjaGFzZS1pbmZvIiA9ICJld29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREV6TFRBM0xUQTRJREU0T2pRNE9qVTNJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0l3TW1WbE9HTmhNV0V3TnpkaFlUUTROMlJqTVRBNU56WXdaV0ZoWVRVNE9ERTVNMkZrTlRSaUlqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTVRBd01EQXdNREEzT1RnMU56RXlNU0k3Q2draVluWnljeUlnUFNBaU1TNHdJanNLQ1NKMGNtRnVjMkZqZEdsdmJpMXBaQ0lnUFNBaU1UQXdNREF3TURBM09UZzFOekV5TVNJN0Nna2ljWFZoYm5ScGRIa2lJRDBnSWpFaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFek56TXpNelExTXpjME1EQWlPd29KSW5WdWFYRjFaUzEyWlc1a2IzSXRhV1JsYm5ScFptbGxjaUlnUFNBaVJESkNRakJHUmpNdE16WTFSaTAwUWpNeUxUZzJSVGd0UkVWQk5VVTRNVGsyTmtNeUlqc0tDU0p3Y205a2RXTjBMV2xrSWlBOUlDSk5lR2w1YjNVdVoyOXNaR1Z1TGpFd01EQXdNQ0k3Q2draWFYUmxiUzFwWkNJZ1BTQWlOalk0TlRJd056Y3lJanNLQ1NKaWFXUWlJRDBnSW1OdmJTNTBhV0Z1ZEhVdVRYaHBlVzkxSWpzS0NTSndkWEpqYUdGelpTMWtZWFJsTFcxeklpQTlJQ0l4TXpjek16TTBOVE0zTkRBd0lqc0tDU0p3ZFhKamFHRnpaUzFrWVhSbElpQTlJQ0l5TURFekxUQTNMVEE1SURBeE9qUTRPalUzSUVWMFl5OUhUVlFpT3dvSkluQjFjbU5vWVhObExXUmhkR1V0Y0hOMElpQTlJQ0l5TURFekxUQTNMVEE0SURFNE9qUTRPalUzSUVGdFpYSnBZMkV2VEc5elgwRnVaMlZzWlhNaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVaUlEMGdJakl3TVRNdE1EY3RNRGtnTURFNk5EZzZOVGNnUlhSakwwZE5WQ0k3Q24wPSI7CgkiZW52aXJvbm1lbnQiID0gIlNhbmRib3giOwoJInBvZCIgPSAiMTAwIjsKCSJzaWduaW5nLXN0YXR1cyIgPSAiMCI7Cn0=
		2013-07-09 09:48:01 -key:product  value:Mxiyou.golden.100000
		2013-07-09 09:48:01 -key:result  value:{"receipt":{"original_purchase_date_pst":"2013-07-08 18:48:57 America/Los_Angeles","purchase_date_ms":"1373334537400","unique_identifier":"02ee8ca1a077aa487dc109760eaaa588193ad54b","original_transaction_id":"1000000079857121","bvrs":"1.0","transaction_id":"1000000079857121","quantity":"1","unique_vendor_identifier":"D2BB0FF3-365F-4B32-86E8-DEA5E81966C2","item_id":"668520772","product_id":"Mxiyou.golden.100000","purchase_date":"2013-07-09 01:48:57 Etc/GMT","original_purchase_date":"2013-07-09 01:48:57 Etc/GMT","purchase_date_pst":"2013-07-08 18:48:57 America/Los_Angeles","bid":"com.tiantu.Mxiyou","original_purchase_date_ms":"1373334537400"},"status":0}
		2013-07-09 09:48:01 -payMessage , give money-->{product=Mxiyou.golden.100000, result={"receipt":{"original_purchase_date_pst":"2013-07-08 18:48:57 America/Los_Angeles","purchase_date_ms":"1373334537400","unique_identifier":"02ee8ca1a077aa487dc109760eaaa588193ad54b","original_transaction_id":"1000000079857121","bvrs":"1.0","transaction_id":"1000000079857121","quantity":"1","unique_vendor_identifier":"D2BB0FF3-365F-4B32-86E8-DEA5E81966C2","item_id":"668520772","product_id":"Mxiyou.golden.100000","purchase_date":"2013-07-09 01:48:57 Etc/GMT","original_purchase_date":"2013-07-09 01:48:57 Etc/GMT","purchase_date_pst":"2013-07-08 18:48:57 America/Los_Angeles","bid":"com.tiantu.Mxiyou","original_purchase_date_ms":"1373334537400"},"status":0}, terrace=Device, cmd=888888, order=ewoJInNpZ25hdHVyZSIgPSAiQWdPczlLcUJXSTBoVDVreEIxMmFCdFIzdDM5ZkM2OUlQc2xUemt0em1ZSUdtaHo3ZGFtd0Z1QjhjYWFoem1adkJ3LzVUTU1wb08zRXhHNHBWZVRpNEJMOUtvYkRSakUyZlBOZ1daK2NuS0tSd3MySndCTGQvQWpSRkhHblE0djZZRWFNUEF0UVRpeHM2Q2lxS3pMM2liVC9ORTBNOEhWalhXTU80aHliQnhPRUFBQURWekNDQTFNd2dnSTdvQU1DQVFJQ0NHVVVrVTNaV0FTMU1BMEdDU3FHU0liM0RRRUJCUVVBTUg4eEN6QUpCZ05WQkFZVEFsVlRNUk13RVFZRFZRUUtEQXBCY0hCc1pTQkpibU11TVNZd0pBWURWUVFMREIxQmNIQnNaU0JEWlhKMGFXWnBZMkYwYVc5dUlFRjFkR2h2Y21sMGVURXpNREVHQTFVRUF3d3FRWEJ3YkdVZ2FWUjFibVZ6SUZOMGIzSmxJRU5sY25ScFptbGpZWFJwYjI0Z1FYVjBhRzl5YVhSNU1CNFhEVEE1TURZeE5USXlNRFUxTmxvWERURTBNRFl4TkRJeU1EVTFObG93WkRFak1DRUdBMVVFQXd3YVVIVnlZMmhoYzJWU1pXTmxhWEIwUTJWeWRHbG1hV05oZEdVeEd6QVpCZ05WQkFzTUVrRndjR3hsSUdsVWRXNWxjeUJUZEc5eVpURVRNQkVHQTFVRUNnd0tRWEJ3YkdVZ1NXNWpMakVMTUFrR0ExVUVCaE1DVlZNd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFNclJqRjJjdDRJclNkaVRDaGFJMGc4cHd2L2NtSHM4cC9Sd1YvcnQvOTFYS1ZoTmw0WElCaW1LalFRTmZnSHNEczZ5anUrK0RyS0pFN3VLc3BoTWRkS1lmRkU1ckdYc0FkQkVqQndSSXhleFRldngzSExFRkdBdDFtb0t4NTA5ZGh4dGlJZERnSnYyWWFWczQ5QjB1SnZOZHk2U01xTk5MSHNETHpEUzlvWkhBZ01CQUFHamNqQndNQXdHQTFVZEV3RUIvd1FDTUFBd0h3WURWUjBqQkJnd0ZvQVVOaDNvNHAyQzBnRVl0VEpyRHRkREM1RllRem93RGdZRFZSMFBBUUgvQkFRREFnZUFNQjBHQTFVZERnUVdCQlNwZzRQeUdVakZQaEpYQ0JUTXphTittVjhrOVRBUUJnb3Foa2lHOTJOa0JnVUJCQUlGQURBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQUVhU2JQanRtTjRDL0lCM1FFcEszMlJ4YWNDRFhkVlhBZVZSZVM1RmFaeGMrdDg4cFFQOTNCaUF4dmRXLzNlVFNNR1k1RmJlQVlMM2V0cVA1Z204d3JGb2pYMGlreVZSU3RRKy9BUTBLRWp0cUIwN2tMczlRVWU4Y3pSOFVHZmRNMUV1bVYvVWd2RGQ0TndOWXhMUU1nNFdUUWZna1FRVnk4R1had1ZIZ2JFL1VDNlk3MDUzcEdYQms1MU5QTTN3b3hoZDNnU1JMdlhqK2xvSHNTdGNURXFlOXBCRHBtRzUrc2s0dHcrR0szR01lRU41LytlMVFUOW5wL0tsMW5qK2FCdzdDMHhzeTBiRm5hQWQxY1NTNnhkb3J5L0NVdk02Z3RLc21uT09kcVRlc2JwMGJzOHNuNldxczBDOWRnY3hSSHVPTVoydG04bnBMVW03YXJnT1N6UT09IjsKCSJwdXJjaGFzZS1pbmZvIiA9ICJld29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREV6TFRBM0xUQTRJREU0T2pRNE9qVTNJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0l3TW1WbE9HTmhNV0V3TnpkaFlUUTROMlJqTVRBNU56WXdaV0ZoWVRVNE9ERTVNMkZrTlRSaUlqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTVRBd01EQXdNREEzT1RnMU56RXlNU0k3Q2draVluWnljeUlnUFNBaU1TNHdJanNLQ1NKMGNtRnVjMkZqZEdsdmJpMXBaQ0lnUFNBaU1UQXdNREF3TURBM09UZzFOekV5TVNJN0Nna2ljWFZoYm5ScGRIa2lJRDBnSWpFaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFek56TXpNelExTXpjME1EQWlPd29KSW5WdWFYRjFaUzEyWlc1a2IzSXRhV1JsYm5ScFptbGxjaUlnUFNBaVJESkNRakJHUmpNdE16WTFSaTAwUWpNeUxUZzJSVGd0UkVWQk5VVTRNVGsyTmtNeUlqc0tDU0p3Y205a2RXTjBMV2xrSWlBOUlDSk5lR2w1YjNVdVoyOXNaR1Z1TGpFd01EQXdNQ0k3Q2draWFYUmxiUzFwWkNJZ1BTQWlOalk0TlRJd056Y3lJanNLQ1NKaWFXUWlJRDBnSW1OdmJTNTBhV0Z1ZEhVdVRYaHBlVzkxSWpzS0NTSndkWEpqYUdGelpTMWtZWFJsTFcxeklpQTlJQ0l4TXpjek16TTBOVE0zTkRBd0lqc0tDU0p3ZFhKamFHRnpaUzFrWVhSbElpQTlJQ0l5TURFekxUQTNMVEE1SURBeE9qUTRPalUzSUVWMFl5OUhUVlFpT3dvSkluQjFjbU5vWVhObExXUmhkR1V0Y0hOMElpQTlJQ0l5TURFekxUQTNMVEE0SURFNE9qUTRPalUzSUVGdFpYSnBZMkV2VEc5elgwRnVaMlZzWlhNaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVaUlEMGdJakl3TVRNdE1EY3RNRGtnTURFNk5EZzZOVGNnUlhSakwwZE5WQ0k3Q24wPSI7CgkiZW52aXJvbm1lbnQiID0gIlNhbmRib3giOwoJInBvZCIgPSAiMTAwIjsKCSJzaWduaW5nLXN0YXR1cyIgPSAiMCI7Cn0=, zoneid=1, random=1281766650, account=10:1C:0C:3A:E6:B8, orderId=1000000079857121, key=97b7e0f03a7b187eb1b64d37792d1e73}
	 * @param map
	 * @return 1成功 ， 0 失败
	 */
	public int trigger(Map<String, String> map){
		try {
			logger.info("payMessage , give money-->"+map);
			int cmd = Integer.parseInt(map.get("cmd"));
			String random = map.get("random");
			String code = map.get("key");
//			 2013-07-09 09:48:01 -key:key  value:97b7e0f03a7b187eb1b64d37792d1e73
//			 2013-07-09 09:48:01 -key:cmd  value:888888
//			 2013-07-09 09:48:01 -key:random  value:1281766650
			String encodeByMD5 = MD5.MD5(Globalconstants.key+cmd+random);
			if (!code.equals(encodeByMD5)) {
				logger.info("encodeByMD5!=key    encodeByMD5="+encodeByMD5+",key="+code);
				return -1;
			}
//			http://192.168.1.199:9007/key=ECB7962D6A6F52E8C688F978EC3290EB&cmd=888888&account=1&money=11111&random=1
			if(cmd==888888){
				if (map.containsKey("account")&&map.containsKey("product")) {
					String acc = map.get("account");
					String terrace = map.get("terrace");
//					2013-07-09 09:48:01 -key:account  value:10:1C:0C:3A:E6:B8
//					2013-07-09 09:48:01 -key:terrace  value:Device
					if (terrace==null) {
						terrace = "";
					}
//					key:product  value:Mxiyou.golden.100000
					String productId = map.get("product").toString();
					if (!SysGloablMap.getProductMap().containsKey(productId)) {
						logger.info("wrong productId , =  "+productId);
						return -2;
					}
					Product product = SysGloablMap.getProductMap().get(productId);
					int addMoney = 0;
					int addCopper = 0;
					if (product.getMoneyType() == 1) {
						addMoney = product.getNum();
					}else if (product.getMoneyType()==2) {
						addCopper = product.getNum();
					}else {
						logger.info("wrong product   product="+product.toString());
						return -1;
					}
					
					AccountManager accountManager = (AccountManager) BaseAction.getIntance().getBean("accountManager");
					List<Account> accounts = accountManager.getByAccount(acc);
					if (accounts!=null) {
						Account account = accounts.get(0);
						HeroManager heroManager = (HeroManager) BaseAction.getIntance().getBean("heroManager");
						List<Hero> heros = heroManager.getHeros(account.getId());
						if (heros!=null) {
							Hero hero = heros.get(0);
							int hid = hero.getId();
							
							syncPayMessageToDB(map, account.getAccount(), product,hid,terrace);
							Connection connection = GlobalMap.getConns().get(hid);
							if (connection!=null) {
								logger.info("pay message -- > send 6001");
								PayHandler.getPayHandler().login(hid, connection);
							}else {
								logger.info("pay message -- > connection = null");
							}
							return 1;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return -1;
		}
		return -1;
	}
	/**
	 * 保存支付信息到数据库
	 */
	public static void syncPayMessageToDB(Map<String, String> map, String account, Product product, int hid, String terrace) {
//		2013-07-09 09:48:01 -key:orderId  value:1000000079857121
//		2013-07-09 09:48:01 -key:order  value:ewoJInNpZ25hdHVyZSIgPSAiQWdPczlLcUJXSTBoVDVreEIxMmFCdFIzdDM5ZkM2OUlQc2xUemt0em1ZSUdtaHo3ZGFtd0Z1QjhjYWFoem1adkJ3LzVUTU1wb08zRXhHNHBWZVRpNEJMOUtvYkRSakUyZlBOZ1daK2NuS0tSd3MySndCTGQvQWpSRkhHblE0djZZRWFNUEF0UVRpeHM2Q2lxS3pMM2liVC9ORTBNOEhWalhXTU80aHliQnhPRUFBQURWekNDQTFNd2dnSTdvQU1DQVFJQ0NHVVVrVTNaV0FTMU1BMEdDU3FHU0liM0RRRUJCUVVBTUg4eEN6QUpCZ05WQkFZVEFsVlRNUk13RVFZRFZRUUtEQXBCY0hCc1pTQkpibU11TVNZd0pBWURWUVFMREIxQmNIQnNaU0JEWlhKMGFXWnBZMkYwYVc5dUlFRjFkR2h2Y21sMGVURXpNREVHQTFVRUF3d3FRWEJ3YkdVZ2FWUjFibVZ6SUZOMGIzSmxJRU5sY25ScFptbGpZWFJwYjI0Z1FYVjBhRzl5YVhSNU1CNFhEVEE1TURZeE5USXlNRFUxTmxvWERURTBNRFl4TkRJeU1EVTFObG93WkRFak1DRUdBMVVFQXd3YVVIVnlZMmhoYzJWU1pXTmxhWEIwUTJWeWRHbG1hV05oZEdVeEd6QVpCZ05WQkFzTUVrRndjR3hsSUdsVWRXNWxjeUJUZEc5eVpURVRNQkVHQTFVRUNnd0tRWEJ3YkdVZ1NXNWpMakVMTUFrR0ExVUVCaE1DVlZNd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFNclJqRjJjdDRJclNkaVRDaGFJMGc4cHd2L2NtSHM4cC9Sd1YvcnQvOTFYS1ZoTmw0WElCaW1LalFRTmZnSHNEczZ5anUrK0RyS0pFN3VLc3BoTWRkS1lmRkU1ckdYc0FkQkVqQndSSXhleFRldngzSExFRkdBdDFtb0t4NTA5ZGh4dGlJZERnSnYyWWFWczQ5QjB1SnZOZHk2U01xTk5MSHNETHpEUzlvWkhBZ01CQUFHamNqQndNQXdHQTFVZEV3RUIvd1FDTUFBd0h3WURWUjBqQkJnd0ZvQVVOaDNvNHAyQzBnRVl0VEpyRHRkREM1RllRem93RGdZRFZSMFBBUUgvQkFRREFnZUFNQjBHQTFVZERnUVdCQlNwZzRQeUdVakZQaEpYQ0JUTXphTittVjhrOVRBUUJnb3Foa2lHOTJOa0JnVUJCQUlGQURBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQUVhU2JQanRtTjRDL0lCM1FFcEszMlJ4YWNDRFhkVlhBZVZSZVM1RmFaeGMrdDg4cFFQOTNCaUF4dmRXLzNlVFNNR1k1RmJlQVlMM2V0cVA1Z204d3JGb2pYMGlreVZSU3RRKy9BUTBLRWp0cUIwN2tMczlRVWU4Y3pSOFVHZmRNMUV1bVYvVWd2RGQ0TndOWXhMUU1nNFdUUWZna1FRVnk4R1had1ZIZ2JFL1VDNlk3MDUzcEdYQms1MU5QTTN3b3hoZDNnU1JMdlhqK2xvSHNTdGNURXFlOXBCRHBtRzUrc2s0dHcrR0szR01lRU41LytlMVFUOW5wL0tsMW5qK2FCdzdDMHhzeTBiRm5hQWQxY1NTNnhkb3J5L0NVdk02Z3RLc21uT09kcVRlc2JwMGJzOHNuNldxczBDOWRnY3hSSHVPTVoydG04bnBMVW03YXJnT1N6UT09IjsKCSJwdXJjaGFzZS1pbmZvIiA9ICJld29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREV6TFRBM0xUQTRJREU0T2pRNE9qVTNJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0l3TW1WbE9HTmhNV0V3TnpkaFlUUTROMlJqTVRBNU56WXdaV0ZoWVRVNE9ERTVNMkZrTlRSaUlqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTVRBd01EQXdNREEzT1RnMU56RXlNU0k3Q2draVluWnljeUlnUFNBaU1TNHdJanNLQ1NKMGNtRnVjMkZqZEdsdmJpMXBaQ0lnUFNBaU1UQXdNREF3TURBM09UZzFOekV5TVNJN0Nna2ljWFZoYm5ScGRIa2lJRDBnSWpFaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFek56TXpNelExTXpjME1EQWlPd29KSW5WdWFYRjFaUzEyWlc1a2IzSXRhV1JsYm5ScFptbGxjaUlnUFNBaVJESkNRakJHUmpNdE16WTFSaTAwUWpNeUxUZzJSVGd0UkVWQk5VVTRNVGsyTmtNeUlqc0tDU0p3Y205a2RXTjBMV2xrSWlBOUlDSk5lR2w1YjNVdVoyOXNaR1Z1TGpFd01EQXdNQ0k3Q2draWFYUmxiUzFwWkNJZ1BTQWlOalk0TlRJd056Y3lJanNLQ1NKaWFXUWlJRDBnSW1OdmJTNTBhV0Z1ZEhVdVRYaHBlVzkxSWpzS0NTSndkWEpqYUdGelpTMWtZWFJsTFcxeklpQTlJQ0l4TXpjek16TTBOVE0zTkRBd0lqc0tDU0p3ZFhKamFHRnpaUzFrWVhSbElpQTlJQ0l5TURFekxUQTNMVEE1SURBeE9qUTRPalUzSUVWMFl5OUhUVlFpT3dvSkluQjFjbU5vWVhObExXUmhkR1V0Y0hOMElpQTlJQ0l5TURFekxUQTNMVEE0SURFNE9qUTRPalUzSUVGdFpYSnBZMkV2VEc5elgwRnVaMlZzWlhNaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVaUlEMGdJakl3TVRNdE1EY3RNRGtnTURFNk5EZzZOVGNnUlhSakwwZE5WQ0k3Q24wPSI7CgkiZW52aXJvbm1lbnQiID0gIlNhbmRib3giOwoJInBvZCIgPSAiMTAwIjsKCSJzaWduaW5nLXN0YXR1cyIgPSAiMCI7Cn0=
//		int status = Integer.parseInt(result.get("status").toString());
//		2013-07-09 09:48:01 -key:result  value:{"receipt":
//		{"original_purchase_date_pst":"2013-07-08 18:48:57 America/Los_Angeles",
//			"purchase_date_ms":"1373334537400",
//			"unique_identifier":"02ee8ca1a077aa487dc109760eaaa588193ad54b",
//			"original_transaction_id":"1000000079857121",
//			"bvrs":"1.0",
//			"transaction_id":"1000000079857121",
//			"quantity":"1",
//			"unique_vendor_identifier":"D2BB0FF3-365F-4B32-86E8-DEA5E81966C2",
//			"item_id":"668520772",
//			"product_id":"Mxiyou.golden.100000",
//			"purchase_date":"2013-07-09 01:48:57 Etc/GMT",
//			"original_purchase_date":"2013-07-09 01:48:57 Etc/GMT",
//			"purchase_date_pst":"2013-07-08 18:48:57 America/Los_Angeles",
//			"bid":"com.tiantu.Mxiyou",
//			"original_purchase_date_ms":"1373334537400"}
//		,"status":0}
		
		try {
			int zoneid = Integer.parseInt(map.get("zoneid"));
			JSONObject result = JSONObject.parseObject(map.get("result"));
			JSONObject receipt = JSONObject.parseObject(result.get("receipt").toString());
			String original_transaction_id = (String) receipt.get("original_transaction_id");
			String bvrs = (String) receipt.get("bvrs");
			String transaction_id = (String) receipt.get("transaction_id");
			String quantity = (String) receipt.get("quantity");
			String purchase_date = (String) receipt.get("purchase_date");
			String product_id = (String) receipt.get("product_id");
			String original_purchase_date = (String) receipt.get("original_purchase_date");
			String bid = (String) receipt.get("bid");
			String purchase_date_pst = (String) receipt.get("purchase_date_pst");
			String original_purchase_date_ms = (String) receipt.get("original_purchase_date_ms");
			String original_purchase_date_pst = (String) receipt.get("original_purchase_date_pst");
			String purchase_date_ms = (String) receipt.get("purchase_date_ms");
			String unique_identifier = (String) receipt.get("unique_identifier");
			String unique_vendor_identifier = (String) receipt.get("unique_vendor_identifier");
			String item_id = (String) receipt.get("item_id");
			String orderId = map.get("orderId");
			String order = map.get("order");
			Back_pay_message pay_message = new Back_pay_message();
			Back_pay_messageManager pay_messageManager = (Back_pay_messageManager) BaseAction.getIntance().getBean("pay_messageManager");
			Back_pay_message byOrderId = pay_messageManager.getByOrderId(orderId);
			if (byOrderId!=null) {
				return;
			}
			pay_message.setAccount(account);
			pay_message.setBid(bid);
			pay_message.setBvrs(bvrs);
			pay_message.setHid(hid);
			pay_message.setItem_id(item_id);
			pay_message.setOrder(order);
			pay_message.setOrderId(orderId);
			pay_message.setOriginal_purchase_date(original_purchase_date);
			pay_message.setOriginal_purchase_date_ms(original_purchase_date_ms);
			pay_message.setOriginal_purchase_date_pst(original_purchase_date_pst);
			pay_message.setOriginal_transaction_id(original_transaction_id);
			pay_message.setProduct_id(product_id);
			pay_message.setProductId(product.getId());
			pay_message.setPurchase_date(purchase_date);
			pay_message.setPurchase_date_ms(purchase_date_ms);
			pay_message.setPurchase_date_pst(purchase_date_pst);
			pay_message.setQuantity(quantity);
			pay_message.setTerrace(terrace);
			pay_message.setTime(TimeUtil.TimeMillis());
			pay_message.setTransaction_id(transaction_id);
			pay_message.setUnique_identifier(unique_identifier);
			pay_message.setUnique_vendor_identifier(unique_vendor_identifier);
			pay_message.setZoneid(zoneid);
			pay_messageManager.insert(pay_message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	
}
