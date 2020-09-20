/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schoolguard.messages.gateway.weixin;

/**
 *
 * @author Rogers
 */
public class WeixinAPIException extends Exception{
    private int code;

    public WeixinAPIException() {
        super();
    }

    public WeixinAPIException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage(){
       return String.format("[errcode: %d, errmsg: %s]", this.code, super.getMessage());
    }
}
