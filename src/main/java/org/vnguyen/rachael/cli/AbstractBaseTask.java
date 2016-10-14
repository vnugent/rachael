package org.vnguyen.rachael.cli;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vnguyen.rachael.OCHelper;
import org.vnguyen.rachael.policy.Policy;

import com.google.common.base.Objects;


public abstract class AbstractBaseTask<T> implements Task<T> {
	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Inject
	protected OCHelper oc;

	protected Set<Class <? extends Policy>> policies = new HashSet<>();

	protected final Options opts = new Options();
	protected final HelpFormatter formatter = new HelpFormatter();



	@Override
	public Task<T> addPolicy(Class<? extends Policy> clz) {
		this.policies.add(clz);
		return this;
	}	
	
	@Override
	public StringWriter helpFormatter() {
		StringWriter sw = new StringWriter();

		formatter.printHelp(new PrintWriter(sw, true), 80, "oc " + verb(), null, opts, 4, 2, null);
		return sw;

	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("verb", this.verb())
				.toString();
	}
}
