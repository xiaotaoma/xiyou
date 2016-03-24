package com.model.sys;

public class Tool {
	private int toolId;
	private String name;
	private int sortId;
	private String desc;
	private int effectType;
	private String effect;
	private int overlap;
	private int overlapNum;
	private String useQuery;//
	public int getToolId() {
		return toolId;
	}
	public void setToolId(int toolId) {
		this.toolId = toolId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getEffectType() {
		return effectType;
	}
	public void setEffectType(int effectType) {
		this.effectType = effectType;
	}
	public String getEffect() {
		return effect;
	}
	public void setEffect(String effect) {
		this.effect = effect;
	}
	/**
	 * 道具能否叠加 ，1可以叠加，0不能叠加
	 * @return
	 */
	public int getOverlap() {
		return overlap;
	}
	public void setOverlap(int overlap) {
		this.overlap = overlap;
	}
	public int getOverlapNum() {
		return overlapNum;
	}
	public void setOverlapNum(int overlapNum) {
		this.overlapNum = overlapNum;
	}
	public void setUseQuery(String useQuery) {
		this.useQuery = useQuery;
	}
	public String getUseQuery() {
		return useQuery;
	}
	
}
