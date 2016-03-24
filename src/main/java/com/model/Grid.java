package com.model;

public class Grid {
	private int gridId;//格子id
	private int toolId;//道具id
	private int num;//道具数量
	private int overlap;//道具能否叠加 ，1可以叠加，0不能叠加
	private int overlapNum;//道具叠加数量
	private int sortId;//排序id
	private String g;// 格子id 补全3位
	public int getToolId() {
		return toolId;
	}
	public void setToolId(int toolId) {
		this.toolId = toolId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
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
	
	public int getSortId() {
		return sortId;
	}
	
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	
	public Grid() {
		
	}
	
	public Grid(String data) {
//		private int gridId;//格子id
//		private int toolId;//道具id
//		private int num;//道具数量
//		private int overlap;//道具能否叠加 ，1可以叠加，0不能叠加
//		private int overlapNum;//道具叠加数量
//		private int sortId;//排序id
		String[] split = data.split("_");
		int gridId = Integer.parseInt(split[0]);
		int toolId = Integer.parseInt(split[1]);
		int num = Integer.parseInt(split[2]);
		int overlap = Integer.parseInt(split[3]);
		int overlapNum = Integer.parseInt(split[4]);
		int sortId = Integer.parseInt(split[5]);
		
		this.gridId = gridId;
		this.toolId = toolId;
		this.num = num;
		this.overlap = overlap;
		this.overlapNum = overlapNum;
		this.sortId = sortId;
		String s = "";
		if (gridId>=0 && gridId <=9) {
			s = "00"+gridId;
		}else if (gridId>=10 && gridId<=99) {
			s = "0"+gridId;
		}else {
			s = ""+gridId;
		}
		this.g = s;
	}
	
	public void setGridId(int gridId) {
		String s = "";
		if (gridId>=0 && gridId <=9) {
			s = "00"+gridId;
		}else if (gridId>=10 && gridId<=99) {
			s = "0"+gridId;
		}else {
			s = ""+gridId;
		}
		this.g = s;
		this.gridId = gridId;
	}
	public int getGridId() {
		return gridId;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(g).append("_");
		sb.append(toolId).append("_");
		sb.append(num).append("_");
		sb.append(overlap).append("_");
		sb.append(overlapNum).append("_");
		sb.append(sortId);
		return sb.toString();
	}
	public void setG(String g) {
		this.g = g;
	}
	public String getG() {
		return g;
	}
}
