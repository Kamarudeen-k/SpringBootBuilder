package com.enterprisecore.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.enterprisecore.interfaces.EnterpriseInjector;
import com.enterprisecore.model.APIApplication;
import com.enterprisecore.model.APIEndpoints;
import com.enterprisecore.model.Aspects;
import com.enterprisecore.model.Parameter;
import com.enterprisecore.model.SpringBootApp;
import com.enterprisecore.model.SpringBootConfigurator;

@Component
@Scope("prototype")
public class ConfigurationProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProcessor.class);
	
	public void setProcessQueue() {
		
	}
	
	public boolean generateSpringBootApps(APIApplication apiApp) {
		try {
			String basePath = System.getProperty("user.dir") + File.separator + "target-deliverables" + File.separator + apiApp.getOrganisationName();
			String filePath;
			SpringBootApp bootApps[] = apiApp.getSpringBoot();
			for(SpringBootApp bootApp: bootApps) {
				String orgName = apiApp.getOrganisationName();
				String appName = bootApp.getApplicationName();
				filePath = basePath + File.separator + appName + File.separator + getStringFirstCharUppercase(appName) + ".java";
				LOG.debug("Spring Boot App File: "+filePath);
				File file = new File(filePath);
				file.getParentFile().mkdirs();
				
				//CREATE SPRING BOOT APP FILE
				this.createSpringBootAppFile(bootApp, orgName, filePath);
				
				//CREATE CONTROLLER FILE
				this.createControllerFile(bootApp, orgName, basePath);
				
				//CREATE ASPECTS FILE
				this.createAspectFiles(bootApp, basePath, orgName, appName);
			}
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
			return false;
		}
		
		return true;
	}
	
	public void createControllerFile(SpringBootApp bootApp, String orgName, String basePath) {
		try {
			String filePath = basePath + File.separator + bootApp.getApplicationName() + File.separator + getStringFirstCharUppercase(bootApp.getApplicationName()) + "Controller.java";
			LOG.debug("Controller File: "+filePath);
			FileWriter fileWriter = new FileWriter(filePath);
			Set<String> fileImportHeader = new TreeSet<String>();
			List<String> fileData = new ArrayList<String>();
			
			fileImportHeader.add("import org.springframework.boot.RestController");
			
			APIEndpoints[] apiArray = bootApp.getApiEndpoints();
			
			for(APIEndpoints apiData: apiArray) {
				String methodRefs = (String)apiData.getUri();
				String action = (String)apiData.getAction();
				switch(action) {
				case "GET":
					fileData.add("@GetMapping(\"/"+methodRefs+"\")");
					fileImportHeader.add("import org.springframework.web.bind.GetMapping");
					break;
				case "POST":
					fileData.add("@PostMapping(\"/"+methodRefs+"\")");
					fileImportHeader.add("import org.springframework.web.bind.PostMapping");
					break;
				case "PUT":
					fileData.add("@PutMapping(\"/"+methodRefs+"\")");
					fileImportHeader.add("import org.springframework.web.bind.PutMapping");
					break;
				case "PATCH":
					fileData.add("@PatchMapping(\"/"+methodRefs+"\")");
					fileImportHeader.add("import org.springframework.web.bind.PatchMapping");
					break;
				case "DELETE":
					fileData.add("@DeleteMapping(\"/"+methodRefs+"\")");
					fileImportHeader.add("import org.springframework.web.bind.DeleteMapping");
					break;
				default:
					LOG.info("Incorrect http action: "+ action + "configured");
				}
				
			}
			
			//Add package
			fileWriter.write("package com." + orgName + "." + bootApp.getApplicationName()+ ";");
			fileWriter.append("\n");
			
			//Add import headers
			fileImportHeader.forEach(entry -> {
				try {
					fileWriter.append("\n" + entry + ";");
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
			});
			
			//Add file content		
			fileWriter.append("\n\n@RestController"); 
			fileWriter.append("\npublic class "+ this.getStringFirstCharUppercase(bootApp.getApplicationName()) +"Controller {"); 
			fileWriter.append("\n");
			
			ListIterator<String> lIter = fileData.listIterator();
			String methodName;
			String text;
			boolean nextAPI = true;
			while(lIter.hasNext()) {
				text = lIter.next();
				
				if(nextAPI && !text.contains("Mapping")) {
					fileWriter.append("\n");
					nextAPI = false;
				}
					
				fileWriter.append("\n\t" + text);
				
				methodName = "";
				int index = text.lastIndexOf("/");
				if(index >= 0 && text.contains("Mapping")) {
					int nextIndex = text.lastIndexOf(")");
					if(nextIndex > 0) {
						methodName = text.substring(index+1, nextIndex-1);
						methodName = this.getStringFirstCharUppercase(methodName);
					}
				}
				
				if(text.contains("GetMapping")) {
					text = "\n\tpublic ResponseEntity<?> get" + methodName + "(){";
					text += "\n\t\treturn ResponseEntity.OK().build();";
					text += "\n\t}";				
				}else
				if(text.contains("PostMapping")) {
					text = "\n\tpublic ResponseEntity<?> add" + methodName + "(){";
					text += "\n\t\treturn ResponseEntity.Created().build();";
					text += "\n\t}";				
				}else
				if(text.contains("PutMapping")) {
					text = "\n\tpublic ResponseEntity<?> addnUpdate" + methodName + "(){";
					text += "\n\t\treturn ResponseEntity.OK().build();";
					text += "\n\t}";				
				}else
				if(text.contains("PatchMapping")) {
					text = "\n\tpublic ResponseEntity<?> update" + methodName + "(){";
					text += "\n\t\treturn ResponseEntity.OK().build();";
					text += "\n\t}";				
				}else
				if(text.contains("DeleteMapping")) {
					text = "\n\tpublic ResponseEntity<?> delete" + methodName + "(){";
					text += "\n\t\treturn ResponseEntity.OK().build();";
					text += "\n\t}";				
				}
				//add Method signature
				if(text.contains("ResponseEntity")) {
					fileWriter.append(text);
					nextAPI = true;
				}
					
			}
			
			fileWriter.append("\n}");
			fileWriter.close();
			//clear file data collections memory
			fileImportHeader.clear();
			fileData.clear();
			
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
		}
	}
	
	public void createAspectFiles(SpringBootApp bootApp, String rootPath, String orgName, String applicationName) {		
		try {
			Aspects aspects[] = bootApp.getAspects();
			for(Aspects aspect: aspects) {
				String filePath = rootPath + File.separator + applicationName + File.separator + this.getStringFirstCharUppercase(aspect.getName()) + ".java";
				File file = new File(filePath);
				file.getParentFile().mkdirs();
				
				FileWriter fileWriter = new FileWriter(filePath);
				Set<String> fileImportHeader = new TreeSet<String>();
				List<String> fileData = new ArrayList<String>();
				
				fileImportHeader.add("import org.aspectj.lang.annotation.Aspect");
				String methodRefs = aspect.getBefore();
				if(!methodRefs.isEmpty()) {
					fileData.add("@Before(\"" + methodRefs + "\")");
					fileData.add("public void before" + aspect.getName() +"Aspect(){");
					fileData.add("}");
					fileImportHeader.add("import org.aspectj.lang.annonation.Before");
				}
				methodRefs = aspect.getAfter();
				if(!methodRefs.isEmpty()) {
					fileData.add("@After(\"" + methodRefs + "\")");
					fileData.add("public void after" + aspect.getName() +"Aspect(){");
					fileData.add("}");
					fileImportHeader.add("import org.aspectj.lang.annonation.After");
				}
				methodRefs = aspect.getAfterReturning();
				if(!methodRefs.isEmpty()) {
					fileData.add("@AfterReturning(\"" + methodRefs + "\")");
					fileData.add("public void afterReutring" + aspect.getName() +"Aspect(){");
					fileData.add("}");
					fileImportHeader.add("import org.aspectj.lang.annonation.AfterReturning");
				}
				methodRefs = aspect.getAround();
				if(!methodRefs.isEmpty()) {
					fileData.add("@Around(\"" + methodRefs + "\")");
					fileData.add("public void around" + aspect.getName() +"Aspect(){");
					fileData.add("}");
					fileImportHeader.add("import org.aspectj.lang.annonation.Around");
				}
				methodRefs = aspect.getAfterThrowing();
				if(!methodRefs.isEmpty()) {
					fileData.add("@AfterThrowing(\"" + methodRefs + "\")");
					fileData.add("public void afterThrowing" + aspect.getName() +"Aspect(){");
					fileData.add("}");
					fileImportHeader.add("import org.aspectj.lang.annonation.AfterThrowing");
				}
				//start writing the Aspect file
				fileWriter.append("package com." + orgName + "." + applicationName + ";");
				fileWriter.append("\n");
				//write Aspect import headers
				fileImportHeader.forEach(entry -> {
					try {
						fileWriter.append("\n" + entry + ";");
					} catch (IOException e) {
						LOG.error(e.getMessage());;					
					}
				});
				
				//write Aspect class
				fileWriter.append("\n\n@Aspect");
				fileWriter.append("\npublic class "+ this.getStringFirstCharUppercase(aspect.getName()) + "{");
				fileWriter.append("\n");
				
				//write Aspect methods
				fileData.forEach(data -> {
					try {
						fileWriter.append("\n\t" + data);
					} catch (IOException e) {
						LOG.error(e.getMessage());
					}
				});
				fileWriter.append("\n\n}");
				fileWriter.close();
				fileImportHeader.clear();
				fileData.clear();
			}
				
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
		}
		return;
	}
	
	public void createSpringBootAppFile(SpringBootApp bootApp, String orgName, String filePath) {
		
		try(FileWriter fileWriter = new FileWriter(filePath)) {
						
			String fileContent = "package com." + orgName + "." + bootApp.getApplicationName() + ";" +
			"\n\nimport org.springframework.boot.SpringApplication;" + 
			"\nimport org.springframework.boot.autoconfigure.SpringBootApplication;" +  
			"\n\n@SpringBootApplication" + 
			"\npublic class "+ this.getStringFirstCharUppercase(bootApp.getApplicationName())+"Application {" +  
			"\n\n\tpublic static void main(String[] args) {" + 
			"\n\t\tSpringApplication.run(" + this.getStringFirstCharUppercase(bootApp.getApplicationName())+"Application.class, args);" + 
			"\n\t}" +  
			"\n\n}";
			fileWriter.write(fileContent);
			
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
		}
		return;
	}
	
	public String getStringFirstCharUppercase(String className) throws Exception {
		if(className.isEmpty())
			throw new NullPointerException();
		
		String stdClassName = className.charAt(0) +"";
		stdClassName = stdClassName.toUpperCase() + className.substring(1);
		
		return  stdClassName;
	}

	public Optional<?> configureSpringBootAPI(SpringBootConfigurator configs) {
		try {
			RestTemplate template = new RestTemplate();
			String response = null;
			Map<String, String> params = new HashMap<String, String>();
			for(Parameter param: configs.getApiParameters()) {
				params.put(param.getName(), param.getValue());
			}
			switch(configs.getApiType()) {
			case "GET":
				response = template.getForObject(configs.getApiEndpoint(), String.class, params);
				break;
			}
			
			EnterpriseInjector client = configs.getInjector();
			Optional<?> computedResponse = client.process(configs.getApiParameters(), response);
			return computedResponse;
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
		}
		return null;
	}
	
	
}
