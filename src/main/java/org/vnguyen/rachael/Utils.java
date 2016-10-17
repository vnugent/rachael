package org.vnguyen.rachael;

public class Utils {
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
