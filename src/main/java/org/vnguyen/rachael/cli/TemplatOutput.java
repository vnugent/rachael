package org.vnguyen.rachael.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.template.ITemplate;

public class TemplatOutput extends TaskOutput<Map<String, List<IResource>>> {

	public TemplatOutput(Map<String, List<IResource>> result) {
		super(result);
	}

	@Override
	public List<String> transform() {
		List<IResource> localTemplates = super.result.get("local");
		List<IResource> globalTemplates = super.result.get("global");

		List<String> buffer = new ArrayList<>();
		buffer.add("Your project templates:");
		buffer.add(" - " + localTemplates.toString());
		buffer.add("Global templates:");
		buffer.add(" - " + globalTemplates.toString());

		return buffer;
	}

}
