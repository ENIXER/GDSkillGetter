package jp.enixer.gdskillgetter.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.enixer.gdskillgetter.internal.HttpClientWrapper;
import jp.enixer.gdskillgetter.model.LevelData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SkillNoteHelper {

	private static final Log log = LogFactory.getLog(SkillNoteHelper.class);

	private static final Pattern skillListPattern = Pattern
			.compile(
					"<tr data-music_id=\\\"\\d+\\\">.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.+?)</td>.*?<td>(.+?)</td>.*?<td>(.+?)</td>.*?</tr>",
					Pattern.DOTALL);

	private static final Pattern skillValuePattern = Pattern
			.compile("(\\d+\\.\\d+)");

	private static final String SKILLNOTE_DIFFICULTYLIST_URL = "http://tri.gfdm-skill.net/musics/";

	public static List<LevelData> loadMusics(HttpClientWrapper client) {

		List<LevelData> list = new ArrayList<LevelData>();

		for (String s : new String[] { "hot", "other" }) {
			String data = client.GET(SKILLNOTE_DIFFICULTYLIST_URL + s);
			final boolean isNew = "hot".equals(s);

			Matcher skillListMatcher = skillListPattern.matcher(data);
			while (skillListMatcher.find()) {
				String musicName = skillListMatcher.group(1);
				String skillnoteId = skillListMatcher.group(14);

				Matcher m;
				for(int i=2;i<14;i++){
					m = skillValuePattern.matcher(skillListMatcher.group(i));
					if(m.find()){
						LevelData level = new LevelData(musicName, i-2, Double.parseDouble(m.group(1)), skillnoteId, isNew);
						list.add(level);
					}
				}
			}
		}

		log.info("Skill Simulator から曲の難易度データを読み込みました.");
		return list;
	}
}
