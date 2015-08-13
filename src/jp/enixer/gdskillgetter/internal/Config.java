package jp.enixer.gdskillgetter.internal;

import static org.apache.commons.lang3.BooleanUtils.toBoolean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {
	private static Properties properties;
	private static final Log log = LogFactory.getLog(Config.class);

	private Config() {
	}

	public static void getPropertiesFromConfig() {
		properties = new Properties();
		String fileName = new ConfigSearcher().search();
		if (fileName == null) {
			log.fatal("設定ファイルが見つかりません。");
			throw new RuntimeException();
		}
		log.info("設定ファイル(" + fileName + ")を読み込みます。");
		InputStream in = Config.class.getResourceAsStream('/'+fileName);
		try {
			if (fileName.endsWith("xml")) {
				properties.loadFromXML(in);
			} else if (fileName.endsWith("properties")) {
				properties.load(in);
			} else if (fileName.endsWith("ini")) {
				// TODO: 独自書式の設定ファイルから設定を読み込めるようにする
			} else {
				log.error("予期しない設定ファイル名です。");
				throw new RuntimeException();
			}
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.info("設定の読み込みが完了しました。");
	}

	public static String getProxyHost() {
		return properties.getProperty("ProxyHost");
	}

	public static int getProxyPort() {
		return NumberUtils.toInt(properties.getProperty("ProxyPort"));
	}

	public static String getEAGateUsername() {
		return properties.getProperty("EAGateUsername");
	}

	public static String getEAGatePassword() {
		return properties.getProperty("EAGatePassword");
	}

	public static boolean canUpdateGf() {
		String updateGuitarSkill = properties.getProperty("UpdateGuitarSkill");
		return StringUtils.isEmpty(updateGuitarSkill)
				|| toBoolean(updateGuitarSkill);
	}

	public static boolean canUpdateDm() {
		String updateDrumSkill = properties.getProperty("UpdateDrumSkill");
		return StringUtils.isEmpty(updateDrumSkill)
				|| toBoolean(updateDrumSkill);
	}

	public static boolean canUpdateFullcombo() {
		String updateFullcombo = properties.getProperty("UpdateFullcombo");
		return StringUtils.isNotEmpty(updateFullcombo)
				&& toBoolean(updateFullcombo);
	}

	private static class ConfigSearcher {
		/**
		 * 設定ファイルのファイル名を表す正規表現。設定ファイル名はconfigを含む文字列で、拡張子が.xml、.properties、.ini
		 * のいずれかになっていることを期待する。
		 */
		private final String PATTERN = "\\w*config\\w*(\\.xml|\\.properties|\\.ini)";

		/**
		 * 設定ファイルを探す。該当するファイルが複数存在した場合、一番最初にマッチしたファイルのファイル名を返す。
		 * 
		 * @author ENIXER
		 * @return 正規表現によってマッチするファイルのうち、一番最初にマッチしたファイルのファイル名。
		 *         マッチするファイルが存在しない場合はnullを返す。
		 * @see #PATTERN
		 */
		public String search() {
			Pattern pattern = Pattern.compile(PATTERN);
			Queue<File> files = new LinkedList<File>();
			files.add(new File("./"));
			while (!files.isEmpty()) {
				File file = files.poll();
				if (file.isFile()) {
					Matcher matcher = pattern.matcher(file.getName());
					if (matcher.matches()) {
						return file.getName();
					}
				} else {
					for (File f : file.listFiles()) {
						files.add(f);
					}
				}
			}
			return null;
		}
	}

}
