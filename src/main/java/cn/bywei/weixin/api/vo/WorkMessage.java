package cn.bywei.weixin.api.vo;

import cn.bywei.weixin.common.enums.WorkMsgType;

public class WorkMessage {

	private String touser;// 否 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向该企业应用的全部成员发送
	private String toparty;// 否 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	private String totag;// 否 标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	private String msgtype = WorkMsgType.TEXT.getType();// 是 消息类型，此时固定为：text
	private String agentid;// 是 企业应用的id，整型。可在应用的设置页面查看
	private TextMessage text;// 是 消息内容，最长不超过2048个字节
	private String safe = "0";// 否 表示是否是保密消息，0表示否，1表示是，默认0

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}

	public String getTotag() {
		return totag;
	}

	public void setTotag(String totag) {
		this.totag = totag;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public TextMessage getText() {
		return text;
	}

	public void setText(TextMessage text) {
		this.text = text;
	}

	public String getSafe() {
		return safe;
	}

	public void setSafe(String safe) {
		this.safe = safe;
	}
}
