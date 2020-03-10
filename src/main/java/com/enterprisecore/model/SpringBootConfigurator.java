package com.enterprisecore.model;

import java.util.Optional;

import com.enterprisecore.interfaces.EnterpriseInjector;

public class SpringBootConfigurator {
	
	private EnterpriseInjector injector;
	private Optional<?> injectorMethod;
	private Parameter methodParameters[];
	private String apiEndpoint;
	private Parameter apiParameters[];
	private String apiType;
	private String response;
	private String classType;
	
	public EnterpriseInjector getInjector() {
		return injector;
	}
	public void setInjector(EnterpriseInjector injector) {
		this.injector = injector;
	}
	public Optional<?> getInjectorMethod() {
		return injectorMethod;
	}
	public void setInjectorMethod(Optional<?> injectorMethod) {
		this.injectorMethod = injectorMethod;
	}
	public Parameter[] getMethodParameters() {
		return methodParameters;
	}
	public void setMethodParameters(Parameter[] methodParameters) {
		this.methodParameters = methodParameters;
	}
	public String getApiEndpoint() {
		return apiEndpoint;
	}
	public void setApiEndpoint(String apiEndpoint) {
		this.apiEndpoint = apiEndpoint;
	}
	public Parameter[] getApiParameters() {
		return apiParameters;
	}
	public void setApiParameters(Parameter[] apiParameters) {
		this.apiParameters = apiParameters;
	}
	public String getApiType() {
		return apiType;
	}
	public void setApiType(String apiType) {
		this.apiType = apiType;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	
}
