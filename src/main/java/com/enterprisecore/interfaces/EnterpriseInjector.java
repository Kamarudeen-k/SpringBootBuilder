package com.enterprisecore.interfaces;

import com.enterprisecore.model.Parameter;

public interface EnterpriseInjector {
	
	public <T> T process(Parameter[] params, T response);

}
