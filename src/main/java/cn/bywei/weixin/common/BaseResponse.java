package cn.bywei.weixin.common;

import cn.bywei.weixin.common.enums.ExceptionEnum;

public class BaseResponse<T> {
	private String code = ExceptionEnum.SUCCESS.getCode();
	private String message;
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setError(ExceptionEnum error) {
		this.code = error.getCode();
		this.message = error.getMsg();
	}

	public static <T> BaseResponse<T> success() {
		return success(null);
	}

	public static <T> BaseResponse<T> success(T data) {
		BaseResponse<T> response = new BaseResponse<>();
		response.setData(data);
		return response;
	}

	public static <T> BaseResponse<T> fail(ExceptionEnum error, T data) {
		return fail(error.getMsg(), data);
	}

	public static <T> BaseResponse<T> fail(String message) {
		BaseResponse<T> response = new BaseResponse<>();
		response.setCode(ExceptionEnum.FAIL.getCode());
		response.setMessage(message);
		return response;
	}

	public static <T> BaseResponse<T> fail(String message, T data) {
		BaseResponse<T> response = new BaseResponse<>();
		response.setCode(ExceptionEnum.FAIL.getCode());
		response.setMessage(message);
		response.setData(data);
		return response;
	}

	public static <T> BaseResponse<T> fail(ExceptionEnum error) {
		return fail(error.getMsg());
	}
	
	public static <T> BaseResponse<T> of(boolean success) {
		BaseResponse<T> response = new BaseResponse<>();
		response.setCode(success ? ExceptionEnum.SUCCESS.getCode() : ExceptionEnum.FAIL.getCode());
		response.setMessage(success ? ExceptionEnum.SUCCESS.getMsg() : ExceptionEnum.FAIL.getMsg());
		return response;
	}
	
	public static <T> BaseResponse<T> of(boolean success, String message, T data) {
		BaseResponse<T> response = new BaseResponse<>();
		response.setCode(success ? ExceptionEnum.SUCCESS.getCode() : ExceptionEnum.FAIL.getCode());
		response.setMessage(message);
		response.setData(data);
		return response;
	}

}
