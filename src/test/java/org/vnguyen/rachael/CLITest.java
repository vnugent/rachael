package org.vnguyen.rachael;

import static org.mockito.Mockito.verify;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.cli.ParseException;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.vnguyen.rachael.cli.CLI;
import org.vnguyen.rachael.cli.DeployTask;
import org.vnguyen.rachael.cli.GetAppTask;
import org.vnguyen.rachael.cli.HelpTask;
import org.vnguyen.rachael.cli.ListAppTask;
import org.vnguyen.rachael.cli.NewAppTask;
import org.vnguyen.rachael.cli.Task;
import org.vnguyen.rachael.cli.TaskOutputIF;

import com.beust.testng.TestNG;
import com.openshift.restclient.model.IProject;

@ContextConfiguration(classes={CLI.class, HelpTask.class, DeployTask.class, GetAppTask.class, NewAppTask.class, ListAppTask.class, MockOCHelper.class})
public class CLITest extends AbstractTestNGSpringContextTests {

	@Inject
	CLI cli;
	
	@Inject
	OCHelper mockOC;
	
	@Test
	public void newAppTest() throws Exception {
	    String[] args = new String[]{"oc", "new-app", "--template", "hawkular-full", "-n", "myproject"};
			
	    Task<?> task = cli.findCommand("jdoe", args);
	    Assert.assertTrue(task instanceof NewAppTask);

	    task.call();
	    verify(mockOC).getProject("myproject");
	    verify(mockOC).newAppByTemplateName("hawkular-full", (IProject)null);
	}
	
	@Test
	public void newAppTestWithoutNamespace() throws Exception {
	    String[] args = new String[]{"oc", "new-app", "--template", "jenkins"};
	    
	    Task<?> task = cli.findCommand("jdoe", args);
	    task.call();
	    verify(mockOC).getProject("jdoe");  // namespace is defaulted to userid
	}
	
	
	@Test
	public void parseTest() throws Exception {
		String[] args = new String[]{"oc", "foobar"};
		try {
			Task<?> task = cli.findCommand("abc", args);
		} catch(RuntimeException e1) {
			Assert.assertTrue(e1.getMessage().contains("verb"));
			return;
		}
		Assert.fail("Invalid verb exceptione expected");
	}
	
	
	@Test
	public void getHelpTest1() throws Exception {
	    String[] args = new String[] {"oc", "help", "new-app"};
	    HelpTask task = (HelpTask)cli.findCommand("user1", args);
	    TaskOutputIF<String> helpText = task.call();
	    List<String> multiLines = helpText.transform();
	    Assert.assertTrue(multiLines.size()>=4, "At least 3 lines");
	}
	
}

@Configuration
class MockOCHelper {
	@Bean
	public OCHelper ocHelper() {
		return Mockito.mock(OCHelper.class);
	}
	
	@Bean
	public AppParams appParams() {
		return Mockito.mock(AppParams.class);
	}
}
