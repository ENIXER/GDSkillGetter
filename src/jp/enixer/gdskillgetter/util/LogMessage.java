package jp.enixer.gdskillgetter.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import jp.enixer.gdskillgetter.types.Type;

public final class LogMessage {
	private static final Properties properties = new Properties();

	static {
		try {
			properties.load(new InputStreamReader(LogMessage.class
					.getResourceAsStream("/log_message.properties"),
					StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private LogMessage() {
		throw new RuntimeException("DO NOT instanciate this class.");
	}

	public static String startGDSkillGetter() {
		return properties.getProperty("startGDSkillGetter");
	}

	public static String cannotLoginEAGate() {
		return properties.getProperty("cannotLoginEAGate");
	}

	public static String loginEAGateSuccessful() {
		return properties.getProperty("loginEAGateSuccessful");
	}

	public static String loadFromEAGate(Type type) {
		if (type == Type.D) {
			return properties.getProperty("loadFromEAGateD");
		}
		return properties.getProperty("loadFromEAGateG");
	}

	public static String loadEndFromEAGate(Type type) {
		if (type == Type.D) {
			return properties.getProperty("loadEndFromEAGateD");
		}
		return properties.getProperty("loadEndFromEAGateG");
	}

	public static String cannotLoadMusicPlayData(String musicName) {
		StringBuilder builder = new StringBuilder();
		builder.append(properties.getProperty("music"));
		builder.append(musicName);
		builder.append(properties.getProperty("cannotLoadMusicPlayData"));
		return builder.toString();
	}

	public static String loadWrongMusicPlayData(String musicName,
			String wrongMusicName) {
		StringBuilder builder = new StringBuilder();
		builder.append(properties.getProperty("music"));
		builder.append(musicName);
		builder.append(properties.getProperty("cannotLoadMusicPlayData"));
		builder.append('(');
		builder.append(wrongMusicName);
		builder.append(properties.getProperty("wrongMusicPlayData"));
		builder.append(')');
		return builder.toString();
	}

	public static String logoutEAGateSuccessful() {
		return properties.getProperty("logoutEAGateSuccessful");
	}

	public static String loadLevelDataSuccessful() {
		return properties.getProperty("loadLevelDataSuccessful");
	}

	public static String cannotFindConfigFile() {
		return properties.getProperty("cannotFindConfigFile");
	}

	public static String loadConfigFile(String fileName) {
		StringBuilder builder = new StringBuilder();
		builder.append(properties.getProperty("configFile"));
		builder.append('(');
		builder.append(fileName);
		builder.append(')');
		builder.append(properties.getProperty("loadConfigFile"));
		return builder.toString();
	}

	public static String illegalConfigFileName(String fileName) {
		StringBuilder builder = new StringBuilder();
		builder.append(properties.getProperty("illegalConfigFileName"));
		builder.append('(');
		builder.append(fileName);
		builder.append(')');
		return builder.toString();
	}

	public static String loadConfigSuccessful() {
		return properties.getProperty("loadConfigSuccessful");
	}

	public static String loadPage(String url) {
		StringBuilder builder = new StringBuilder();
		builder.append(url);
		builder.append(properties.getProperty("loadPage"));
		return builder.toString();
	}

	public static String failToLoadPage(String url) {
		StringBuilder builder = new StringBuilder();
		builder.append(url);
		builder.append(properties.getProperty("failToLoadPage"));
		return builder.toString();
	}

}
