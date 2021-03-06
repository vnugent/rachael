package org.vnguyen.rachael.cli;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

@Component
public class CLI {
	private static Logger LOGGER = LoggerFactory.getLogger(CLI.class);
		
	@Inject
	private ApplicationContext applicationContext;
	
	protected CommandLineParser parser = new DefaultParser();
	
	protected Map<String, Task<?>> commands;
	
	public CLI() {
	}
	
	@PostConstruct
	public void init() {
		commands = ImmutableMap.<String, Task<?>>builder()
		 	.put(NewAppTask.VERB, applicationContext.getBean(NewAppTask.class))
		 	.put(GetAppTask.VERB, applicationContext.getBean(GetAppTask.class))
		 	.put(DeployTask.VERB, applicationContext.getBean(DeployTask.class))
		 	.put(ListAppTask.VERB, applicationContext.getBean(ListAppTask.class))
		 	.put(ListTemplateCmd.VERB, applicationContext.getBean(ListTemplateCmd.class))
		 	.put(HelpTask.VERB, applicationContext.getBean(HelpTask.class))
		 	.build();
	}
	
	public Map<String, Task<?>> commands() {
		return commands;
	}
		
	/**
	 * Execute an oc command
	 * @param args
	 * @return
	 * @throws ParseException
	 */
	public Task<?> findCommand(String userid, String[] args) throws ParseException {
		LOGGER.debug("uid: {}, args: {}", userid, args);
		if (args.length < 2) {
			throw new ParseException("Too few parameters");
		}
		Task<?> task = commands.get(args[1]);
		LOGGER.debug("uid: {}, found task: {}", userid, task);
		if (task != null) {
			task.processArgs(userid, parser, Arrays.copyOfRange(args, 2, args.length));
			return task;
		} else {
			throw new RuntimeException("Invalid verb: " + args[1]);
		}
	}
	
	
	
	
}
