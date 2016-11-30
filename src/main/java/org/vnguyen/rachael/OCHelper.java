package org.vnguyen.rachael;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.NotFoundException;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.capability.CapabilityVisitor;
import com.openshift.restclient.capability.resources.IDeployCapability;
import com.openshift.restclient.capability.resources.IProjectTemplateProcessing;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IList;
import com.openshift.restclient.model.IPod;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.IStatus;
import com.openshift.restclient.model.template.ITemplate;

@Component
public class OCHelper {
	
	@Inject
	private AppParams params;
	
	private IClient client;
	
	public OCHelper(AppParams params) {
		this.params = params;
		client = new ClientBuilder(params.openshiftEndpoint)
		.withUserName(params.apiUser)
		.withPassword(params.apiPassword)
		.build();
	}
	
	public IClient client() {
		return this.client;
	}
	
	/**
	 * Create a project
	 * @param name
	 * @return IProject
	 */
	public IProject newProject(String name) {
		IResource request = client.getResourceFactory().stub(ResourceKind.PROJECT_REQUEST, name);
		return (IProject)client.create(request);
	}
	
	/**
	 * Create a new instant app based on a template 
	 * <pre>
	 * {@code oc new-app --template <templateName> --namespace namespace}
	 * </pre>
	 * @param templateName
	 * @param targetNamespace 
	 */
	public  List<IResource> newAppByTemplateName(String templateName, String targetNamespace) {
		IProject project = getProject(targetNamespace);
		return newAppByTemplateName(templateName, project);
	}
	
	/**
	 * Create a new instant app based on a template. 
	 * @see OCHelper#newAppByTemplateName(String templateName, String targetNamespace)
	 * @param templateName
	 * @param targetProject 
	 */
	public List<IResource> newAppByTemplateName(final String templateName, IProject targetProject) {
		List<IResource> status = targetProject.accept(new CapabilityVisitor<IProjectTemplateProcessing, List<IResource>>() {

			@Override
			public List<IResource> visit(IProjectTemplateProcessing capability) {
				ITemplate original = getUniqueGlobalTemplate(templateName);
				original.addObjectLabel("ircbot", templateName);
				ITemplate processed = capability.process(original);
				Collection<IResource> resources = capability.apply(processed);
				return handleResponse(resources);
			}

		},
		null);

		if (status == null) {
			throw new RuntimeException("Operation not supported");
		}
		return status;
		
	}
	
	/**
	 * Get DeploymentConfig object in a project. 
	 * @param project
	 * @param dcName
	 * @return null if not found
	 */
	public IDeploymentConfig getDC(IProject project, String dcName) {
		try {
			return client.get(ResourceKind.DEPLOYMENT_CONFIG, dcName, project.getNamespace());
		} catch(NotFoundException e) {
			return null;
		}
	}
	
	
	public List<IDeploymentConfig> getAllDCs(String projectName) {
		// make sure project exists
		this.getProject(projectName);
		IList res = client.get(ResourceKind.DEPLOYMENT_CONFIG, projectName);
		List<IDeploymentConfig> dcs = res.getItems().stream()
						.map(c -> (IDeploymentConfig)c)
						.collect(Collectors.toList());
		return dcs;
	}

	public IDeploymentConfig deploy(String dcName, boolean latest, IProject targetProject) {
		IDeploymentConfig dc = this.getDC(targetProject, dcName);
		if (dc == null) {
			throw new RuntimeException("DeploymentConfig does not exist: " + dcName);
		}
		if (!latest) {
			
			throw new RuntimeException("Deployment exists. Version: " + dc.getLatestVersionNumber()); 
		}
		
		Object status = dc.accept(new CapabilityVisitor<IDeployCapability, Object>() {

			@Override
			public Object visit(IDeployCapability capability) {
				capability.deploy();
				return new Object();
			}

		}, null);
		
		if (status == null) {
			throw new RuntimeException("Operation not supported");
		}
		return dc;
	}
	
	public List<IPod> getPods(IProject project) {
		IList res = client.get(ResourceKind.POD, project.getName());
		List<IPod> pods = res.getItems().stream()
										.map(c -> (IPod)c)
										.collect(Collectors.toList());
		return pods;
										
	}
	
	/**
	 * Return all resources matching label
	 * @param project
	 * @param labels
	 * @return
	 */
	public Map<String, List<IResource>> getMultipleResources(IProject project, String[] resourcesKinds, Map<String, String> labels) {
		Map<String, List<IResource>> result = new HashMap<String, List<IResource>>();
		for(String kind: resourcesKinds) {
			if (ResourceKind.values().contains(kind)) {
				List<IResource> res =  client.list(kind, project.getName());
				result.put(kind, res);
			} else {
				//TODO log error
			}
		}
		
		return ImmutableMap.copyOf(result);
	}
	
	public IProject getProject(String name) {
		IList resources =  (IList)client.get(ResourceKind.PROJECT, null);
		List<Object> projects = resources.getItems().stream()
				.filter(c -> name.equals(((IResource) c).getName()))
				.collect(Collectors.toList());
		int size = projects.size();
		
		if (size==0) {
			throw new RuntimeException("Project " + name + " not found.  Please contact admin.");
		}
		if (size  !=1) {
			throw new RuntimeException("Expecting 1 project but found " + size);
		}
		return (IProject) projects.get(0);
	}
	
	protected ITemplate getUniqueGlobalTemplate(String name) {
		IList allTemplates = client.get(ResourceKind.TEMPLATE, "openshift");
		
		List<Object> l = allTemplates.getItems().stream()
				.filter(c -> ((ITemplate) c).isMatching(name))
				.collect(Collectors.toList());
		if (l.size() == 0) {
			throw new RuntimeException("Template not found: " + name);
		}
		return (ITemplate)l.get(0);
	}
	
	private List<IResource> handleResponse(Collection<IResource> resources) {
		String severity = IStatus.SUCCESS;
		for (IResource resource : resources) {
			if(resource.getKind() == ResourceKind.STATUS) {
				IStatus status = (IStatus) resource;
				throw new RuntimeException(status.getMessage());
			}
		}
		return new ArrayList<IResource>(resources);
}

}
