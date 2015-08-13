package jp.enixer.gdskillgetter.helper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.enixer.gdskillgetter.internal.Config;
import jp.enixer.gdskillgetter.internal.HttpClientWrapper;
import jp.enixer.gdskillgetter.internal.RequestParams;
import jp.enixer.gdskillgetter.model.Achievement;
import jp.enixer.gdskillgetter.model.Music;
import jp.enixer.gdskillgetter.model.Result;
import jp.enixer.gdskillgetter.model.ResultDetail;
import jp.enixer.gdskillgetter.types.Level;
import jp.enixer.gdskillgetter.types.Rank;
import jp.enixer.gdskillgetter.types.Type;
import jp.enixer.gdskillgetter.util.Normalizer;

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

	public static Map<String, Result> getGfAllResult(HttpClientWrapper client,
			Map<String, Music> musics, boolean canUpdateFullcombo) {
		Map<String, Result> list = new LinkedHashMap<String, Result>();
		log.info("eAMUSEMENT から GuitarFreaks プレーデータを読み込みます.");

		for (int i = 0; i < LOOP_COUNT; i++) {
			String contents = client.GET(EAGATE_GF_PLAYDATA_LIST_URL + i);
			Matcher gfResultListMatcher = gfResultListPattern.matcher(contents);
			while (gfResultListMatcher.find()) {

				String musicName = gfResultListMatcher.group(3);
				Music music = musics.get(Normalizer
						.normalizeMusicName(musicName));
				if (music == null) {
					continue;
				}
				music.eagateName = musicName;
				Result result = new Result();
				result.music = music;
				list.put(music.name, result);

				String resultContents = client.GET(EAGATE_GF_DETAILRESULT_URL
						+ gfResultListMatcher.group(2) + "&index="
						+ gfResultListMatcher.group(1));

				Matcher dmn = resultDetailMusicNamePattern
						.matcher(resultContents);
				if (!dmn.find()) {
					warnResult(result, Type.G);
					continue;
				}
				if (!musicName.equals(dmn.group(1))) {
					warnResult(result, Type.G, dmn.group(1));
					continue;
				}

				Matcher dm = resultDetailGfPattern.matcher(resultContents);
				while (dm.find()) {
					if(Integer.valueOf(dm.group(3)).equals(new Integer(0))){
						continue;
					}
					Type type = Type.getInstanceOf(dm.group(1));
					Level level = Level.getInstanceOf(dm.group(2));
					ResultDetail detail = new ResultDetail(result, level, type,
							Rank.getInstanceOf(dm.group(5)), new Achievement(
									dm.group(7)));
					detail.isFullcombo = StringUtils.isNotEmpty(dm.group(6));
					detail.points = Integer.valueOf(dm.group(8));
					detail.maxcombo = Integer.valueOf(dm.group(9));
					detail.playCount = Integer.valueOf(dm.group(3));
					detail.clearCount = Integer.valueOf(dm.group(4));
					result.details.add(detail);
					writeResultLog(result, detail);
				}
			}
		}

		log.info("eAMUSEMENT から GuitarFreaks プレーデータを読み込みました.");
		return list;
	}

	public static Map<String, Result> getDmAllResult(HttpClientWrapper client,
			Map<String, Music> musics, boolean canUpdateFullcombo) {
		Map<String, Result> list = new LinkedHashMap<String, Result>();
		log.info("eAMUSEMENT から DrumMania プレーデータを読み込みます.");

		for (int i = 0; i < LOOP_COUNT; i++) {
			String contents = client.GET(EAGATE_DM_PLAYDATA_LIST_URL + i);
			Matcher dmResultListMatcher = dmResultListPattern.matcher(contents);
			while (dmResultListMatcher.find()) {
				String musicName = dmResultListMatcher.group(3);
				Music music = musics.get(Normalizer
						.normalizeMusicName(musicName));
				if (music == null) {
					continue;
				}
				music.eagateName = musicName;
				Result result = new Result();
				result.music = music;
				list.put(music.name, result);

				String resultContents = client.GET(EAGATE_DM_DETAILRESULT_URL
						+ dmResultListMatcher.group(2) + "&index="
						+ dmResultListMatcher.group(1));

				Matcher dmn = resultDetailMusicNamePattern
						.matcher(resultContents);
				if (!dmn.find()) {
					warnResult(result, Type.D);
					continue;
				}
				if (!musicName.equals(dmn.group(1))) {
					warnResult(result, Type.D, dmn.group(1));
					continue;
				}

				Matcher dm = resultDetailDmPattern.matcher(resultContents);
				while (dm.find()) {
					if(Integer.valueOf(dm.group(3)).equals(new Integer(0))){
						continue;
					}
					Type type = Type.D;
					Level level = Level.getInstanceOf(dm.group(2));
					ResultDetail detail = new ResultDetail(result, level, type,
							Rank.getInstanceOf(dm.group(5)), new Achievement(
									dm.group(7)));
					detail.isFullcombo = StringUtils.isNotEmpty(dm.group(6));
					detail.points = Integer.valueOf(dm.group(8));
					detail.maxcombo = Integer.valueOf(dm.group(9));
					detail.playCount = Integer.valueOf(dm.group(3));
					detail.clearCount = Integer.valueOf(dm.group(4));
					result.details.add(detail);
					writeResultLog(result, detail);
				}
			}
		}

		log.info("eAMUSEMENT から DrumMania プレーデータを読み込みました.");
		return list;
	}

	private static void writeResultLog(Result result, ResultDetail detail) {
		resultlog.info(result.music.eagateName + "|" + detail.level + "-"
				+ detail.type + "|" + detail.achievements.value + "%|"
				+ detail.getSkillPoints() + "pts|" + detail.maxcombo + "combo|"
				+ (detail.isFullcombo ? "FC" : "") + "|" + detail.points + "|"
				+ detail.clearCount + "|" + detail.playCount);
	}

	private static void warnResult(Result result, Type type) {
		if (log.isWarnEnabled()) {
			log.warn(result.music.eagateName + " のプレーデータが正常に読み込めません.");
		}
	}

	private static void warnResult(Result result, Type type, String musicName) {
		if (log.isWarnEnabled()) {
			log.warn(result.music.eagateName + " のプレーデータが正常に読み込めません. ("
					+ musicName + " を取得した模様)");
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
