package com.enterprisecore.model;

public class APIEndpoints{
	
	private String uri;
	private String action;
	private String before;
	private String after;
	private String around;
	private String afterThrowing;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getBefore() {
		return before;
	}
	public void setBefore(String before) {
		this.before = before;
	}
	public String getAfter() {
		return after;
	}
	public void setAfter(String after) {
		this.after = after;
	}
	public String getAround() {
		return around;
	}
	public void setAround(String around) {
		this.around = around;
	}
	public String getAfterThrowing() {
		return afterThrowing;
	}
	public void setAfterThrowing(String afterThrowing) {
		this.afterThrowing = afterThrowing;
	}
	
}
