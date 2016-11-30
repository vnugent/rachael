package org.vnguyen.rachael.cli;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.pircbotx.hooks.events.MessageEvent;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;


@Component
public class CommandExecutor {

	@Inject
	private CLI cli;
		
	private ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
	
	
	public void execute(String userid, String command, MessageEvent event) throws ParseException {
		String[] args = StringUtils.trim(command).split(" ");
		Task<?> task = cli.findCommand(userid, args);
		ListenableFuture<? extends TaskOutputIF<?>> futureResponse = executorService.submit(task);
		
		event.respond("Command accepted.  Please wait...");

		processAsyncResponse(event, futureResponse);
	}
	
	protected void processAsyncResponse(final MessageEvent event, ListenableFuture<? extends TaskOutputIF<?>> future) {
		
		Futures.addCallback(future, new FutureCallback<Object>() {
			@Override
			public void onFailure(Throwable t) {
				event.respond("Error: " + t.getMessage());
			}

			@Override
			public void onSuccess(Object result) {
				List<String> buffer = ((TaskOutputIF<?>)result).transform();
				for(String s:buffer) {
					event.respond(s);
				}
				event.respond("<end>");
			}
			
		});
	}
}
