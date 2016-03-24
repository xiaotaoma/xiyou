package com.socket.handler;

import com.cache.GlobalMap;
import com.cache.Globalconstants;
import com.main.BaseAction;
import com.model.Arena;
import com.model.Bag;
import com.model.Grid;
import com.model.Hero;
import com.model.backstage.Back_record;
import com.model.backstage.Back_tool;
import com.model.sys.CopperShop;
import com.model.sys.Shop;
import com.model.sys.SysGloablMap;
import com.model.sys.Tool;
import com.service.ArenaManager;
import com.service.BagManager;
import com.socket.back.ToolStatistics;
import com.socket.battle.BattleHandler;
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
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BagHandler {
	private static Logger logger = LoggerFactory.getLogger(BagHandler.class);
	private static BagHandler bagHandler;
	public static BagHandler getBagHandler() {
		if (bagHandler==null) {
			bagHandler = new BagHandler();
		}
		return bagHandler;
	}
	public void init(int hid,String terrace) {
		try {
			BagManager bagManager = (BagManager) BaseAction.getIntance().getBean("bagManager");
			Bag bag = bagManager.getByHid(hid);
			if (bag==null) {
				bag = new Bag(hid);
				
				addTools(bag, 16000, 1, Back_tool.REASON_GIFT);
				if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
					addTools(bag, 16001, 1, Back_tool.REASON_GIFT);
					addTools(bag, 16002, 1, Back_tool.REASON_GIFT);
					addTools(bag, 16003, 1, Back_tool.REASON_GIFT);
					addTools(bag, 16004, 1, Back_tool.REASON_GIFT);
					addTools(bag, 16005, 1, Back_tool.REASON_GIFT);
					addTools(bag, 16006, 1, Back_tool.REASON_GIFT);
					addTools(bag, 16007, 1, Back_tool.REASON_GIFT);
					addTools(bag, 16008, 1, Back_tool.REASON_GIFT);
					
					if (terrace.equals(Globalconstants.TAIWAN_FB)) {
						addTools(bag, 16011, 1, Back_tool.REASON_GIFT);
					}
				}
				
				bagManager.insert(bag);
			}
			GlobalMap.getBagMap().put(hid, bag);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
		bagMessage(hid);
	}
	
	public void bagMessage(int hid) {
		try {
			Bag bag = GlobalMap.getBagMap().get(hid);
			if (bag==null) {
				return;
			}
			String data = bag.getData();
			String[] split = data.split(",");
			int length = split.length;
			List<Grid> list = new ArrayList<Grid>();
			for (int i = 0; i < length; i++) {
				Grid grid = new Grid(split[i]);
				if (grid.getToolId()!=0) {
					list.add(grid);
				}
			}
			int size = list.size();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(8001);
			output.writeByte(length);//背包格子总数
			output.writeShort(size);
			for (int i = 0; i < size; i++) {
				Grid grid = list.get(i);
				int toolId = grid.getToolId();
				int num = grid.getNum();
				output.writeInt(toolId);
				output.writeByte(num);
			}
			bos.close();
			output.close();
			
			Connection connection = GlobalMap.getConns().get(hid);
			if (connection!=null) {
				connection.write(bos.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public boolean addTools(Bag bag, int toolId, int num, int reason) {
		Bag copy = bag.copy();
		//验证能否放入
		boolean addTool = addTool(copy, toolId, num);
		if (!addTool) {
			return false;
		}
		int hasNum = getNumByToolId(toolId, bag);
		addTool(bag, toolId, num);
		Connection connection = GlobalMap.getConns().get(bag.getHid());
		if (connection!=null) {
			List<Integer[]> modify = new ArrayList<Integer[]>();
			modify.add(new Integer[]{toolId,num});
			bagChanges(connection, 1, modify);
		}
		int leftNum = getNumByToolId(toolId, bag);
		ToolStatistics.getToolStatistics().statistics(bag.getHid(), hasNum, leftNum, num, reason, Back_tool.TYPE_ADD, toolId);
		return true;
	}
	/**
	 * 
	 * @param bag
	 * @param list int[] id num
	 */
	public boolean addTools(Bag bag, List<Integer[]> list, int reason) {
		int size = list.size();
		Bag copy = bag.copy();
		//验证能否放入
		for (int i = 0; i < size; i++) {
			Integer[] integers = list.get(i);
			int toolId = integers[0];
			int num = integers[1];
			boolean addTool = addTool(copy, toolId, num);
			if (!addTool) {
				return false;
			}
		}
		//
		for (int i = 0; i < size; i++) {
			Integer[] integers = list.get(i);
			int toolId = integers[0];
			int num = integers[1];
			int hasNum = getNumByToolId(toolId, bag);
			addTool(bag, toolId, num);
			int leftNum = getNumByToolId(toolId, bag);
			ToolStatistics.getToolStatistics().statistics(bag.getHid(), hasNum, leftNum, num, reason, Back_tool.TYPE_ADD, toolId);
		}
		return true;
	}
	/**
	 * 
	 */
	public void giveTools(int toolId,int num,int hid,int reason) {
		try {
			Bag bag = GlobalMap.getBagMap().get(hid);
			if (bag!=null) {
				BagHandler.getBagHandler().addTools(bag, toolId, num,reason);
				BagHandler.getBagHandler().sortBag(bag);
				BagHandler.getBagHandler().bagMessage(hid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public void getPracticeExp(Object arg0,Object arg1) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (arg0!=null) {
				connection = (Connection) arg0;
			}
			if (arg1!=null) {
				bytes = (byte[]) arg1;
			}
			if (connection!=null && bytes !=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				
				int exp = hero.getExp();
				int currentTime = TimeUtil.currentTime();
				int expOverTime = hero.getExpOverTime();
				
				if (currentTime>=expOverTime) {//时间到，可以领
					if (exp>0) {
						HeroHandler.getHeroHandler().addExp(exp, connection);
						hero.setExp(0);
						hero.setExpOverTime(currentTime);
						connection.write(SysUtil.getBytes(8009, (byte)1));
					}else {
						connection.write(SysUtil.getBytes(8009, (byte)2));
					}
				}else {
					connection.write(SysUtil.getBytes(8009, (byte)2));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public void getPracticeTime(Object arg0,Object arg1) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (arg0!=null) {
				connection = (Connection) arg0;
			}
			if (arg1!=null) {
				bytes = (byte[]) arg1;
			}
			if (connection!=null && bytes !=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				getPracticeTime(connection, hero);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 延迟获得经验
	 */
	public boolean laterGetExp(Tool tool, int useNum, Hero hero, Connection connection) {
		int currentTime = TimeUtil.currentTime();
		int expOverTime = hero.getExpOverTime();
		if (currentTime<expOverTime) {
			return false;
		}
		String effect = tool.getEffect();
		List<Integer> list = SysUtil.splitGetInt(effect, "_");
		int time = list.get(0);
		int exp = list.get(1);
		int overTime = currentTime+time*60;
		hero.setExp(hero.getExp()+exp*useNum);
		hero.setExpOverTime(overTime);
		getPracticeTime(connection, hero);
		return true;
	}
	/**
	 * 使用获得道具
	 * @param tool
	 * 001_16008_1_0_0_15,002_16007_1_0_0_14,003_16006_1_0_0_13,004_16005_1_0_0_12,005_16004_1_0_0_11,
	 * 006_16003_1_0_0_10,007_16002_1_0_0_9,008_16001_1_0_0_8,009_16000_1_0_0_7,010_15904_10_1_20_5,
	 * 011_15903_10_1_20_4,012_15902_10_1_20_3,013_15901_10_1_20_2,014_15900_20_1_20_1,
	 * 015_15900_5_1_20_1,016_0_0_0_0_0
	 */
	public boolean useGetTools(Tool tool, Bag bag, Hero hero, Connection connection, int useNum) {
		try {
			List<Integer> list = null;
			List<Integer[]> tools = new ArrayList<Integer[]>();
			int money = 0;
			int copper = 0;
			int exp = 0;
			for (int j = 0; j < useNum; j++) {
				String effect = tool.getEffect();
				String[] split = effect.split(",");
				int len = split.length;
				for (int i = 0; i < len; i++) {
					//				type 元宝1，铜币2，道具3，经验4
					//				id   道具id，元宝或铜币是0
					//				num  具体数量
					list = SysUtil.splitGetInt(split[i], "_");
					Integer type = list.get(0);
					Integer toolId = list.get(1);
					Integer num = list.get(2);
					if (type == 1) {
						money += num;
					} else if (type == 2) {
						copper += num;
					} else if (type == 3) {
						Integer[] aa = new Integer[2];
						aa[0] = toolId;
						aa[1] = num;
						tools.add(aa);
					} else if (type == 4) {
						exp += num;
					}
				}
			}
			
			boolean addTools = addTools(bag, tools, Back_tool.REASON_USETOOL);
			if (addTools) {
				if (money>0) {
					MoneyControl.moneyIncome(hero, Back_record.REASON_USELIBAO, money);
					connection.write(SysUtil.getBytes(1017, hero.getMoney()));
				}
				if (exp>0) {
					HeroHandler.getHeroHandler().addExp(exp, connection);
				}
				if (copper>0) {
					HeroHandler.getHeroHandler().giveCopper(copper, connection);
				}
				bagChanges(connection, 1, tools);
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return false;
	}
	/**
	 * 
	 * @param bag
	 * @param toolId
	 * @param num
	 * @return
	 */
	public boolean checkAddTool(Bag bag, int toolId, int num) {
		Tool tool = SysGloablMap.getToolMap().get(toolId);
		int overlap = tool.getOverlap();
		int overlapNum = tool.getOverlapNum();
		List<Grid> emptys = getByTooId(bag, 0);
		int size = emptys.size();
		List<Grid> byTooId = getByTooId(bag, toolId);
		if (overlap == 0) {
			if (size<num) {//格子不够，不能放入
				return false;
			}
		}else {
			int n = 0;
			Grid grid = null;
			if (byTooId.size()>0) {
				grid = byTooId.get(byTooId.size()-1);
				int hasNum = grid.getNum();
				n = overlapNum - hasNum;//最后一个可放入的数量
			}
			if (num>n+size*overlapNum) {//空格子不够，不能够放入
				return false;
			}
		}
		return true;
	}
	public void useTools(Object arg0,Object arg1) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (arg0!=null) {
				connection = (Connection) arg0;
			}
			if (arg1!=null) {
				bytes = (byte[]) arg1;
			}
			if (connection!=null && bytes !=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid==null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero==null) {
					return;
				}
				Bag bag = GlobalMap.getBagMap().get(hid);
				if (bag==null) {
					return;
				}
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int toolId = input.readInt();
				int useNum = input.readByte();
				int n = useNum;
				bis.close();
				input.close();
				Tool tool = SysGloablMap.getToolMap().get(toolId);
				if (tool==null) {
					connection.write(SysUtil.getBytes(8005, (byte)2,toolId));
					return;
				}
				
				int numByTooId = getNumByToolId(toolId,bag);
				if (numByTooId<useNum) {//
					connection.write(SysUtil.getBytes(8005, (byte)2,toolId));
					return;
				}
				//使用条件判断
				boolean useQuery = useQuery(tool, hero);
				if (!useQuery) {
					connection.write(SysUtil.getBytes(8005, (byte)2,toolId));
					return;
				}
				
				boolean toolEffect = toolEffect(tool, hero, connection, bag,useNum);
				if (toolEffect) {//使用物品成功
					List<Grid> list = getByTooId(bag, toolId);
					List<Grid> modify = new ArrayList<Grid>();
					int size = list.size();
					for (int i = 0; i < size; i++) {
						if (useNum<=0) {
							break;
						}
						Grid grid = list.get(i);
						int num = grid.getNum();
						if (useNum>=num) {//格子中没有足够的数量
							grid.setNum(0);
							grid.setOverlap(0);
							grid.setOverlapNum(0);
							grid.setSortId(0);
							grid.setToolId(0);
							useNum = useNum - num;
						}else if (useNum<num) {
							grid.setNum(num-useNum);
						}
						modify.add(grid);
					}
					
					updateGrids(bag, modify);
					sortBag(bag);
					bagMessage(hid);
					connection.write(SysUtil.getBytes(8005, (byte)1,toolId));
					
					List<Integer[]> changes = new ArrayList<Integer[]>();
					changes.add(new Integer[]{toolId,n});
					bagChanges(connection, 2, changes);
					
					int leftNum = getNumByToolId(toolId,bag);
					ToolStatistics.getToolStatistics().statistics(bag.getHid(), numByTooId, leftNum, n, Back_tool.REASON_USE, Back_tool.TYPE_REDUCE, toolId);
				}else {
					connection.write(SysUtil.getBytes(8005, (byte)2,toolId));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 购买道具
	 * @param obj1
	 * @param obj2
	 */
	public void buyTools(Object obj1,Object obj2) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (obj1!=null) {
				connection = (Connection) obj1;
			}
			if (obj2!=null) {
				bytes = (byte[]) obj2;
			}
			if (connection!=null && bytes !=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid == null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero == null) {
					return;
				}
				Bag bag = GlobalMap.getBagMap().get(hid);
				if (bag==null) {
					return;
				}
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int shopId = input.readByte();
				bis.close();
				input.close();
				Shop shop = SysGloablMap.getShopMap().get(shopId);
				if (shop==null) {
					return;
				}
				
				int moneyType = shop.getMoneyType();
				int price = shop.getPrice();
				int money = hero.getMoney();
				if (moneyType==1 && money>=price) {
					boolean checkAddTool = checkAddTool(bag, shop.getToolId(), 1);
					if (checkAddTool) {
						boolean moneyExpenses = MoneyControl.moneyExpenses(hero, Back_record.REASON_BUYFROMSHOP, price);
						if (moneyExpenses) {
							addTools(bag,  shop.getToolId(), 1, Back_tool.REASON_BUYFROMSHOP);
							
							connection.write(SysUtil.getBytes(8003, (byte) 1));
							bagMessage(hid);
							connection.write(SysUtil.getBytes(1017, hero.getMoney()));
						}else {
							connection.write(SysUtil.getBytes(8003, (byte) 2));
						}
						/**
						 * 光顧商城,任意成功購買一次道具
						 */
						String buyToolsTimes = hero.getBuyToolsTimes();
						if (buyToolsTimes.equals("")) {
							hero.setBuyToolsTimes("1_0");
						}else {
							List<Integer> splitGetInt = SysUtil.splitGetInt(buyToolsTimes, "_");
							Integer times = splitGetInt.get(0);
							times++;
							hero.setBuyToolsTimes(times+"_"+splitGetInt.get(1));
						}
					}else {
						connection.write(SysUtil.getBytes(8003, (byte) 2));
					}
				}else {
					connection.write(SysUtil.getBytes(8003, (byte) 2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void buyCopper(Object arg0,Object arg1) {
		Connection connection = null;
		byte[] bytes = null;
		try {
			if (arg0!=null) {
				connection = (Connection) arg0;
			}
			if (arg1!=null) {
				bytes = (byte[]) arg1;
			}
			if (connection!=null && bytes !=null) {
				Integer hid = (Integer) connection.getAttributes().getAttribute("hid");
				if (hid == null) {
					return;
				}
				Hero hero = GlobalMap.getHeroMap().get(hid);
				if (hero == null) {
					return;
				}
				
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int shopId = input.readByte();
				bis.close();
				input.close();
				
				CopperShop copperShop = SysGloablMap.getCopperShopMap().get(shopId);
				if (copperShop==null) {
					return;
				}
				
				int money = hero.getMoney();
				if (money<copperShop.getPrice()) {
					connection.write(SysUtil.getBytes(8020, 2));
					return;
				}
				
				boolean moneyExpenses = MoneyControl.moneyExpenses(hero, Back_record.REASON_BUYCOPPER,copperShop.getPrice());
				if (moneyExpenses) {
					connection.write(SysUtil.getBytes(8020, 1));
					HeroHandler.getHeroHandler().giveCopper(copperShop.getCopper(), connection);
					connection.write(BattleHandler.getTestHandler().getByte(1017, hero.getMoney()));
				}else {
					connection.write(SysUtil.getBytes(8020, 2));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	/**
	 * 获取某一道具的数量
	 * @param toolId
	 * @param bag
	 * @return
	 */
	private int getNumByToolId(int toolId,Bag bag) {
		if (bag==null) {
			return 0;
		}
		String data = bag.getData();
		if (data==null || "".equals(data)) {
			return 0;
		}
		String query = "\\d*_"+toolId+"_\\d*_\\d*_\\d*_\\d*";
		Pattern pattern = Pattern.compile(query);
		Matcher matcher = pattern.matcher(data);
		int n = 0;
		while (matcher.find()) {
			String group = matcher.group();
			Grid grid = new Grid(group);
			n += grid.getNum();
		}
		return n;
	}
	
	/**
	 * 获取物品
	 * @param toolId
	 * @param num
	 * @return true 放入成功   false 放入背包失败
	 */
	private boolean addTool(Bag bag, int toolId, int num) {
		Tool tool = SysGloablMap.getToolMap().get(toolId);
		if (bag!=null && tool!=null) {
			int overlap = tool.getOverlap();
			int overlapNum = tool.getOverlapNum();
			int sortId = tool.getSortId();
			List<Grid> emptys = getByTooId(bag, 0);
			int size = emptys.size();
			List<Grid> byTooId = getByTooId(bag, toolId);
			List<Grid> modify = new ArrayList<Grid>();
			if (overlap == 0) {//不能叠加，只能放入空格子
				if (size<num) {//格子不够，不能放入
					return false;
				}else {
					for (int i = 0; i < num; i++) {//依次放入空格子
						Grid grid = emptys.get(i);
						grid.setNum(1);
						grid.setOverlap(0);
						grid.setOverlapNum(0);
						grid.setSortId(sortId);
						grid.setToolId(toolId);
						modify.add(grid);
					}
					updateGrids(bag, modify);
					sortBag(bag);
					return true;
				}
			}else {
				int n = 0;
				Grid grid = null;
				if (byTooId.size()>0) {
					grid = byTooId.get(byTooId.size()-1);
					int hasNum = grid.getNum();
					n = overlapNum - hasNum;//最后一个可放入的数量
				}
				
				if (num>n+size*overlapNum) {//空格子不够，不能够放入
					return false;
				}else {
					int leftNum = num;
					if (grid!=null) {
						if (n>=num) {
							grid.setNum(grid.getNum()+num);
							leftNum = 0;
							modify.add(grid);
						}else {
							grid.setNum(overlapNum);
							leftNum = num - n;
							modify.add(grid);
						}
					}
					for (int i = 0; i < size; i++) {
						if (leftNum==0) {
							break;
						}
						Grid g = emptys.get(i);
						if (leftNum>=overlapNum) {//剩余数量大于可叠加数量
							g.setNum(overlapNum);
							leftNum = leftNum - overlapNum;
						}else {
							g.setNum(leftNum);
							leftNum = 0;
						}
						g.setOverlap(overlap);
						g.setOverlapNum(overlapNum);
						g.setSortId(sortId);
						g.setToolId(toolId);
						modify.add(g);
					}
					updateGrids(bag, modify);
					sortBag(bag);
				}
				return true;
			}
		}else {
			return false;
		}
	}
	/**
	 * 获取道具id = toolId的所有格子
	 * toolId = 0时 获取所有空格子
	 * 
	 */
	private List<Grid> getByTooId(Bag bag, int toolId) {
		if (bag==null) {
			return null;
		}
		String data = bag.getData();
		if (data==null || "".equals(data)) {
			return null;
		}
		String query = "\\d*_"+toolId+"_\\d*_\\d*_\\d*_\\d*";
		Pattern pattern = Pattern.compile(query);
		Matcher matcher = pattern.matcher(data);
		List<Grid> list = new ArrayList<Grid>();
		while (matcher.find()) {
			String group = matcher.group();
			Grid grid = new Grid(group);
			list.add(grid);
		}
		return list;
	}
	/**
	 * 更新，修改背包数据
	 * @param bag
	 * @param list
	 */
	private void updateGrids(Bag bag, List<Grid> list) {
		int size = list.size();
		if (bag == null || list == null || size == 0) {
			return;
		}
		String data = bag.getData();
		if (data==null || data.equals("")) {
			return;
		}
		Pattern pattern = null;
		Matcher matcher = null;
		for (int i = 0; i < size; i++) {
			Grid grid = list.get(i);
			data = updateGrid(data, grid, pattern, matcher);
		}
		bag.setData(data);
	}
	/**
	 * 
	 * @param grid
	 */
	private String updateGrid(String data, Grid grid, Pattern pattern, Matcher matcher) {
		String g = grid.getG();
		pattern = Pattern.compile(g+"_\\d*_\\d*_\\d*_\\d*_\\d*");
		matcher = pattern.matcher(data);
		if (matcher.find()) {
			String string = grid.toString();
			String group = matcher.group();
			data = data.replace(group, string);
		}
		return data;
	}
	
	private void sortBag(Bag bag) {
		if (bag == null) {
			return;
		}
		String data = bag.getData();
		if (data==null || data.equals("")) {
			return;
		}
		String[] split = data.split(",");
		int length = split.length;
		List<Grid> list = new ArrayList<Grid>();
		for (int i = 0; i < length; i++) {
			list.add(new Grid(split[i]));
		}
		bagOverlap(list);
		Collections.sort(list, new BagSort());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			Grid grid = list.get(i);
			grid.setGridId(i+1);
			sb.append(grid.toString()).append(",");
		}
		String substring = sb.substring(0, sb.length()-1);
		bag.setData(substring);
	}
	/**
	 * 背包物品叠加
	 * @param list
	 */
	private void bagOverlap(List<Grid> list) {
		int size=list.size();
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < size; j++) {
				Grid grid1 = list.get(i);
				Grid grid2 = list.get(j);
				if (grid1.getOverlap()==1) {//可以叠加
					if (grid1.getToolId()==grid2.getToolId()) {//判断是否同一种物品，绑定状态是否相同
						Integer sss = 0;
						int overlapNum = grid1.getOverlapNum();
						int a = grid1.getNum();
						int b = grid2.getNum();
						if (a+b<=overlapNum) {
							sss=a+b;
							grid2.setNum(0);
							grid2.setOverlap(0);
							grid2.setSortId(0);
							grid2.setToolId(0);
							grid2.setOverlapNum(0);
						}else {
							sss=overlapNum;
							grid2.setNum(a+b-overlapNum);
						}
						grid1.setNum(sss);
					}
				}
			}
		}
	}
	
	/** 
	 * 道具使用条件判断
	 * @param tool
	 * @param hero
	 * @return true 可以使用，false 不能使用
	 */
	private boolean useQuery(Tool tool, Hero hero) {
		String useQuery = tool.getUseQuery();
		if (useQuery.equals("0")) {
			return true;
		}
		List<String> split = SysUtil.split(useQuery, "_");
		int type = Integer.parseInt(split.get(0));
		int query = Integer.parseInt(split.get(1));
		boolean result = false;
		switch (type) {
		case 1:
			int maxWave = hero.getMaxWave();
			if (maxWave>query) {
				result = true;
			}
			break;
		default:
			break;
		}
		return result;
	}
	private boolean toolEffect(Tool tool, Hero hero, Connection connection, Bag bag, int useNum) {
		boolean result = false;
		try {
			int effectType = tool.getEffectType();
			String effect = tool.getEffect();
			switch (effectType) {
			case 1://使用获得物品
				result = useGetTools(tool, bag, hero, connection,useNum);
				break;
			case 2://获得经验
				int exp = Integer.parseInt(effect)*useNum;
				HeroHandler.getHeroHandler().addExp(exp, connection);
				result = true;
				break;
			case 3://增加竞技场次数
				result = addArenaTimes(hero, result, effect,useNum,connection);
				break;
			case 4://增加徒弟大战次数
				result = addBattleTimes(hero, result, effect,useNum);
				break;
			case 5:// n 久后获得经验
				result = laterGetExp(tool, useNum, hero,connection);
				break;
			case 6://月卡
				result = useVip(tool, hero, connection);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return result;
	}
	private boolean useVip(Tool tool, Hero hero, Connection connection) {
		String effect = tool.getEffect();
		int days = Integer.parseInt(effect);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		calendar.add(Calendar.DAY_OF_YEAR, days);
		hero.setEndCardTime((int) (calendar.getTimeInMillis()/1000));
		ActivityHandler.getActivityHandler().vipMessage(connection, hero);
		return true;
	}
	/**
	 * 增加竞技场次数
	 * @param hero
	 * @param result
	 * @param effect
	 * @return
	 * @throws SQLException
	 */
	private boolean addArenaTimes(Hero hero, boolean result, String effect, int useNum, Connection connection)
			throws SQLException {
		int arenaTimes = Integer.parseInt(effect)*useNum;
		ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
		Arena arena = arenaManager.getByHid(hero.getId());
		int times = arena.getTimes();
		times+=arenaTimes;
		if (times <= 5) {
			arena.setTimes(times);
			ArenaHandler.getArenaHandler().sendChanglleTimes(arena,	connection);
			ArenaHandler.getArenaHandler().sendRank(arena.getRank(), connection);
			ArenaHandler.getArenaHandler().sendPre(arena.getPrestige(), connection);
			arenaManager.update(arena);
			result = true;
		}else if (times>5) {
			result = false;
		}
		return result;
	}
	/**
	 * 增加土地大战次数
	 * @param hero
	 * @param result
	 * @param effect
	 * @return
	 */
	private boolean addBattleTimes(Hero hero, boolean result, String effect, int useNum) {
		BattleHandler.getTestHandler().setHonourTime(hero);
		int n = Integer.parseInt(effect)*useNum;
		int battleTimes = hero.getTimes();
		battleTimes+=n;
		if (battleTimes<= Globalconstants.BATTLETIMES) {
			hero.setTimes(battleTimes);
			BattleHandler.getTestHandler().battleTimes(hero);
			result = true;
		}else if (battleTimes> Globalconstants.BATTLETIMES) {
			result = false;
		}
		return result;
	}
	private void getPracticeTime(Connection connection, Hero hero){
		try {
			int exp = hero.getExp();
			int currentTime = TimeUtil.currentTime();
			int expOverTime = hero.getExpOverTime();
			int leftTime = expOverTime - currentTime;
			if (leftTime<0 || exp <= 0) {
				leftTime = 0;
			}
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bos);
			output.writeInt(8007);
			output.writeInt(leftTime);
			output.writeInt(exp);
			bos.close();
			output.close();
			connection.write(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	/**
	 * 
	 * @param connection
	 * @param type 类型 1：增加  ， 2：减少
	 * @param modify int[] [id,num]
	 */
	private void bagChanges(Connection connection,int type,List<Integer[]> modify) {
		try {
			if (modify!=null && modify.size()>0) {
				int size = modify.size();
				ByteBuffer toolIds = ByteBuffer.allocate(size*4);
				ByteBuffer nums = ByteBuffer.allocate(size);
				for (int i = 0; i < size; i++) {
					int toolId = modify.get(i)[0];
					toolIds.putInt(toolId);
					int num = modify.get(i)[1];
					nums.put((byte) num);
				}
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(bos);
				output.writeInt(8017);
				output.writeByte(type);
				output.writeShort(size);
				output.write(toolIds.array());
				output.writeShort(size);
				output.write(nums.array());
				bos.close();
				output.close();
				connection.write(bos.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}

class BagSort implements Comparator<Grid> {
	@Override
	public int compare(Grid g1, Grid g2) {
		if (g1 == null && g2 == null) return 0; 
		if (g1 != null && g2 == null) return 1; 
		if (g1 == null && g2 != null) return -1;
		int sortId1 = g1.getSortId();
		int sortId2 = g2.getSortId();
		if (sortId1 == sortId2) {
			return g2.getNum() - g1.getNum();
		}else {
			return g2.getSortId() - g1.getSortId();
		}
	}
}

