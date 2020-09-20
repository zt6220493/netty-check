package com.ecar.eoc.entity.vo;

public class CmdInfo {
	private String springBeanName;
	private String cmdName;
	
	public CmdInfo(String springBeanName, String cmdName) {
		this.springBeanName = springBeanName;
		this.cmdName = cmdName;
	}
	public String getSpringBeanName() {
		return springBeanName;
	}
	public void setSpringBeanName(String springBeanName) {
		this.springBeanName = springBeanName;
	}
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	
	@Override
	public String toString() {
		return "CmdInfo [springBeanName=" + springBeanName + ", cmdName=" + cmdName + "]";
	}
	
}
