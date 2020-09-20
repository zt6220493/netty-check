/**
 * Project Name:eoc-sim
 * File Name:HttpUtil.java
 * Package Name:com.ecar.eoc.common
 * Date:2018年6月14日下午3:18:29
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.common;

import java.io.File;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;

/**
 * ClassName:HttpUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年6月14日 下午3:18:29 <br/>
 * @author   zhongying	 
 */
public class HttpUtil {
	
	public static final long appId = 10012948;
	public static final String mybucket = "isafe";
	
	 // 将本地文件上传到COS
    public static void SimpleUploadFileFromLocal() {
        // 1 初始化用户身份信息(secretId, secretKey)
    	COSCredentials cred = new BasicCOSCredentials("AKIDdBXhgVzzJXZP4XKwscOzSlssgJl0m94o"
				,"gAUIzUp0zmMkwwkMp6HLFxTmqjQvFPOC");
        // 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket名需包含appid
        String bucketName = "safetest-1251669605";
        
        String key = "/aaa/bbb.txt";
        File localFile = new File("d:\\123.txt");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        try {
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            putObjectResult.getETag();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }
        
        // 关闭客户端
        cosclient.shutdown();
    }
    public static void main(String[] args) {
    	HttpUtil.SimpleUploadFileFromLocal();
	}
	
	

}

