package jp.enixer.gdskillgetter.model;

import jp.enixer.gdskillgetter.types.Type;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Result implements Comparable<Result> {
	private Chart chart;
	private ResultDetail detail = new ResultDetail();

	@Override
	public int compareTo(Result o) {
		if (o == null) {
			return 1;
		}
		return detail.compareTo(o.detail);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public double getSkillPoint() {
		return detail.getSkillPoint();
	}

	public void merge(LevelData level, ResultData result) {
		chart = level.getChart();
		detail.merge(level, result);
	}

	public boolean isType(Type type) {
		return chart.isType(type);
	}

	public boolean isModel(Type type) {
		switch (type) {
		case D:
			return isType(Type.D);
		default:
			return isType(Type.G) || isType(Type.B);
		}
	}

	public String getKindString() {
		return Integer.toString(chart.getKind());
	}

	public String getAchievementRate() {
		return Double.toString(detail.getAchievementRate());
	}

	public String toCSVString() {
		StringBuilder builder = new StringBuilder();
		builder.append(chart.toString());
		builder.append(':');
		builder.append(' ');
		builder.append(detail.toCSVString());
		return builder.toString();
	}

	public String isFullcombo() {
		return Boolean.toString(detail.isFullcombo());
	}

}
