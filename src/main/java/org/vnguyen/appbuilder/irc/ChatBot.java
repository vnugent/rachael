package org.vnguyen.appbuilder.irc;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.Configuration.Builder;
import org.vnguyen.appbuilder.Main;
import org.vnguyen.appbuilder.Utils;

public class ChatBot {

	public ChatBot() {

		Main mainAppBuilder = new Main();
		KubernetesCommandHandler cmdHandler= new KubernetesCommandHandler(mainAppBuilder);
		Builder<PircBotX> config = parseParams();
		config.addListener(cmdHandler);
    	PircBotX myBot = new PircBotX(config.buildConfiguration());
    	try {
    		myBot.startBot();
    	} catch (Exception e) {
   			e.printStackTrace(); 		
    	}		
	}
	
	public Builder<PircBotX> parseParams() {
		String ircServer = System.getenv("SAAS_IRC_SERVER");
		if (ircServer == null) {
			throw new RuntimeException("Missing SAAS_IRC_SERVER env variable");
		}
		
		String ircChannelCSV = System.getenv("SAAS_IRC_CHANNELS");
		if (ircChannelCSV == null) {
			throw new RuntimeException("Missing SAAS_IRC_CHANNELS env variable");
		}
		String[] channels = ircChannelCSV.split(",");
		
		Builder<PircBotX> config = new Configuration.Builder<PircBotX>();
		
		config.setName(Utils.getEnvWithDefault("SAAS_IRCBOT_NIC", "saas-bot"))
			.setAutoNickChange(true)
			.setServer(ircServer, 6667);
			
	    for(String channel : channels) {
	   		config.addAutoJoinChannel(channel);
	   	}
	    return config;	
	}
	
	public static void main(String[] args) {
		new ChatBot();
	}
}
