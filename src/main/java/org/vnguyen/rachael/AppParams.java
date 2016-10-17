package org.vnguyen.rachael;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AppParams {
	public static final String DEFAULT_WELCOME="Welcome to OpenShift chatbot. Syntax: oc <command>  or oc help";
	public static final String DEFAULT_IRC_SERVER="irc.freenode.net";
	public static final String DEFAULT_IRC_CHANNEL="#openshift-ircbot";

	public static final String DEFAULT_NAME="openshift-bot";
	public final String openshiftEndpoint;
	public final String welcomeMessage;
	public final String ircServer;
	public final List<String> channels;
	public final String botNick;
	public final String apiUser;
	public final String apiPassword;
	
	public AppParams() {
		openshiftEndpoint = Utils.getEnvStrict("OB_OPENSHIFT_ENDPOINT");
		welcomeMessage = Utils.getEnvWithDefault("OB_WELCOME_MESSAGE", DEFAULT_WELCOME);
		ircServer = Utils.getEnvWithDefault("OB_IRC_SERVER", DEFAULT_IRC_SERVER);
		String channelCSV = Utils.getEnvWithDefault("OB_IRC_CHANNELS", DEFAULT_IRC_CHANNEL);
		channels = new ArrayList<String>(Arrays.asList(channelCSV.split(",")));
		botNick = Utils.getEnvWithDefault("OB_NICK", DEFAULT_NAME);
		apiUser = Utils.getEnvStrict("OB_API_USER");
		apiPassword = Utils.getEnvStrict("OB_API_PASSWORD");
	}
}
