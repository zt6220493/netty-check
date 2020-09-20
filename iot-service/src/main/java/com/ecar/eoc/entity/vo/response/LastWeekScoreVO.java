package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class LastWeekScoreVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;
	private String score;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
}
