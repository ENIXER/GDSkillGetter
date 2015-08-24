package jp.enixer.gdskillgetter.model;

import jp.enixer.gdskillgetter.types.Rank;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ResultDetail implements Comparable<ResultDetail> {
	private Rank rank;
	private double achievementRate;
	private double skillpoint;
	private boolean isFullcombo;
	private int maxcombo;
	private int points;
	private int playCount;
	private int clearCount;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public double getSkillPoint() {
		return skillpoint;
	}

	public int compareTo(ResultDetail o) {
		double diff = o.skillpoint - skillpoint;
		if (diff < 0) {
			return -1;
		}
		if (diff > 0) {
			return 1;
		}
		return 0;
	}

	public String toCSVString() {
		StringBuilder builder = new StringBuilder();
		builder.append(achievementRate);
		builder.append('%');
		builder.append('(');
		builder.append((int) (skillpoint * 100) / 100.0);
		builder.append(')');
		return builder.toString();
	}

	public void merge(LevelData level, ResultData result) {
		rank = result.getRank();
		achievementRate = result.getAchievementRate();
		skillpoint = 20 * level.getLevel() * achievementRate / 100;
		isFullcombo = result.isFullcombo();
		maxcombo = result.getMaxcombo();
		points = result.getPoints();
		playCount = result.getPlayCount();
		clearCount = result.getClearCount();
	}

	public double getAchievementRate() {
		return achievementRate;
	}

	public boolean isFullcombo() {
		return isFullcombo;
	}
}
