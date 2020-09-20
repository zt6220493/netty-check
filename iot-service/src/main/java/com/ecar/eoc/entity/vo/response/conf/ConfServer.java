package com.ecar.eoc.entity.vo.response.conf;

import java.io.Serializable;

public class ConfServer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String host;
	private String port;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}

}
