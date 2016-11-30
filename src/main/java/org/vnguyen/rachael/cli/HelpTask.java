package org.vnguyen.rachael.cli;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HelpTask extends AbstractBaseTask<String>  {
    private static Logger LOGGER = LoggerFactory.getLogger(HelpTask.class);

	public static final String VERB = "help";

	@Inject
	protected CLI cli;
	
	protected Task<?> helpContext;
	
	public HelpTask() {
		super(false);
	}
	
	@Override
	public String verb() {
		return VERB;
	}

	@Override
	public void parse(String userid, CommandLine cmdLine,
			String[] arguments) throws ParseException {
	    String helpKeyword =(arguments.length == 0)? "" : arguments[0]; 
	    helpContext = cli.commands.get(helpKeyword);
	    LOGGER.debug("Help context: {}", helpContext);
	    if (helpContext == null) {
	        throw new RuntimeException("Missing or invalid help keyword: " + helpKeyword + "'.  Valid keywords " + cli.commands().keySet());
	    }	
	}

	@Override
	public TaskOutputIF<String> call() throws Exception {
	    final String helpText = helpContext.helpFormatter().toString();
		return new TaskOutputIF<String>(){

		    @Override
		    public List<String> transform() {
		    	return Arrays.asList(helpText.split("\\n"));
		    }

		    @Override
		    public String rawResult() {
		    	return helpText;
		    }};
	}
}
