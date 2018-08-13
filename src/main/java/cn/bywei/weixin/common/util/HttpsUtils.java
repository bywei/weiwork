package cn.bywei.weixin.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpsUtils {
	
	public static void main(String[] args) {
		String requestUrl= "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ww9e291c165338dfc1&corpsecret=JG9mMJFvx0-GLD3HOCi0hktAcsigdqGY79Pt0LyfmGA";
		HttpsUtils.https(requestUrl, "GET", null);
	}

	public static void https(String requestUrl,String requestMethod,String submitData) {
        try {
            //设置https访问模式，采用SSL加密
            TrustManager[] tm={new MyX509TrustManager()};
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            SSLContext sslContext=SSLContext.getInstance("SSL","SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            //从sslContext获取SSLSocketFactory
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            URL url=new URL(requestUrl);
            HttpsURLConnection httpsURLCon=(HttpsURLConnection) url.openConnection();
            httpsURLCon.setSSLSocketFactory(ssf);
            httpsURLCon.setDoInput(true);
            httpsURLCon.setDoOutput(true);
            httpsURLCon.setUseCaches(false);
            httpsURLCon.setRequestMethod(requestMethod);//设置请求方式get；post
            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpsURLCon.connect();
            }
            //当需要有数据提交给微信接口时
            if (null!=submitData) {
                OutputStream outputStream=httpsURLCon.getOutputStream();
                outputStream.write(submitData.getBytes("UTF-8"));
                outputStream.close();
            }
            int code = httpsURLCon.getResponseCode();
            if (code == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpsURLCon.getInputStream(), "UTF-8"));
                String retData = null;
                String responseData = "";
                while ((retData = in.readLine()) != null) {
                    responseData += retData;
                }
                System.out.println(responseData);
                in.close();                     
            } else {
//                Log.i("https","return error");
            }
    }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	
	/*
	 *证书信任管理器（用于实现https） 
	 */
	public static class MyX509TrustManager implements X509TrustManager{

	    @Override
	    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
	            throws CertificateException {
	        // TODO Auto-generated method stub

	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
	            throws CertificateException {
	        // TODO Auto-generated method stub

	    }

	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
	        // TODO Auto-generated method stub
	        return null;
	    }

	}
}
