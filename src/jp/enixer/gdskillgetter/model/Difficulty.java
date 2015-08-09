package jp.enixer.gdskillgetter.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jp.enixer.gdskillgetter.types.Level;
import jp.enixer.gdskillgetter.types.Type;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Difficulty implements Serializable {

	private static final long serialVersionUID = 1L;

	public final Music music;

	public final Level level;

	public final Type type;

	public final BigDecimal value;

	public Difficulty(Music music, Level level, Type type, BigDecimal value) {
		super();
		this.music = music;
		this.level = level;
		this.type = type;
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Difficulty)) {
			return false;
		}
		Difficulty o = (Difficulty) obj;
		return music.equals(o.music) && level.equals(o.level)
				&& type.equals(o.type);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
