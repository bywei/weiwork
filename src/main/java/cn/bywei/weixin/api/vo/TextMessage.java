package cn.bywei.weixin.api.vo;

public class TextMessage {

	private String content;// 是 消息内容，最长不超过2048个字节

	public TextMessage(String content) {
		super();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
