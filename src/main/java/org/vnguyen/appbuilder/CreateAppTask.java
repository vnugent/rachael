package org.vnguyen.appbuilder;

import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.apache.stratos.kubernetes.api.model.Pod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vnguyen.appbuilder.irc.KubernetesCommandHandler;
import org.vnguyen.appbuilder.model.AppTemplate;
import org.vnguyen.appbuilder.model.ServiceEndpoint;

public class CreateAppTask implements Callable<ServiceEndpoint> {
	private Logger log = LoggerFactory.getLogger(CreateAppTask.class);

	private String owner;
	private String appName;
	private Commands cmds;
	

	public CreateAppTask(String owner, String appName, Commands cmds) {
		this.owner = owner;
		this.appName = appName;
		this.cmds = cmds;
	}


	@Override
	public ServiceEndpoint call() throws Exception {
		AppTemplate appTemplate = cmds.createApp(owner, appName);
		ServiceEndpoint endpoint = new ServiceEndpoint();
		long timer = 0;
		final long maxWait = 300; 
		//TODO parameterize maxWait
		while(true) {
			Pod pod = cmds.appContext().api().getPod(appTemplate.getPod().getId());
			String hostIP = StringUtils.trimToNull(pod.getCurrentState().getHost());
			log.debug("Polling pod: {}, ip: {}", pod, hostIP);
			if (hostIP != null) {
				endpoint.ofPod(pod);
				break;
			}
			if (timer >= maxWait) {
				throw new RuntimeException("Get pod info has timed out");
			}
			timer = timer + 500;
			Thread.sleep(500);
		}
		
		return endpoint.ofServices(appTemplate.getServices());
	}
}