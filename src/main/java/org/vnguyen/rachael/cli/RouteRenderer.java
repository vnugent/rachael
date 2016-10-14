package org.vnguyen.rachael.cli;

import java.util.ArrayList;
import java.util.List;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.route.IRoute;


/**
 *  
 *
 */
public class RouteRenderer extends TaskOutput<List<IResource>> {

	public RouteRenderer(List<IResource> result) {
		super(result);
	}

	@Override
	public List<String> transform() {
		List<String> buffer = new ArrayList<>();

		buffer.add("Public endpoint(s):");
		for(IResource resource : result) {
			if (resource.getKind().equals(ResourceKind.ROUTE)) {
				IRoute route = (IRoute)resource;
				buffer.add(" > " + route.getURL() + " -> " + route.getServiceName());
			}			
		}
		return buffer;
	}
}
