package cn.bywei.weixin.common;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import cn.bywei.weixin.common.util.JaxbUtils;
import cn.bywei.weixin.common.util.JsonUtils;

@Component
public class HttpsClient {

    private static final Logger LOGGER = LogManager.getLogger(HttpsClient.class);

    private static final int CONNECT_TIMEOUT = 5000;//设置超时毫秒数

    private static final int SOCKET_TIMEOUT = 10000;//设置传输毫秒数

    private static final int REQUESTCONNECT_TIMEOUT = 3000;//获取请求超时毫秒数

    private static final int CONNECT_TOTAL = 200;//最大连接数

    private static final int CONNECT_ROUTE = 20;//设置每个路由的基础连接数

    private static final int VALIDATE_TIME = 30000;//设置重用连接时间

    private static PoolingHttpClientConnectionManager manager = null;

    private static CloseableHttpClient client = null;

    static {
        ConnectionSocketFactory csf = PlainConnectionSocketFactory.getSocketFactory();
        SSLConnectionSocketFactory lsf = createSSLConnSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", csf).register("https", lsf).build();
        manager = new PoolingHttpClientConnectionManager(registry);
        manager.setMaxTotal(CONNECT_TOTAL);
        manager.setDefaultMaxPerRoute(CONNECT_ROUTE);
        manager.setValidateAfterInactivity(VALIDATE_TIME);
        SocketConfig config = SocketConfig.custom().setSoTimeout(SOCKET_TIMEOUT).build();
        manager.setDefaultSocketConfig(config);
        RequestConfig requestConf = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(REQUESTCONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        client = HttpClients.custom().setConnectionManager(manager).setDefaultRequestConfig(requestConf).setRetryHandler(
                (exception, executionCount, context) -> { 
                	return retryHander(exception, executionCount, context);
                }).build();
        if(manager!=null && manager.getTotalStats()!=null) {
            LOGGER.info("客户池状态："+manager.getTotalStats().toString());
        }
    }

	private static boolean retryHander(IOException exception, int executionCount, HttpContext context) {
		if(executionCount >= 3) {
		    return false;
		}
		if(exception instanceof NoHttpResponseException) {//如果服务器断掉了连接那么重试
		    return true;
		}
		if(exception instanceof SSLHandshakeException) {//不重试握手异常
		    return false;
		}
		if(exception instanceof InterruptedIOException) {//IO传输中断重试
		    return true;
		}
		if(exception instanceof UnknownHostException) {//未知服务器
		    return false;
		}
		if(exception instanceof ConnectTimeoutException) {//超时就重试
		    return true;
		}
		if(exception instanceof SSLException) {
		    return false;
		}
		HttpClientContext cliContext = HttpClientContext.adapt(context);
		HttpRequest request = cliContext.getRequest();
		if(!(request instanceof HttpEntityEnclosingRequest)) {
		    return true;
		}
		return false;
	}
	
	/**
	 *证书信任管理器（用于实现https）  实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
	 */
	public static class MyX509TrustManager implements X509TrustManager{

	    @Override
	    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException { }

	    @Override
	    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException { }

	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
	        return null;
	    }
	}

	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        try {
        	SSLContext sc = SSLContext.getInstance("SSLv3");
     		sc.init(null, new TrustManager[] { new MyX509TrustManager() }, null);
     		return new SSLConnectionSocketFactory(sc);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("SSL create error" + e.getLocalizedMessage());
            e.printStackTrace();
        } catch(KeyManagementException e) {
        	LOGGER.error("SSL create key error" + e.getLocalizedMessage());
        	e.printStackTrace();
        } catch(Exception ex) {
        	LOGGER.error("SSL create key Exception" + ex.getLocalizedMessage());
        	ex.printStackTrace();
        }
        return null;
    }
    
    private String doMethod(HttpRequestBase method) {
        CloseableHttpResponse response = null;
        try {
        	HttpClientContext context = HttpClientContext.create();
            response = client.execute(method, context);
            HttpEntity entity = response.getEntity();
            if(entity!=null) {
                Charset charset = ContentType.getOrDefault(entity).getCharset();
                String content = EntityUtils.toString(entity, charset);
                EntityUtils.consume(entity);
                return content;
            }
        } catch(ConnectTimeoutException cte) {
            LOGGER.error("请求连接超时，由于 " + cte.getLocalizedMessage());
            cte.printStackTrace();
        } catch(SocketTimeoutException ste) {
            LOGGER.error("请求链接超时，由于 " + ste.getLocalizedMessage());
            ste.printStackTrace();
        } catch(ClientProtocolException cpe) {
            LOGGER.error("协议错误（比如构造HttpGet对象时传入协议不对(将'http'写成'htp')or响应内容不符合），由于 " + cpe.getLocalizedMessage());
            cpe.printStackTrace();
        } catch(IOException ie) {
            LOGGER.error("实体转换异常或者网络异常， 由于 " + ie.getLocalizedMessage());
            ie.printStackTrace();
        } finally {
            try {
                if(response!=null) {
                    response.close();
                }
            } catch(IOException e) {
                LOGGER.error("响应关闭异常， 由于 " + e.getLocalizedMessage());
            }
            if(method!=null) {
                method.releaseConnection();
            } 
        }
        return null;
    }

    public String get(String url) {
        HttpGet get = new HttpGet(url);
        return doMethod(get);
    }
    
    public <T> T getJson(String url, Class<T> type) {
    	String jsonStr = get(url);
		return JsonUtils.toObject(jsonStr, type);
	}
    
    public <T> T getJson(String url, TypeReference<T> typeReference) {
    	String jsonStr = get(url);
    	return JsonUtils.toObject(jsonStr, typeReference);
    }
    
    public <T> T getXml(String url, Class<T> type) {
		String xmlStr = get(url);
		return JaxbUtils.convertToObject(xmlStr, type);
	}

    public String post(String url, Map<String, String> params) {
        HttpPost post = new HttpPost(url);
        params.keySet().stream().forEach(key -> {
            String value = params.get(key);
            if(value!=null) {
                post.addHeader(key, value);
            }
        });
        return doMethod(post);
    }

    public String post(String url, String data,ContentType contentType) {
        HttpPost post = new HttpPost(url);
        if(StringUtils.isNotBlank(data)) {
            post.addHeader("Content-Type", contentType.getMimeType());
        }
        post.setEntity(new StringEntity(data, contentType));
        return doMethod(post);
    }
    
    public String postJson(String url, String data) {
		return post(url, data , ContentType.APPLICATION_JSON);
	}

    public <T> T postJson(String url, Object data, Class<T> type) {
		String jsonStr = post(url, JsonUtils.toJson(data), ContentType.APPLICATION_JSON);
		return JsonUtils.toObject(jsonStr, type);
	}
    
    public <T> T postJson(String url, Object data, TypeReference<T> typeReference) {
    	String jsonStr = post(url, JsonUtils.toJson(data), ContentType.APPLICATION_JSON);
    	return JsonUtils.toObject(jsonStr, typeReference);
    }
    
    public <T> T postXml(String url, Object data, Class<T> type) {
		String xmlStr = post(url, JaxbUtils.convertToXmlString(data), ContentType.APPLICATION_XML);
		return JaxbUtils.convertToObject(xmlStr, type);
	}
}
