package org.vnguyen.rachael.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IPod;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;

public class AppDetailResult extends TaskOutput<Map<String, List<IResource>>> {
	
	public AppDetailResult(Map<String, List<IResource>> data) {
		super(data);
	}
	
	@Override
	public List<String> transform() {
		List<String> buffer = new ArrayList<>();
		if (result.isEmpty()) {
			buffer.add("No app found");
			return buffer;
		}
		buffer.add("DeploymentConfigs: ");
		List<IResource> dcs = result.get(ResourceKind.DEPLOYMENT_CONFIG);
		for(IResource res : dcs) {
			buffer.add(" - " + res.getName());
		}
		
		buffer.add("Pods: ");
		List<IResource> pods = result.get(ResourceKind.POD);
		for(IResource res : pods) {
			IPod pod = (IPod)res;
			buffer.add(" - " + pod.getName() + " Status: " + pod.getStatus());
		}		
		
		
		buffer.add("Services: ");
		List<IResource> svcs = result.get(ResourceKind.SERVICE);
		for(IResource res : svcs) {
			IService svc = (IService)res;
			buffer.add(" - " + svc.getName() + ":" + svc.getTargetPort());
		}		

		buffer.add("Routes: ");
		List<IResource> routes = result.get(ResourceKind.ROUTE);
		for(IResource res : routes) {
			IRoute route = (IRoute)res;
			buffer.add(" - " + route.getURL() + " -> " + route.getServiceName());
		}
		return buffer;
	}
}
