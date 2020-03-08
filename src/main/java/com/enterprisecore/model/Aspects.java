package com.enterprisecore.model;

import org.springframework.stereotype.Component;

@Component
public class Aspects {
	
	private String name;
	private String before;
	private String after;
	private String afterReturning;
	private String around;
	private String afterThrowing;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getAfterReturning() {
		return afterReturning;
	}
	public void setAfterReturning(String afterReturning) {
		this.afterReturning = afterReturning;
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
