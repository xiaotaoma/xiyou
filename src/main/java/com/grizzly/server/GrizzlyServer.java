package com.grizzly.server;

import com.cache.GlobalMap;
import com.http.server.GrizzlyHttpServer;
import com.main.BaseAction;
import com.main.LoadConfig;
import com.register.RegisterHandler;
import com.socket.back.BackstageHandler;
import com.socket.handler.RankHandler;
import com.socket.thread.Burn;
import com.socket.thread.CallMonster;
import com.socket.thread.GiveMp;
import com.sysData.map.Loadmap;
import com.util.InitStaticData;
import org.apache.mina.dispatcherHandler.DispatcherAdapter;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.strategies.WorkerThreadIOStrategy;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

public class GrizzlyServer {
	
	private final int POOL_MAX=Runtime.getRuntime().availableProcessors() +(int)(Runtime.getRuntime().availableProcessors() * 0.75f);
	private static Logger logger = LoggerFactory.getLogger(GrizzlyServer.class);
	public static int port=7777;
	
	
	public static FilterChainBuilder filterChainBuilder;
	public static TCPNIOTransport transport;
	public GrizzlyServer() throws IOException, InstantiationException, IllegalAccessException {
		filterChainBuilder = FilterChainBuilder.stateless();
		filterChainBuilder.add(new TransportFilter());
		
		TCPNIOTransportBuilder builder = TCPNIOTransportBuilder.newInstance();
		ThreadPoolConfig config = builder.getWorkerThreadPoolConfig();
		config.setQueueLimit(-1);
		config.setMaxPoolSize(POOL_MAX);
		config.setCorePoolSize(POOL_MAX);
		
		builder.setWorkerThreadPoolConfig(config);
		GrizzlyServerFilter grizzlyServerFilter = new GrizzlyServerFilter();
		
		DispatcherAdapter dispatcher = new DispatcherAdapter();
		RegisterHandler registerHandler = new RegisterHandler();
		registerHandler.setDispatcher(dispatcher);
		registerHandler.setGrizzlyServerFilter(grizzlyServerFilter);
		registerHandler.initialize();
		
		grizzlyServerFilter.setDispatcher(dispatcher);
		
		filterChainBuilder.add(grizzlyServerFilter);
		transport = builder.build();
		transport.setProcessor(filterChainBuilder.build());
		transport.setIOStrategy(WorkerThreadIOStrategy.getInstance());
		transport.bind(port);
		transport.start();
	}
	/**
	 *  jvm参数
	 *  -server 设置JVM使Server模式，特点是启动速度比较慢，但运行时性能和内存管理效率很高
	 *  -Xmx1024m JVM最大堆内存为1024M
	 *  -Xms1024m JVM初始堆内存为1024M 相同以避免JVM反复重新申请内存。-Xmx 的大小约等于系统内存大小的一半，即充分利用系统资源，又给予系统安全运行的空间。
	 *  -Xmn384m 设置年轻代大小为384MB。此值对系统性能影响较大，Sun官方推荐配置年轻代大小为整个堆的3/8。
	 *  -XX:HeapDumpPath=./java_pid.hprof  指定Dump堆内存时的路径。
	 *  -XX:-HeapDumpOnOutOfMemoryError：当首次遭遇内存溢出时Dump出此时的堆内存。
	 *  -Xss1m 线程栈 线程栈参数越大可创建线程数越小
	 *  
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			/**
			 * 加载静态数据
			 */
			LoadConfig.loadConfig();
			InitStaticData.init();
			//初始化spring
			BaseAction.getIntance();
			//排行榜刷新
			RankHandler.getRankHandler().setRank();
			BackstageHandler.getBackstageHandler().serverStart();//加载数据库数据
			
			//加载地图文件
			Loadmap.loadMap();
			new Burn();
			new CallMonster();
			new GiveMp();
			//开启服务器
			new GrizzlyServer();
			//开启接口服务
			new GrizzlyHttpServer();
			
			//关闭钩子
			shutDownHandler();
			logger.info("服务器开启");
			System.out.println("服务器开启");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	public static void shutDownHandler() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("服务器关闭");
				logger.info("服务器关闭");
				allLogout();
				for (int i = 0; i < 3; i++) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("", e);
					}
				}
				
				BackstageHandler.getBackstageHandler().serverClose();
				try {
					transport.stop();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("", e);
				}
			}
		}));
	}
	
	public static void allLogout() {
		Iterator<Entry<Integer, Connection>> iterator = GlobalMap.getConns().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Connection> next = iterator.next();
			Connection connection = next.getValue();
			if (connection!=null && connection.isOpen()) {
				connection.close();
			}
		}
	}
	
}
