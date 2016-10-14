package org.vnguyen.rachael.cli;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;

import com.openshift.restclient.model.IDeploymentConfig;

@Service
public class ListAppTask extends AbstractBaseTask<List<IDeploymentConfig>> {
	public static final String VERB = "list-app";
	protected String projectName;
	
	public ListAppTask() {
		 Option namespaceOption = Option.builder("n")
				 	.longOpt("namespace")
				 	.required(false)
				 	.desc("Namespace")
				 	.hasArg()
				 	.build();
		 
		 opts.addOption(namespaceOption);
	}
	
	@Override
	public String verb() {
		return VERB;
	}

	@Override
	public void parse(String userid, CommandLineParser parser, String[] args) throws ParseException {
		CommandLine cli = parser.parse(opts, args);
		projectName = cli.getOptionValue('n');
		if (projectName == null) {
			projectName = userid;
		}
	}

	@Override
	public TaskOutputIF<List<IDeploymentConfig>> call() throws Exception {
		return new DCRenderer(oc.getAllDCs(projectName));
	}
}
