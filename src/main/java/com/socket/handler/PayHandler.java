package com.socket.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Hero;
import com.model.backstage.Back_pay_dianxin;
import com.model.backstage.Back_pay_message;
import com.model.backstage.Back_record;
import com.model.sys.Product;
import com.model.sys.ProductDianxin;
import com.model.sys.SysGloablMap;
import com.service.Back_pay_dianxinManager;
import com.service.Back_pay_messageManager;
import com.util.MoneyControl;
import com.util.SysUtil;
import com.util.TimeUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;

public class PayHandler {
	private static Logger logger = LoggerFactory.getLogger(PayHandler.class);
	private static PayHandler payHandler;
	public static PayHandler getPayHandler() {
		if (payHandler==null) {
			payHandler = new PayHandler();
		}
		return payHandler;
	}
	
	public void give(Object obj1,Object obj2) {
		byte[] bytes = null;
		Connection connection = null;
		try {
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (bytes!=null && connection!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				String orderid = input.readUTF();
				bis.close();
				input.close();
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					logger.info("hid == null");
					return;
				}
				
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					logger.info("hero==null");
					return;
				}
				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_91)||Globalconstants.VERSION.equals(Globalconstants.VERSION_APPLESTORE)) {
					Back_pay_messageManager pay_messageManager =(Back_pay_messageManager) BaseAction.getIntance().getBean("pay_messageManager");
					Back_pay_message pay_message = pay_messageManager.getByOrderId(orderid);
					
					if (pay_message.getHid()!=hid) {
						logger.info("pay_message.getHid()!=hid");
						return;
					}
					
					if (pay_message.getIsdone()!=0) {
						logger.info("pay message--> pay isdone = 1,time = "+ TimeUtil.formatTime(pay_message.getGiveTime()));
						return;
					}
					
					String product_id = pay_message.getProduct_id();
					if (!SysGloablMap.getProductMap().containsKey(product_id)) {
						connection.write(SysUtil.getBytes(1018, (byte) 0));
						logger.info("pay message--> wrong product_id , product_id= " + product_id);
						return;
					}
					Product product = SysGloablMap.getProductMap().get(product_id);
					
//					1 元宝， 2 铜币
					int moneyType = product.getMoneyType();
					int num = product.getNum();
					
					//首次购买双倍奖励
					String firstDouble = hero.getFirstDouble();
					List<Integer> splitGetInt = SysUtil.splitGetInt(firstDouble, "_");
					boolean first = false;
					if (!splitGetInt.contains(product.getId())) {//有首次双倍奖励，
						hero.setFirstDouble(hero.getFirstDouble()+"_"+product.getId());
						num = num*2;
						first = true;
					}
					
					if (moneyType==1) {
						MoneyControl.moneyIncome(hero, Back_record.REASON_RECHARGE, num);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						DataOutputStream output = new DataOutputStream(bos);
						output.writeInt(1017);//元宝更新
						output.writeInt(hero.getMoney());
						bos.close();
						output.close();
						connection.write(bos.toByteArray());
					}else if (moneyType==2) {
						HeroHandler.getHeroHandler().giveCopper(num, connection);
					}
					connection.write(SysUtil.getBytes(1018, (byte) 1));//充值成功
					if (first) {
						HeroHandler.getHeroHandler().firstDouble(hero, connection);
					}
					HeroHandler.getHeroHandler().recharge(hero, connection, product.getPrice());
					pay_messageManager.update(TimeUtil.currentTime(), orderid, 1);
				}else if (Globalconstants.VERSION.equals(Globalconstants.VERSION_DIANXIN)) {
					int orderId = Integer.parseInt(orderid);
					Back_pay_dianxinManager dianxinManager = (Back_pay_dianxinManager) BaseAction.getIntance().getBean("dianxinManager");
					Back_pay_dianxin dianxin = dianxinManager.getByOrderId(orderId);
					if (dianxin==null) {
						logger.info("dianxin=null");
						connection.write(SysUtil.getBytes(1018, (byte) 0));
						return;
					}
					if (dianxin.getHid()!=hid) {
						connection.write(SysUtil.getBytes(1018, (byte) 0));
						logger.info("dianxin.getHid()!=hid");
						return;
					}
					
					if (dianxin.getGive()!=0) {
						connection.write(SysUtil.getBytes(1018, (byte) 0));
						logger.info("dianxin.getGive()!=0");
						return;
					}
					int pid = dianxin.getPid();
					ProductDianxin product = SysGloablMap.getProductDianxinMap().get(pid);
					if (product==null) {
						connection.write(SysUtil.getBytes(1018, (byte) 0));
						logger.info("product==null");
						return;
					}
					
					int moneyType = product.getMoneyType();
					int num = product.getNum();
					if (moneyType==1) {
						MoneyControl.moneyIncome(hero, Back_record.REASON_RECHARGE, num);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						DataOutputStream output = new DataOutputStream(bos);
						output.writeInt(1017);//元宝更新
						output.writeInt(hero.getMoney());
						bos.close();
						output.close();
						connection.write(bos.toByteArray());
					}else if (moneyType==2) {
						HeroHandler.getHeroHandler().giveCopper(num, connection);
					}
					connection.write(SysUtil.getBytes(1018, (byte) 1));//充值成功
					HeroHandler.getHeroHandler().recharge(hero, connection, product.getPrice());
					dianxinManager.updateDone(orderId);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("" , e);
		}
	}
	
	public void login(int hid,Connection connection) {
		if (Globalconstants.VERSION.equals(Globalconstants.VERSION_91)||Globalconstants.VERSION.equals(Globalconstants.VERSION_APPLESTORE)) {
			Back_pay_messageManager pay_messageManager =(Back_pay_messageManager) BaseAction.getIntance().getBean("pay_messageManager");
			try {
				List<Back_pay_message> list = pay_messageManager.getUndone(hid);
				if (list!=null && list.size()>0) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(6001);
					output.writeShort(list.size());
					for (Back_pay_message pay_message : list) {
						output.writeUTF(pay_message.getOrderId());
					}
					connection.write(bos.toByteArray());
					output.close();
					bos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("" , e);
			}
		}else if (Globalconstants.VERSION.equals(Globalconstants.VERSION_DIANXIN)) {
			try {
				Back_pay_dianxinManager dianxinManager = (Back_pay_dianxinManager) BaseAction.getIntance().getBean("dianxinManager");
				List<Back_pay_dianxin> list = dianxinManager.getUndoneDeal(hid);
				if (list!=null && list.size()>0) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream output = new DataOutputStream(bos);
					output.writeInt(6001);
					output.writeShort(list.size());
					Iterator<Back_pay_dianxin> iterator = list.iterator();
					while (iterator.hasNext()) {
						String orderId = iterator.next().getOrderId()+"";
						output.writeUTF(orderId);
					}
					connection.write(bos.toByteArray());
					output.close();
					bos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("" , e);
			}
		}
	}
}
