package cn.bywei.weixin.common.enums;

public enum WorkMsgType {

	TEXT("text"),IMAGE("image"),VOICE("voice"),VIDEO("video"),FILE("file"),TEXTCARD("textcard"),NEWS("news");
	
	private String type;

	private WorkMsgType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
