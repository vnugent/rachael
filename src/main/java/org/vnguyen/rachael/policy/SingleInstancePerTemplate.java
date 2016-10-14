package org.vnguyen.rachael.policy;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.vnguyen.rachael.OCHelper;

import com.openshift.restclient.model.IProject;



public class SingleInstancePerTemplate implements Policy {

	private OCHelper oc;
	
	private IProject project;
	private String templateName;
	
	public SingleInstancePerTemplate(OCHelper oc) {
		this.oc = oc;
	}
	
	public SingleInstancePerTemplate project(IProject project) {
		this.project = project;
		return this;
	}
	
	public SingleInstancePerTemplate template(String templateName) {
		this.templateName = templateName;
		return this;
	}
	
	public boolean allowed() {
		if (oc.getDC(project, templateName) == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getDescription() {
		return "Only one instance per template is allowed";
	}

}
