package cn.bywei.weixin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bywei.weixin.api.vo.WorkMessage;
import cn.bywei.weixin.common.BaseResponse;
import cn.bywei.weixin.common.Constant;
import cn.bywei.weixin.common.HttpsClient;
import cn.bywei.weixin.common.config.WeiXinConfig;
import cn.bywei.weixin.common.enums.ExceptionEnum;
import cn.bywei.weixin.common.util.JsonUtils;


/**
 * 秘钥Service
 * 
 * @author wejack.zeng
 *
 */
@Service
public class DefaultService {
	
	private static final String GET_TOKEN_URL  = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
//	private static final String GET_TOKEN_URL  = "http://work.weixin.qq.com/api/devtools/devhandler.php?tid=1&corpid=%s&corpsecret=%s&f=json";
	
	private static final String SEND_MESSAGE_URL  = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";
	
	private static Logger logger = LoggerFactory.getLogger(DefaultService.class);
   
	@Autowired
	private WeiXinConfig weiXinConfig;
	
	@Autowired
	private HttpsClient httpsClient;
	
	public BaseResponse<String> process(WorkMessage wmsg) {
		try {
			String data = JsonUtils.toJson(wmsg);
			String accessToken = getAccessToken();
			String messageResult = httpsClient.postJson(String.format(SEND_MESSAGE_URL, accessToken), data);
			logger.info("发送监控信息：" + data);
			return BaseResponse.success(messageResult);
		} catch(Exception ex) {
			logger.error("process()_error",ex);
		}
		return BaseResponse.fail(ExceptionEnum.FAIL);
	}
	
	public String getAccessToken() {
		try {
			String accessToken = Constant.getAccessTokenCache();
			if(accessToken == null) {
				String content = httpsClient.get(String.format(GET_TOKEN_URL, weiXinConfig.getCorpId(),weiXinConfig.getCorpsecret()));
				accessToken = JsonUtils.getValue(content, "access_token");
				Constant.setAccessTokenCache(accessToken);
			}
			return accessToken;
		} catch(Exception ex) {
			logger.error("getAccessToken()_error",ex);
		}
		return null;
	}
}
