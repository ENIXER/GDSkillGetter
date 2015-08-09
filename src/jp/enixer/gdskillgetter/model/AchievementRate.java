package jp.enixer.gdskillgetter.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AchievementRate implements Serializable {

	private static final BigDecimal ZERO = new BigDecimal("0");

	private static final BigDecimal MAX = new BigDecimal("100");

	private static final long serialVersionUID = 1L;

	private final BigDecimal value;

	public AchievementRate(String value) {
		if ("MAX".equals(value)) {
			this.value = MAX;
		} else if ("NO".equals(value)) {
			this.value = ZERO;
		} else {
			this.value = new BigDecimal(value);
		}
	}

	@Override
	public String toString() {
		return value.toPlainString();
	}

	public String toMultiLineString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
