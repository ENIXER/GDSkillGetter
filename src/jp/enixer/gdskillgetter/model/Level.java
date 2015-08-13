package jp.enixer.gdskillgetter.model;

import java.math.BigDecimal;

import jp.enixer.gdskillgetter.types.Difficulty;
import jp.enixer.gdskillgetter.types.Type;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Level extends BigDecimal {

	private static final long serialVersionUID = 1L;

	public final Music music;

	public final Difficulty level;

	public final Type type;

	public Level(Music music, Difficulty level, Type type, BigDecimal value) {
		super(value.toPlainString());
		this.music = music;
		this.level = level;
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Level)) {
			return false;
		}
		Level o = (Level) obj;
		return music.equals(o.music) && level.equals(o.level)
				&& type.equals(o.type);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
