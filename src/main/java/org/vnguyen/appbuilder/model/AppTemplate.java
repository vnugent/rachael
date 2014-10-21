package org.vnguyen.appbuilder.model;

import java.io.File;

import org.apache.stratos.kubernetes.api.model.Pod;
import org.apache.stratos.kubernetes.api.model.Service;
import org.vnguyen.appbuilder.Utils;

public class AppTemplate {

	private Pod pod;
	private Service[] services;
	private AppMetadata metadata;
	
	public AppTemplate(Pod pod, Service[] services, AppMetadata metadata) {
		this.pod = pod;
		this.services = services;
		this.metadata = metadata;
	}
	
	public Pod getPod() { return pod; }
	public Service[] getServices() { return services; }
	public AppMetadata appMetadata() { return metadata; }
	
	public static AppTemplate from(AppMetadata metadata) {
		String dataDir = Utils.getEnvStrict("RACHAEL_DATA_DIR");
		Pod pod = Utils.load(Pod.class, new File(dataDir + "/" + metadata.getPod()));
		Service[] services = new Service[metadata.getServices().length];
		int i=0;
		for(String serviceFile : metadata.getServices()) {
			Service service = Utils.load(Service.class, new File(dataDir + "/" +serviceFile));
			services[i++] = service;
		}		
		return new AppTemplate(pod, services, metadata);
	}
}
