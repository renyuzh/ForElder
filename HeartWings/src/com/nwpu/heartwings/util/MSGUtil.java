package com.nwpu.heartwings.util;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


public class MSGUtil {

	public static final String THISCLIENT = "thisclient";

	public static final String TYPE_HELLO = "hello";
	
	public static final String TYPE_BIND = "bind";
	
	public static final String TYPE_SYSTEM = "system";
	
	public static final String TYPE_POSITION = "position";
	
	public static final String TYPE_REQUESTPOSITION = "requestposition";
	
	public static final String TYPE_REQUESTPOSITIONERR = "requestpositionerr";
	
	public static final String TYPE_BINDRETURN = "returnbind";

	public static final String TYPE_SHARE = "image";
	
	public static NioSocketConnector connector = getConnector();
	
	public static IoSession session;
	
  public static final String MINAIP = "114.215.122.96";
//public static final String MINAIP = "10.128.52.177";
	public static final int MINAPORT = 33333;
	
	public static NioSocketConnector getConnector() {

		NioSocketConnector connector = new NioSocketConnector();

		connector.setConnectTimeoutMillis(10 * 1000);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

		connector.setHandler(new MinaClientHandler());

		return connector;
	}
	
	
	public static IoSession getIOSession(String clientPhone){
		     
		   IoSession session = null;
		   
		   ConnectFuture future = connector.connect(new InetSocketAddress(MINAIP, MINAPORT));
		   
		   future.awaitUninterruptibly();
		   
		   session = future.getSession();
		   session.setAttribute(MSGUtil.THISCLIENT, clientPhone);
		   
		   return session;
	}
}
