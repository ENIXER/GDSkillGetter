package jp.enixer.gdskillgetter.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Achievement implements Serializable {

	private static final BigDecimal ZERO = new BigDecimal("0.00");

	private static final BigDecimal MAX = new BigDecimal("100.00");

	private static final long serialVersionUID = 1L;

	public final BigDecimal value;

	public Achievement(String value) {
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
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
