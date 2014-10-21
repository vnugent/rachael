package org.vnguyen.appbuilder.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ServicePortRange {
	
	@JsonProperty
	public int min;
	
	@JsonProperty
	public int max;
}
