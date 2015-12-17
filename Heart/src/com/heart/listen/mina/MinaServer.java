package com.heart.listen.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.heart.util.CONSTANTS;


public class MinaServer extends HttpServlet {

	private static final long serialVersionUID = 4135428855370888548L;

	public static final String LOGGER = "logger";
	public static final String CODEC = "codec";
	public static final int SIZE = 1024*102400;
	public static final int TIME = 10;
	
	public static IoAcceptor acceptor;
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		acceptor = new NioSocketAcceptor();
		
		/*acceptor.getFilterChain().addLast(LOGGER, new LoggingFilter());*/
		
		acceptor.getFilterChain().addLast(CODEC, new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		
       // acceptor.getSessionConfig().setReadBufferSize(SIZE);
        
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, TIME);
        
        acceptor.setHandler(new MinaServerHandler());
        
        try {
			acceptor.bind(new InetSocketAddress(CONSTANTS.MINAPORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		
	}

	
}
