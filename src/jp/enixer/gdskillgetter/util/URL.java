package jp.enixer.gdskillgetter.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import jp.enixer.gdskillgetter.types.Type;

public class URL {
	private static final Properties properties = new Properties();

	static {
		try {
			properties.load(new InputStreamReader(LogMessage.class
					.getResourceAsStream("/url.properties"),
					StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private URL() {
		throw new RuntimeException("DO NOT instanciate this class.");
	}

	public static String loginEAGate() {
		return properties.getProperty("EAGateLoginUrl");
	}

	public static String EAGateIndex() {
		return properties.getProperty("EAGateIndexUrl");
	}

	public static String logoutEAGate() {
		return properties.getProperty("EAGateLogoutUrl");
	}

	public static String EAGatePlaydataList(Type type) {
		if(type == Type.D){
			return properties.getProperty("EAGateDmPlaydataListUrl");
		}
		return properties.getProperty("EAGateGfPlaydataListUrl");
	}

	public static String EAGateDetailResultList(Type type) {
		if(type == Type.D){
			return properties.getProperty("EAGateDmDetailResultUrl");
		}
		return properties.getProperty("EAGateGfDetailResultUrl");
	}
	
	public static String skillnoteLevelList(){
		return properties.getProperty("SkillnoteLevelListUrl");
	}

}
