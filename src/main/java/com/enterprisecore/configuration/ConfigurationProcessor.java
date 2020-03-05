package com.enterprisecore.configuration;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enterprisecore.model.APIApplication;
import com.enterprisecore.model.APIEndpoints;
import com.enterprisecore.model.SpringBootApp;

@Component
@Scope("prototype")
public class ConfigurationProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProcessor.class);
	
	public void setProcessQueue() {
		
	}
	
	public boolean generateSpringBootApp(APIApplication apiApp) {
			
		try {
			FileWriter fileWriter = null;
			
			String basePath = System.getProperty("user.dir") + File.separator + apiApp.getOrganisationName();
			String filePath;
			SpringBootApp bootApp[] = apiApp.getSpringBoot();
			for(int i=0; i<bootApp.length; i++) {
				//CREATE SPRING BOOT APP FILE
				filePath = basePath + File.separator + getStringFirstCharUppercase(bootApp[i].getApplicationName()) + ".java";
				LOG.debug("Spring Boot App File: "+filePath);
				File file = new File(filePath);
				file.getParentFile().mkdirs();
				createSpringBootAppFile(bootApp[i], apiApp.getOrganisationName(), filePath);
				
				//CREATE CONTROLLER FILE
				filePath = basePath + File.separator + getStringFirstCharUppercase(bootApp[i].getApplicationName()) + "Controller.java";
				LOG.debug("Controller File: "+filePath);
				fileWriter = new FileWriter(filePath);
				Set<String> fileImportHeader = new TreeSet<String>();
				List<String> fileData = new ArrayList<String>();
				
				fileImportHeader.add("import org.springframework.boot.RestController");
				
				APIEndpoints[] apiArray = bootApp[i].getApiEndpoints();
				
				for(int j=0; j<apiArray.length; j++) {
					APIEndpoints apiData = apiArray[j];
					String methodRefs = (String)apiData.getBefore();
					if(!methodRefs.isEmpty()) {
						fileData.add("@Before(\"" + methodRefs + "\")");
						fileImportHeader.add("import org.aspectj.lang.annonation.Before");
					}
					methodRefs = (String)apiData.getAfter();
					if(!methodRefs.isEmpty()) {
						fileData.add("@After(\"" + methodRefs + "\")");
						fileImportHeader.add("import org.aspectj.lang.annonation.After");
					}
					methodRefs = (String)apiData.getAround();
					if(!methodRefs.isEmpty()) {
						fileData.add("@Around(\"" + methodRefs + "\")");
						fileImportHeader.add("import org.aspectj.lang.annonation.Around");
					}
					methodRefs = (String)apiData.getAfterThrowing();
					if(!methodRefs.isEmpty()) {
						fileData.add("@AfterThrowing(\"" + methodRefs + "\")");
						fileImportHeader.add("import org.aspectj.lang.annonation.AfterThrowing");
					}
					
					methodRefs = (String)apiData.getUri();
					String action = (String)apiData.getAction();
					switch(action) {
					case "GET":
						fileData.add("@GetMapping(\""+methodRefs+"\")");
						fileImportHeader.add("import org.springframework.web.bind.GetMapping");
						break;
					case "POST":
						fileData.add("@PostMapping(\""+methodRefs+"\")");
						fileImportHeader.add("import org.springframework.web.bind.PostMapping");
						break;
					case "PUT":
						fileData.add("@PutMapping(\""+methodRefs+"\")");
						fileImportHeader.add("import org.springframework.web.bind.PutMapping");
						break;
					case "PATCH":
						fileData.add("@PatchMapping(\""+methodRefs+"\")");
						fileImportHeader.add("import org.springframework.web.bind.PatchMapping");
						break;
					default:
						LOG.info("Incorrect http action: "+ action + "configured");
					}
					
				}
				
				//Add package
				fileWriter.write("package com." + apiApp.getOrganisationName() + "." + bootApp[i].getApplicationName()+ ";");
				fileWriter.append("\n");
				
				//Add import headers
				Iterator lIter = fileImportHeader.iterator();
				String text;
				while(lIter.hasNext()) {
					text = (String)lIter.next() + ";";
					fileWriter.append("\n"+text);
				}
				
				//Add file content		
				fileWriter.append("\n\n@RestController"); 
				fileWriter.append("\npublic class "+ this.getStringFirstCharUppercase(bootApp[i].getApplicationName()) +"Controller {"); 
				
				lIter = fileData.iterator();
				String methodName;
				while(lIter.hasNext()) {
					text = (String)lIter.next();
					
					if(!text.contains("Mapping"))
						fileWriter.append("\n");
					fileWriter.append("\n\t" + text);
					
					methodName = "";
					int index = text.lastIndexOf("(");
					if(index >= 0 && text.contains("Mapping")) {
						int nextIndex = text.lastIndexOf(")");
						if(nextIndex > 0) {
							methodName = text.substring(index+2, nextIndex-1);
							methodName = this.getStringFirstCharUppercase(methodName);
						}
					}
					
					if(text.contains("GetMapping")) {
						text = "\n\tpublic ResponseEntity<T> get" + methodName + "(){";
						text += "\n\t\treturn ResponseEntity.OK().build();";
						text += "\n\t}";				
					}
					if(text.contains("PostMapping")) {
						text = "\n\tpublic ResponseEntity<T> add" + methodName + "(){";
						text += "\n\t\treturn ResponseEntity.Created().build();";
						text += "\n\t}";				
					}
					if(text.contains("PutMapping")) {
						text = "\n\tpublic ResponseEntity<T> addnUpdate" + methodName + "(){";
						text += "\n\t\treturn ResponseEntity.OK().build();";
						text += "\n\t}";				
					}
					if(text.contains("PatchMapping")) {
						text = "\n\tpublic ResponseEntity<T> update" + methodName + "(){";
						text += "\n\t\treturn ResponseEntity.OK().build();";
						text += "\n\t}";				
					}
					//add Method signature
					if(text.contains("ResponseEntity"))
						fileWriter.append(text);
				}
				
				fileWriter.append("\n}");
				fileWriter.close();
				//clear file data collections memory
				fileImportHeader.clear();
				fileData.clear();
			}
			
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
			return false;
		}
		
		return true;
	}
	
	private void createSpringBootAppFile(SpringBootApp bootApp, String orgName, String filePath) throws Exception {
		
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
			throw ex;
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
	
	
}
