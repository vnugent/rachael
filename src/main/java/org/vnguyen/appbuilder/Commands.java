package org.vnguyen.appbuilder;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vnguyen.appbuilder.model.AppMetadata;
import org.vnguyen.appbuilder.model.AppTemplate;
import org.vnguyen.appbuilder.model.ServiceEndpoint;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class Commands {
	private Logger log = LoggerFactory.getLogger(Commands.class);
	private final String APP_FILE_SUFFIX = "-app.json";
	private Main mainContext;
	private ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

	public Commands() {

	}
	
	public Commands setAppContext(Main context) {
		this.mainContext = context;
		return this;
	}
	
	public Main appContext() {
		return mainContext;
	}
	
	public Map<String, String> listApp() {
		File directory = new File(mainContext.dataDir());
		
		File[] appFiles = directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(APP_FILE_SUFFIX);
			}
		});
		
		Map<String, String> appMap = new HashMap<String, String>();
		for(File file : appFiles) {
			String appName = file.getName().substring(0, file.getName().length() - APP_FILE_SUFFIX.length());
			AppMetadata appTemplate = AppMetadata.fromJSONFile(file);				
			appMap.put(appName, appTemplate.getName());
		}
		
		return appMap;
	}
	
	/**
	 * Createa a new App 
	 * @param appName
	 * @return
	 * @throws Exception
	 */
	public AppTemplate createApp(String owner, String appName) throws Exception {
		File appFile = new File(mainContext.dataDir() + "/" + appName + "-app.json");
		if (!appFile.exists()) {
			throw new RuntimeException("File not found: <dataDir>/" + appFile.getName());
		}
		
		AppMetadata metadata = AppMetadata.fromJSONFile(appFile);
		AppTemplate kApp = AppTemplate.from(metadata);
		mainContext.appBuilder().from(owner, kApp);
		return kApp;
	}
	
	public ListenableFuture<ServiceEndpoint> createAppTask(String owner, String appName) {
		log.info("Creating app {} {}", owner, appName );
		return executorService.submit(new CreateAppTask(owner, appName, this));
		
	}
	
}
