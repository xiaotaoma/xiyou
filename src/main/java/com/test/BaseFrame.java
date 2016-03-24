package com.test;

import com.sysData.map.Loadmap;
import com.sysData.map.Map;
import com.sysData.map.UserLocation;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BaseFrame extends JFrame{
	private Map map;
	private Point point;
	public BaseFrame(Map map) {
		this.setMap(map);
		this.point = new Point();
		addMouseListener(new mouseListener());
		setBounds(0, 0, map.getWidth(), map.getHeight());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
//		timer();
	}
	
	@Override
	public void paint(Graphics g) {
		/*List<Integer[]> obstacle = getMap().getObstacle();
		for (Integer[] integers : obstacle) {
			int x = integers[0];
			int y = integers[1];
			int width = integers[2];
			int height = integers[3];
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics graphics = image.getGraphics();
			graphics.setColor(Color.gray);
			graphics.fillRect(0, 0, width, height);
			g.drawImage(image, x, y, this);
		}
		List<UserLocation> users = getMap().getUsers();
		try {
			if (users!=null) {
				for (UserLocation user : users) {
					int id = user.getId();
					BufferedImage image=ImageIO.read(new FileInputStream("src/com/sysData/map/men.png"));
					Graphics graphics = image.getGraphics();
					graphics.setColor(Color.red);
					graphics.drawString("id:"+id, 10, 10);
					graphics.drawString("hp:"+user.getHp(), 0, 20);
					g.drawImage(image, user.getX(), user.getY(), this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		BufferedImage image = new BufferedImage(getPoint().getWidth(), getPoint().getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.gray);
		graphics.fillRect(0, 0, getPoint().getWidth(), getPoint().getHeight());
		g.drawImage(image, getPoint().getX(), getPoint().getY(), this);
	}
	
	public static void main(String[] args) {
		Loadmap.loadMap();
		java.util.Map<Integer, Map> sysMap = Loadmap.getSysMap();
		Map map = sysMap.get(5000);
		UserLocation user = new UserLocation();
		user.setId(1);
		user.setHp(new Float(220.0));
		user.setX(200);
		user.setY(200);
		UserLocation user2 = new UserLocation();
		user2.setId(2);
		user2.setHp(new Float(220.0));
		user2.setX(800);
		user2.setY(400);
		List<UserLocation> list = new ArrayList<UserLocation>();
		list.add(user);
		list.add(user2);
		map.setUsers(list);
		new BaseFrame(map);
	}
	
	public void timer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
			}
		}, 100, 100);
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Map getMap() {
		return map;
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}

	public Point getPoint() {
		return point;
	}

	class Point{
		private int x = 0;
		private int y = 0;
		private int width = 50;
		private int height = 50;
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		
	}
}


