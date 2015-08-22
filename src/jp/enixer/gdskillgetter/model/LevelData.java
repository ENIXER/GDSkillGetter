package jp.enixer.gdskillgetter.model;

import jp.enixer.gdskillgetter.util.Normalizer;

public class LevelData {
	private String musicName;
	private String normalizedName;
	private Chart chart;
	private double level;
	private String skillnoteId;
	private boolean isNew;

	public LevelData(String musicName, int kind, double level,
			String skillNoteId, boolean isNew) {
		this.musicName = musicName;
		normalizedName = Normalizer.normalizeMusicName(musicName);
		this.chart = Chart.getInstance(kind);
		this.level = level;
		this.skillnoteId = skillNoteId;
		this.isNew = isNew;
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

	public double getLevel() {
		return level;
	}

	public String getSkillnoteId() {
		return skillnoteId;
	}

	public boolean isNew() {
		return isNew;
	}

	public boolean isName(String name) {
		return normalizedName.equals(name);
	}

	public boolean isChart(Chart chart) {
		return this.chart == chart;
	}

	public void giveChart(Chart chart) {
		chart = this.chart;
	}

}
