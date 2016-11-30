package org.vnguyen.rachael.cli;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vnguyen.rachael.OCHelper;
import org.vnguyen.rachael.policy.Policy;

import com.google.common.base.Objects;


public abstract class AbstractBaseTask<T> implements Task<T> {
	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Inject
	protected OCHelper oc;

	protected Set<Class <? extends Policy>> policies = new HashSet<>();

	protected final Options opts = new Options();
	protected final HelpFormatter formatter = new HelpFormatter();

	protected String projectName;
	
	/**
	 * Specify projectNameRequired=true if the backend requires project name
	 * for this operation
	 * @param projectNameRequired
	 */
	public AbstractBaseTask(boolean projectNameRequired) {
		 Option namespaceOption = Option.builder("n")
				 	.longOpt("namespace")
				 	.required(projectNameRequired)
				 	.desc("Namespace")
				 	.hasArg()
				 	.build();
		 
		 opts.addOption(namespaceOption);
	}

	@Override
	public void processArgs(String userid, CommandLineParser parser, String[] arguments) throws ParseException {
		CommandLine cli = parser.parse(opts, arguments);
		if (opts.getOption("namespace").isRequired()) {
			projectName = cli.getOptionValue('n');
			if (projectName == null) {
				projectName = userid;
			}
		}
		parse(userid, cli, arguments);
	}
	
	/**
	 * Template method for additional parsing of command options and arguments
	 * @param userid
	 * @param cmdLine
	 * @param arguments
	 * @throws ParseException
	 */
	protected void parse(String userid, CommandLine cmdLine, String[] arguments) throws ParseException {
		
	}
	
	@Override
	public Task<T> addPolicy(Class<? extends Policy> clz) {
		this.policies.add(clz);
		return this;
	}	
	
	@Override
	public StringWriter helpFormatter() {
		StringWriter sw = new StringWriter();

		formatter.printHelp(new PrintWriter(sw, true), 80, "oc " + verb(), null, opts, 4, 2, null);
		return sw;

	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("verb", this.verb())
				.toString();
	}
}
