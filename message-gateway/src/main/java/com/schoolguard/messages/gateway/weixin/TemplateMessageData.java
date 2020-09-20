/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schoolguard.messages.gateway.weixin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

/**
 *
 * @author Rogers
 */
public class TemplateMessageData implements MessageBody {
    /*
     {{first.DATA}}
     班级：{{keyword1.DATA}}
     通知人：{{keyword2.DATA}}
     时间：{{keyword3.DATA}}
     通知内容：{{keyword4.DATA}}
     {{remark.DATA}}
     */

    protected static final String topColor = "#4A8BF5";
    protected static final String valueColor = "#173177";
    protected static final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    protected String toUser;
    protected String url;
    private final String templateId;
    protected Map<String, String> dataMap;


    public TemplateMessageData(String toUser, String url, String templateId, Map<String, String> data) {
        this.toUser = toUser;
        this.url = url;
        this.templateId = templateId;
        this.dataMap = data;
    }

    @Override
    public JsonNode toJson() {
        ObjectNode rootNode = nodeFactory.objectNode();
        rootNode.put("touser", this.toUser);
        rootNode.put("template_id", this.templateId);
        rootNode.put("url", this.url);
        rootNode.put("topcolor", TemplateMessageData.topColor);
        rootNode.set("data", getMessageData());
        return rootNode;
    }

    protected JsonNode getMessageData() {
        ObjectNode dataNode = nodeFactory.objectNode();
        for (String key : this.dataMap.keySet()) {
            setDataField(dataNode, key, this.dataMap.get(key));
        }

        return dataNode;
    }

    protected ObjectNode setDataField(ObjectNode dataNode, String fieldName, String value) {
        ObjectNode valueNode = nodeFactory.objectNode();
        valueNode.put("color", TemplateMessageData.valueColor);
        valueNode.put("value", value);
        dataNode.set(fieldName, valueNode);

        return dataNode;
    }
}
