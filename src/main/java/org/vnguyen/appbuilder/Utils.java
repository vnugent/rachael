package org.vnguyen.appbuilder;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	
	public static <T> T load(Class<T> clz, File jsonFile) {
	    ObjectMapper mapper = new ObjectMapper();  

	    try {
			return mapper.readValue(jsonFile, clz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 	
	}

	public static String getEnvStrict(String name) {
		String value = System.getenv(name);
		if (value == null) {
			throw new RuntimeException(name + " env variable not defined");
		}
		return value;
	}
	
	public static String getEnvWithDefault(String name, String defaultVal) {
		String value = System.getenv(name);
		return value == null ? defaultVal : value;
	}
	
}
