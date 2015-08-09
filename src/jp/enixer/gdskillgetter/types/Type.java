package jp.enixer.gdskillgetter.types;

import org.apache.commons.lang3.StringUtils;

public enum Type {
	G, B, D;

	public static Type getInstanceOf(String name) {
		if (StringUtils.isBlank(name)) {
			throw new NullPointerException();
		}
		if (StringUtils.startsWithIgnoreCase(name, "G")) {
			return G;
		}
		if (StringUtils.startsWithIgnoreCase(name, "B")) {
			return B;
		}
		if (StringUtils.startsWithIgnoreCase(name, "D")) {
			return D;
		}
		return null;
	}

}
