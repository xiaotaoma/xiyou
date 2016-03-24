package com.test;

import com.main.BaseAction;
import com.model.Arena;
import com.service.ArenaManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		
		list.remove(0);
		System.out.println(list);
		list.remove(Integer.valueOf(3));
		System.out.println(list);
	}
	private static void arenaRankReset() {
		ArenaManager arenaManager = (ArenaManager) BaseAction.getIntance().getBean("arenaManager");
		try {
			List<Arena> all = arenaManager.getAll();
			Iterator<Arena> iterator = all.iterator();
			int n = 1;
			while (iterator.hasNext()) {
				Arena arena = iterator.next();
				arena.setRank(n);
				n++;
			}
			arenaManager.updateRank(all);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void print() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print("#");
			}
			System.out.println();
		}
	}
	public void buffer() {
		
	}
	public static void ac() {
		float a = 0;//暴击率
		int ac = 2;
		//稳定递增
		float f = 0.0012f;
//		f = 0.0016f;
//		f = 0.002f;
//		f = 0.0028f;
		for (int i = 0; i < 300; i++) {
			a = (float) (i*f+0.05);
			System.out.println(i+" "+ a);
		}
		
		//27开3次方
//		double pow = Math.pow(27, 1/3);
		
	}
}
