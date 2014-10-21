package org.vnguyen.appbuilder.model;

import java.io.File;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.vnguyen.appbuilder.Utils;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AppMetadata {

	@JsonProperty
	private String type;
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private String pod;
	
	@JsonProperty("services")
	private String[] services;
	
	public void setType(String type) { this.type = type; }
	public String getType() { return type; }
	
	public void setPod(String pod) { this.pod = pod; }
	public String getPod() { return pod; }
	
	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	
	public void String(String[] services) { this.services = services; }
	public String[] getServices() { return services; }
	
	public static AppMetadata fromJSONFile(File jsonFile) {
		return Utils.load(AppMetadata.class, jsonFile);
	}

}
