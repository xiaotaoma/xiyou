package com.http.server;

import com.cache.Globalconstants;
import com.http.handler.*;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

import java.io.IOException;

public class GrizzlyHttpServer {
	private HttpServer httpServer;
	public GrizzlyHttpServer() throws IOException {
		httpServer = HttpServer.createSimpleServer();
		ServerConfiguration serverConfiguration = httpServer.getServerConfiguration();
		/**
		 * 后门接口
		 */
		serverConfiguration.addHttpHandler(new BackDoorHandler(), BackDoorHandler.HANDLERNAME);
		
		if (Globalconstants.VERSION.equals(Globalconstants.VERSION_91)||Globalconstants.VERSION.equals(Globalconstants.VERSION_APPLESTORE)) {
			/**
			 * 苹果支付接口
			 */
			serverConfiguration.addHttpHandler(new OrderHandler(), OrderHandler.HANDLERNAME);
			/**
			 * 91 支付接口
			 */
			serverConfiguration.addHttpHandler(new Pay91Handler(), Pay91Handler.HANDLERNAME);
			/**
			 * pp支付接口
			 */
			serverConfiguration.addHttpHandler(new PayppHandler(), PayppHandler.HANDLERNAME);
		}else if (Globalconstants.VERSION.equals(Globalconstants.VERSION_DIANXIN)) {
			/**
			 * 电信支付接口
			 */
			serverConfiguration.addHttpHandler(new PayDianxinHandler(), PayDianxinHandler.HANDLERNAME);
		}else if (Globalconstants.VERSION.equals(Globalconstants.VERSION_TAIWAN)) {
			/**
			 * 台湾支付接口
			 */
			serverConfiguration.addHttpHandler(new PaytaiwanHandler(), PaytaiwanHandler.HANDLERNAME);
		}else if (Globalconstants.VERSION.equals(Globalconstants.VERSION_YIDONG)) {
			/**
			 * 移动支付
			 */
			serverConfiguration.addHttpHandler(new PayYiDongHandler(), PayYiDongHandler.HANDLERNAME);
		}
		
		NetworkListener networkInterface = new NetworkListener("grizzly", NetworkListener.DEFAULT_NETWORK_HOST, Globalconstants.payport);
		ThreadPoolConfig setMaxPoolSize = ThreadPoolConfig.defaultConfig().setCorePoolSize(2).setMaxPoolSize(2);
		networkInterface.getTransport().setWorkerThreadPoolConfig(setMaxPoolSize);
		
		httpServer.addListener(networkInterface);
		httpServer.start();
	}
}
