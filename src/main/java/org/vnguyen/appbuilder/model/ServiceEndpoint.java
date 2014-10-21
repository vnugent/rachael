package org.vnguyen.appbuilder.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.stratos.kubernetes.api.model.Pod;
import org.apache.stratos.kubernetes.api.model.Service;

public class ServiceEndpoint {
	private Pod pod;
	private Service[] services;
	
	public ServiceEndpoint ofPod(Pod pod) {
		this.pod = pod;
		return this;
	}
	
	public ServiceEndpoint ofServices(Service[] services) {
		this.services = services;
		return this;
	}
	
	public Pod pod() { return pod; }
	
	public String hostIP() {
		return pod.getCurrentState().getHost();
	}
	
	public Map<String, Long> port() {
		List<Service> list = Arrays.asList(services);

		Map<String, Long> endpoints = new HashMap<String, Long>();
		for(Service service : list) {
			endpoints.put(service.getContainerPort(), new Long(service.getPort()));
		}
		return endpoints;
	}
	
	@Override
	public int hashCode() {
		 return new HashCodeBuilder(17, 37).
			       append(pod.hashCode()).
			       append(services.hashCode()).
			       toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
						.append(hostIP())
						.append(port()).toString();
	}
	
	
}
