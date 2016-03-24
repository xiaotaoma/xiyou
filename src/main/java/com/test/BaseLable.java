package com.test;

import com.socket.battle.Node;
import com.sysData.map.Loadmap;
import com.sysData.map.Map;
import com.sysData.map.UserLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class BaseLable extends JLabel{
	public static void main(String[] args) {
		Loadmap.loadMap();
		java.util.Map<Integer, Map> sysMap = Loadmap.getSysMap();
		Map map = sysMap.get(5007);
		UserLocation user = new UserLocation();
		user.setId(1);
		user.setHp(new Float(220.0));
		user.setX(200);
		user.setY(200);
		user.setMoveSpeed(20);
//		UserLocation user2 = new UserLocation();
//		user2.setId(2);
//		user2.setHp(new Float(220.0));
//		user2.setX(800);
//		user2.setY(400);
		List<UserLocation> list = new CopyOnWriteArrayList<UserLocation>();
		list.add(user);
//		list.add(user2);
		map.setUsers(list);
		
		JFrame frame = new JFrame();
		frame.setBounds(0, 0, map.getWidth(), map.getHeight());
		frame.setVisible(true);
		BaseLable lable = new BaseLable(0, 0, map.getWidth(), map.getHeight(), map);
		frame.add(lable);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private Map map;
	public BaseLable(int x,int y,int width,int height,Map map) {
		setBounds(x, y, width, height);
		this.setMap(map);
		addMouseListener(new mouseListener());
		timer();
	}
	@Override
	public void paint(Graphics g) {
		//障碍物
		List<Integer[]> obstacle = map.getObstacle();
		for (Integer[] nn : obstacle) {
			BufferedImage image = new BufferedImage(nn[2], nn[3], BufferedImage.TYPE_3BYTE_BGR);
			Graphics graphics = image.getGraphics();
			graphics.setColor(Color.gray);
			graphics.fillRect(0, 0, nn[2], nn[3]);
			g.drawImage(image, nn[0], nn[1], this);
		}
		List<UserLocation> users = map.getUsers();
		try {
			for (UserLocation userLocation : users) {
				int x = userLocation.getX();
				int y = userLocation.getY();
				BufferedImage image = ImageIO.read(new FileInputStream("src/com/test/men.png"));
				Graphics graphics = image.getGraphics();
				graphics.setColor(Color.BLACK);
				graphics.drawString("id:"+userLocation.getId(), 0, 10);
				graphics.drawString("hp:"+userLocation.getHp(), 0, 20);
//				graphics.setColor(Color.gray);
//				graphics.fillRect(0, 0, 50, 50);
				g.drawImage(image, x, y, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void timer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				List<UserLocation> users = getMap().getUsers();
				for (UserLocation userLocation : users) {
					/*float targetX = userLocation.getTargetX();
					float targetY = userLocation.getTargetY();
					if (targetX==0 && targetY==0) {
						continue;
					}
					int x = userLocation.getX();
					int y = userLocation.getY();
					float moveSpeed = userLocation.getMoveSpeed();
					int hypot = (int) Math.hypot((targetX - x), (targetY - y));
					int speendX = (int) ((Math.abs(targetX - x) / hypot) * moveSpeed);
					int speendY = (int) ((Math.abs(targetY - y) / hypot) * moveSpeed);
					if (targetX > x) {
						userLocation.setX(x + speendX);
					} else {
						userLocation.setX(x - speendX);
					}
					if (targetY > y) {
						userLocation.setY(y + speendY);
					} else {
						userLocation.setY(y - speendY);
					}
					if (userLocation.getX()==targetX && userLocation.getY()==targetY) {
						userLocation.setTargetX(0);
						userLocation.setTargetY(0);
					}*/
					List<Node> way = userLocation.getWay();
					if (way!=null && way.size()>0) {
						Node node = way.get(0);
						userLocation.setX(node.getX());
						userLocation.setY(node.getY());
						way.remove(0);
					}
				}
				repaint();
			}
		}, 200, 200);
	}
	
	
	
	public void setMap(Map map) {
		this.map = map;
	}
	public Map getMap() {
		return map;
	}
}

class mouseListener implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			int x = e.getX();
			int y = e.getY();
			BaseLable lable = (BaseLable) e.getComponent();
			List<UserLocation> users = lable.getMap().getUsers();
			UserLocation userLocation = users.get(0);
			userLocation.setTargetX(x);
			userLocation.setTargetY(y);
			System.err.println("X:"+x+"\tY:"+y);
//			FindWay findWay = new FindWay(lable.getMap(), userLocation);
////			findWay.search(userLocation.getX(), userLocation.getY(), x, y);
//			List<Node> search = findWay.search(userLocation.getX(), userLocation.getY(), x, y);
//			userLocation.setWay(search);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
}