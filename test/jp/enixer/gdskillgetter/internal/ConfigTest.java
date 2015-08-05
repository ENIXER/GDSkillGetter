package jp.enixer.gdskillgetter.internal;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigTest {

	@Test
	public void isConfigSingleton() {
		Config instance1 = Config.getInstance();
		Config instance2 = Config.getInstance();
		assertEquals(instance1, instance2);
	}

}
