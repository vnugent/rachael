package org.vnguyen.rachael.cli;

import java.util.List;

import org.springframework.stereotype.Service;

import com.openshift.restclient.model.IDeploymentConfig;

@Service
public class ListAppTask extends AbstractBaseTask<List<IDeploymentConfig>> {
	public static final String VERB = "list-app";
	
	public ListAppTask() {
		super(true);
	}
	
	@Override
	public String verb() {
		return VERB;
	}

	@Override
	public TaskOutputIF<List<IDeploymentConfig>> call() throws Exception {
		return new DCRenderer(oc.getAllDCs(projectName));
	}
}
