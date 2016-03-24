package com.socket.back;

import com.cache.GlobalMap;
import com.main.BaseAction;
import com.model.backstage.Back_tool;
import com.service.Back_toolManager;
import com.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ToolStatistics {
	private static ToolStatistics toolStatistics ;
	
	public static ToolStatistics getToolStatistics() {
		if (toolStatistics==null) {
			toolStatistics = new ToolStatistics();
		}
		return toolStatistics;
	}
	
	private static Logger logger = LoggerFactory.getLogger(ToolStatistics.class);
	/**
	 * 
	 * @param hid
	 * @param hasNum 使用前数量
	 * @param leftNum 使用后数量
	 * @param num 使用数量
	 * @param reason 使用
	 * @param type 增加或时间
	 * @param toolId 道具id
	 */
	public void statistics(int hid,int hasNum,int leftNum,int num,int reason,int type,int toolId) {
		try {
			Back_tool tool = new Back_tool();
			tool.setHid(hid);
			tool.setTime(TimeUtil.currentTime());
			tool.setType(type);
			tool.setToolId(toolId);
			tool.setHasNum(hasNum);
			tool.setLeftNum(leftNum);
			tool.setNum(num);
			tool.setReason(reason);
			
			List<Back_tool> list = GlobalMap.getBackToolMap().get(hid);
			if (list==null) {
				list = new CopyOnWriteArrayList<Back_tool>();
			}
			list.add(tool);
			GlobalMap.getBackToolMap().put(hid, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public void sync(int hid) {
		if (GlobalMap.getBackToolMap().containsKey(hid)) {
			try {
				List<Back_tool> list = GlobalMap.getBackToolMap().get(hid);
				Iterator<Back_tool> iterator = list.iterator();
				Back_toolManager toolManager = (Back_toolManager) BaseAction.getIntance().getBean("toolManager");
				while (iterator.hasNext()) {
					Back_tool tool = iterator.next();
					toolManager.insert(tool);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("", e);
			}
			GlobalMap.getBackToolMap().remove(hid);
		}
	}
	
}
