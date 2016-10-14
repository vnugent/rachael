package org.vnguyen.rachael;

import java.io.IOException;

import javax.inject.Inject;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
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
	
	public void start() throws IrcException, IOException {
		logger.debug("Starting IRCBot object");
		bot = new PircBotX(configure());
		bot.startBot();
	}
	
	private Configuration configure() {
		   Configuration configuration = new Configuration.Builder()
           		.setName(params.botNick) 
           		.addServer(params.ircServer)
           		.addListener(listener)
           		.addAutoJoinChannels(params.channels)
           		.setAutoReconnectDelay(60)
           .buildConfiguration();	
		return configuration;
	}

}
