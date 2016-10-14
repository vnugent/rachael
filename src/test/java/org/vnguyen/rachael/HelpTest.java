package org.vnguyen.rachael;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.vnguyen.rachael.cli.CLI;
import org.vnguyen.rachael.cli.DeployTask;
import org.vnguyen.rachael.cli.GetAppTask;
import org.vnguyen.rachael.cli.HelpTask;
import org.vnguyen.rachael.cli.ListAppTask;
import org.vnguyen.rachael.cli.NewAppTask;

@ContextConfiguration(classes={CLI.class, HelpTask.class, DeployTask.class, 
        GetAppTask.class, NewAppTask.class, ListAppTask.class, MockOCHelper.class})
public class HelpTest extends AbstractTestNGSpringContextTests {
    @Inject
    private CLI cli;
    
    @Inject
    private OCHelper mockOC;
    
    @Test
    public void test1() {
        
    }

}
