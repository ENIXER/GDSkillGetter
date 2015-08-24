package jp.enixer.gdskillgetter.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.enixer.gdskillgetter.internal.Config;
import jp.enixer.gdskillgetter.internal.HttpClientWrapper;
import jp.enixer.gdskillgetter.internal.RequestParams;
import jp.enixer.gdskillgetter.model.ResultData;
import jp.enixer.gdskillgetter.types.Difficulty;
import jp.enixer.gdskillgetter.types.Rank;
import jp.enixer.gdskillgetter.types.Type;
import jp.enixer.gdskillgetter.util.LogMessage;
import jp.enixer.gdskillgetter.util.HTMLPattern;
import jp.enixer.gdskillgetter.util.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EAGateHelper {

	private static final Log log = LogFactory.getLog(EAGateHelper.class);

	private static final Log resultlog = LogFactory.getLog(EAGateHelper.class
			.getName() + ".Result");

	public static void loginEAGate(HttpClientWrapper client) {
		String username = Config.getEAGateUsername();
		String password = Config.getEAGatePassword();
		client.GET(URL.loginEAGate());
		client.POST(
				URL.loginEAGate(),
				new RequestParams("KID", username).add("pass", password).add(
						"OTP", ""));
		if (!isLoginSuccessful(username, client.GET(URL.EAGateIndex()))) {
			log.error(LogMessage.cannotLoginEAGate());
			throw new RuntimeException();
		}
		log.info(LogMessage.loginEAGateSuccessful());
	}

	public static List<ResultData> getGfAllResult(HttpClientWrapper client) {
		List<ResultData> list = new ArrayList<ResultData>();
		log.info(LogMessage.loadFromEAGate(Type.G));

		for (int i = 0; i < LOOP_COUNT; i++) {
			String contents = client.GET(URL.EAGatePlaydataList(Type.G) + i);
			Matcher gfResultListMatcher = HTMLPattern.getResultListPattern(
					Type.G).matcher(contents);
			while (gfResultListMatcher.find()) {
				String musicName = gfResultListMatcher.group(3);

				String resultContents = client.GET(URL.EAGateDetailResultList(Type.G)
						+ gfResultListMatcher.group(2) + "&index="
						+ gfResultListMatcher.group(1));

				if (cannotMatchMusicName(resultContents, musicName)) {
					continue;
				}

				Matcher dm = HTMLPattern.getResultDetailPattern(Type.G)
						.matcher(resultContents);
				while (dm.find()) {
					ResultData data = createResultData(dm, musicName);
					if (data != null) {
						list.add(data);
					}
				}
			}
		}

		log.info(LogMessage.loadEndFromEAGate(Type.G));
		return list;
	}

	public static List<ResultData> getDmAllResult(HttpClientWrapper client) {
		List<ResultData> list = new ArrayList<ResultData>();
		log.info(LogMessage.loadFromEAGate(Type.D));

		for (int i = 0; i < LOOP_COUNT; i++) {
			String contents = client.GET(URL.EAGatePlaydataList(Type.D) + i);
			Matcher dmResultListMatcher = HTMLPattern.getResultListPattern(
					Type.D).matcher(contents);
			while (dmResultListMatcher.find()) {
				String musicName = dmResultListMatcher.group(3);

				String resultContents = client.GET(URL.EAGateDetailResultList(Type.D)
						+ dmResultListMatcher.group(2) + "&index="
						+ dmResultListMatcher.group(1));

				if (cannotMatchMusicName(resultContents, musicName)) {
					continue;
				}

				Matcher dm = HTMLPattern.getResultDetailPattern(Type.D)
						.matcher(resultContents);
				while (dm.find()) {
					ResultData data = createResultData(dm, musicName);
					if (data != null) {
						list.add(data);
					}
				}
			}
		}

		log.info(LogMessage.loadEndFromEAGate(Type.D));
		return list;
	}

	private static boolean cannotMatchMusicName(String resultContents,
			String musicName) {
		Matcher dmn = HTMLPattern.getResultMusicNamePattern().matcher(resultContents);
		if (!dmn.find()) {
			warnResult(musicName);
			return true;
		}
		if (!musicName.equals(dmn.group(1))) {
			warnResult(musicName, dmn.group(1));
			return true;
		}
		return false;
	}

	private static ResultData createResultData(Matcher dm, String musicName) {
		if (Integer.parseInt(dm.group(3)) == 0) {
			return null;
		}
		String t = dm.group(1);
		Type type = Type.getInstanceOf("".equals(t) ? "DRUM" : t);
		Difficulty difficulty = Difficulty.getInstanceOf(dm.group(2));
		int kind = type.getKind() + difficulty.getKind();
		Rank rank = Rank.getInstanceOf(dm.group(5));
		String achievement = dm.group(7);
		double achievementRate = 0;
		if (achievement.equals("MAX")) {
			achievementRate = 100;
		} else if (achievement.equals("NO")) {
			achievementRate = 0;
		} else {
			achievementRate = Double.parseDouble(achievement);
		}
		boolean isFullcombo = StringUtils.isNotEmpty(dm.group(6));
		int points = Integer.parseInt(dm.group(8));
		int maxcombo = Integer.parseInt(dm.group(9));
		int playCount = Integer.parseInt(dm.group(3));
		int clearCount = Integer.parseInt(dm.group(4));
		resultlog.info(musicName + "|" + difficulty + "-" + type + "|"
				+ achievementRate + "%|" + maxcombo + "combo|"
				+ (isFullcombo ? "FC" : "") + "|" + points + "|" + clearCount
				+ "|" + playCount);
		return new ResultData(musicName, kind, rank, achievementRate,
				isFullcombo, maxcombo, points, playCount, clearCount);
	}

	private static void warnResult(String musicName) {
		if (log.isWarnEnabled()) {
			log.warn(LogMessage.cannotLoadMusicPlayData(musicName));
		}
	}

	private static void warnResult(String musicName, String resultContentsName) {
		if (log.isWarnEnabled()) {
			log.warn(LogMessage.loadWrongMusicPlayData(musicName,
					resultContentsName));
		}
	}

	private static boolean isLoginSuccessful(String username, String contents) {
		if (StringUtils.isBlank(contents)) {
			return false;
		}
		return contents.contains("<p class=\"id_text\">" + username + "</p>");
	}

	public static void logoutEAGate(HttpClientWrapper client) {
		client.GET(URL.logoutEAGate());
		log.info(LogMessage.logoutEAGateSuccessful());
	}

	private static final int LOOP_COUNT = 37;

}
