package jp.enixer.gdskillgetter.model;

import java.util.ArrayList;
import java.util.List;

import jp.enixer.gdskillgetter.types.Type;

public class Music {
	private MusicName musicName = new MusicName();
	private Results results = new Results();
	private String skillnoteId;
	private boolean isNew = false;

	public MusicName getMusicName() {
		return musicName;
	}

	public void setMusicName(MusicName musicName) {
		this.musicName = musicName;
	}

	public void setResults(Results results) {
		this.results = results;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Music)) {
			return false;
		}
		Music music = (Music) o;
		return musicName.equals(music.musicName);
	}

	public List<String> getOutputs(Type type) {
		List<String> result = new ArrayList<String>();
		List<String> outputs = results.getOutputs(type);
		if (outputs != null) {
			result.add(skillnoteId);
			result.addAll(outputs);
			return result;
		}
		return null;
	}

	public boolean isName(LevelData level) {
		return musicName.isName(level);
	}

	public void merge(LevelData level, ResultData result) {
		skillnoteId = level.getSkillnoteId();
		isNew = level.isNew();
		musicName.merge(level, result);
		results.merge(level, result);
	}

	public double getTargetSkillPoint(Type type) {
		return results.getSkillTargetPoint(type);
	}

	public boolean isNew() {
		return isNew;
	}

}
