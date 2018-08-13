package cn.bywei.weixin;

import org.junit.Test;

import cn.bywei.weixin.api.vo.TextMessage;
import cn.bywei.weixin.api.vo.WorkMessage;
import cn.bywei.weixin.common.enums.WorkMsgType;
import cn.bywei.weixin.common.util.JsonUtils;

public class MessageTest {

	@Test
	public void testMsg() {
		WorkMessage wmsg = new WorkMessage();
    	wmsg.setText(new TextMessage("测试发送内容"));
    	wmsg.setMsgtype(WorkMsgType.TEXT.getType());
		wmsg.setAgentid("1000015");
		wmsg.setTouser("edison.zuo");
		System.out.println(JsonUtils.toJson(wmsg));
	}
}
