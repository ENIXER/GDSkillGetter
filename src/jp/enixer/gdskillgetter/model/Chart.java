package jp.enixer.gdskillgetter.model;

import jp.enixer.gdskillgetter.types.Difficulty;
import jp.enixer.gdskillgetter.types.Type;

public class Chart implements Comparable<Chart> {
	private Difficulty difficulty;
	private Type type;

	private static final Chart[] instances = {
			new Chart(Difficulty.BSC, Type.D),
			new Chart(Difficulty.ADV, Type.D),
			new Chart(Difficulty.EXT, Type.D),
			new Chart(Difficulty.MAS, Type.D),
			new Chart(Difficulty.BSC, Type.G),
			new Chart(Difficulty.ADV, Type.G),
			new Chart(Difficulty.EXT, Type.G),
			new Chart(Difficulty.MAS, Type.G),
			new Chart(Difficulty.BSC, Type.B),
			new Chart(Difficulty.ADV, Type.B),
			new Chart(Difficulty.EXT, Type.B),
			new Chart(Difficulty.MAS, Type.B), };

	private Chart(Difficulty difficulty, Type type) {
		this.difficulty = difficulty;
		this.type = type;
	}

	@Override
	public int compareTo(Chart o) {
		if (o == null) {
			return -1;
		}
		if (difficulty != o.difficulty) {
			return difficulty.compareTo(o.difficulty);
		}
		if (type != o.type) {
			return type.compareTo(o.type);
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(difficulty.toString());
		builder.append('(');
		builder.append(type.toString());
		builder.append(')');
		return builder.toString();
	}

	public int getKind() {
		return difficulty.getKind() + type.getKind();
	}

	public static Chart getInstance(int kind) {
		return instances[kind];
	}

	public boolean isType(Type type) {
		return this.type == type;
	}

}