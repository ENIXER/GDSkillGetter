package jp.enixer.gdskillgetter.model;

import java.util.ArrayList;
import java.util.List;

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
	public boolean equals(Object anotherChart) {
		if (anotherChart == null || !(anotherChart instanceof Chart)) {
			return false;
		}
		Chart chart = (Chart) anotherChart;
		return difficulty == chart.difficulty && type == chart.type;
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

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Type getType() {
		return type;
	}

	public List<String> toCSVString() {
		List<String> result = new ArrayList<String>();
		result.add(Integer.toString(difficulty.getKind() + type.getKind()));
		return result;
	}

	public static Chart getInstance(int kind) {
		return instances[kind];
	}

	public boolean isGf() {
		return type == Type.G || type == Type.B;
	}

	public boolean isDm() {
		return type == Type.D;
	}

}