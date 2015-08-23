package jp.enixer.gdskillgetter.model;

import java.util.List;
import java.util.ArrayList;

public class Musics {
	private static List<Music> list = new ArrayList<Music>();

	public static List<Music> mergeTables(List<LevelData> levelTable,
			List<ResultData> resultTable) {
		for (LevelData level : levelTable) {
			for (ResultData result : resultTable) {
				if (result.isSame(level)) {
					Music music = getMusic(level);
					music.merge(level, result);
				}
			}
		}
		return list;
	}

	private static Music getMusic(LevelData level) {
		for (Music music : list) {
			if (music.isName(level)) {
				return music;
			}
		}
		Music newMusic = new Music();
		list.add(newMusic);
		return newMusic;
	}

	private Musics() {
		throw new RuntimeException("DO NOT instanciate this class.");
	}

}
