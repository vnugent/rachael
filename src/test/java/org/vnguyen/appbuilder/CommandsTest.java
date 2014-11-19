package org.vnguyen.appbuilder;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.stratos.kubernetes.api.model.Pod;
import org.apache.stratos.kubernetes.api.model.Service;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.vnguyen.appbuilder.model.AppTemplate;

import com.google.common.collect.ImmutableSet;

@Test
public class CommandsTest {

	@Test
	public void appListTest() throws Exception {
		Main main = new Main();
		Map<String, String> apps = main.cmds().listApp();	
		
		Assert.assertEquals(apps.keySet(), ImmutableSet.of("apache", "redis"));
	}
	
	@Test
	public void createAppTest() throws Exception {
		Main main = new Main();
		Map<String, String> apps = main.cmds().listApp();
		Assert.assertTrue(apps.size() > 0, "should have at least one app");
		
		AppTemplate appTemplate = main.cmds().createApp("unittest", "redis");
		
		Thread.sleep(10000);
		Pod pod = appTemplate.getPod();
		Service[] services = appTemplate.getServices();
		
		Pod actualPod = main.api().getPod(pod.getId());
		
		Assert.assertFalse(StringUtils.isEmpty(actualPod.getDesiredState().getHost()));
		Assert.assertFalse(StringUtils.isEmpty(actualPod.getDesiredState().getStatus()));
		
		for(Service s : services) {
			Service actualService = main.api().getService(s.getId());
			Assert.assertEquals(actualService.getPort(), s.getPort(), "service port");
		}
		
		
	}
}
