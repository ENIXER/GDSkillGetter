package jp.enixer.gdskillgetter.util;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.replaceEach;

import java.util.regex.Pattern;

public class Normalizer {

	private static final Pattern blankPattern = Pattern.compile("\\s");

	private static final String LOVERY_RADIO_TITLE_PREFIX = "†渚の小悪魔ラヴリィ～レイディオ†";

	private static final String LOVERY_RADIO_TITLE = LOVERY_RADIO_TITLE_PREFIX
			+ "(GITADORAver.)";

	private static final Pattern loveryRadioPattern = Pattern.compile("^"
			+ LOVERY_RADIO_TITLE_PREFIX + "$");

	protected Normalizer() {

	}

	public static String normalizeMusicName(String musicName) {
		return lowerCase(replaceEach(java.text.Normalizer.normalize(
				replaceLoveryRadio(blankPattern.matcher(musicName).replaceAll(
						"")), java.text.Normalizer.Form.NFKC), new String[] {
				" ", "Ё", "Φ", "WILDCREATURES" }, new String[] { "", "E", "O",
				"WILDCREATRURES" }));
	}

	private static String replaceLoveryRadio(String target) {
		if (loveryRadioPattern.matcher(target).matches()) {
			return LOVERY_RADIO_TITLE;
		} else {
			return target;
		}
	}

}
