package jp.enixer.gdskillgetter.model;

import jp.enixer.gdskillgetter.types.Rank;
import jp.enixer.gdskillgetter.util.Normalizer;

public class ResultData {
	private String musicName;
	private String normalizedName;
	private Chart chart;
	private Rank rank;
	private double achievementRate;
	private boolean isFullcombo;
	private int maxcombo;
	private int points;
	private int playCount;
	private int clearCount;

	public ResultData(String musicName, int kind,
			Rank rank, double achievementRate, boolean isFullcombo,
			int maxcombo, int points, int playCount, int clearCount) {
		super();
		this.musicName = musicName;
		this.normalizedName = Normalizer.normalizeMusicName(musicName);
		this.chart = Chart.getInstance(kind);
		this.rank = rank;
		this.achievementRate = achievementRate;
		this.isFullcombo = isFullcombo;
		this.maxcombo = maxcombo;
		this.points = points;
		this.playCount = playCount;
		this.clearCount = clearCount;
	}

	public String getMusicName() {
		return musicName;
	}

	public String getNormalizedName() {
		return normalizedName;
	}

	public Chart getChart() {
		return chart;
	}

	public Rank getRank() {
		return rank;
	}

	public double getAchievementRate() {
		return achievementRate;
	}

	public boolean isFullcombo() {
		return isFullcombo;
	}

	public int getMaxcombo() {
		return maxcombo;
	}

	public int getPoints() {
		return points;
	}

	public int getPlayCount() {
		return playCount;
	}

	public int getClearCount() {
		return clearCount;
	}

	public boolean isName(LevelData level) {
		return level.isName(normalizedName);
	}

	public boolean isSame(LevelData level) {
		return isName(level) && isChart(level);
	}

	private boolean isChart(LevelData level) {
		return level.isChart(chart);
	}

}
