package org.vnguyen.appbuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.vnguyen.appbuilder.model.AppMetadata;

public class BasicIDGenerators  {
	public static final IDGenerator DEFAULT_POD_ID_GENERATOR = new DefaultPodIdGenerator();
	
	public static class DefaultPodIdGenerator implements IDGenerator {
		private final int MAX_LENGTH=24;
		@Override
		public String generate() {
			return StringUtils.lowerCase(RandomStringUtils.randomAlphanumeric(8));
		}

		
		@Override
		public String generate(String owner, AppMetadata metadata) {
			String id = owner + "-" + generate(metadata);
			return id.length() > MAX_LENGTH ? id.substring(0, MAX_LENGTH) : id;
		}


		@Override
		public String generate(AppMetadata metadata) {
			return  metadata.getType() + "-" + generate();
		}
	}

}
