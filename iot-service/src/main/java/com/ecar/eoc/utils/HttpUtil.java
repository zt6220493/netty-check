package com.ecar.eoc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
public class HttpUtil {
    static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final int HTTP_CONNECTION_TIMEOUT = 60 * 1000;
    private static final int HTTP_READ_TIMEOUT = 60 * 1000;
//    private static final String gateway_SOURCE = "source=alligator";
    /**
     * 
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     */
    public static String sendGet(String url, String param) {
        return sendGet(url, param, HTTP_CONNECTION_TIMEOUT, HTTP_READ_TIMEOUT);
    }
    /**
     * 
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     */
    public static String sendGet(String url, String param, int timeOut) {
        return sendGet(url, param, timeOut, timeOut);
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, int connectionTimeOut, int readTimeOut) {
        String result = "";
        BufferedReader in = null;
        String urlNameString = url;
        if (!url.contains("?")){
            urlNameString += "?";
        }
        try {
            if (!StringUtils.isEmpty(param)) {
                urlNameString += param;
            }
            logger.info("请求url+参数：" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setConnectTimeout(connectionTimeOut);
            connection.setReadTimeout(readTimeOut);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！url: " + urlNameString + ", " + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("close exception", e2);
            }
        }
        return result;
    }


    /**
     * 向指定URL发送GET方法的请求=>专门针对想ERP发送的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet4ERP(String url, String param) {
        int connectionTimeOut = HTTP_CONNECTION_TIMEOUT;
        int readTimeOut = HTTP_READ_TIMEOUT;
        String result = "";
        BufferedReader in = null;
        String urlNameString = url + "?";
        try {
            if (!StringUtils.isEmpty(param)) {
                urlNameString += param;
            }
            logger.info("请求url+参数：" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setConnectTimeout(connectionTimeOut);
            connection.setReadTimeout(readTimeOut);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！url: " + urlNameString + ", " + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("close exception", e2);
            }
        }
        return result;
    }


    public static String sendJSONPost(String url, String param) {
        return sendPost(url, param, "application/json", "application/json");
    }

    public static String sendJSONPost(String url, String param, int timeOut) {
        return sendPost(url, param, "application/json", "application/json", timeOut, timeOut);
    }


    public static String sendPost(String url, String param) {
        return sendPost(url, param, "*/*", "application/json");
    }

    public static String sendPost(String url, String param, String accept, String contentType) {
        return sendPost(url, param, accept, contentType, HTTP_CONNECTION_TIMEOUT,
                HTTP_READ_TIMEOUT);

    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String accept, String contentType, int connectionTimeOut, int readTimeOut) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", contentType);
            conn.setRequestProperty("connection", "Keep-Alive");
            // 前端确认通过请求头放session_token来处理
//             conn.setRequestProperty("session_token", "Jt31Sp4n3Q0kdrxo9cyPHCsbnXePF0Fx98CsWkS6FxA=");
            conn.setConnectTimeout(connectionTimeOut);
            conn.setReadTimeout(readTimeOut);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！url: " + url + ",参数: " + param + "," + e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error("IO exception", ex);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param url
     * @param contextType  "image/jpeg","application/Json"
     * @return
     */
    public static byte[] sendHttpsGetUrl(String url,String contextType) {
      
      // 响应内容
      byte[] bs = null;
      
      // 创建默认的httpClient实例
  		HttpClient httpClient = new DefaultHttpClient();
//      HttpClient httpClient =		httpClient1;
      
      // 创建TrustManager
      X509TrustManager xtm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        }
        
        public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        }
        
        public X509Certificate[] getAcceptedIssuers() {
          return new X509Certificate[] {};
        }
      };
      try {
        SSLContext ctx = SSLContext.getInstance("SSL");
        
        // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
        ctx.init(null, new TrustManager[] { xtm }, null);
        
        SSLSocketFactory sf = new SSLSocketFactory(
            ctx,
            SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme sch = new Scheme("https", 443, sf);
        httpClient.getConnectionManager().getSchemeRegistry().register(sch);
        // 创建HttpPost
        HttpGet httpPost = new HttpGet(url); 
        httpPost.setHeader("content-type", contextType);
        // 执行POST请求
        HttpResponse response = httpClient.execute(httpPost); 
        // 获取响应实体
        HttpEntity entity = response.getEntity();
         bs = IOUtils.toByteArray(entity.getContent());
         if (null != entity) {
            EntityUtils.consume(entity); // Consume response content
          }
        return bs;
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        // 关闭连接,释放资源
//  			httpClient.getConnectionManager().shutdown(); 
      }
      return bs;
    }
    
    /**
     * 获取当前网络ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (null != inet){
                	ipAddress = inet.getHostAddress();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
    
    /**
     * 讲map拼接成xxx=xxx&xxx=xxx的形式
     * @param map
     * @param ignore
     * @return
     * @throws UnsupportedEncodingException 
     */
//    public static String map2LinkString(Map<String, String> map, String[] ignore) {
//		ArrayList<String> mapKeys = new ArrayList<String>(map.keySet());
//        Collections.sort(mapKeys);
//        StringBuilder link = new StringBuilder();
//        boolean first = true;
//        for_map_keys:
//        for(String key: mapKeys) {
//            String value = map.get(key);
//            if (value==null || "".equals(value.trim())) continue;
//            if (ignore != null) {
//	            for(String i: ignore) {
//	                if (i.equalsIgnoreCase(key)) continue for_map_keys;
//	            }
//            }
//
//            if (!first) link.append("&");
//            link.append(key).append("=").append(value);
//            if (first) first = false;
//        }
//        return link.toString();
//	}
    
    public static void main(String[] args) throws UnsupportedEncodingException {
    	System.out.println(sendGet("http://192.168.171.231:7033/bd-service/pushMessage/mobileReminder", 
    			"terminalId=1865360&alarmTime="+URLEncoder.encode("2019-05-13 17:35:07","utf-8")+"&lat=22.546473&lon=113.93639"));
    }
}
