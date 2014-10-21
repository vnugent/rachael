package org.vnguyen.appbuilder.irc;

import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class ExecutorService {
	private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

}
