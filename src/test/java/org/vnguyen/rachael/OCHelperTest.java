package org.vnguyen.rachael;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;

@Test(groups="integration")
@ContextConfiguration(classes={OCHelper.class, AppParams.class})
public class OCHelperTest extends AbstractTestNGSpringContextTests {
	
	@Inject
	OCHelper client;
	
	private String projectName = "test-" + RandomStringUtils.randomAlphanumeric(5).toLowerCase();

	
	
	@Test 
	public void test1() {
		System.out.println(client);
		
		IProject project = client.newProject(projectName);
		
		List<IResource> result = client.newAppByTemplateName("hawkular-full", project);

	}
	
	@Test (dependsOnMethods="test1")
	public void test2() {
		List<IDeploymentConfig> dcList = client.getAllDCs(projectName);
		Assert.assertTrue(dcList.size() == 1);
		Assert.assertEquals(dcList.get(0).getName(), "hawkular-full");
	}

}
