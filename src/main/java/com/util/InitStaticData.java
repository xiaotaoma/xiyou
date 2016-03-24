package com.util;

import com.main.BaseAction;
import com.model.Arena;
import com.model.sys.*;
import com.service.ArenaManager;
import com.socket.handler.AccountHandler;
import com.socket.handler.ArenaHandler;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitStaticData {
	public static void main(String[] args) {
		readLoot();
	}
	public static void init() {
		monsterSplit();
		monsters();
		readMoney();
		readSysSkill();
		readSysCall();
		readAppProduct();
		loadProduct91();
		AccountHandler.getAccountHandler().initAnnouncements();
		readTools();
		readShop();
		loadProductDianxin();
		readTask();
		readLoot();
		readCopperShop();
		readProductYiDong();
	}
	public static void changeScore() {
		ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
		try {
			List<Arena> all = arenaManager.getAll();
			Pattern pattern = null;
			Matcher matcher = null;
			Map<Integer, ArenaMonster> monsterMap = SysGloablMap.getMonsterMap();
			for (Arena arena : all) {
				String achievement = arena.getAchievement();
				String s = "\\[\\d*,\\d*]";
				StringBuffer sb = new StringBuffer();
				if (achievement!=null  && !"".equals(achievement)) {
					pattern =Pattern.compile(s);
					matcher = pattern.matcher(achievement);
					while (matcher.find()) {
						String group = matcher.group();
						String[] split = group.substring(1, group.length()-1).split(",");
						int mapId = Integer.parseInt(split[0]);
						int wave = Integer.parseInt(split[1]);
						ArenaMonster arenaMonster = monsterMap.get(wave);
						if (arenaMonster!=null) {
							sb.append("[").append(mapId).append(",").append((int)arenaMonster.getScore()).append("]");
						}else {
							System.out.println("wave:"+wave);
						}
					}
					System.out.println("achievement:"+achievement+"\tsb:"+sb);
				}
				arena.setAchievement(sb.toString());
				arenaManager.update(arena);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static Logger logger = LoggerFactory.getLogger(InitStaticData.class);
	
	public static void initMonsters() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/monster.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				Monster monster = new Monster();
				int id = Integer.parseInt(element.elementText("id"));
				monster.setId(id);
				String name = element.elementText("name");
				monster.setName(name);
				String hard = element.elementText("hard");
				monster.setHard(hard);
				int wave = Integer.parseInt(element.elementText("wave"));
				monster.setWave(wave);
				
//				<monsters>111csdasd</monsters>
				String monsterString = element.elementText("monsters");
				List<String> monsters = SysUtil.split(monsterString, ",");
				monster.setMonsters(monsters);
				int waveNum = Integer.parseInt(element.elementText("waveNum"));
				monster.setWaveNum(waveNum);
				int addWaveNum = Integer.parseInt(element.elementText("addWaveNum"));
				monster.setAddWaveNum(addWaveNum);
//				<mixWaves>11,11</mixWaves>
				String mixWavesString = element.elementText("mixWaves");
				List<Integer> mixWaves = new ArrayList<Integer>();
				String[] split = mixWavesString.split(",");
				for (String string : split) {
					Pattern pattern = Pattern.compile("~");
					Matcher matcher = pattern.matcher(string);
					if (matcher.find()) {
						String[] split2 = string.split("~");
						for (int i = Integer.parseInt(split2[0].trim()); i < Integer.parseInt(split2[1].trim()); i++) {
							mixWaves.add(i);
						}
					}else {
						mixWaves.add(Integer.parseInt(string.trim()));
					}
				}
				monster.setMixWaves(mixWaves);
//				<parallelNum>111,22</parallelNum>
				String paralleNumString = element.elementText("parallelNum");
				List<Integer> parallelNum = new ArrayList<Integer>();
				String[] split1 = paralleNumString.split(",");
				for (String string : split1) {
					Pattern pattern = Pattern.compile("~");
					Matcher matcher = pattern.matcher(string);
					if (matcher.find()) {
						String[] split2 = string.split("~");
						for (int i = Integer.parseInt(split2[0].trim()); i <= Integer.parseInt(split2[1].trim()); i++) {
							parallelNum.add(i);
						}
					}else {
						parallelNum.add(Integer.parseInt(string.trim()));
					}
				}
				monster.setParallelNum(parallelNum);
				
				float conffie = Float.parseFloat(element.elementText("conffie"));
				monster.setConffie(conffie);
				float addConffie = Float.parseFloat(element.elementText("addConffie"));
				monster.setAddConffie(addConffie);
				int hide = Integer.parseInt(element.elementText("hide"));
				monster.setHide(hide);
				int relife = Integer.parseInt(element.elementText("relife"));
				monster.setRelife(relife);
				String content = element.elementText("content");
				if (content==null) {
					setContent(monster);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public static void setContent(Monster model) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		sb.append("0~");
		List<String> monsters = model.getMonsters();
		int monstersSize = monsters.size();
		int wave = model.getWave();
		int addWaveNum = model.getAddWaveNum();
		List<Integer> mixWaves = model.getMixWaves();
		float conffie = model.getConffie();
		float addConffie = model.getAddConffie();
		int waveNum = model.getWaveNum();
		int hide = model.getHide();
		int relife = model.getRelife();
		List<Integer> parallelNum = model.getParallelNum();
		float currentConffie = conffie;
		BigDecimal b1 = new BigDecimal(currentConffie);
		b1=b1.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal b2 = new BigDecimal(addConffie);
		b2=b2.setScale(2, BigDecimal.ROUND_HALF_UP);
		for (int i = 1; i <= wave; i++) {
			int a=(i-1)/10;
			int n =a*addWaveNum + waveNum;//每波出怪总数量
			int isHide=0;
			int isRelife = 0;
			if (mixWaves.contains(i)) {//混合波
				StringBuffer buffer = new StringBuffer();
				Integer times = parallelNum.get(random.nextInt(parallelNum.size()));//随机次数
				List<Integer> splitNum = new ArrayList<Integer>();
				if (times==1) {
					splitNum.add(n);
				}else {
					splitNum = splitNum(n, times);
				}
				int size = splitNum.size();
				for (int j = 0; j < size; j++) {
					if (hide==2) {
						isHide = random.nextInt(hide);
					}
					if (relife == 2) {
						isRelife = random.nextInt(relife);
					}
					b1=b1.add(b2);
					buffer.append(monsters.get(random.nextInt(monstersSize))).append(",").append(splitNum.get(j))
					.append(",").append(b1.floatValue()).append(",").append(isHide).append(",").append(isRelife).append("|");
				}
				String substring = buffer.substring(0, buffer.length()-1);
				sb.append(substring);
			}else {//普通波
				if (hide==2) {
					isHide = random.nextInt(hide);
				}
				if (relife == 2) {
					isRelife = random.nextInt(relife);
				}
				b1=b1.add(b2);
				sb.append(monsters.get(random.nextInt(monstersSize))).append(",").
				append(n).append(",").append(b1.floatValue()).append(",").append(isHide).append(",").append(isRelife);
			}
			sb.append("\n");
		}
		sb = sb.delete(sb.length()-1, sb.length());
		sb.append("~");
		System.out.println(sb);
		System.out.println("----------------------");
		model.setContent(sb.toString());
	}
	public static int[] ii ={2,3,5,7,8,10,15,20,30,40};
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
	/**
	 * 
	 * @param list
	 */
	public static int sum(List<Integer> list) {
		int n = 0;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			n+=list.get(i);
		}
		return n;
	}
	
	
	
	public static String delim(String s) {
//		10019,15,19.7,0,0|10030,15,19.8,1,1|1~10031,1,25,1,0~
//		10033,7,20.6,0,0|10004,10,20.7,1,1|10004,3,20.8,1,1|10005,10,20.9,1,1~
		String[] split = s.split("\\|");
		StringBuffer sb = new StringBuffer();
		for (String string : split) {
			int n = string.indexOf("~");
			if (n==-1) {//没有
				sb.append(string).append("|");
			}else if (n==1) {//1~10031,1,25,1,0
				String[] split2 = string.split("~");
				sb.append(split2[1]).append("|");
			}else if (n==string.length()-1) {//10005,10,20.9,1,1~
				String[] split2 = string.split("~");
				sb.append(split2[0]).append("|");
			}
		}
		return sb.substring(0, sb.length()-1);
	}
	
	public static void monsters() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/monster.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int wave = Integer.parseInt(element.elementText("wave"));
				List<Integer> monsters = SysUtil.splitGetInt(element.elementText("monsters"), ",");
				List<Integer> boss = SysUtil.splitGetInt(element.elementText("boss"), ",");
				int monseterNum = Integer.parseInt(element.elementText("monseterNum"));
				String s = element.elementText("list");
				List<Integer> list = new ArrayList<Integer>();
				String[] split = s.split("~");
				if (split.length==2) {
					int n = Integer.parseInt(split[0]);
					int m = Integer.parseInt(split[1]);
					for (int i = n; i <= m; i++) {
						list.add(i);
					}
				}else {
					list.add(Integer.parseInt(s));
				}
				int hide = Integer.parseInt(element.elementText("hide"));
				int relife = Integer.parseInt(element.elementText("relife"));
				
				float coefficient = Float.parseFloat(element.elementText("coefficient"));
				int bossCoeffic = Integer.parseInt(element.elementText("bossCoeffic"));
				ArenaMonster monster = new ArenaMonster();
				monster.setWave(wave);
				monster.setBoss(boss);
				monster.setHide(hide);
				monster.setList(list);
				monster.setMonsterNum(monseterNum);
				monster.setMonsters(monsters);
				monster.setRelife(relife);
				monster.setCoefficient(coefficient);
				monster.setBossCoeffi(bossCoeffic);
				SysGloablMap.getMonsterMap().put(wave, monster);
				if (wave==100) {
					ArenaHandler.boss.addAll(boss);
					ArenaHandler.monsters.addAll(monsters);
					ArenaHandler.list.addAll(list);
					ArenaHandler.maxMonsterConfi = (int) coefficient;
				}
			}
			
			Object[] array = SysGloablMap.getMonsterMap().values().toArray();
			List<ArenaMonster> list = new ArrayList<ArenaMonster>();
			for (int i = 0; i < array.length; i++) {
				ArenaMonster monster = (ArenaMonster) array[i];
				list.add(monster);
			}
			
			Collections.sort(list, new SortArenaMonster());
			int size = list.size();
			int copper = 0;
			int score = 0;
			for (int i = 0; i < size; i++) {
				ArenaMonster monster = list.get(i);
				String s = getMonster(monster);
				SysGloablMap.getWaveMap().put(monster.getWave(), s);
				score+=monster.getCoefficient();
				monster.setScore(score);
				copper += (int) (monster.getCoefficient()*5);
				monster.setCopper(copper);
				
				logger.info("wave : "+monster.getWave()+"  score:"+score+ " copper:"+monster.getCopper());
			}
			logger.info("monster,boss:"+ArenaHandler.boss);
			logger.info("monster,monsters:"+ArenaHandler.monsters);
			logger.info("monster,list:"+ArenaHandler.list);
			logger.info("monster,maxMonsterConfi:"+ArenaHandler.maxMonsterConfi);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	/**
	 * 生成怪物
	 */
	public static String getMonster(ArenaMonster monster) {
		if (monster!=null) {
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			List<Integer> list = monster.getList();
			List<Integer> monsters = monster.getMonsters();
			int monsterNum = monster.getMonsterNum();
			Integer n = list.get(random.nextInt(list.size()));
			int hide = monster.getHide();
			int relife = monster.getRelife();
			List<Integer> boss = monster.getBoss();
			int bossNum = boss.size();
			int bossCoeffi = monster.getBossCoeffi();
			float coefficient = monster.getCoefficient();
			BigDecimal b1 = new BigDecimal(coefficient);
			b1 = b1.setScale(2, BigDecimal.ROUND_HALF_UP);
			if (hide==2) {
				hide = random.nextInt(hide);
			}
			if (relife == 2) {
				relife = random.nextInt(relife);
			}
			if (n==1) {//10003,15,9.1,0,0
				sb.append(monsters.get(random.nextInt(monsters.size()))).append(",").append(monsterNum).append(",").append(b1.floatValue())
				.append(",").append(hide).append(",").append(relife).append("|");
			}else if(n>1){
				Map<Integer, List<List<Integer>>> map = SysGloablMap.getMap().get(monsterNum);
				List<List<Integer>> list2 = map.get(n);
				int nextInt = random.nextInt(list2.size());
				if (nextInt<0) {
					nextInt = 0;
				}
				List<Integer> splitNum = list2.get(nextInt);
				for (int i = 0; i < n; i++) {
					sb.append(monsters.get(random.nextInt(monsters.size()))).append(",").append(splitNum.get(i))
					.append(",").append(b1.floatValue()).append(",").append(hide).append(",").append(relife).append("|");
				}
			}
			if (bossCoeffi!=0 && bossNum>0) {
				Integer bossId = boss.get(random.nextInt(bossNum));
				sb.append(bossId).append(",").append(1).append(",").append(bossCoeffi)
				.append(",").append(0).append(",").append(0);
			}
			String s = sb.toString();
			if (s.endsWith("|")) {
				s = s.substring(0, s.length()-1);
			}
			return s;
		}else {
			return null;
		}
	}
	/**
	 * 读取怪物分配表
	 */
	public static void monsterSplit() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/monsterSplit.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int wave = Integer.parseInt(element.elementText("totalNum"));
				int num = Integer.parseInt(element.elementText("n"));
				String s = element.elementText("splits");
				String[] split = s.split("\\|");
				int len = split.length;
				List<List<Integer>> value = new CopyOnWriteArrayList<List<Integer>>();
				for (int i = 0; i < len; i++) {
					String[] split2 = split[i].split(",");
					List<Integer> list = new CopyOnWriteArrayList<Integer>();
					if (split2.length!=num) {
						continue;
					}else {
						int length = split2.length;
						for (int j = 0; j < length; j++) {
							list.add(Integer.parseInt(split2[j].trim()));
						}
					}
					value.add(list);
				}
				if (SysGloablMap.getMap().containsKey(wave)) {
					Map<Integer, List<List<Integer>>> map = SysGloablMap.getMap().get(wave);
					map.put(num, value);
				}else {
					Map<Integer, List<List<Integer>>> map = new ConcurrentHashMap<Integer, List<List<Integer>>>();
					map.put(num, value);
					SysGloablMap.getMap().put(wave, map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	public static void readMoney() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/money.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				Money money = new Money();
				int type =Integer.parseInt(element.elementText("type"));
				int id = Integer.parseInt(element.elementText("id"));
				int level = Integer.parseInt(element.elementText("level"));
				int m = Integer.parseInt(element.elementText("money"));
				money.setId(id);
				money.setType(type);
				money.setLevel(level);
				money.setMoney(m);
				if (SysGloablMap.getMoneyMap().containsKey(type)) {
					Map<Integer, Money> map = SysGloablMap.getMoneyMap().get(type);
					map.put(id, money);
				}else {
					Map<Integer, Money> map = new ConcurrentHashMap<Integer, Money>();
					map.put(id, money);
					SysGloablMap.getMoneyMap().put(type, map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	
	public static void readSysSkill() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/sysSkill.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				SysSkill skill = new SysSkill();
				int parseInt = Integer.parseInt(element.elementText("id"));
				skill.setId(parseInt);
				String attSpeedString = element.elementText("attSpeed");
				if (attSpeedString!=null && !attSpeedString.equals("")) {
					skill.setAttSpeed(Float.parseFloat(attSpeedString));
				}
				skill.setName(element.elementText("name"));
				String demageString = element.elementText("demage");
				if (demageString!=null && !demageString.equals("")) {
					skill.setDemage(Integer.parseInt(demageString));
				}
				String effect = element.elementText("effect");
				if (effect!=null && !effect.equals("")) {
					skill.setEffect(effect);
				}
				String effectNums = element.elementText("effectNums");
				if (effectNums!=null && !effectNums.equals("")) {
					skill.setEffectsNums(Integer.parseInt(effectNums));
				}
				String lastTime = element.elementText("lastTime");
				if (lastTime!=null && !lastTime.equals("")) {
					skill.setLastTime(Integer.parseInt(lastTime));
				}
				String level = element.elementText("level");
				if (level!=null &&!level.equals("")) {
					skill.setLevel(Integer.parseInt(level));
				}
				String range = element.elementText("range");
				if (range!=null && !"".equals(range)) {
					skill.setRange(Float.parseFloat(range));
				}
				String special = element.elementText("special");
				if (special!=null && !special.equals("")) {
					skill.setSpecial(Integer.parseInt(special));
				}
				String zhaohuan = element.elementText("zhaohuan");
				if (zhaohuan!=null &&!zhaohuan.equals("")) {
					skill.setZhaohuan(zhaohuan);
				}
				String mp = element.elementText("mp");
				if (mp!=null && !mp.equals("")) {
					skill.setCostMP(Integer.parseInt(mp));
				}
				String cd = element.elementText("cd");
				if (cd!=null && !cd.equals("")) {
					skill.setCd(Float.parseFloat(cd));
				}
				SysGloablMap.getSkillMap().put(skill.getId(), skill);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	public static void readSysCall() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/sysCall.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				SysCall call = new SysCall();
				call.setSysId(Integer.parseInt(element.elementText("sysId")));
				call.setName(element.elementText("name"));
				call.setDemage(Float.parseFloat(element.elementText("demage")));
				call.setAttSpeed(Float.parseFloat(element.elementText("attSpeed")));
				call.setMoveSpeed(Float.parseFloat(element.elementText("moveSpeed")));
				call.setRange(Float.parseFloat(element.elementText("range")));
				call.setAttType(Integer.parseInt(element.elementText("attType")));
				call.setPenetrate(Integer.parseInt(element.elementText("penetrate")));
				call.setLastTime(Integer.parseInt(element.elementText("lastTime")));
				String elementText = element.elementText("skillId");
				if (elementText!=null && !"".equals(elementText)) {
					call.setSkillId(Integer.parseInt(elementText));
				}
				SysGloablMap.getCallMap().put(call.getSysId(), call);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	public static void readSysEquip() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/equipment.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int id = Integer.parseInt(element.elementText("id"));
				int type = Integer.parseInt(element.elementText("type"));
				String typeName = element.elementText("typeName");
				String name = element.elementText("name");
				String role = element.elementText("role");
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	public static void readAppProduct() {
		try {
			FileInputStream input = new FileInputStream("src/com/model/sys/app_product.plist");
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader reader = factory.createXMLEventReader(input);
			
			assert reader.nextEvent().isStartDocument();
			while (reader.hasNext()) {
				XMLEvent event = reader.nextTag();
				Product product = null;
				if (event.isStartElement()) {
					String name = event.asStartElement().getName().getLocalPart();
					if (name.equals("dict")) {
						List<String> list = new ArrayList<String>();
						boolean dict = false;
						while ((event = reader.nextTag()).isStartElement()) {
							final String name2 = event.asStartElement().getName().getLocalPart();
							String s = null;
							if (name2.equals("key")) {
								s = reader.getElementText();
//								System.out.println("key: " + s);
							} else if (name2.equals("integer")) {
								s = reader.getElementText();
//								System.out.println("integer: " + s);
								
							} else if (name2.equals("string")) {
								s = reader.getElementText();
//								System.out.println("string: " + s);
							}
							if (s!=null && s.equals("id")) {
								dict = true;
								product = new Product();
							}
							if (dict) {
								if (s != null) {
									list.add(s);
								}
								if (list.size() == 2) {
									String key = list.get(0);
									String value = list.get(1);
									Method method = SysUtil.getSetMethod(Product.class, key);
									method.invoke(product, SysUtil.getValue(value));
									list.clear();
								}
							}
						}
					}
				}
				if (product!=null) {
					String name = product.getName();
					Pattern pattern = Pattern.compile("\\d*");
					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {
						String group = matcher.group();
						int num = Integer.parseInt(group);
						name = name.replace(group, "");
						product.setNum(num);
						if (name.equals("元宝")) {
							product.setMoneyType(1);
						}else if (name.equals("铜币")) {
							product.setMoneyType(2);
						}
					}
					SysGloablMap.getProductMap().put(product.getProduct_id(), product);
					logger.info(product.toString());
				}
				reader.next();
			}
			assert reader.nextEvent().isEndDocument();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	private static void loadProduct91() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/product91.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int id = Integer.parseInt(element.elementText("id"));
				String good = element.elementText("good");
				String price = element.elementText("price");
				String pid91 = element.elementText("pid91");
				String productName = element.elementText("productName");
				int moneyType = Integer.parseInt(element.elementText("moneyType"));
				int num = Integer.parseInt(element.elementText("num"));
				Product91 product91 = new Product91();
				product91.setId(id);
				product91.setGood(good);
				product91.setMoneyType(moneyType);
				product91.setNum(num);
				product91.setPid91(pid91);
				product91.setPrice((int) Float.parseFloat(price));
				product91.setProductName(productName);
				SysGloablMap.getProduct91Map().put(id, product91);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}
	}
	public static void readTools() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/tool.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int toolId = Integer.parseInt(element.elementText("toolId"));
				String name = element.elementText("name");
				int sortId = Integer.parseInt(element.elementText("sortId"));
				
				int effectType = Integer.parseInt(element.elementText("effectType"));
				String effect = element.elementText("effect");
				int overlap = Integer.parseInt(element.elementText("overlap"));
				int overlapNum = Integer.parseInt(element.elementText("overlapNum"));
				String useQuery = element.elementText("useQuery");
				Tool tool = new Tool();
				tool.setEffect(effect);
				tool.setEffectType(effectType);
				tool.setName(name);
				tool.setOverlap(overlap);
				tool.setOverlapNum(overlapNum);
				tool.setSortId(sortId);
				tool.setToolId(toolId);
				tool.setUseQuery(useQuery);
				SysGloablMap.getToolMap().put(toolId, tool);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public static void readShop() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/shop.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int id = Integer.parseInt(element.elementText("id"));
				int toolId = Integer.parseInt(element.elementText("toolId"));
				int moneyType = Integer.parseInt(element.elementText("moneyType"));
				int price = Integer.parseInt(element.elementText("price"));
				Shop shop = new Shop();
				shop.setId(id);
				shop.setToolId(toolId);
				shop.setMoneyType(moneyType);
				shop.setPrice(price);
				SysGloablMap.getShopMap().put(shop.getId(), shop);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	private static void loadProductDianxin() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/productDianxin.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int id = Integer.parseInt(element.elementText("id"));
				String good = element.elementText("good");
//				int price = Integer.parseInt(element.elementText("price"));
				int price = (int) Float.parseFloat(element.elementText("price"));
				String productName = element.elementText("productName");
				int moneyType = Integer.parseInt(element.elementText("moneyType"));
				int num = Integer.parseInt(element.elementText("num"));
				ProductDianxin product = new ProductDianxin();
				product.setId(id);
				product.setGood(good);
				product.setMoneyType(moneyType);
				product.setNum(num);
				product.setPrice(price);
				product.setProductName(productName);
				SysGloablMap.getProductDianxinMap().put(id, product);
				logger.info("电信支付商品："+product.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	private static void readTask() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/task.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int id = Integer.parseInt(element.elementText("id"));
				String name = element.elementText("name");
				int loop = Integer.parseInt(element.elementText("loop"));
				String desc = element.elementText("desc");
				String awardDesc = element.elementText("awardDesc");
				String award = element.elementText("award");
				
				Task task = new Task();
				task.setId(id);
				task.setName(name);
				task.setLoop(loop);
				task.setDesc(desc);
				task.setAward(award);
				task.setAwardDesc(awardDesc);
				SysGloablMap.getTaskMap().put(id, task);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	private static void readCopperShop() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/copperShop.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int id = Integer.parseInt(element.elementText("id"));
				int moneyType = Integer.parseInt(element.elementText("moneyType"));
				int price = Integer.parseInt(element.elementText("price"));
				String name = element.elementText("name");
				String context = element.elementText("context");
				int copper = Integer.parseInt(element.elementText("copper"));
				CopperShop shop  = new CopperShop();
				shop.setContext(context);
				shop.setCopper(copper);
				shop.setId(id);
				shop.setMoneyType(moneyType);
				shop.setName(name);
				shop.setPrice(price);
				
				SysGloablMap.getCopperShopMap().put(shop.getId(), shop);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	private static void readLoot() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/monsterLoot.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int id = Integer.parseInt(element.elementText("id"));
				String easyLoot = element.elementText("easyLoot");
				String normalLoot = element.elementText("normalLoot");
				String hardLoot = element.elementText("hardLoot");
				
				MonsterLoot monsterLoot = new MonsterLoot();
				monsterLoot.setMapId(id);
				if (easyLoot!=null && !easyLoot.equals("")) {
					Loot loot = getLoot(easyLoot);
					monsterLoot.setEasy(loot);
				}
				if (normalLoot!=null && !normalLoot.equals("")) {
					Loot loot = getLoot(normalLoot);
					monsterLoot.setNormal(loot);
				}
				if (hardLoot!=null && !hardLoot.equals("")) {
					Loot loot = getLoot(hardLoot);
					monsterLoot.setHard(loot);
				}
				SysGloablMap.getLootMap().put(id, monsterLoot);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	public static Loot getLoot(String s) {
		String[] split = s.split("-");
		String[] timesStrings = split[0].split(",");
		String[] lootStrings = split[1].split(",");
		Loot loot = new Loot();
		List<Integer> timesIntegers = new ArrayList<Integer>();
		List<Integer> timesRate = new ArrayList<Integer>();
		timesRate.add(0);
		for (String ss : timesStrings) {
			List<Integer> list = SysUtil.splitGetInt(ss, "_");
			int times = list.get(0);
			timesIntegers.add(times);
			timesRate.add(timesRate.get(timesRate.size()-1)+list.get(1));
		}
		
		List<String> tools = new ArrayList<String>();
		List<Integer> toolsRate = new ArrayList<Integer>();
		toolsRate.add(0);
		for (String ss : lootStrings) {
			List<Integer> list = SysUtil.splitGetInt(ss, "_");
			tools.add(ss);
			toolsRate.add(toolsRate.get(toolsRate.size()-1)+list.get(3));
		}
		loot.setTimes(timesIntegers);
		loot.setTimesRate(timesRate);
		loot.setTools(tools);
		loot.setToolsRate(toolsRate);
		return loot;
	}
	private static void readProductYiDong() {
		try {
			SAXReader reader = new SAXReader();
			File f = new File("src/com/model/sys/productYiDong.xml");
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			Iterator iterator = root.elementIterator("row");
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				int id = Integer.parseInt(element.elementText("id"));
//				<id>1</id>
//				<payCode>006010913001</payCode>
//				<name>2元宝</name>
//				<price>10</price>
//				<dec>获得2个可以购买游戏中道具与游戏消耗的元宝</dec>
//				<path>道具商城-充值</path>
//				<trigger>购买页面</trigger>
//				<content>新建</content>
//				<moneyType>1</moneyType>
//				<moneyValue>2</moneyValue>
				
				ProductYiDong yiDong = new ProductYiDong();
				yiDong.setDec(element.elementText("dec"));
				yiDong.setId(id);
				yiDong.setMoneyNum(Integer.parseInt(element.elementText("moneyValue")));
				yiDong.setMoneyType(Integer.parseInt(element.elementText("moneyType")));
				yiDong.setName(element.elementText("name"));
				yiDong.setPayCode(element.elementText("payCode"));
				yiDong.setPrice(element.elementText("price"));
				SysGloablMap.getYidongProductMap().put(yiDong.getId(), yiDong);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}

class Sort implements Comparator<Object> {
	@Override
	public int compare(Object arg0, Object arg1) {
		if (arg0 instanceof Object[] && arg1 instanceof Object[]) {
			Object[] o1 = (Object[]) arg0;
			Object[] o2 = (Object[]) arg1;
			int a1 = (Integer) o1[0];
			int a2 = (Integer) o2[0];
			return a1 - a2;
		}
		return 0;
	}
}

class SortArenaMonster implements Comparator<ArenaMonster> {
	@Override
	public int compare(ArenaMonster m1, ArenaMonster m2) {
		return m1.getWave()-m2.getWave();
	}
}
