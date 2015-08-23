package jp.enixer.gdskillgetter.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.enixer.gdskillgetter.internal.HttpClientWrapper;
import jp.enixer.gdskillgetter.model.LevelData;
import jp.enixer.gdskillgetter.util.HTMLPattern;
import jp.enixer.gdskillgetter.util.LogMessage;
import jp.enixer.gdskillgetter.util.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SkillNoteHelper {

	private static final Log log = LogFactory.getLog(SkillNoteHelper.class);

	public static List<LevelData> loadMusics(HttpClientWrapper client) {

		List<LevelData> list = new ArrayList<LevelData>();

		for (String s : new String[] { "hot", "other" }) {
			String data = client.GET(URL.skillnoteLevelList() + s);
			final boolean isNew = "hot".equals(s);

			Matcher skillListMatcher = HTMLPattern.getLevelListPattern().matcher(data);
			while (skillListMatcher.find()) {
				String musicName = skillListMatcher.group(1);
				String skillnoteId = skillListMatcher.group(14);

				Matcher m;
				for (int i = 2; i < 14; i++) {
					m = HTMLPattern.getLevelValuePattern().matcher(skillListMatcher.group(i));
					if (m.find()) {
						LevelData level = new LevelData(musicName, i - 2,
								Double.parseDouble(m.group(1)), skillnoteId,
								isNew);
						list.add(level);
					}
				}
			}
		}

		log.info(LogMessage.loadLevelDataSuccessful());
		return list;
	}
}
