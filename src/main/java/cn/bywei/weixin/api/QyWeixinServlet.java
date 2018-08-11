package cn.bywei.weixin.api;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cn.bywei.weixin.common.aes.AesException;
import cn.bywei.weixin.common.aes.WXBizMsgCrypt;
import cn.bywei.weixin.common.config.WeiXinConfig;

/**
 * @author bywei
 * @category 企业微信通用开发配置
 */
@Component
public class QyWeixinServlet extends HttpServlet {

	private static final long serialVersionUID = 9017395156175115158L;

	Logger logger = Logger.getLogger(QyWeixinServlet.class);

	@Autowired
	private WeiXinConfig weiXinConfig;
	
	@Autowired
	private Environment env;

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		logger.info("into servlet service");
		super.service(arg0, arg1);
	}

	// 企业微信通用开发配置GET请求处理
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = weiXinConfig.getToken();
		env.getProperty("weixin.token");
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
		System.out.println("微信加密签名的值是：" + msg_signature);
		System.out.println("时间戳的值是：" + timestamp);
		System.out.println("随机数的值是：" + nonce);
		System.out.println("加密随机字符串：" + echostr);
		System.out.println("=============调试===================");
		logger.debug("微信加密签名的值是：" + msg_signature);
		logger.debug("时间戳的值是：" + timestamp);
		logger.debug("随机数的值是：" + nonce);
		logger.debug("加密随机字符串：" + echostr);

		try {
			Writer w = resp.getWriter();
			WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, encodingAesKey, corpId);
			String echoStr = crypt.VerifyURL(msg_signature, timestamp, nonce, echostr);
			w.write(echoStr);
			w.flush();
			w.close();
			req.getRequestDispatcher("/index.html").forward(req, resp);
		} catch (AesException | IOException | ServletException e) {
			e.printStackTrace();
			req.getRequestDispatcher("/error.html").forward(req, resp);
		}
	}

	// 处理微信其他的请求
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/index.html").forward(req, resp);
		super.doPost(req, resp);
	}

}
