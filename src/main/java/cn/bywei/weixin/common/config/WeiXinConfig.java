package cn.bywei.weixin.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeiXinConfig {
	//token
	@Value("${weixin.token}")
	private String token = "ray";
	// encodingAESKey
	@Value("${weixin.encodingAESKey}")
	private String encodingAESKey = "z2W9lyOAR1XjY8mopEmiSqib0TlBZzCFiCLp6IdS2Iv";
	//企业ID
	@Value("${weixin.corpId}")
	private String corpId = "ww92f5da92bb24696e";
	//应用的凭证密钥
	@Value("${weixin.corpsecret}")
	private String corpsecret = "I73733veH3uCs6H_ijPvIq0skjTaOePsFF4MyCEi3Ag";
	//企业应用的id，整型。可在应用的设置页面查看
	@Value("${weixin.agentid}")
	private String agentid = "1232132";
	
	//发送消息到用户:部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	@Value("${weixin.sendmsg.toparty:''}")
	private String sendmsgToparty = "";
	//发送消息到用户:标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	@Value("${weixin.sendmsg.totag:''}")
	private String sendmsgTotag = "";
	//发送消息到用户:成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	@Value("${weixin.sendmsg.touser:''}")
	private String sendmsgTouser = "";

	public String getToken() {
		return token;
	}

	public String getEncodingAESKey() {
		return encodingAESKey;
	}

	public String getCorpId() {
		return corpId;
	}

	public String getCorpsecret() {
		return corpsecret;
	}

	public String getAgentid() {
		return agentid;
	}

	public String getSendmsgToparty() {
		return sendmsgToparty;
	}

	public String getSendmsgTotag() {
		return sendmsgTotag;
	}

	public String getSendmsgTouser() {
		return sendmsgTouser;
	}
	
}
