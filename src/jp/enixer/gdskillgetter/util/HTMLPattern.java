package jp.enixer.gdskillgetter.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Pattern;

import jp.enixer.gdskillgetter.types.Type;

public final class HTMLPattern {
	private static final Properties properties = new Properties();
	private static final Pattern gfResultListPattern;
	private static final Pattern dmResultListPattern;
	private static final Pattern gfResultDetailPattern;
	private static final Pattern dmResultDetailPattern;
	private static final Pattern resultPattern;
	private static final Pattern resultDetailMusicNamePattern;
	private static final Pattern levelListPattern;
	private static final Pattern levelValuePattern;

	static {
		try {
			properties.load(new InputStreamReader(HTMLPattern.class
					.getResourceAsStream("/pattern.properties"),
					StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		gfResultListPattern = Pattern.compile(
				properties.getProperty("guitarResultPattern"), Pattern.DOTALL);
		dmResultListPattern = Pattern.compile(
				properties.getProperty("drumResultPattern"), Pattern.DOTALL);
		gfResultDetailPattern = Pattern.compile(
				properties.getProperty("guitarResultDetailPattern"),
				Pattern.DOTALL);
		dmResultDetailPattern = Pattern.compile(
				properties.getProperty("drumResultDetailPattern"),
				Pattern.DOTALL);
		resultPattern = Pattern.compile(properties
				.getProperty("resultDetailPattern"));
		resultDetailMusicNamePattern = Pattern.compile(properties
				.getProperty("resultMusicNamePattern"));
		levelListPattern = Pattern.compile(
				properties.getProperty("levelListPattern"), Pattern.DOTALL);
		levelValuePattern = Pattern.compile(properties
				.getProperty("levelValuePattern"));
	}

	private HTMLPattern() {
		throw new RuntimeException("DO NOT instanciate this class.");
	}

	public static Pattern getResultListPattern(Type type) {
		if (type == Type.D) {
			return dmResultListPattern;
		}
		return gfResultListPattern;
	}

	public static Pattern getResultDetailPattern(Type type) {
		if (type == Type.D) {
			return dmResultDetailPattern;
		}
		return gfResultDetailPattern;
	}

	public static Pattern getResultMusicNamePattern() {
		return resultDetailMusicNamePattern;
	}

	public static Pattern getLevelListPattern() {
		return levelListPattern;
	}

	public static Pattern getLevelValuePattern() {
		return levelValuePattern;
	}

}
