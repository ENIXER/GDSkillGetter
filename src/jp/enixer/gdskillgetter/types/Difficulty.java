package jp.enixer.gdskillgetter.types;

import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;

import org.apache.commons.lang3.StringUtils;

public enum Difficulty {
	BSC, ADV, EXT, MAS;

	public static Difficulty getInstanceOf(String name) {
		if (StringUtils.isBlank(name)) {
			throw new NullPointerException();
		}
		if (startsWithIgnoreCase(name, "N") || startsWithIgnoreCase(name, "B")) {
			return BSC;
		}
		if (startsWithIgnoreCase(name, "R") || startsWithIgnoreCase(name, "A")) {
			return ADV;
		}
		if (startsWithIgnoreCase(name, "E")) {
			return EXT;
		}
		if (startsWithIgnoreCase(name, "M")) {
			return MAS;
		}
		return null;
	}

}
