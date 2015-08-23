package jp.enixer.gdskillgetter.internal;

import static org.apache.commons.lang3.BooleanUtils.toBoolean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.enixer.gdskillgetter.util.LogMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {
	private static final Properties properties = new Properties();
	private static final Log log = LogFactory.getLog(Config.class);

	private Config() {
	}

	public static void getPropertiesFromConfig() {
		String fileName = new ConfigSearcher().search();
		if (fileName == null) {
			log.fatal(LogMessage.cannotFindConfigFile());
			throw new RuntimeException();
		}
		log.info(LogMessage.loadConfigFile(fileName));
		InputStream in = Config.class.getResourceAsStream('/' + fileName);
		try {
			if (fileName.endsWith("xml")) {
				properties.loadFromXML(in);
			} else if (fileName.endsWith("properties")) {
				properties.load(in);
			} else if (fileName.endsWith("ini")) {
				// TODO: Be able to read the settings from the own format of the
				// configuration file
			} else {
				log.error(LogMessage.illegalConfigFileName(fileName));
				throw new RuntimeException();
			}
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.info(LogMessage.loadConfigSuccessful());
	}

	public static String getProxyHost() {
		return properties.getProperty("ProxyHost");
	}

	public static int getProxyPort() {
		return NumberUtils.toInt(properties.getProperty("ProxyPort"));
	}

	public static String getEAGateUsername() {
		return properties.getProperty("EAGateUsername");
	}

	public static String getEAGatePassword() {
		return properties.getProperty("EAGatePassword");
	}

	public static boolean canUpdateGf() {
		String updateGuitarSkill = properties.getProperty("UpdateGuitarSkill");
		return StringUtils.isEmpty(updateGuitarSkill)
				|| toBoolean(updateGuitarSkill);
	}

	public static boolean canUpdateDm() {
		String updateDrumSkill = properties.getProperty("UpdateDrumSkill");
		return StringUtils.isEmpty(updateDrumSkill)
				|| toBoolean(updateDrumSkill);
	}

	public static boolean canUpdateFullcombo() {
		String updateFullcombo = properties.getProperty("UpdateFullcombo");
		return StringUtils.isNotEmpty(updateFullcombo)
				&& toBoolean(updateFullcombo);
	}

	private static class ConfigSearcher {
		private final String PATTERN = "\\w*config\\w*(\\.xml|\\.properties|\\.ini)";

		public String search() {
			Pattern pattern = Pattern.compile(PATTERN);
			File root = new File("./");
			for (File file : root.listFiles()) {
				if (file.isFile()) {
					Matcher matcher = pattern.matcher(file.getName());
					if (matcher.matches()) {
						return file.getName();
					}
				}
			}
			return "config.xml";
		}
	}

}
