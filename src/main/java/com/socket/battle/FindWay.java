package com.socket.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FindWay {
	private static FindWay findWay;
	public static FindWay getFindWay() {
		if (findWay==null) {
			findWay = new FindWay();
		}
		return findWay;
	}
	private FindWay() {
		
	}
	
	private com.sysData.map.Map map;
	private int mapId;
	private Member userLocation;
	private List<Node> openList;//开启列表
	private List<Node> closeList;//关闭列表
	private final int COST_STRAIGHT = 10;//垂直方向或水平方向移动的路径评分
	private final int COST_DIAGONAL = 14;//斜方向移动的路径评分
	private int width;
	private int height;
	private int targetX;
	private int targetY;
	public FindWay(com.sysData.map.Map map,Member member) throws Exception{
		this.userLocation = member;
		this.map = map;
		this.width = map.getWidth();
		this.height = map.getHeight();
		this.openList = new ArrayList<Node>();
		this.closeList = new ArrayList<Node>();
	}
	public List<Integer[]> getNextPoint(int x,int y) {
//		BigDecimal moveSpeed = userLocation.getMoveSpeed();//80像素每秒   0.2s 16像素
		List<Integer[]> list= new ArrayList<Integer[]>();
		int n = (int) (16/Math.sqrt(2));
		//上
//		list.add(new Integer[]{x,y+n,getValue(x, y+n)});//上
//		list.add(new Integer[]{x+n,y+n,getValue(x+n, y+n)});//右上
//		list.add(new Integer[]{x+n,y,getValue(x+n, y)});//右
//		list.add(new Integer[]{x+n,y-n,getValue(x+n, y-n)});//右下
//		list.add(new Integer[]{x,y-n,getValue(x, y-n)});//下
//		list.add(new Integer[]{x-n,y-n,getValue(x-n, y-n)});//左下
//		list.add(new Integer[]{x-n,y,getValue(x-n, y)});//左
//		list.add(new Integer[]{x-n,y+n,getValue(x-n, y+n)});//左上
		list.add(new Integer[]{x,y+n,COST_STRAIGHT});//上
		list.add(new Integer[]{x+n,y+n,COST_DIAGONAL});//右上
		list.add(new Integer[]{x+n,y,COST_STRAIGHT});//右
		list.add(new Integer[]{x+n,y-n,COST_DIAGONAL});//右下
		list.add(new Integer[]{x,y-n,COST_STRAIGHT});//下
		list.add(new Integer[]{x-n,y-n,COST_DIAGONAL});//左下
		list.add(new Integer[]{x-n,y,COST_STRAIGHT});//左
		list.add(new Integer[]{x-n,y+n,COST_DIAGONAL});//左上
		return list;
	}
	public int getValue(int x,int y) {
		return (int) Math.hypot(targetX-x, targetY-y);
	}
	/**
	 * 目标点是否在障碍物中
	 * @param x2
	 * @param y2
	 */
	public int inObstacle(int x2,int y2) {
		if (x2<=0 || x2>=width) {
			return 1;
		}
		if (y2<=0 || y2>=height) {
			return 1;
		}
		List<Integer[]> obstacle = map.getObstacle();
		for (Integer[] integers : obstacle) {
			int obsX = integers[0];
			int obsY = integers[1];
			int obsWidth = integers[2];
			int obsHeight = integers[3];
			if (x2>=obsX && x2<=obsX+obsWidth && y2>=obsY && y2<=obsY+obsHeight) {
				return 1;
			}
		}
		return -1;
	}
	/**
	 * 查找坐标
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return -1错误坐标，0没有找到，1找到
	 */
	public List<Node> search(int x1, int y1, int x2, int y2) {
		//左下角坐标
		System.out.println("起始坐标："+x1+","+y1+"  目标坐标："+x2+","+y2);
		
		if (x1<0||x1>=width||x2<0||x2>=width||y1<0||y1>=height||y2<0||y2>=height) {
			return null;
		}
		this.targetX = x2;
		this.targetY = y2;
		//判断两点是否在障碍物中
		if (inObstacle(x2, y2)==1) {
			return null;
		}
		Node snode = new Node(x1, y1, null);
		Node enode = new Node(x2, y2, null);
		openList.add(snode);
		
		List<Node> resultList=search(snode, enode);
		if(resultList.size()==0){
            return null;
        }
        return resultList;
	}
	
	public List<Node> search(Node sNode, Node eNode) {
		List<Node> resultList = new ArrayList<Node>();
		boolean findFlag =false;
		Node node = null;
		while (openList.size()>0) {
			//取出开启列表中最小F值
			node = openList.get(0);
			//距离终点不到一步的距离,判断是否找到目标点
//			System.out.println("EX:"+eNode.getX()+"\tEY:"+eNode.getY());
//			System.out.println("X:"+node.getX()+"\tY:"+node.getY());
//			int value = getValue(node.getX()-eNode.getX(), node.getY()-eNode.getY());
			if (Math.hypot(node.getX()-eNode.getX(), node.getY()-eNode.getY())<16) {
				findFlag = true;
				break;
			}
			/*//上
			if (node.getX()-1>=0) {
				checkPath(node.getX(), node.getY(), node, eNode, COST_STRAIGHT);
			}
			//下
            if((node.getY()+1)<height){
                checkPath(node.getX(),node.getY()+1,node, eNode, COST_STRAIGHT);
            }
            //左
            if((node.getX()-1)>=0){
                checkPath(node.getX()-1,node.getY(),node, eNode, COST_STRAIGHT);
            }
            //右
            if((node.getX()+1)<width){
                checkPath(node.getX()+1,node.getY(),node, eNode, COST_STRAIGHT);
            }
            //左上
            if((node.getX()-1)>=0&&(node.getY()-1)>=0){
                checkPath(node.getX()-1,node.getY()-1,node, eNode, COST_DIAGONAL);
            }
            //左下
            if((node.getX()-1)>=0&&(node.getY()+1)<height){
                checkPath(node.getX()-1,node.getY()+1,node, eNode, COST_DIAGONAL);
            }
            //右上
            if((node.getX()+1)<width&&(node.getY()-1)>=0){
                checkPath(node.getX()+1,node.getY()-1,node, eNode, COST_DIAGONAL);
            }
            //右下
            if((node.getX()+1)<width&&(node.getY()+1)<height){
                checkPath(node.getX()+1,node.getY()+1,node, eNode, COST_DIAGONAL);
            }*/
            
			List<Integer[]> nextPoint = getNextPoint(node.getX(), node.getY());
			for (Integer[] nn : nextPoint) {
				checkPath(nn[0], nn[1], node, eNode, nn[2]);
			}
            //从开启列表中删除
            //添加到关闭列表中
            closeList.add(openList.remove(0));
            //开启列表中排序，把F值最低的放到最底端
            Collections.sort(openList, new NodeFComparator());
		}
		if (findFlag) {
			getPath(resultList, node);
		}
		return resultList;
	}
	/**
	 * 判断此路能否走通
	 */
	public boolean checkPath(int x, int y, Node parentNode, Node eNode, int cost) {
		Node node = new Node(x, y, parentNode);
		//查找地图中能否通过,判断是否在障碍物种
		if (inObstacle(x, y)==1) {//在障碍物中
			return false;
		}
		//查找关闭列表中是否存在
		if (isListContains(closeList, x, y)!=-1) {
			return false;
		}
		//查找开启列表中是否存在
		int index = -1;
		if ((index=isListContains(openList, x, y))!=-1) {
			//G值是否更小，是否更新G,F值
			if (parentNode.getG()+cost<openList.get(index).getG()) {
				node.setParentNode(parentNode);
				countG(node, eNode, cost);
				countF(node);
				countH(node, eNode);
			}
		}else {
			//添加到开启列表
			node.setParentNode(parentNode);
			count(node, eNode, cost);
			openList.add(node);
		}
		return true;
	}
	private int isListContains(List<Node> list, int x, int y) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Node node = list.get(i);
			if (node.getX()==x && node.getY()==y) {
				return i;
			}
		}
		return -1;
	}
	//从终点往返回到起点
    private void getPath(List<Node> resultList, Node node){
        if(node.getParentNode()!=null){
            getPath(resultList, node.getParentNode());
        }
        resultList.add(node);
    }
	//计算G,H,F值
    private void count(Node node, Node eNode, int cost){
        countG(node, eNode, cost);
        countH(node, eNode);
        countF(eNode);
    }
    //计算G值
    private void countG(Node node, Node eNode, int cost){
        if(node.getParentNode()==null){
            node.setG(cost);
        }else{
            node.setG(node.getParentNode().getG()+cost);
        }
    }
    //计算H值 
    private void countH(Node node, Node eNode){
        node.setF(Math.abs(node.getX()-eNode.getX())+Math.abs(node.getY()-eNode.getY()));
    }
    //计算F值
    private void countF(Node node){
        node.setF(node.getG()+node.getF());
    }
	
}

//节点比较类
class NodeFComparator implements Comparator<Node>{

    public int compare(Node o1, Node o2) {
        return o1.getF()-o2.getF();
    }
    
}