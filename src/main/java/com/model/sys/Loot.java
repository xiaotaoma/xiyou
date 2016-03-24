package com.model.sys;

import java.util.List;

public class Loot {
	private List<Integer> times;//随机掉落次数
	private List<Integer> timesRate;//掉落次数几率
	
	private List<String> tools;//掉落物品
	private List<Integer> toolsRate;//掉落几率
	public List<Integer> getTimes() {
		return times;
	}
	public void setTimes(List<Integer> times) {
		this.times = times;
	}
	public List<Integer> getTimesRate() {
		return timesRate;
	}
	public void setTimesRate(List<Integer> timesRate) {
		this.timesRate = timesRate;
	}
	public List<String> getTools() {
		return tools;
	}
	public void setTools(List<String> tools) {
		this.tools = tools;
	}
	public List<Integer> getToolsRate() {
		return toolsRate;
	}
	public void setToolsRate(List<Integer> toolsRate) {
		this.toolsRate = toolsRate;
	}
	
	
}
