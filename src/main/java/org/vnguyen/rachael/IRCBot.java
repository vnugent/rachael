package org.vnguyen.rachael;

import javax.inject.Inject;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IRCBot {
	final Logger logger = LoggerFactory.getLogger(IRCBot.class);


	@Inject
	private AppParams params;

	@Inject
	private MyListener listener;
	
	private PircBotX bot;
	
	public void start() {
		bot = new PircBotX(configure());
		try {
			logger.info("Starting IRCBot");
			bot.startBot();
		} catch (Exception e) {
			logger.error("Error: {}", e);
		}
	}
	
	private Configuration configure() {
		   Configuration configuration = new Configuration.Builder()
           		.setName(params.botNick) 
           		.addServer(params.ircServer)
           		.addListener(listener)
           		.addAutoJoinChannels(params.channels)
           		.setAutoReconnectDelay(60)
           		.setAutoReconnect(true)
           .buildConfiguration();	
		return configuration;
	}

}
