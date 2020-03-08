package com.enterprisecore.model;

public class SpringBootApp {
	private String applicationName;
	private APIEndpoints apiEndpoints[];
	private Aspects aspects[];
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public APIEndpoints[] getApiEndpoints() {
		return apiEndpoints;
	}
	public void setApiEndpoints(APIEndpoints[] apiEndpoints) {
		this.apiEndpoints = apiEndpoints;
	}
	public Aspects[] getAspects() {
		return aspects;
	}
	public void setAspects(Aspects[] aspects) {
		this.aspects = aspects;
	}
	
}