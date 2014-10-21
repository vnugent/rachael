package org.vnguyen.appbuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.vnguyen.appbuilder.model.AppMetadata;

public class BasicIDGenerators  {
	public static final IDGenerator DEFAULT_POD_ID_GENERATOR = new DefaultPodIdGenerator();
	
	public static class DefaultPodIdGenerator implements IDGenerator {
		@Override
		public String generate() {
			return StringUtils.lowerCase(RandomStringUtils.randomAlphanumeric(8));
		}

		@Override
		public String generate(String owner, AppMetadata metadata) {
			String id = owner + "-" + metadata.getType() + "-" + generate();
			return id.length() > 24 ? id.substring(0, 24) : id;
		}
	}

}
