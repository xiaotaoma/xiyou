package com.socket.handler;

import com.cache.GlobalMap;
import com.model.Bag;
import com.model.Hero;
import com.model.backstage.Back_record;
import com.model.backstage.Back_tool;
import com.model.sys.Loot;
import com.model.sys.MonsterLoot;
import com.model.sys.SysGloablMap;
import com.util.MoneyControl;
import com.util.SysUtil;
import org.glassfish.grizzly.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootHandler {
	private static LootHandler lootHandler;
	public static LootHandler getLootHandler() {
		if (lootHandler == null) {
			lootHandler = new LootHandler();
		}
		return lootHandler;
	}
	private static Logger logger = LoggerFactory.getLogger(LootHandler.class);
	
	
	public void loot(Object arg0,Object arg1) {
		try {
			Connection connection = null;
			byte[] bytes = null;
			if (arg0!=null) {
				connection = (Connection) arg0;
			}
			if (arg1!=null) {
				bytes = (byte[]) arg1;
			}
			if (connection!=null && bytes !=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				DataInputStream input = new DataInputStream(bis);
				input.readInt();
				int mapId = input.readShort();
				int difficulty = input.readByte();
				bis.close();
				input.close();
				
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
				
				if (difficulty<0 || difficulty>3) {
					return;
				}
				
				MonsterLoot monsterLoot = SysGloablMap.getLootMap().get(mapId);
				if (monsterLoot==null) {
					return;
				}
				Loot loot = null;
				switch (difficulty) {
				case 1:
					loot = monsterLoot.getEasy();
					break;
				case 2:
					loot = monsterLoot.getNormal();
					break;
				case 3:
					loot = monsterLoot.getHard();
					break;

				default:
					break;
				}
				
				if (loot==null) {
					return;
				}
				
				List<Integer> timesList = loot.getTimes();
				List<Integer> timesRate = loot.getTimesRate();
				int size = timesRate.size();
				Integer max = timesRate.get(size-1);
				Random random = new Random();
				//[0,max)
				int nextInt = random.nextInt(max);
				int n = 0;
				for (int i = 0; i < size-1; i++) {
					int a = timesRate.get(i);
					int b = timesRate.get(i+1);
					if (nextInt>=a && nextInt<b) {
						n = i;
						break;
					}
				}
				int times = timesList.get(n);
				
				List<String> tools = new ArrayList<String>(loot.getTools());
				List<Integer> toolsRate = new ArrayList<Integer>(loot.getToolsRate());
				List<String> goods = new ArrayList<String>();
				for (int i = 0; i < times; i++) {
					String s = getTools(tools, toolsRate, random);
					goods.add(s);
				}
				
				for (String s : goods) {
					List<Integer> list = SysUtil.splitGetInt(s, "_");
					int type = list.get(0);//元宝1，铜币2，道具3，经验4
					int toolId = list.get(1);//道具id
					int num = list.get(2);//数量
					switch (type) {
					case 1://元宝1
						MoneyControl.moneyIncome(hero, Back_record.REASON_LOOT, num);
						connection.write(SysUtil.getBytes(1017, hero.getMoney()));
						break;
					case 2://铜币2
						HeroHandler.getHeroHandler().giveCopper(num, connection);
						break;
					case 3://道具3
						BagHandler.getBagHandler().addTools(bag, toolId, num, Back_tool.REASON_LOOT);
						break;
					case 4://经验4
						HeroHandler.getHeroHandler().addExp(num, connection);
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public String getTools(List<String> tools,List<Integer> toolsRate,Random random) {
		int size = toolsRate.size();
		int max = toolsRate.get(size-1);
		int nextInt = random.nextInt(max);
		int n = 0;
		for (int i = 0; i < size-1; i++) {
			int a = toolsRate.get(i);
			int b = toolsRate.get(i+1);
			if (nextInt>=a && nextInt<b) {
				n = i;
				break;
			}
		}
		
		String s = tools.get(n);
		tools.remove(n);
		
		toolsRate.clear();
		toolsRate.add(0);
		for (String ss : tools) {
			List<Integer> list = SysUtil.splitGetInt(ss, "_");
			toolsRate.add(toolsRate.get(toolsRate.size()-1)+list.get(3));
		}
		return s;
	}
}
