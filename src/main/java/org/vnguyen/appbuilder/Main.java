package org.vnguyen.appbuilder;

import org.apache.stratos.kubernetes.api.client.v2.KubernetesApiClient;
import org.apache.stratos.kubernetes.api.exceptions.KubernetesClientException;

/**
 * Entry point for Rachael app
 *
 */
public class Main {
	private KubernetesApiClient kubernetesClient ;
	private PortManagerForServices portManager;
	private AppBuilder appBuilder;
	private Commands commands;
	final private String dataDir;
	
	public Main() {		
    	String apiEndpoint = Utils.getEnvStrict("KUBERNETES_API_ENDPOINT");
    	dataDir = Utils.getEnvStrict("RACHAEL_DATA_DIR");
		kubernetesClient = new  KubernetesApiClient(apiEndpoint);
		portManager = new PortManagerForServices(kubernetesClient);
		appBuilder = new AppBuilder();
		appBuilder.setApiClient(kubernetesClient)
					.setPortManager(portManager);
		
		commands = new Commands();
		commands.setAppContext(this);
		preflightChecks();
	}
	
	public void preflightChecks() {
		try {
			kubernetesClient.getAllPods();
		} catch (KubernetesClientException e) {
			throw new RuntimeException("");
		}
	
	}
	
	public String dataDir() { return this.dataDir; }
	
	public KubernetesApiClient api() {
		return kubernetesClient;
	}
	
	public Commands cmds() {
		return commands;
	}
	
	public AppBuilder appBuilder() {
		return appBuilder;
	}
	
    public static void main( String[] args )    {
    	
    }
}
