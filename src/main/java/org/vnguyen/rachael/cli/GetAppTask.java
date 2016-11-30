package org.vnguyen.rachael.cli;

import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;

@Service
public class GetAppTask extends AbstractBaseTask<Map<String, List<IResource>>> {
	public static final String VERB = "get-app";
	public static final String HELP_STRING = "oc get-app <deployment config name> --namespace <your namespace>";

	protected String dcName;

	public GetAppTask() {
		super(true);
	}

	@Override
	public String verb() {
		return VERB;
	}

	@Override
	public void parse(String userid, CommandLine cmdLine, String[] arguments) throws ParseException {
		if (arguments.length == 0) {
			throw new RuntimeException("Missing app template");
		}

		dcName = arguments[0];
		LOGGER.debug("userid={}, projectName={}, args={}", userid, projectName, arguments);
	}


	@Override
	public TaskOutputIF<Map<String, List<IResource>>> call() throws Exception {
		IProject project = oc.getProject(projectName);
		
		String[] types = new String[] {ResourceKind.DEPLOYMENT_CONFIG, ResourceKind.SERVICE, ResourceKind.ROUTE};
		Map<String, List<IResource>> res1 = oc.getMultipleResources(project, types, ImmutableMap.of("template", dcName));
		types = new String[] {ResourceKind.POD};
		Map<String, List<IResource>> podRes = oc.getMultipleResources(project, types, ImmutableMap.of("deploymentconfig", dcName));
		
		return new AppDetailResult(ImmutableMap.<String, List<IResource>>builder()
									.putAll(res1)
									.putAll(podRes)
									.build());
	}
}
