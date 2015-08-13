package jp.enixer.gdskillgetter.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jp.enixer.gdskillgetter.types.Difficulty;
import jp.enixer.gdskillgetter.types.Rank;
import jp.enixer.gdskillgetter.types.Type;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SkillNoteResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public final Difficulty level;

	public final Type type;

	public final Rank rank;

	public final AchievementRate achievements;

	public final BigDecimal skillpoints;

	public final boolean isFullcombo;

	public final boolean isRandom;

	public final boolean isSuperRandom;

	public final boolean isLeft;

	public final String comment;

	public SkillNoteResult(Difficulty level, Type type, Rank rank,
			AchievementRate achievements, BigDecimal skillpoints,
			boolean isFullcombo, boolean isRandom, boolean isSuperRandom,
			boolean isLeft, String comment) {
		super();
		this.level = level;
		this.type = type;
		this.rank = rank;
		this.achievements = achievements;
		this.skillpoints = skillpoints;
		this.isFullcombo = isFullcombo;
		this.isRandom = isRandom;
		this.isSuperRandom = isSuperRandom;
		this.isLeft = isLeft;
		this.comment = comment;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
