package jp.enixer.gdskillgetter.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AchievementRate extends BigDecimal {

	private static final String NO = "0.00";

	private static final String MAX = "100.00";

	private static final long serialVersionUID = 1L;

	public AchievementRate(String value) {
		super("MAX".equals(value) ? MAX : "NO".equals(value) ? NO : value);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
