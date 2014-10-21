package org.vnguyen.appbuilder;

import org.apache.stratos.kubernetes.api.model.Service;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.google.common.collect.Range;

@Test
public class PortManagerTest {

	@Test
	public void findFreePortsTest() throws Exception {
		PortManagerForServices portManager = new PortManagerForServices();
		
		final Range<Integer> validRange = Range.closed(5, 9);
		Service[] inuse = makeTestService(6,8);
		
		int[] ports = portManager.allocatePorts(validRange, inuse, 2);
		Assert.assertEquals(ports, new int[] {5,7}, "next two free");
		
		inuse = makeTestService(5, 7, 6,8);
		ports = portManager.allocatePorts(validRange, inuse, 1);
		Assert.assertEquals(ports, new int[] {9}, "next one");
		
		inuse = makeTestService(6,7,8);
		
		ports = portManager.allocatePorts(validRange, inuse, 2);
		Assert.assertEquals(ports, new int[] {5,9}, "Wrapped around");
		
		inuse = makeTestService(50, 60, 70); // in use ports are out of valid range
		ports = portManager.allocatePorts(validRange, inuse, 3);
		Assert.assertEquals(ports, new int[] {5,6,7}, "In use out of range");
	}

	
	@Test
	public void notEnoughRoomTest() throws Exception {
		PortManagerForServices portManager = new PortManagerForServices();
		final Range<Integer> validRange = Range.closed(10, 13);

		Service[] inuse = makeTestService(10,11,12);
		try {
			int[] ports = portManager.allocatePorts(validRange, inuse, 2);
			Assert.fail("Should fail due to not enough free ports");
		} catch (Exception e) {
			Reporter.log(e.getMessage());
		}
		
		
	}
	
	
	private static Service[] makeTestService(int ... ports) {
		Service[] services = new Service[ports.length];
		int i=0;
		for(int p : ports) {
			services[i] = new Service();
			services[i].setPort(p);
			i++;
		}
		return services;
	}
	
}
