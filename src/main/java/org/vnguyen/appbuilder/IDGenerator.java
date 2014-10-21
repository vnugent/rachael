package org.vnguyen.appbuilder;

import org.vnguyen.appbuilder.model.AppMetadata;

public interface IDGenerator {
	String generate();
	String generate(String owner, AppMetadata metadata);
}
