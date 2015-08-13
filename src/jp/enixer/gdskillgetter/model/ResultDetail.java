package jp.enixer.gdskillgetter.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jp.enixer.gdskillgetter.types.Difficulty;
import jp.enixer.gdskillgetter.types.Rank;
import jp.enixer.gdskillgetter.types.Type;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ResultDetail implements Serializable, Comparable<ResultDetail> {

	private static final long serialVersionUID = 1L;

	private static final BigDecimal SKILL_COEFFICIENT = new BigDecimal("0.2");

	private static final BigDecimal ZERO = new BigDecimal("0.0");

	public final Result result;

	public final Difficulty level;

	public final Type type;

	public final Rank rank;

	public final AchievementRate achievements;

	public Boolean isFullcombo;

	public Integer maxcombo;

	public Integer points;

	public Integer playCount;

	public Integer clearCount;

	public ResultDetail(Result result, Difficulty level, Type type, Rank rank,
			AchievementRate achievements) {
		super();
		this.result = result;
		this.level = level;
		this.type = type;
		this.rank = rank;
		this.achievements = achievements;
	}

	public BigDecimal getSkillPoints() {
		if (result != null) {
			for (Level d : result.music.difficulties) {
				if (d.level == level && d.type == type) {
					return d.multiply(achievements)
							.multiply(SKILL_COEFFICIENT)
							.setScale(2, BigDecimal.ROUND_DOWN);
				}
			}
		}
		return ZERO;
	}

	public int compareTo(ResultDetail o) {
		int compared = getSkillPoints().compareTo(o.getSkillPoints());
		if (compared != 0) {
			return -compared;
		}
		int comparedByLevel = -(level.compareTo(o.level));
		if (comparedByLevel != 0) {
			return comparedByLevel;
		}
		int comparedByType = type.compareTo(o.type);
		return comparedByType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public int getKind() {
		int kind = 0;
		switch (level) {
		case MAS:
			kind++;
		case EXT:
			kind++;
		case ADV:
			kind++;
		case BSC:
			break;
		default:
			break;
		}
		switch (type) {
		case B:
			kind += 4;
		case G:
			kind += 4;
		case D:
			break;
		default:
			break;
		}
		return kind;
	}

}
