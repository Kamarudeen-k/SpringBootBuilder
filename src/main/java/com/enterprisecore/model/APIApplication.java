package com.enterprisecore.model;

import org.springframework.stereotype.Component;

@Component
public class APIApplication {
	
	private SpringBootApp springBoot[];
	private String organisationName;
	private Aspects aspects[];
	
	public APIApplication() {
		
	}
	
	public SpringBootApp[] getSpringBoot() {
		return springBoot;
	}

	public void setSpringBoot(SpringBootApp[] springBoot) {
		this.springBoot = springBoot;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public Aspects[] getAspects() {
		return aspects;
	}

	public void setAspects(Aspects[] aspects) {
		this.aspects = aspects;
	}
}
