package org.vnguyen.rachael.cli;

import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.template.ITemplate;

/**
 * Get app templates
 *
 */
@Service
public class ListTemplateCmd extends AbstractBaseTask<Map<String, List<IResource>>> {
	public static final String VERB = "list-template";

	protected boolean getAll;
	
	@Override
	public String verb() {
		return VERB;
	}
	
	public ListTemplateCmd() {
		super(true);
		Option allOption = Option.builder("a")
                .longOpt("all")
                .hasArg(false)
                .required(false)
                .desc("Return all templates including those are not tagged with instant-app")
                .build();
	
		opts.addOption(allOption);
	}

	@Override
	public void parse(String userid, CommandLine cmdLine, String[] arguments) throws ParseException {
		getAll = cmdLine.hasOption('a');
	}

	@Override
	public TaskOutputIF<Map<String, List<IResource>>> call() throws Exception {
		IProject p1 = oc.getProject(projectName);
		Map<String, String> labels = null; //ImmutableMap.of("tags", "instant-app");
		Map<String, List<IResource>> local = oc.getMultipleResources(p1, new String[]{ResourceKind.TEMPLATE}, labels);
		List<IResource> localTemplates = local.get(ResourceKind.TEMPLATE);
		
		IProject p2 = oc.getProject("openshift");
		Map<String, List<IResource>> global = oc.getMultipleResources(p2, new String[]{ResourceKind.TEMPLATE}, labels);
		List<IResource> globalTemplates = global.get(ResourceKind.TEMPLATE);

		return  new TemplatOutput(ImmutableMap.of("local", localTemplates,
							   "global", globalTemplates));
	}

}
