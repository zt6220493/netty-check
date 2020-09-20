/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schoolguard.messages.gateway.weixin;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author Rogers
 */
public interface MessageBody {
    
    public abstract JsonNode toJson();
}
