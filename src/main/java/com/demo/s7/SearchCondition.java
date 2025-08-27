package com.demo.s7;

import java.util.List;

public class SearchCondition {

	private String fd;
	private String op;
	private List<Object> vals;

	public String getFd() {
		return fd;
	}

	public void setFd(String fd) {
		this.fd = fd;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public List<Object> getVals() {
		return vals;
	}

	public void setVals(List<Object> vals) {
		this.vals = vals;
	}

}
