package org.wulfnoth.rc.commons;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

/**
 * @author Young
 */
public class Configuration {

	private static Config config = ConfigFactory.defaultReference();

	public static String getString(String key) {
		return config.getString(key);
	}

	public static List<String> getStrings(String key) {
		return config.getStringList(key);
	}

}
