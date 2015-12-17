package org.heartwings.network;

import java.lang.reflect.Field;

// 消息的抽象接口
public class HeartMessage {

	public String toUserName;

	public String fromUserName;

	public long createTime;

	public String msgType;

	public String msgID;

	public String msgContent;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		sb.append('{');
		Field[] fields = HeartMessage.class.getFields();
		for (Field field : fields) {
			if (!first) {
				sb.append(',');
			} else {
				first = false;
			}
			sb.append('"');
			sb.append(field.getName());
			sb.append('"');
			sb.append(':');
			sb.append('"');
			try {
				Object obj;
				obj = field.get(this);
				sb.append(obj.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.append('"');
		}
		sb.append('}');
		return sb.toString();
	}
}
