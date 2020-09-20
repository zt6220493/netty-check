/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schoolguard.messages.gateway.weixin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 *
 * @author Rogers
 */
public class TemplateMessage {
    private static final String serverUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send";
    
    private MessageBody message;
    private String accessToken;
    
    public TemplateMessage(MessageBody message, String accessToken) {
        this.accessToken = accessToken;
        this.message = message;
    }
    
    /**
     * 发送模板消息
     * @throws WeixinAPIException
     * @throws java.io.IOException
     */
    public void sendMessage() throws WeixinAPIException, IOException{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = getRequest(getSendingAPIUrl(), message.toJson());
        try{
            CloseableHttpResponse response = httpClient.execute(request);
            checkSendingStatus(response.getEntity());
        } catch(WeixinAPIException | IOException e){
            throw e;
        } finally {
            httpClient.close();
        }
    }


    private String getSendingAPIUrl(){
        return serverUrl + "?access_token=" + this.accessToken;
    }

    private HttpPost getRequest(String url, JsonNode jsonData){
        HttpPost request = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonData.toString(), "UTF-8");
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json; charset=utf-8");

        return request;
    }



    /**
     * 检查模板消息发送结果（只是推送到微信服务器，至于用户是否接收到，则需要检查微信反馈的xml消息
     * @param entity
     * @throws WeixinAPIException
     * @throws java.io.IOException
     */
    private void checkSendingStatus(final HttpEntity entity)throws WeixinAPIException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObject = null;
        
        //JSON jsonObject = null;
        try{
            jsonObject = mapper.readTree(entity.getContent());
        } catch (IOException e){
           throw e;
        }
        // API error to exception
        WeixinAPIException exception = toException(jsonObject);
        if(exception != null){
            throw exception;
        }
    }

    private WeixinAPIException toException(JsonNode json){
        if(json.isNull()){
            //需要介入检查具体情况
            return new WeixinAPIException("未收到微信响应", -2);
        }
        
        ObjectNode node = (ObjectNode) json;

        if(node.hasNonNull("errcode") && node.hasNonNull("errmsg")){
            // Error occurred
            if(node.get("errcode").asInt() != 0){
                return new WeixinAPIException(node.get("errmsg").textValue(),
                        node.get("errcode").asInt());
            }
        }
        
        //收到成功响应
        return null;


    }
}
