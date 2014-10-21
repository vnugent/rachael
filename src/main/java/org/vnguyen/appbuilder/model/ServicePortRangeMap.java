package org.vnguyen.appbuilder.model;

import java.io.File;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.vnguyen.appbuilder.Utils;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ServicePortRangeMap {
	@JsonProperty
	public Map<String, ServicePortRange> portMap;
	
	
	public ServicePortRange lookup(String appId) {
		if (portMap.containsKey(appId)) {
			return portMap.get(appId);
		}
		return null;
	}
	
	public static ServicePortRangeMap fromJsonFile(File jsonFile) {
		return Utils.load(ServicePortRangeMap.class, jsonFile);
	}
}
