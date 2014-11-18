package org.vnguyen.appbuilder;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.stratos.kubernetes.api.client.v2.KubernetesApiClient;
import org.apache.stratos.kubernetes.api.exceptions.KubernetesClientException;
import org.apache.stratos.kubernetes.api.model.Pod;
import org.apache.stratos.kubernetes.api.model.Selector;
import org.apache.stratos.kubernetes.api.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vnguyen.appbuilder.irc.KubernetesCommandHandler;
import org.vnguyen.appbuilder.model.AppMetadata;
import org.vnguyen.appbuilder.model.AppTemplate;

public class AppBuilder {
	private Logger log = LoggerFactory.getLogger(AppBuilder.class);

    private KubernetesApiClient client;
    
    private PortManagerForServices portManager;
    private IDGenerator idGenerator = BasicIDGenerators.DEFAULT_POD_ID_GENERATOR;
    
    public AppBuilder setApiClient(KubernetesApiClient client) {
    	this.client = client;
    	return this;
    }
    
    public AppBuilder setPortManager(PortManagerForServices portMgr) {
    	this.portManager = portMgr;
    	return this;
    }
    
    public AppBuilder setIDGenerator(IDGenerator idGen) {
    	this.idGenerator = idGen;
    	return this;
    }
    
    public void from(String ownerId, AppTemplate appTemplate) throws Exception {

    	final String podId = idGenerator.generate(ownerId, appTemplate.appMetadata());
    	
    	Pod pod = appTemplate.getPod();
    	pod.setId(podId);
    	pod.getLabels().setName(podId);
    	client.createPod(pod);
    	int retryAttempt = 0;
    	for(Service service : appTemplate.getServices()) {
    		try {
				int[] freePorts = portManager.getAvailablePorts(appTemplate.appMetadata().getType(), 1);
	    		service.setPort(freePorts[0]);
	    		service.setId(podId + "-p" + service.getContainerPort());
	    		Selector selector = new Selector();
	    		selector.setName(podId);
	    		service.setSelector(selector);
	    		log.debug("Creating service: {}", service);
	        	client.createService(service);				
			} catch (IllegalArgumentException e1) {
				throw new Exception(e1);
			} catch (Exception e2) {
				log.warn("Attempt #{} Error while creating service: {}", retryAttempt, service);
				log.warn(e2.getMessage());
				e2.printStackTrace();
				if (retryAttempt >= 5) {
					throw new Exception(e2);
				}
			}

    	}
    	
    	   	
    }
    
    
    public AppMetadata fromJSON(String ownerId, File jsonFile) throws Exception {
    	
    
    	AppMetadata app = AppMetadata.fromJSONFile(jsonFile);

    	return app;
    }

}
