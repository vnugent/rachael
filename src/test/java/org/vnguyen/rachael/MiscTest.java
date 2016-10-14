package org.vnguyen.rachael;

import java.util.Map;

import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

public class MiscTest {

    @Test
    public void test1() {
	Map<String, String> m = ImmutableMap.of("1", "one");
	String v = m.get("2");
	Assert.isNull(v);
    }
}
