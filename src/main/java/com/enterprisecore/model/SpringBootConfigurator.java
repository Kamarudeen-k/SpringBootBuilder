package com.enterprisecore.model;


import com.enterprisecore.interfaces.EnterpriseInjector;

public class SpringBootConfigurator {
	
	private EnterpriseInjector injector;
	private String injectorName;
	private String injectorPackage;
	private String injectorMethod;
	private Parameter methodParameters[];
	private String apiEndpoint;
	private Parameter apiParameters[];
	private String apiType;
	private String apiBody;
	private String response;
	private String classType;
	
	public EnterpriseInjector getInjector() {
		return injector;
	}
	
	public void setInjector(EnterpriseInjector injector) {
		this.injector = injector;
	}
	
	protected void setUpNewInjector() throws Exception {
		if(this.injectorName.isEmpty())
			throw new NullPointerException("Empty class name!");
		
		injector = (EnterpriseInjector)Class.forName(this.injectorPackage + "." + this.injectorName).newInstance();
		return;
	}
	
	public String getInjectorName() {
		return injectorName;
	}
	public void setInjectorName(String injectorName) throws Exception {
		this.injectorName = injectorName;
		
		this.setUpNewInjector();
	}
	public String getInjectorPackage() {
		return injectorPackage;
	}
	public void setInjectorPackage(String injectorPackage) {
		this.injectorPackage = injectorPackage;
	}
	public String getInjectorMethod() {
		return injectorMethod;
	}
	public void setInjectorMethod(String injectorMethod) {
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
	public String getApiBody() {
		return apiBody;
	}

	public void setApiBody(String apiBody) {
		this.apiBody = apiBody;
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
