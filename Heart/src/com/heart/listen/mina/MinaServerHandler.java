package com.heart.listen.mina;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.heart.bean.HeartMsg;
import com.heart.dao.MsgDao;
import com.heart.service.CommomService;
import com.heart.service.MsgService;
import com.heart.service.OldManService;
import com.heart.util.CONSTANTS;

public class MinaServerHandler extends IoHandlerAdapter {

	public static Map<String, IoSession> activeSessionMap = new HashMap<String, IoSession>();
	
	public static Map<Long, String> sessionIDPhonePair = new HashMap<Long, String>();
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		
		Long id = session.getId();
		
		String phone = sessionIDPhonePair.get(id);
		sessionIDPhonePair.remove(id);
		activeSessionMap.remove(phone);
		
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		 
		
		System.out.println("from client :" + message.toString());
		JSONObject jsonObject = JSONObject.fromObject(message.toString());
		
		String msgType = jsonObject.getString(CONSTANTS.MSGTYPE);
		
		
        switch (msgType) {
         
		case CONSTANTS.HELLO:
			
			String fromUserName = jsonObject.getString(CONSTANTS.FROMUSERNAME);
			activeSessionMap.put(fromUserName, session);
			sessionIDPhonePair.put(session.getId(), fromUserName);
			
			
		   
			MsgService.sendMsg(new MsgDao().fetchUnReadMsg(fromUserName), session);
			
		
			
			break;
		
		case CONSTANTS.POSITION:
			String toUserNamePosition = jsonObject.getString(CONSTANTS.TOUSERNAME);
			
			String positionWithMsg = new MsgService().detailPositionMsg(message);
			
			if(activeSessionMap.get(toUserNamePosition) != null)
			{ 
				activeSessionMap.get(toUserNamePosition).write(positionWithMsg);
				
				new MsgDao().saveMsg(positionWithMsg, CONSTANTS.READEDMESSAGE);
			}
			
			else {
				
				new MsgDao().saveMsg(positionWithMsg, CONSTANTS.UNREADMESSAGE);
			}
			break;
			
			
		case CONSTANTS.REQUSETPOSITION:
			
			 String toUserNameRequstPosition = jsonObject.getString(CONSTANTS.TOUSERNAME);
			 
			 
			 if(activeSessionMap.get(toUserNameRequstPosition) != null)
			 {
				 activeSessionMap.get(toUserNameRequstPosition).write(message);
			 }
			
			
			 else {
				
				 HeartMsg heartMsg = new HeartMsg();
				 heartMsg.setMsgType(CONSTANTS.REQUEST_P_ERR);
				 
				 session.write(JSONObject.fromObject(heartMsg).toString());
			}
			break;

		case "returnbind":
			
			String guardian = jsonObject.getString(CONSTANTS.TOUSERNAME);
			String oldman = jsonObject.getString(CONSTANTS.FROMUSERNAME);
			
		    String yesorno = jsonObject.getString(CONSTANTS.MSGCONTENT);
		    
		    if(yesorno.equals("1"))
		    {
		    	new CommomService().binding(oldman, guardian);
		    }
		    
		    String bindReturnWithName = new OldManService().parseReturnBindMsgWithName(message);
		    
		    if(activeSessionMap.get(guardian) != null)
		    {
		    	activeSessionMap.get(guardian).write(bindReturnWithName);
		    	
		    	new MsgDao().saveMsg(bindReturnWithName, CONSTANTS.READEDMESSAGE);
		    }
		    else
		    {
		    	new MsgDao().saveUnReadMsg(bindReturnWithName);
		    }
		    
			break;
		default:
			
			
			String toUserName = jsonObject.getString(CONSTANTS.TOUSERNAME);
			if(activeSessionMap.get(toUserName) != null)
			{
				
				
				System.out.println("send directly ");
				activeSessionMap.get(toUserName).write(message);
		
				new MsgDao().saveMsg(message, CONSTANTS.READEDMESSAGE);
				
				
			}
			else {
				 
				System.out.println("save in db...");
				
				new MsgDao().saveUnReadMsg(message);
				
			}
			
			
			break;
		}
		 
      /* HeartMsg heartMsg = new HeartMsg();
       heartMsg.setCreateTime(System.currentTimeMillis());
       heartMsg.setFromUserName("");
       heartMsg.setToUserName("");
       heartMsg.setMsgContent("receiving");
       heartMsg.setMsgType("response_hello");
       
	   session.write(JSONObject.fromObject(heartMsg).toString());
	   */
	   
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		
		
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		
		System.out.println("server closed.." + session.getId());	
		
        Long id = session.getId();
		
		String phone = sessionIDPhonePair.get(id);
		sessionIDPhonePair.remove(id);
		activeSessionMap.remove(phone);
		
		session.close(true);
	    
	        
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		
		System.out.println("server created...");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		
	//	System.out.println("server idle...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		
		System.out.println("server opend..");
	}

	
}
