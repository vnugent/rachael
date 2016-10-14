package org.vnguyen.rachael.cli;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IProject;

@Service
public class DeployTask extends AbstractBaseTask<List<IDeploymentConfig>> {
	public static final String VERB = "deploy";

	protected String projectName;
	protected boolean deployNow = false;
	protected String dcName;
	
	public DeployTask() {
		 Option latestOption = Option.builder("l")
                 .longOpt("latest")
                 .required(false)
                 .desc("Start a new deployment now")
                 .build();
		 
		 Option namespaceOption = Option.builder("n")
				 	.longOpt("namespace")
				 	.required(false)
				 	.desc("Namespace")
				 	.hasArg()
				 	.build();
		 opts.addOption(latestOption)
		 	 .addOption(namespaceOption);
	}
	
	@Override
	public String verb() {
		return VERB;
	}

	@Override
	public void parse(String userid, CommandLineParser parser, String[] arguments) throws ParseException {
		CommandLine cli = parser.parse(opts, arguments);
		deployNow = cli.hasOption("l");
		if (arguments.length < 1) {
			throw new ParseException("Missing deployment config name");
		}
		projectName = cli.getOptionValue('n');
		if (projectName == null) {
			projectName = userid;
		}
		dcName = arguments[0];
	}


	@Override
	public TaskOutputIF<List<IDeploymentConfig>> call() throws Exception {
		IProject project = oc.getProject(projectName);
		IDeploymentConfig dc = oc.deploy(dcName, deployNow, project);
		return new DCRenderer(ImmutableList.of(dc));
	}
}
