package org.vnguyen.appbuilder;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.vnguyen.appbuilder.irc.KubernetesCommandHandler;

@Test
public class SafeNickTest {

	@Test
	public void testSafeNick() {
		Assert.assertEquals(KubernetesCommandHandler.getSafeNick("johdoe123345"), "johdoe123345");
		Assert.assertEquals(KubernetesCommandHandler.getSafeNick("john_afk"), "john");
		Assert.assertEquals(KubernetesCommandHandler.getSafeNick("john|lunch"), "john");
		Assert.assertEquals(KubernetesCommandHandler.getSafeNick("john-lunch"), "john");
		Assert.assertEquals(KubernetesCommandHandler.getSafeNick("__john-lunch"), "");

	}
}
