package org.vnguyen.rachael.cli;

import java.util.List;

public interface TaskOutputIF<T> {
	List<String> transform();
	T rawResult();
}
