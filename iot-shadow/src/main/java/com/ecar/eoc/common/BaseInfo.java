package com.ecar.eoc.common;

import java.io.Serializable;

/**
 * 本项目，所有rest的返回结果
 * @author pom
 *
 * @param <T>
 */
public class BaseInfo<T> implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private T data; // 请求的数据
	private String errorCode; // 消息码
	private String success; // 是否成功
	private String errorMessage; // 消息信息

	public BaseInfo() {
		this.success = "true";
	}

	public BaseInfo(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("BaseInfo >> : ");
		sb.append("[\n");
		if (data != null && !"".equals(data)) {
			sb.append("data : " + data.toString() + ",\n");
		}
		if (errorCode != null && !"".equals(errorCode)) {
			sb.append("errorCode : " + errorCode + ",\n");
		}
		if (errorMessage != null && !"".equals(errorMessage)) {
			sb.append("errorMessage : " + errorMessage + ",\n");
		}
		if (success != null && !"".equals(success)) {
			sb.append("success : " + success + ",\n");
		}
		sb.append("]");
		return sb.toString();
	}
}
