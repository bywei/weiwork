package cn.bywei.weixin.common.enums;

public enum ExceptionEnum {
	SUCCESS("0", "成功"), FAIL("10000", "操作失败"),PARAM_ERROR("10001","参数校验失败");

	private String code;
	private String msgZH;
	private String msgEN;
	private String msgJA;

	private ExceptionEnum(String msgZH) {
		this.msgZH = msgZH;
	}

	private ExceptionEnum(String code, String msgZH) {
		this.code = code;
		this.msgZH = msgZH;
	}

	private ExceptionEnum(String code, String msgZH, String msgEN, String msgJA) {
		this.code = code;
		this.msgZH = msgZH;
		this.msgEN = msgEN;
		this.msgJA = msgJA;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msgZH;
	}

	public String getMsg(int language) {
		switch (language) {
		case 0:
			return msgZH;
		case 1:
			return msgEN;
		case 2:
			return msgJA;
		default:
			return msgZH;
		}
	}

}
