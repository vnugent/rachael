package org.vnguyen.rachael.cli;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class DoNothingRenderer extends TaskOutput<Object> {

	public DoNothingRenderer(Object result) {
		super(result);
	}

	@Override
	public List<String> transform() {
		return ImmutableList.of();
	}

}
