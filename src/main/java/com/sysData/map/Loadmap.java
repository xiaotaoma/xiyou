package com.sysData.map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Loadmap {
	
	private static java.util.Map<Integer, Map> sysMap = new ConcurrentHashMap<Integer, Map>();
	public static java.util.Map<Integer, Map> getSysMap() {
		return sysMap;
	}
	public static void loadMap() {
		try {
			String[] ss={
//					"src/com/sysData/map/5000.xml",
					"src/com/sysData/map/5001.xml",
					"src/com/sysData/map/5002.xml",
					"src/com/sysData/map/5003.xml",
					"src/com/sysData/map/5004.xml",
					"src/com/sysData/map/5005.xml",
					"src/com/sysData/map/5006.xml",
					"src/com/sysData/map/5007.xml",
					"src/com/sysData/map/5008.xml",
					"src/com/sysData/map/5009.xml",
					"src/com/sysData/map/5010.xml",
					"src/com/sysData/map/5011.xml",
					"src/com/sysData/map/5012.xml",
					"src/com/sysData/map/5013.xml",
					"src/com/sysData/map/5014.xml",
					"src/com/sysData/map/5015.xml",
					"src/com/sysData/map/5016.xml",
					"src/com/sysData/map/5017.xml",
					"src/com/sysData/map/5018.xml",
					"src/com/sysData/map/5019.xml",
					"src/com/sysData/map/5020.xml",
					"src/com/sysData/map/5021.xml",
					"src/com/sysData/map/5022.xml",
					"src/com/sysData/map/5023.xml"
					};
			for (int i = 0; i < ss.length; i++) {
				SAXReader reader = new SAXReader();
				File f = new File(ss[i]);
				Document doc = reader.read(f);
				Element root = doc.getRootElement();
				Element tileset = root.element("tileset");
				Element image = tileset.element("image");
				Map map = new Map();
				int imageWidth = Integer.parseInt(image.attribute("width").getValue());
				int imageHeight = Integer.parseInt(image.attribute("height").getValue());
				int mapId = Integer.parseInt(tileset.attribute("name").getValue());
				map.setId(mapId);
				map.setHeight(imageHeight);
				map.setWidth(imageWidth);
				Iterator elementIterator = root.elementIterator("objectgroup");
				while (elementIterator.hasNext()) {
					Element element = (Element) elementIterator.next();
					Iterator iterator = element.elementIterator("object");
					while (iterator.hasNext()) {
						Element next = (Element) iterator.next();
						Attribute attribute = next.attribute("name");
						if (attribute != null) {
							String name = attribute.getValue();
							int x = Integer.parseInt(next.attribute("x").getValue());
							int y = Integer.parseInt(next.attribute("y").getValue());
							int width = Integer.parseInt(next.attribute("width").getValue());
							int height = Integer.parseInt(next.attribute("height").getValue());
							if (name.equals("obstacle")) {
								List<Integer[]> list = map.getObstacle();
								if (list == null) {
									list = new ArrayList<Integer[]>();
								}
								list.add(new Integer[] {x, y, width, height});
								map.setObstacle(list);
							} else if (name.equals("bornplace")) {
								List<Integer[]> startPoint = map.getStartPoint();
								if (startPoint==null) {
									startPoint = new ArrayList<Integer[]>();
								}
								startPoint.add(new Integer[]{x,map.getHeight()-y-80});
//								System.out.println("map:"+mapId+" x:"+x+" y:"+(map.getHeight()-y)+" width:"+width+" height:"+height);
								map.setStartPoint(startPoint);
							}
						}
					}
				}
				sysMap.put(mapId, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
