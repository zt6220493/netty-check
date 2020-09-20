package com.ecar.eoc.common;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipInputStream;



/**
 * Description: 获取设备日志 url 中的 zip 文件内容，存在内存中
 * Date:        2019年3月7日 上午10:39:31
 * @author      chenjiahao
 */
public class DownloadUrl {

    public StringBuffer download(String urlString) throws IOException {

        URL url = new URL(urlString);

        StringBuffer resultLog = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        InputStream inputStream = null;
        ZipInputStream zis = null;

        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            conn = (HttpURLConnection) url.openConnection();
            // 设置超时时间为 5 秒
            conn.setConnectTimeout(5 * 1000);
            // 设置代理模式
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 得到输入流
            inputStream = conn.getInputStream();
            zis = new ZipInputStream(inputStream);
            resultLog = readZipFile(zis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                // 关闭连接
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (zis != null) {
                    zis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultLog;
    }

    /**
     * 读取 zip 文件内容
     * @param zipInputStream zip 输入流
     * @return
     * @throws IOException
     */
    private static StringBuffer readZipFile(ZipInputStream zipInputStream) throws IOException {
        // 字节数组
        byte[] cache = new byte[2048];
        BufferedReader bufferedReader = null;
        StringBuffer result = new StringBuffer("");

        try {
            while ((zipInputStream.getNextEntry()) != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}


