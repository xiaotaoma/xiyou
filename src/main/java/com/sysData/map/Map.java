package com.sysData.map;

import java.util.List;

public class Map {
	private int id;
	private int width;//地图宽
	private int height;//地图高
	private List<Integer[]> road;//路
	private List<Integer[]> obstacle;//障碍物
	private List<UserLocation> users;
	private List<Integer[]> startPoint;//出生坐标
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Integer[]> getRoad() {
		return road;
	}
	public void setRoad(List<Integer[]> road) {
		this.road = road;
	}
	public List<Integer[]> getObstacle() {
		return obstacle;
	}
	public void setObstacle(List<Integer[]> obstacle) {
		this.obstacle = obstacle;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}
	public void setUsers(List<UserLocation> users) {
		this.users = users;
	}
	public List<UserLocation> getUsers() {
		return users;
	}
	public void setStartPoint(List<Integer[]> startPoint) {
		this.startPoint = startPoint;
	}
	public List<Integer[]> getStartPoint() {
		return startPoint;
	}
	
}
