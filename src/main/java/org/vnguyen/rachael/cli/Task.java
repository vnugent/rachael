package org.vnguyen.rachael.cli;

import java.io.StringWriter;
import java.util.concurrent.Callable;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.vnguyen.rachael.policy.Policy;

public interface Task<T> extends Callable<TaskOutputIF<T>> {
	String verb();
	
	void processArgs(String userid, CommandLineParser parser, String[] arguments) throws ParseException;
	
	Task<T> addPolicy(Class <? extends Policy> clz);
	StringWriter helpFormatter();
}
