package jp.enixer.gdskillgetter.model;

import java.util.ArrayList;
import java.util.List;

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

	public List<String> getOutputs() {
		List<String> list = new ArrayList<String>();
		list.addAll(chart.toCSVString());
		list.addAll(detail.toCSVString());
		return list;
	}

	public void merge(LevelData level, ResultData result) {
		chart = level.getChart();
		detail.merge(level, result);
	}

	public boolean isGf() {
		return chart.isGf();
	}

	public boolean isDm() {
		return chart.isDm();
	}

}
