package org.vnguyen.rachael;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.springframework.stereotype.Component;
import org.vnguyen.rachael.cli.CommandExecutor;

@Component
public class MyListener extends ListenerAdapter {
	
	@Inject
	private AppParams params;
	
	@Inject 
	private CommandExecutor commandExecutor;

	@Override
	public void onPrivateMessage(PrivateMessageEvent event) {
		String message = StringUtils.trimToNull(event.getMessage());
		if (message == null) {
			return;
		}
		String safeNick = getSafeNick(event.getUser().getNick());
		if (message.startsWith("oc")) {
			try {
				commandExecutor.execute(safeNick, message, event);
			} catch (Exception e) {
				event.respond("Error: " + e.getMessage());
			}
			
		} else {
			event.respond(params.welcomeMessage);
		}
	}
	

	
	public static String getSafeNick(String userNick) {
		String safeNick = StringUtils.substringBefore(userNick, "_");
		safeNick = StringUtils.substringBefore(safeNick,"|");
		safeNick = StringUtils.substringBefore(safeNick,"-");
		safeNick = safeNick.replaceAll("[^a-zA-Z0-9]", "_");
		return safeNick;
	}
}
