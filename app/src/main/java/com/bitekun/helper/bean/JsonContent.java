/**
 * 
 */
package com.bitekun.helper.bean;

import java.io.Serializable;

public class JsonContent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int status;

	private String paramz;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getParamz() {
		return paramz;
	}

	public void setParamz(String paramz) {
		this.paramz = paramz;
	}
}
