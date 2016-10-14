package org.vnguyen.rachael;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.openshift.restclient.model.IProject;

@ContextConfiguration(classes={OCHelper.class, AppParams.class})
public class DCTest extends AbstractTestNGSpringContextTests{

	@Inject
	private OCHelper oc;
	
	@Test
	public void testSinglePolicy() {
		String projectName = "test-" + RandomStringUtils.randomAlphanumeric(5).toLowerCase();

		IProject project = oc.getProject("vnguyen");
		if (oc.getDC(project, "hawkular-full") != null) {
			
		}
	}
}
