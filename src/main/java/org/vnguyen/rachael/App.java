package org.vnguyen.rachael;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.vnguyen.rachael.cli.NewAppTask;
import org.vnguyen.rachael.policy.SingleInstancePerTemplate;


@Configuration
@ComponentScan
public class App {
	@Bean
    public NewAppTask newAppTask() {
		NewAppTask newAppTask = new NewAppTask();
		newAppTask.addPolicy(SingleInstancePerTemplate.class);
        return newAppTask;
    }
	
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = 
			      new AnnotationConfigApplicationContext(App.class);
		
		IRCBot bot = ctx.getBean(IRCBot.class);
		bot.start();
	}
}
