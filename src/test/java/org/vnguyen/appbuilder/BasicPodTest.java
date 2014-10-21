package org.vnguyen.appbuilder;

import java.io.File;

import org.apache.stratos.kubernetes.api.client.v2.KubernetesApiClient;
import org.apache.stratos.kubernetes.api.model.Pod;
import org.apache.stratos.kubernetes.api.model.PodList;
import org.apache.stratos.kubernetes.api.model.Selector;
import org.apache.stratos.kubernetes.api.model.Service;
import org.apache.stratos.kubernetes.api.model.ServiceList;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

@Test
public class BasicPodTest {

    final private KubernetesApiClient client = new KubernetesApiClient(Utils.getEnvStrict("KUBERNETES_API_ENDPOINT"));
	final private String dataDir = Utils.getEnvStrict("RACHAEL_DATA_DIR");

	@Test(invocationCount=3)
	void createPodTest() throws Exception {
		Pod pod = Utils.load(Pod.class, new File(dataDir + "/apache-pod.json"));
		String podId = BasicIDGenerators.DEFAULT_POD_ID_GENERATOR.generate();
		pod.setId(podId);
		pod.getLabels().setName(podId);

		client.createPod(pod);
		
		Pod actual = client.getPod(podId);
		
		Assert.assertTrue("Waiting".equalsIgnoreCase(actual.getCurrentState().getStatus()) ||  "Running".equalsIgnoreCase(actual.getCurrentState().getStatus()) , "status check");
	}
		
	@Test(dependsOnMethods={"createPodTest"})
	void deletePodsTest() throws Exception {
		PodList list = client.getAllPods();
		Assert.assertTrue(list.getItems() != null && list.getItems().length > 0, "should have at least one pod");
		for(Pod pod : list.getItems()) {
			Reporter.log("Deleting pod: " + pod.getId());
			client.deletePod(pod.getId());
		}
		list = client.getAllPods();
		Assert.assertTrue(list.getItems() == null || list.getItems().length == 0, "should have no pods after delete");
	}
	
	@Test(invocationCount=3)
	void createServiceTest() throws Exception {
		
		Service jonService = Utils.load(Service.class, new File(dataDir + "/apache-service.json"));
		String id = BasicIDGenerators.DEFAULT_POD_ID_GENERATOR.generate();
		jonService.setId(id);
		jonService.setPort(45002);
		Selector selector = new Selector();
		selector.setName(id);
		jonService.setSelector(selector);
		client.createService(jonService);
	}
	
	@Test (dependsOnMethods={"createServiceTest"})
	void deleteServiceTest() throws Exception {
		ServiceList list = client.getAllServices();
		Assert.assertTrue(list.getItems().length > 0, "should have at least one service");
		for(Service service : list.getItems()) {
			Reporter.log("Deleting service :" + service.getId());
			client.deleteService(service.getId());
		}
		list = client.getAllServices();
		Assert.assertTrue(list.getItems() == null || list.getItems().length == 0, "should have no services after delete");
	}
	
	
}
