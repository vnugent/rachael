package org.vnguyen.appbuilder;

import java.io.File;
import java.util.BitSet;

import org.apache.stratos.kubernetes.api.client.v2.KubernetesApiClient;
import org.apache.stratos.kubernetes.api.model.Service;
import org.vnguyen.appbuilder.model.ServicePortRange;
import org.vnguyen.appbuilder.model.ServicePortRangeMap;

import com.google.common.collect.Range;

public class PortManagerForServices {

	private KubernetesApiClient client;
	private ServicePortRangeMap portMap;
	
	/**
	 * For testing purposes
	 */
	public PortManagerForServices() {
		
	}
	
	public PortManagerForServices(KubernetesApiClient client) {
		this.client = client;
		
		File json = new File(Utils.getEnvStrict("RACHAEL_DATA_DIR") + "/service-ports.json");
		portMap = ServicePortRangeMap.fromJsonFile(json);
		
	}
	
	public synchronized int[] getAvailablePorts(String appType, int size) throws Exception {
		ServicePortRange portRange = portMap.lookup(appType);
		if (portRange == null) {
			throw new IllegalArgumentException("Please check our config.  Port range definition not found for type: " + appType);
		}
		
		Service[]  services = client.getAllServices().getItems();
		
		
		if (services == null) {
			services = new Service[0];
		}
		
		final Range<Integer> validRange = Range.closed(portRange.min, portRange.max);		

		
		return allocatePorts(validRange, services, size);
	}
	
	public int[] allocatePorts(Range<Integer> validRange, Service[] services, int size) throws Exception {
		BitSet inuseSet = constructInUseSet(validRange, services);
		
		if (inuseSet.cardinality() + size - 1> validRange.upperEndpoint() - validRange.lowerEndpoint() ) {
			// not enough room
			throw new Exception("Not enough available ports.  In use: " + services.length);
		}
		
		int[] freePorts = new int[size];
		for(int i = 0; i < size; i++) {
			int nextFree = inuseSet.nextClearBit(0);
			inuseSet.set(nextFree);
			freePorts[i] = nextFree + validRange.lowerEndpoint();
		}		
		return freePorts;
	}
		

	public BitSet constructInUseSet(Range<Integer> validRange, Service[] services) {
		
		BitSet inusedSet = new BitSet(validRange.upperEndpoint() - validRange.lowerEndpoint());
		
		for(Service service : services) {
			int port = service.getPort();
			if (validRange.contains(port)) {
				inusedSet.set(service.getPort()-validRange.lowerEndpoint());
			}
		}
		
		return inusedSet;
	}
	
}
