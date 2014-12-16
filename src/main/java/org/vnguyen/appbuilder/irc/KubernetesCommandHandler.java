package org.vnguyen.appbuilder.irc;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vnguyen.appbuilder.AppContext;
import org.vnguyen.appbuilder.model.ServiceEndpoint;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class KubernetesCommandHandler extends ListenerAdapter<PircBotX> {
	private Logger log = LoggerFactory.getLogger(KubernetesCommandHandler.class);

	private AppContext mainApp;
	private ChatBot bot;

	public KubernetesCommandHandler(ChatBot bot, AppContext mainApp) {
		this.bot = bot;
		this.mainApp = mainApp;
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {
		String message = StringUtils.trimToNull(event.getMessage());
		if (message == null) {
			return;
		}
		String safeNick = getSafeNick(event.getUser().getNick());

		Map<String, String> apps = mainApp.cmds().listApp();
		
		if (message.startsWith("!")) {
			String appName = message.substring(1);
			if (apps.containsKey(appName)) {
				event.respond("Creating new app: " + appName);
				ListenableFuture<ServiceEndpoint> futureResult = mainApp.cmds().createAppTask(safeNick, appName);
				processAsyncCreateAppTask(event, futureResult);
			} else {
				event.respond("App name not found: " + appName); 
			}
		} else {
			for(String s : bot.welcomeMessage()) {
				event.respond(s);
			}
			event.respond("!app_name to create new app.  Available apps: " + apps.keySet().toString());
		}
	}
	
	public static String getSafeNick(String userNick) {
		String safeNick = StringUtils.substringBefore(userNick, "_");
		safeNick = StringUtils.substringBefore(safeNick,"|");
		safeNick = StringUtils.substringBefore(safeNick,"-");
		safeNick = safeNick.replaceAll("[^a-zA-Z0-9]", "_");
		return safeNick;
	}
	
	protected  void processAsyncCreateAppTask(final PrivateMessageEvent<PircBotX> event, ListenableFuture<ServiceEndpoint> future) {
		log.info("Register result callback for user {}, ", event.getUser().getNick() );
		Futures.addCallback(future, new FutureCallback<ServiceEndpoint>() {

			@Override
			public void onSuccess(ServiceEndpoint result) {
				event.respond("App id: " + result.pod().getId());
				for(Map.Entry<String, Long> entry : result.port().entrySet()) {
					event.respond("   " + entry.getKey() + " --> " + result.hostIP() + ":" + entry.getValue());
				}
				event.respond("-end-");
			}
			

			@Override
			public void onFailure(Throwable t) {
				event.respond(t.getMessage());
			}
		});
	}
}
