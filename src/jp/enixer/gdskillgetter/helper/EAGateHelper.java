package jp.enixer.gdskillgetter.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.enixer.gdskillgetter.internal.Config;
import jp.enixer.gdskillgetter.internal.HttpClientWrapper;
import jp.enixer.gdskillgetter.internal.RequestParams;
import jp.enixer.gdskillgetter.model.ResultData;
import jp.enixer.gdskillgetter.types.Difficulty;
import jp.enixer.gdskillgetter.types.Rank;
import jp.enixer.gdskillgetter.types.Type;

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
		client.GET(EAGATE_LOGIN_URL); // 読み捨て
		client.POST(
				EAGATE_LOGIN_URL,
				new RequestParams("KID", username).add("pass", password).add(
						"OTP", ""));
		if (!isLoginSuccessful(username, client.GET(EAGATE_INDEX_URL))) {
			log.error("eAMUSEMENT にログインできませんでした.");
			throw new RuntimeException();
		}
		log.info("eAMUSEMENT にログイン完了。");
	}

	public static List<ResultData> getGfAllResult(HttpClientWrapper client,
			boolean canUpdateFullcombo) {
		List<ResultData> list = new ArrayList<ResultData>();
		log.info("eAMUSEMENT から GuitarFreaks プレーデータを読み込みます.");

		for (int i = 0; i < LOOP_COUNT; i++) {
			String contents = client.GET(EAGATE_GF_PLAYDATA_LIST_URL + i);
			Matcher gfResultListMatcher = gfResultListPattern.matcher(contents);
			while (gfResultListMatcher.find()) {
				String musicName = gfResultListMatcher.group(3);

				String resultContents = client.GET(EAGATE_GF_DETAILRESULT_URL
						+ gfResultListMatcher.group(2) + "&index="
						+ gfResultListMatcher.group(1));

				if (cannotMatchMusicName(resultContents, musicName)) {
					continue;
				}

				Matcher dm = resultDetailGfPattern.matcher(resultContents);
				while (dm.find()) {
					ResultData data = createResultData(dm, musicName);
					if (data != null) {
						list.add(data);
					}
				}
			}
		}

		log.info("eAMUSEMENT から GuitarFreaks プレーデータを読み込みました.");
		return list;
	}

	public static List<ResultData> getDmAllResult(HttpClientWrapper client,
			boolean canUpdateFullcombo) {
		List<ResultData> list = new ArrayList<ResultData>();
		log.info("eAMUSEMENT から DrumMania プレーデータを読み込みます.");

		for (int i = 0; i < LOOP_COUNT; i++) {
			String contents = client.GET(EAGATE_DM_PLAYDATA_LIST_URL + i);
			Matcher dmResultListMatcher = dmResultListPattern.matcher(contents);
			while (dmResultListMatcher.find()) {
				String musicName = dmResultListMatcher.group(3);

				String resultContents = client.GET(EAGATE_DM_DETAILRESULT_URL
						+ dmResultListMatcher.group(2) + "&index="
						+ dmResultListMatcher.group(1));

				if (cannotMatchMusicName(resultContents, musicName)) {
					continue;
				}

				Matcher dm = resultDetailDmPattern.matcher(resultContents);
				while (dm.find()) {
					ResultData data = createResultData(dm, musicName);
					if (data != null) {
						list.add(data);
					}
				}
			}
		}

		log.info("eAMUSEMENT から DrumMania プレーデータを読み込みました.");
		return list;
	}

	private static boolean cannotMatchMusicName(String resultContents,
			String musicName) {
		Matcher dmn = resultDetailMusicNamePattern.matcher(resultContents);
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
		Type type = Type.getInstanceOf(t.equals("") ? "DRUM" : t);
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
			log.warn(musicName + " のプレーデータが正常に読み込めません.");
		}
	}

	private static void warnResult(String musicName, String resultContentsName) {
		if (log.isWarnEnabled()) {
			log.warn(musicName + " のプレーデータが正常に読み込めません. (" + resultContentsName
					+ " を取得した模様)");
		}
	}

	private static boolean isLoginSuccessful(String username, String contents) {
		if (StringUtils.isBlank(contents)) {
			return false;
		}
		return contents.contains("<p class=\"id_text\">" + username + "</p>");
	}

	public static void logoutEAGate(HttpClientWrapper client) {
		client.GET(EAGATE_LOGOUT_URL); // 読み捨て
		log.info("eAMUSEMENT からログアウトしました.");
	}

	private static final Pattern gfResultListPattern = Pattern
			.compile(
					"<div class=\"md_title_box\">\\s*?<a class=\"text_link\" href=\".*?music_detail.html\\?"
							+ "gtype=gf&sid=2&index=(\\d+)&cat=(\\d+).*?\">" // index,category
							+ "[\\s\\r\\n]*(.+?)[\\s\\r\\n]*</a>", // 曲名
					Pattern.DOTALL);

	private static final Pattern dmResultListPattern = Pattern
			.compile(
					"<div class=\"md_title_box\">\\s*?<a class=\"text_link\" href=\".*?music_detail.html\\?"
							+ "gtype=dm&sid=2&index=(\\d+)&cat=(\\d+).*?\">" // index,category
							+ "[\\s\\r\\n]*(.+?)[\\s\\r\\n]*</a>", // 曲名
					Pattern.DOTALL);

	@SuppressWarnings("unused")
	private static final Pattern resultPattern = Pattern
			.compile("([ABCDE]|SS?)[\\s\\r\\n]*/[\\s\\r\\n]*(\\d{1,3}\\.\\d{1,2}|MAX)%?");

	private static final Pattern resultDetailMusicNamePattern = Pattern
			.compile("<div class=\"live_title\">\\s*(.+?)\\s*</div>");

	private static final Pattern resultDetailGfPattern = Pattern
			.compile(
					"<div class=\"index_md_tb gf\">.*?<font class=\"seq.*?\">\\s*(.+?)\\s*</font>[\\s\\r\\n]*/[\\s\\r\\n]*<font class=\"seq.*?\">\\s*(.+?)\\s*</font></div>"
							+ ".*?<td class=\"idx_tb h\">プレー回数</td>.*?<td>(\\d+) 回</td>"
							+ ".*?<td class=\"idx_tb h\">クリア回数</td>.*?<td>(\\d+) 回</td>"
							+ ".*?<td class=\"idx_tb h\">最高ランク</td>.*?<td>([ABCDE-]|SS?) (FULL COMBO|EXCELLENT)?</td>"
							+ ".*?<td class=\"idx_tb h\">達成率</td>.*?<td>(\\d{1,3}\\.\\d{1,2}|MAX|NO|-)%?</td>"
							+ ".*?<td class=\"idx_tb h\">ハイスコア</td>.*?<td>(.+?)</td>"
							+ ".*?<td class=\"idx_tb h\">MAX COMBO</td>.*?<td>(.+?)</td>",
					Pattern.DOTALL);

	private static final Pattern resultDetailDmPattern = Pattern
			.compile(
					"<div class=\"index_md_tb gf\">.*?<font class=\"\">\\s*(.*?)\\s*</font>[\\s\\r\\n]*<font class=\"seq.*?\">\\s*(.+?)\\s*</font></div>"
							+ ".*?<td class=\"idx_tb h\">プレー回数</td>.*?<td>(\\d+) 回</td>"
							+ ".*?<td class=\"idx_tb h\">クリア回数</td>.*?<td>(\\d+) 回</td>"
							+ ".*?<td class=\"idx_tb h\">最高ランク</td>.*?<td>([ABCDE-]|SS?) (FULL COMBO|EXCELLENT)?</td>"
							+ ".*?<td class=\"idx_tb h\">達成率</td>.*?<td>(\\d{1,3}\\.\\d{1,2}|MAX|NO|-)%?</td>"
							+ ".*?<td class=\"idx_tb h\">ハイスコア</td>.*?<td>(.+?)</td>"
							+ ".*?<td class=\"idx_tb h\">MAX COMBO</td>.*?<td>(.+?)</td>",
					Pattern.DOTALL);

	private static final String EAGATE_LOGIN_URL = "https://p.eagate.573.jp/gate/p/login.html";

	private static final String EAGATE_INDEX_URL = "http://p.eagate.573.jp/gate/p/mypage/index.html?p=done_login";

	private static final String EAGATE_LOGOUT_URL = "http://eagate.573.jp/gate/p/logout.html";

	private static final String EAGATE_GF_PLAYDATA_LIST_URL = "http://p.eagate.573.jp/game/gfdm/gitadora/p/cont/play_data_tb/music.html?gtype=gf&cat=";

	private static final String EAGATE_DM_PLAYDATA_LIST_URL = "http://p.eagate.573.jp/game/gfdm/gitadora/p/cont/play_data_tb/music.html?gtype=dm&cat=";

	private static final String EAGATE_GF_DETAILRESULT_URL = "http://p.eagate.573.jp/game/gfdm/gitadora/p/cont/play_data_tb/music_detail.html?gtype=gf&sid=2&page=1&cat="; // &index=

	private static final String EAGATE_DM_DETAILRESULT_URL = "http://p.eagate.573.jp/game/gfdm/gitadora/p/cont/play_data_tb/music_detail.html?gtype=dm&sid=2&page=1&cat="; // &index=

	private static final int LOOP_COUNT = 37;

}
