package cn.bywei.weixin.api;


import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bywei.weixin.api.vo.TextMessage;
import cn.bywei.weixin.api.vo.WorkMessage;
import cn.bywei.weixin.common.BaseResponse;
import cn.bywei.weixin.common.aes.AesException;
import cn.bywei.weixin.common.aes.WXBizMsgCrypt;
import cn.bywei.weixin.common.config.WeiXinConfig;
import cn.bywei.weixin.common.enums.WorkMsgType;
import cn.bywei.weixin.service.DefaultService;

@Controller
public class DefaultController {
	
	Logger logger = Logger.getLogger(DefaultController.class);
	 
    @Autowired
	private WeiXinConfig weiXinConfig;
	
	@Autowired
	private DefaultService defaultService;

    @RequestMapping("{page}")
    public String index(@PathVariable("page") String page){
    	
    	return page;
    }

    @ResponseBody
    @RequestMapping("/api/notify/sendText")
    public BaseResponse<String> sendText(@RequestBody String content) {
    	WorkMessage wmsg = new WorkMessage();
    	wmsg.setText(new TextMessage(content));
    	wmsg.setMsgtype(WorkMsgType.TEXT.getType());
		wmsg.setAgentid(weiXinConfig.getAgentid());
		wmsg.setToparty(weiXinConfig.getSendmsgToparty());
		wmsg.setTotag(weiXinConfig.getSendmsgTotag());
		wmsg.setTouser(weiXinConfig.getSendmsgTouser());
    	return defaultService.process(wmsg);
    }

	// 企业微信通用开发配置GET请求处理
	@RequestMapping("/api/notify")
	public void notify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = weiXinConfig.getToken();
		String encodingAesKey = weiXinConfig.getEncodingAESKey();
		String corpId = weiXinConfig.getCorpId();
		// 微信加密签名
		String msg_signature = req.getParameter("msg_signature");
		// 时间戳
		String timestamp = req.getParameter("timestamp");
		// 随机数
		String nonce = req.getParameter("nonce");
		// 加密的随机字符串
		String echostr = req.getParameter("echostr");
		logger.info("微信加密签名的值是：" + msg_signature);
		logger.info("时间戳的值是：" + timestamp);
		logger.info("随机数的值是：" + nonce);
		logger.info("加密随机字符串：" + echostr);
		String outCode = "fail";
		try {
			WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, encodingAesKey, corpId);
			outCode = crypt.VerifyURL(msg_signature, timestamp, nonce, echostr);
		} catch (AesException e) {
			outCode = e.getMessage();
			logger.error("AesException",e);
		}
		try {
			resp.setContentType("text/plain;charset=UTF-8");
			Writer w = resp.getWriter();
			w.write(outCode);
			w.flush();
			w.close();
		} catch (IOException e) {
			logger.error("IOException",e);
		}
	}
}
  
