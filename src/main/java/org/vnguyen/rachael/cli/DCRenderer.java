package org.vnguyen.rachael.cli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IDeploymentConfig;

public class DCRenderer extends TaskOutput<List<IDeploymentConfig>> {

	public DCRenderer(List<IDeploymentConfig> result) {
		super(result);
	}

	@Override
	public List<String> transform() {
		List<String> buffer = new ArrayList<>();

		buffer.add("DeploymentConfigs:");
		for(IDeploymentConfig dc : result) {
			buffer.add(" > " + dc.getName() + " : version " + dc.getLatestVersionNumber() + ", created " + dc.getCreationTimeStamp());
			Collection<IContainer> containers = dc.getContainers();
			for(IContainer container : containers) {
				buffer.add("   - " + container.getImage());
			}
		}
		return buffer;
	}
}
