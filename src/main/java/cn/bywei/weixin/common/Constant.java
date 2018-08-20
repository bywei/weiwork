package cn.bywei.weixin.common;

import java.time.LocalTime;
import java.util.HashMap;

public class Constant {
	
	private static String ACCESS_TOKEN_NAME = null;
	
	private static String ACCESS_TOKEN = null;

	private static HashMap<String, LocalTime> accessTokenMap = new HashMap<String, LocalTime>();
	
	public static String getAccessTokenCache(int expire) {
		LocalTime time = accessTokenMap.get(ACCESS_TOKEN_NAME);
		if(ACCESS_TOKEN == null || time == null|| LocalTime.now().isAfter(time.plusMinutes(expire))) {
			return null;
		}
		return ACCESS_TOKEN;
	}
	
	public static void setAccessTokenCache(String accessToken) {
		accessTokenMap.put(ACCESS_TOKEN_NAME, LocalTime.now());
		ACCESS_TOKEN = accessToken;
	}
}
