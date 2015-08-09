package jp.enixer.gdskillgetter.types;

public enum Rank {
	EXC, SS, S, A, B, C, D, E;

	public static Rank getInstanceOf(String name) {
		if ("-".equals(name)) {
			return E;
		}
		return valueOf(name);
	}

}
