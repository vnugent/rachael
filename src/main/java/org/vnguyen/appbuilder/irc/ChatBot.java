package org.vnguyen.appbuilder.irc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vnguyen.appbuilder.Utils;

import com.google.common.collect.ImmutableList;

public class ChatBot {
	private static Logger log = LoggerFactory.getLogger(ChatBot.class);

	private List<String> welcomeMessage;
	
	public ChatBot() {
		//AppContext mainAppBuilder = new AppContext();
	//	OpenShiftCommandHandler cmdHandler= new OpenShiftCommandHandler(this, mainAppBuilder);
		Builder configBuilder = parseParams();
		//configBuilder.addListener(cmdHandler);
    	PircBotX myBot = new PircBotX(configBuilder.buildConfiguration());
    	try {
    		myBot.startBot();
    	} catch (Exception e) {
   			e.printStackTrace(); 		
    	}	
    	finally {
    		myBot.close();
    	}
	}
	
	public Builder parseParams() {
		
		String welcomeFile = System.getenv("SAAS_WELCOME_FILE");
		welcomeMessage = (welcomeFile == null) ? defaultWelcomeMessage() : loadWelcomeFile(welcomeFile);
		
		String ircServer = System.getenv("SAAS_IRC_SERVER");
		if (ircServer == null) {
			throw new RuntimeException("Missing SAAS_IRC_SERVER env variable");
		}
		
		String ircChannelCSV = System.getenv("SAAS_IRC_CHANNELS");
		if (ircChannelCSV == null) {
			throw new RuntimeException("Missing SAAS_IRC_CHANNELS env variable");
		}
		String[] channels = ircChannelCSV.split(",");
		
		Builder builder = new Configuration.Builder();
		
		builder.setName(Utils.getEnvWithDefault("SAAS_IRCBOT_NIC", "saas-bot"))
			.setAutoNickChange(true)
			.addServer(ircServer, 6667);
			
	    for(String channel : channels) {
	   		builder.addAutoJoinChannel(channel);
	   	}
	    return builder;	
	}
	
	public List<String> loadWelcomeFile(String fileName) {
		try {
			FileInputStream is = new FileInputStream(fileName);
			return IOUtils.readLines(is);
		} catch (IOException e) {
			log.error("Unable to load welcome file '{}'.  Using default text", fileName);
		}
		return defaultWelcomeMessage();
	}
	
	protected List<String> defaultWelcomeMessage() {
		return ImmutableList.of("Welcome to PaaS chatbot!");
	}
	
	public List<String> welcomeMessage() { 
		return this.welcomeMessage; 
	}
	
	public static void main(String[] args) {
		new ChatBot();
	}
}
