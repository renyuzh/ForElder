package org.heartwings.network;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.heartwings.care.Constants;
import org.heartwings.care.MainActivity;
import org.heartwings.care.falldetect.FallDetectService;
import org.heartwings.care.photoshare.PhotoSharingService;
import org.heartwings.care.schedule.SchedulingHelper;
import org.heartwings.care.schedule.SingleEvent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class ClientHandler extends IoHandlerAdapter {
	private NetworkOperationHelper networkOperationHelper;

	public ClientHandler() {
		networkOperationHelper = NetworkOperationHelper
				.getNetworkOperationHelper();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		Log.e("PhotoShare", "An exception has occured.");
		cause.printStackTrace();
		session.close(true);
	}

	@Override
	public void messageReceived(final IoSession session, Object message)
			throws Exception {
		Log.i("PhotoShare", "Message Received: " + message.toString());
		String result = message.toString();
		if (result.length() >= 5) {
			final HeartMessage heartMessage = JSON.parseObject(result,
					HeartMessage.class);
			// 处理接收图片请求
			if (heartMessage.getMsgType().equalsIgnoreCase("image")) {
				List<String> urlList = JSONArray.parseArray(
						heartMessage.getMsgContent(), String.class);
				for (String url : urlList) {
					networkOperationHelper.download(PhotoSharingService
							.getPhotoSharingService(), url, PhotoSharingService
							.getPhotoSharingService().getNoticeHandler());
				}
				// 处理绑定请求
			} else if (heartMessage.getMsgType().equalsIgnoreCase("bind")) {
				Message msg = new Message();
				msg.what = Constants.MESSAGE_BIND_REQUEST;
				msg.obj = new Pair<HeartMessage, IoSession>(heartMessage,
						session);
				PhotoSharingService.getPhotoSharingService().getNoticeHandler()
						.sendMessage(msg);
			} else if (heartMessage.getMsgType().equalsIgnoreCase(
					"schedule_single_event")) {
				Log.i("MinaClient", "Received scheduling for a single event");
				SingleEvent ev = JSON.parseObject(heartMessage.msgContent,
						SingleEvent.class);
				if (ev != null) {
					SchedulingHelper.getSchedulingHelper().addNewEvent(ev);
				} else {
					Log.e("Schedule", "Unrecognized message");
				}
			} else if (heartMessage.getMsgType().equalsIgnoreCase(
					"emergency_list")) {
				Log.i("MinaClient", "Received emergency list");
				List<EmergencyPhone> emergencyPhones = JSON.parseArray(
						heartMessage.getMsgContent(), EmergencyPhone.class);
				Context context = PhotoSharingService.getPhotoSharingService();
				Editor editor = context.getSharedPreferences(
						Constants.NAME_SHARED_PREFERENCE_MAIN,
						Context.MODE_PRIVATE).edit();
				Set<String> tempStringSet = new HashSet<String>();
				for (EmergencyPhone emergencyPhone : emergencyPhones) {
					tempStringSet.add(emergencyPhone.getPhone());
				}
				editor.putStringSet(Constants.NAME_SHARED_PREFERENCE_MAIN,
						tempStringSet);
				editor.commit();
				Log.i("MinaClient",
						"The Emergency phone has been set successfully with "
								+ String.valueOf(tempStringSet.size())
								+ " phones.");
				for(EmergencyPhone emergencyPhone : emergencyPhones){
					Log.i("EmergencyPhone", emergencyPhone.getName() + ", " + String.valueOf(emergencyPhone.getPhone()));
				}
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		Log.i("PhotoShare", "Message sent: " + message.toString());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Log.i("PhotoShare", "Connection has been closed");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		HeartMessage heartMessage = new HeartMessage();
		heartMessage.setCreateTime(System.currentTimeMillis());
		Log.i("PhotoShare", "Try to send hello packet.");
		heartMessage.setFromUserName((String) session
				.getAttribute("thisClient"));
		heartMessage.setMsgType("hello");

		String jsonHeartMessage = JSON.toJSONString(heartMessage);

		session.write(jsonHeartMessage);
	}
}
