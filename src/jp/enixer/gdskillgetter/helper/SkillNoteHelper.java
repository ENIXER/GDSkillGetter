package jp.enixer.gdskillgetter.helper;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.enixer.gdskillgetter.internal.HttpClientWrapper;
import jp.enixer.gdskillgetter.model.Difficulty;
import jp.enixer.gdskillgetter.model.Music;
import jp.enixer.gdskillgetter.types.Level;
import jp.enixer.gdskillgetter.types.Type;
import jp.enixer.gdskillgetter.util.Normalizer;

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

	public static Map<String, Music> loadMusics(HttpClientWrapper client) {

		Map<String, Music> list = new LinkedHashMap<String, Music>();

		for (String s : new String[] { "hot", "other" }) {
			String data = client.GET(SKILLNOTE_DIFFICULTYLIST_URL + s);
			final boolean isNew = "hot".equals(s);

			Matcher skillListMatcher = skillListPattern.matcher(data);
			while (skillListMatcher.find()) {
				Music music = new Music();
				String musicName = skillListMatcher.group(1);
				music.skillnoteName = musicName;
				String skillnoteId = skillListMatcher.group(14);
				music.skillnoteId = skillnoteId;
				music.name = Normalizer.normalizeMusicName(musicName);
				music.isNew = isNew;
				list.put(music.name, music);

				Set<Difficulty> d = music.difficulties;

				Matcher m;
				m = skillValuePattern.matcher(skillListMatcher.group(2));
				if (m.find()) {
					d.add(new Difficulty(music, Level.BSC, Type.D,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(3));
				if (m.find()) {
					d.add(new Difficulty(music, Level.ADV, Type.D,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(4));
				if (m.find()) {
					d.add(new Difficulty(music, Level.EXT, Type.D,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(5));
				if (m.find()) {
					d.add(new Difficulty(music, Level.MAS, Type.D,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(6));
				if (m.find()) {
					d.add(new Difficulty(music, Level.BSC, Type.G,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(7));
				if (m.find()) {
					d.add(new Difficulty(music, Level.ADV, Type.G,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(8));
				if (m.find()) {
					d.add(new Difficulty(music, Level.EXT, Type.G,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(9));
				if (m.find()) {
					d.add(new Difficulty(music, Level.MAS, Type.G,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(10));
				if (m.find()) {
					d.add(new Difficulty(music, Level.BSC, Type.B,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(11));
				if (m.find()) {
					d.add(new Difficulty(music, Level.ADV, Type.B,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(12));
				if (m.find()) {
					d.add(new Difficulty(music, Level.EXT, Type.B,
							new BigDecimal(m.group(1))));
				}
				m = skillValuePattern.matcher(skillListMatcher.group(13));
				if (m.find()) {
					d.add(new Difficulty(music, Level.MAS, Type.B,
							new BigDecimal(m.group(1))));
				}
			}
		}

		Map<String, String> skillNoteMusicIds = new LinkedHashMap<String, String>();

		for (Entry<String, Music> entry : list.entrySet()) {
			if (skillNoteMusicIds.containsKey(entry.getValue().skillnoteName)) {
				entry.getValue().skillnoteId = skillNoteMusicIds.get(entry
						.getValue().skillnoteName);
			}
		}

		log.info("Skill Simulator から曲の難易度データを読み込みました.");
		return list;
	}

}
