package jp.enixer.gdskillgetter.model;

import java.util.ArrayList;
import java.util.List;

public class Music implements Comparable<Music> {
	private MusicName musicName = new MusicName();
	private Results results = new Results();
	private String skillnoteId;

	public MusicName getMusicName() {
		return musicName;
	}

	public void setMusicName(MusicName musicName) {
		this.musicName = musicName;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

	@Override
	public int compareTo(Music o) {
		if (o == null) {
			return -1;
		}
		return musicName.compareTo(o.musicName);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Music)) {
			return false;
		}
		Music music = (Music) o;
		return musicName.equals(music.musicName);
	}

	public List<List<String>> getOutputs() {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> gfOutputs = results.getGfAllOutputs();
		if (gfOutputs != null) {
			List<String> gfResult = new ArrayList<String>();
			gfResult.add(skillnoteId);
			gfResult.addAll(gfOutputs);
			result.add(gfResult);
		}
		List<String> dmOutputs = results.getDmAllOutputs();
		if (dmOutputs != null) {
			List<String> dmResult = new ArrayList<String>();
			dmResult.add(skillnoteId);
			dmResult.addAll(dmOutputs);
			result.add(dmResult);
		}
		return result;
	}

	public boolean isName(LevelData level) {
		return musicName.isName(level);
	}

	public void merge(LevelData level, ResultData result) {
		skillnoteId = level.getSkillnoteId();
		musicName.merge(level, result);
		results.merge(level, result);
	}

}
