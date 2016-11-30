package org.vnguyen.rachael.cli;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;
import org.vnguyen.rachael.policy.SingleInstancePerTemplate;

import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;

/**
 * Simple implementation of {@code 'oc new-app --template <template> --namespace <namespace>} command.
 *  If {@code namespace} is omitted the caller userid is used instead<p>
 * Precondition: project already existed 
 */
@Service
public class NewAppTask extends AbstractBaseTask<List<IResource>> {
	public static final String VERB = "new-app";
	
	protected String templateName;
	
	public NewAppTask() {
		super(true);
		Option templateOption = Option.builder("t")
		         .longOpt("template")
		         .required(true)
		         .desc("Template name")
		         .hasArg()
		         .build();
		opts.addOption(templateOption);
	}
	
	@Override
	public String verb() {
		return VERB;
	}

	@Override
	public void parse(String userid, CommandLine cmdLine, String[] args) throws ParseException {
		templateName = cmdLine.getOptionValue('t');
	}
	
	@Override
	public TaskOutputIF<List<IResource>>call() throws Exception {
		IProject project = oc.getProject(projectName);
		
		// check policies
		if (policies.contains(SingleInstancePerTemplate.class)) {
			SingleInstancePerTemplate policy = new SingleInstancePerTemplate(oc)
														.project(project)
														.template(templateName);
			if (!policy.allowed()) {
				throw new RuntimeException(policy.getDescription());
			}
		}
		
		return new RouteRenderer(oc.newAppByTemplateName(templateName, project));
	}
}
